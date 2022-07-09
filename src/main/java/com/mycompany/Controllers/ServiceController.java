package com.mycompany.Controllers;

import com.mycompany.Controllers.util.DialogMessageFactory;
import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.DAO.ServiceDAO;
import com.mycompany.Models.Service;
import com.mycompany.Swing.NewServiceDialog;
import com.mycompany.Swing.ServicesDialog;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.text.NumberFormat;

public class ServiceController {

    private ServicesDialog servicesDialog;
    private NewServiceDialog newServiceDialog;
    private DefaultTableModel table;
    private ServiceDAO serviceDao;
    private DialogMessageFactory dialogMessageFactory;

    public ServiceController(Connection connection) {
        table = new DefaultTableModel(new String[]{"ID", "Name", "Price(HUF)"}, 0);
        dialogMessageFactory = new DialogMessageFactory();
        try {
            serviceDao = new ServiceDAO(connection);
        } catch (CustomDatabaseException ex) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            ex.printStackTrace();
        }
        fillServiceTable();
        initController();
    }

    private void initController() {
        servicesDialog = new ServicesDialog(table);
        servicesDialog.setLocationRelativeTo(null);
        servicesDialog.getContentPane().setBackground(Color.DARK_GRAY);
        servicesDialog.setTitle("Services");
        this.servicesDialog.getBtBack().addActionListener(e -> closeServicesDialog());
        this.servicesDialog.getBtNew().addActionListener(e -> newServiceDialog());

        servicesDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    serviceDao.close();
                } catch (CustomDatabaseException ex) {
                    dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
                    ex.printStackTrace();
                }
            }
        });

        servicesDialog.setModal(true);
        servicesDialog.setVisible(true);
    }

    private void newServiceDialog() {
        newServiceDialog = new NewServiceDialog();
        newServiceDialog.setLocationRelativeTo(null);
        newServiceDialog.getContentPane().setBackground(Color.DARK_GRAY);
        newServiceDialog.setTitle("New Service");
        newServiceDialog.getBtSave().addActionListener(e -> confirmNewServiceDialog());
        newServiceDialog.getBtCancel().addActionListener(e -> closeNewServiceDialog());
        newServiceDialog.setModal(true);
        newServiceDialog.setVisible(true);
    }

    private void closeNewServiceDialog() {
        newServiceDialog.setVisible(false);
    }

    private void confirmNewServiceDialog() {
        Service service = new Service();
        service.setName(newServiceDialog.getTfName().getText());
        service.setPrice(Integer.parseInt(newServiceDialog.getTfPrice().getText()));
        try {
            serviceDao.save(service);
            fillServiceTable();
        } catch (CustomDatabaseException ex) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            ex.printStackTrace();
        }
        closeNewServiceDialog();
    }

    private String numberFormat(int price) {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(price);
    }

    private void closeServicesDialog() {
        servicesDialog.setVisible(false);
    }

    private void fillServiceTable() {
        try {
            table.setRowCount(0);
            serviceDao.findAll().forEach(service -> {
                String[] row = new String[]{
                        String.valueOf(service.getId()),
                        service.getName(),
                        numberFormat((int) service.getPrice())
                };
                table.addRow(row);
                }
            );
        } catch (CustomDatabaseException ex) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            ex.printStackTrace();
        }
    }

}
