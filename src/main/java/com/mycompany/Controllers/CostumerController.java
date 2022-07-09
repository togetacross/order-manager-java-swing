package com.mycompany.Controllers;

import com.mycompany.Controllers.util.DialogMessageFactory;
import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.DAO.CostumerDAO;
import com.mycompany.Models.Costumer;
import com.mycompany.Swing.CostumerDialog;
import com.mycompany.Swing.NewModifyDialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CostumerController {

    private CostumerDialog costumerDialog;
    private NewModifyDialog newModifyDialog;
    private DefaultTableModel table;
    private DialogMessageFactory dialogMessageFactory;
    private CostumerDAO costumerDao;

    public CostumerController(Connection connection)  {
        table = new DefaultTableModel(new String[]{"ID", "Name", "Address", "Phone", "Email", "Vat number"}, 0);
        dialogMessageFactory = new DialogMessageFactory();
        initController(connection);
    }

    private void initController(Connection connection) {
        try {
            costumerDao = new CostumerDAO(connection);
        } catch (CustomDatabaseException e) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            e.printStackTrace();
        }

        fillCostumerTable();
        costumerDialog = new CostumerDialog(table);
        costumerDialog.setLocationRelativeTo(null);
        costumerDialog.getContentPane().setBackground(Color.DARK_GRAY);
        costumerDialog.setTitle("Costumers");

        costumerDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    costumerDao.close();
                } catch (CustomDatabaseException e) {
                    dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
                    e.printStackTrace();
                }
            }
        });
        costumerDialog.getBtNew().addActionListener(e -> initNewCostumerDialog());
        costumerDialog.getBtModify().addActionListener(e -> initModifyCostumerDialog());
        costumerDialog.getBtDelete().addActionListener(e -> deleteCostumerAction());
        costumerDialog.getBtBack().addActionListener(e -> closeCostumersDialog());
        costumerDialog.setModal(true);
        costumerDialog.setVisible(true);
    }

    private void initNewCostumerDialog() {
        newModifyDialog = new NewModifyDialog(costumerDialog);
        newModifyDialog.setLocationRelativeTo(null);
        newModifyDialog.getContentPane().setBackground(Color.DARK_GRAY);
        newModifyDialog.setTitle("New Costumer");
        newModifyDialog.getBtOk().addActionListener(e -> confirmDialogAction());
        newModifyDialog.getBtCancel().addActionListener(e -> closeNewModifyDialog());
        newModifyDialog.setVisible(true);
    }

    private void initModifyCostumerDialog() {
        if (costumerDialog.getjTable().getSelectedRow() != -1) {
            newModifyDialog = new NewModifyDialog(costumerDialog);
            newModifyDialog.setLocationRelativeTo(null);
            newModifyDialog.getContentPane().setBackground(Color.DARK_GRAY);
            newModifyDialog.setTitle("Modify Costumer");
            newModifyDialog.getTfId().setText((String) table.getValueAt(costumerDialog.getjTable().getSelectedRow(), 0));
            newModifyDialog.getTfName().setText((String) table.getValueAt(costumerDialog.getjTable().getSelectedRow(), 1));
            newModifyDialog.getTfAddress().setText((String) table.getValueAt(costumerDialog.getjTable().getSelectedRow(), 2));
            newModifyDialog.getTfPhoneNumber().setText((String) table.getValueAt(costumerDialog.getjTable().getSelectedRow(), 3));
            newModifyDialog.getTfEmail().setText((String) table.getValueAt(costumerDialog.getjTable().getSelectedRow(), 4));
            newModifyDialog.getTfVatNumber().setText((String) table.getValueAt(costumerDialog.getjTable().getSelectedRow(), 5));
            newModifyDialog.getBtOk().addActionListener(e -> confirmDialogAction());
            newModifyDialog.getBtCancel().addActionListener(e -> closeNewModifyDialog());
            newModifyDialog.setVisible(true);
        }
    }

    private void deleteCostumerAction() {
        if (costumerDialog.getjTable().getSelectedRow() != -1) {
            try {
                costumerDao.delete(Integer.parseInt((String) table.getValueAt(costumerDialog.getjTable().getSelectedRow(), 0)));
                fillCostumerTable();
            } catch (CustomDatabaseException ex) {
                dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
                ex.printStackTrace();
            }
        }
    }

    private void confirmDialogAction() {
        if (isValidCostumer()) {
            Costumer costumer = new Costumer();
            costumer.setName(newModifyDialog.getTfName().getText());
            costumer.setAddress(newModifyDialog.getTfAddress().getText());
            costumer.setPhoneNumber(newModifyDialog.getTfPhoneNumber().getText());
            costumer.setEmail(newModifyDialog.getTfEmail().getText());
            costumer.setVatNumber(newModifyDialog.getTfVatNumber().getText());
            try {
                if (!newModifyDialog.getTfId().getText().isEmpty()) {
                    costumer.setId(Integer.parseInt(newModifyDialog.getTfId().getText()));
                    costumerDao.update(costumer);
                } else {
                    costumerDao.insert(costumer);
                }
                fillCostumerTable();
            } catch (CustomDatabaseException ex) {
                dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
                ex.printStackTrace();
            }
            newModifyDialog.setVisible(false);
        }
    }

    private void closeNewModifyDialog() {
        newModifyDialog.setVisible(false);
    }

    private void closeCostumersDialog() {
        costumerDialog.setVisible(false);
    }

    private boolean isValidCostumer() {
        List<JTextField> fields = new ArrayList<>();
        fields.add(newModifyDialog.getTfName());
        fields.add(newModifyDialog.getTfAddress());
        fields.add(newModifyDialog.getTfPhoneNumber());
        fields.add(newModifyDialog.getTfEmail());
        fields.add(newModifyDialog.getTfVatNumber());

        Border errorBorder = BorderFactory.createLineBorder(Color.RED);
        Border successBorder = BorderFactory.createLineBorder(Color.GREEN);

        fields.forEach(field -> field.setBorder(field.getText().isEmpty() ? errorBorder : successBorder));

        return !fields.stream().anyMatch(field -> field.getText().isEmpty());
    }

    private void fillCostumerTable() {
        try {
            table.setRowCount(0);
            costumerDao.findAll().forEach(costumer -> {
                String[] row = new String[]{
                        String.valueOf(costumer.getId()),
                        costumer.getName(),
                        costumer.getAddress(),
                        costumer.getPhoneNumber(),
                        costumer.getEmail(),
                        costumer.getVatNumber()
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
