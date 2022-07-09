package com.mycompany.Controllers;

import com.mycompany.Controllers.util.DialogMessageFactory;
import com.mycompany.CustomExceptions.CustomIOException;
import com.mycompany.IO.CompanyCSV;
import com.mycompany.Models.Company;
import com.mycompany.Swing.CompanyDialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlling Company dialog
 */
public class CompanyController {

    private CompanyDialog companyDialog;
    private Company company;
    private DialogMessageFactory dialogMessageFactory;
    private static final String COMPANY_FILE_NAME = "company.txt";

    /**
     * Open new dialog, fill with company details
     */
    public CompanyController() {
        dialogMessageFactory = new DialogMessageFactory();
        initController();
    }

    private void initController() {
        fillCompanyFields();
        companyDialog = new CompanyDialog(company);
        companyDialog.getTfName().setText(company.getName());
        companyDialog.getTfRegistrationSeat().setText(company.getRegisteredSeat());
        companyDialog.getTfRegistrationNumber().setText(company.getRegisteredNumber());
        companyDialog.getTfPhone().setText(company.getPhoneNumber());
        companyDialog.getTfEmail().setText(company.getEmail());
        companyDialog.getTfVatNumber().setText(company.getVatNumber());
        companyDialog.setTitle("My Company");
        companyDialog.getContentPane().setBackground(Color.DARK_GRAY);
        companyDialog.setLocationRelativeTo(null);
        companyDialog.getBtModify().addActionListener(e -> modify());
        companyDialog.getBtCancel().addActionListener(e -> close());
        companyDialog.setModal(true);
        companyDialog.setVisible(true);
    }

    /**
     * Modify company details by user input and override in file
     */
    private void modify() {
        if (isValidFields()) {
            company.setName(companyDialog.getTfName().getText());
            company.setRegisteredSeat(companyDialog.getTfRegistrationSeat().getText());
            company.setRegisteredNumber(companyDialog.getTfRegistrationNumber().getText());
            company.setPhoneNumber(companyDialog.getTfPhone().getText());
            company.setEmail(companyDialog.getTfEmail().getText());
            company.setVatNumber(companyDialog.getTfVatNumber().getText());
            CompanyCSV csv = new CompanyCSV(COMPANY_FILE_NAME);
            try {
                csv.writer(company);
            } catch (CustomIOException ex) {
                ex.printStackTrace();
                dialogMessageFactory.getDialogMessage("Modification failed, cannot write to file!", DialogMessageFactory.MessageType.ERROR);
            }
            companyDialog.setVisible(false);
        }
    }

    /**
     * Close company dialog
     */
    private void close() {
        companyDialog.setVisible(false);
    }

    /**
     * Read Company details from file and store in Company object
     */
    private void fillCompanyFields() {
        try {
            CompanyCSV csv = new CompanyCSV(COMPANY_FILE_NAME);
            csv.reader();
            company = csv.getCompany();
        } catch (CustomIOException ex) {
            ex.printStackTrace();
            dialogMessageFactory.getDialogMessage("Cannot read file: " + COMPANY_FILE_NAME + "!", DialogMessageFactory.MessageType.ERROR);
        }
    }

    /**
     * Check user inputs validity
     *
     * @return true if the input fields are valid
     */
    private boolean isValidFields() {

        List<String> invalidFieldsList = new ArrayList<>();

        if (companyDialog.getTfName().getText().isEmpty() || companyDialog.getTfName().getText().contains(";")) {
            invalidFieldsList.add("Name");
        }
        if (companyDialog.getTfRegistrationSeat().getText().isEmpty() || companyDialog.getTfRegistrationSeat().getText().contains(";")) {
            invalidFieldsList.add("Registered Seat");
        }

        if (companyDialog.getTfRegistrationNumber().getText().isEmpty() || companyDialog.getTfRegistrationNumber().getText().contains(";")) {
            invalidFieldsList.add("Registered Number");
        }

        if (companyDialog.getTfPhone().getText().isEmpty() || companyDialog.getTfPhone().getText().contains(";")) {
            invalidFieldsList.add("Phone Number");
        }

        if (companyDialog.getTfEmail().getText().isEmpty() || companyDialog.getTfEmail().getText().contains(";")) {
            invalidFieldsList.add("Email");
        }

        if (companyDialog.getTfVatNumber().getText().isEmpty() || companyDialog.getTfVatNumber().getText().contains(";")) {
            invalidFieldsList.add("Vat Number");
        }

        if(invalidFieldsList.isEmpty()) {
            return true;
        } else {
            showInvalidFieldsDialog(invalidFieldsList);
            return false;
        }
    }

    private void showInvalidFieldsDialog(List<String> invalidFieldsList) {
        String errorMessage =
                "Please check: " + invalidFieldsList.stream()
                            .map(field -> "*\t " + field + " ")
                            .collect(Collectors.joining())
                            .concat(" field(s)!");

        dialogMessageFactory.getDialogMessage(errorMessage, DialogMessageFactory.MessageType.WARNING);
    }
}
