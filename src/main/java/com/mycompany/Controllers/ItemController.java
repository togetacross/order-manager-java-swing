package com.mycompany.Controllers;

import com.mycompany.Controllers.util.DialogMessageFactory;
import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.DAO.CostumerDAO;
import com.mycompany.DAO.ItemDAO;
import com.mycompany.DAO.OrderDAO;
import com.mycompany.DAO.ServiceDAO;
import com.mycompany.Models.Costumer;
import com.mycompany.Models.Item;
import com.mycompany.Models.Order;
import com.mycompany.Models.Service;
import com.mycompany.Swing.ModifyDialog;
import com.mycompany.Swing.NewOrderDialog;
import com.mycompany.Swing.SelectCostumerDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemController {

    private DefaultTableModel itemTable;
    private NewOrderDialog newOrderDialog;
    private ModifyDialog modifyItemDialog;
    private ServiceDAO serviceDao;
    private CostumerDAO costumerDao;
    private OrderDAO orderDao;
    private ItemDAO itemDao;
    private DialogMessageFactory dialogMessageFactory;
    private Costumer selectedCostumer = new Costumer();
    private static final String SQUARE_METER_REGEX = "^((\\d)+(\\.)?(\\d)*)$";

    public ItemController(Connection connection) {
        itemTable = new DefaultTableModel(new String[]{"ID", "Service", "Square Meter", "Price (HUF)"}, 0);
        dialogMessageFactory = new DialogMessageFactory();
        try {
            costumerDao = new CostumerDAO(connection);
            serviceDao = new ServiceDAO(connection);
            orderDao = new OrderDAO(connection);
            itemDao = new ItemDAO(connection);
        } catch (CustomDatabaseException ex) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            ex.printStackTrace();
        }
        initController();
    }

    private void initController() {
        List<Service> serviceList = new ArrayList<>();
        try {
            serviceList = serviceDao.findAll();
        } catch (CustomDatabaseException ex) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            ex.printStackTrace();
        }
        newOrderDialog = new NewOrderDialog(serviceList, itemTable);
        newOrderDialog.setLocationRelativeTo(null);
        newOrderDialog.getContentPane().setBackground(Color.DARK_GRAY);

        newOrderDialog.getBtAdd().addActionListener(e -> addServiceToTable());
        newOrderDialog.getBtSave().addActionListener(e -> save());
        newOrderDialog.getBtDelete().addActionListener(e -> delete());
        newOrderDialog.getBtSelectCostumer().addActionListener(e -> selectCostumer());
        newOrderDialog.getBtModify().addActionListener(e -> initModifyItem());
        itemTable.addTableModelListener(e -> getTotalPrice());

        newOrderDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    itemDao.close();
                    orderDao.close();
                    serviceDao.close();
                    costumerDao.close();
                } catch (CustomDatabaseException e) {
                    dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
                    e.printStackTrace();
                }
            }
        });
        newOrderDialog.setModal(true);
        newOrderDialog.setVisible(true);
    }

    /**
     * Add a new row to item table
     */
    private void addServiceToTable() {
        if (!isValidSquareMeter(newOrderDialog.getTfSquareMeter().getText())) {
            dialogMessageFactory.getDialogMessage("Square Meter field is empty or not valid!", DialogMessageFactory.MessageType.WARNING);
        } else if (newOrderDialog.getjLService().getSelectedIndex() == -1) {
            dialogMessageFactory.getDialogMessage("Please select a Service!", DialogMessageFactory.MessageType.WARNING);
        } else {
            int price = (int) (Double.parseDouble(newOrderDialog.getTfSquareMeter().getText()) * newOrderDialog.getjLService().getSelectedValue().getPrice());
            itemTable.addRow(
                    new String[]{
                            String.valueOf(newOrderDialog.getjLService().getSelectedValue().getId()),
                            newOrderDialog.getjLService().getSelectedValue().getName(),
                            formatSquareMeter(newOrderDialog.getTfSquareMeter().getText()),
                            formatPrice(price)
                    });
            newOrderDialog.getTfSquareMeter().setText("");
        }
    }

    /**
     * Initialize new JDialog for modify item by selected item row
     * Fill input fields with selected row item values
     */
    private void initModifyItem() {
        if (this.newOrderDialog.getjTable1().getSelectedRow() != -1) {
            try {
                modifyItemDialog = new ModifyDialog(
                        serviceDao.findAll(),
                        (String) itemTable.getValueAt(newOrderDialog.getjTable1().getSelectedRow(), 0),
                        (String) itemTable.getValueAt(newOrderDialog.getjTable1().getSelectedRow(), 2)
                );
                modifyItemDialog.setLocationRelativeTo(null);
                modifyItemDialog.setTitle("Modify Item");
                modifyItemDialog.getContentPane().setBackground(Color.DARK_GRAY);
                modifyItemDialog.getBtModify().addActionListener(e -> modifyItem());
                modifyItemDialog.getBtCancel().addActionListener(e -> cancelModifyService());
                modifyItemDialog.setModal(true);
                modifyItemDialog.setVisible(true);
            } catch (CustomDatabaseException ex) {
                dialogMessageFactory.getDialogMessage("Modification failed!", DialogMessageFactory.MessageType.WARNING);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Modify the selected item row values
     */
    private void modifyItem() {
        if (modifyItemDialog.getService() != null) {
            int price = (int) (Double.parseDouble(modifyItemDialog.getTfSquareMeter().getText()) * modifyItemDialog.getService().getPrice());
            int selectedRow = newOrderDialog.getjTable1().getSelectedRow();
            itemTable.setValueAt(String.valueOf(modifyItemDialog.getService().getId()), selectedRow, 0);
            itemTable.setValueAt(modifyItemDialog.getService().getName(), selectedRow, 1);
            itemTable.setValueAt(formatSquareMeter(modifyItemDialog.getTfSquareMeter().getText()), selectedRow, 2);
            itemTable.setValueAt(String.valueOf(price), selectedRow, 3);
            modifyItemDialog.setVisible(false);
        } else {
            dialogMessageFactory.getDialogMessage("Square Meter field is empty or service not selected!", DialogMessageFactory.MessageType.WARNING);
        }
    }

    private void cancelModifyService() {
        modifyItemDialog.setVisible(false);
    }

    /**
     * Save the order and their items to database
     * Reset the ItemDialog inputs and lists
     */
    private void save() {
        String dialogMessage = "";
        if (selectedCostumer.getId() == null) {
            dialogMessage = "Please select a Costumer!";
        } else if (itemTable.getRowCount() == 0) {
            dialogMessage = "Please add an Item!";
        } else {
            try {
                Order order = new Order();
                order.setDate(LocalDate.now());
                order.setCostumerId(selectedCostumer.getId());
                int orderId = orderDao.insert(order);
                
                for (int i = 0; i < itemTable.getRowCount(); i++) {
                    Item item = new Item();
                    item.setServiceId(Integer.parseInt((String) itemTable.getValueAt(i, 0)));
                    item.setSquareMeter(Double.parseDouble((String) itemTable.getValueAt(i, 2)));
                    item.setOrderId(orderId);
                    itemDao.insert(item);
                }

                itemTable.setRowCount(0);
                newOrderDialog.getLbCostumer().setText("");
                dialogMessage = "Order saving successfully!";
            } catch (CustomDatabaseException ex) {
                dialogMessageFactory.getDialogMessage("Saving failed!", DialogMessageFactory.MessageType.ERROR);
                ex.printStackTrace();
            }
        }
        dialogMessageFactory.getDialogMessage( dialogMessage, DialogMessageFactory.MessageType.INFO);
    }

    /**
     * Delete the selected item row from order by table index
     */
    private void delete() {
        if (newOrderDialog.getjTable1().getSelectedRow() != -1) {
            itemTable.removeRow(newOrderDialog.getjTable1().getSelectedRow());
        }
    }

    /**
     * Initialize a new jDialog for select a Costumer
     * Get all costumers in list to select
     */
    public void selectCostumer() {
        try {
            SelectCostumerDialog dialog = new SelectCostumerDialog(new javax.swing.JFrame(), costumerDao.findAll());
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setTitle("Select Costumer");
            dialog.setVisible(true);
            if (dialog.isSelected()) {
                selectedCostumer = dialog.getSelected();
                newOrderDialog.getLbCostumer().setText(selectedCostumer.getName());
            }
        } catch (CustomDatabaseException ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Calculate total price of added items
     */
    private void getTotalPrice() {
       int sum = 0;
        for (int i = 0; i < itemTable.getRowCount(); i++) {
            sum += parsePrice((String)itemTable.getValueAt(i, 3));
        }
        newOrderDialog.getLbPrice().setText(formatPrice(sum));
    }

    /**
     * Check square meter is valid string format
     *
     * @return true, if valid
     */
    private boolean isValidSquareMeter(String squareMeter) {
        Pattern pattern = Pattern.compile(SQUARE_METER_REGEX);
        Matcher mat = pattern.matcher(squareMeter);
        return mat.matches();
    }

    private String formatSquareMeter(String squareMeter) {
        return String.valueOf(Double.parseDouble(squareMeter));
    }

    /**
     * Format int price to String format
     * @param price order items price
     */
    private String formatPrice(int price){
        NumberFormat numberFormatter = NumberFormat.getInstance(new Locale("hu", "HU"));
        return numberFormatter.format(price);
    }

    private int parsePrice(String priceText){
        NumberFormat numberFormatter = NumberFormat.getInstance(new Locale("hu", "HU"));
        Number number = null;
        try {
            number = numberFormatter.parse(priceText);
        } catch (ParseException e) {
            e.printStackTrace();
            dialogMessageFactory.getDialogMessage("Something went wrong!", DialogMessageFactory.MessageType.ERROR);
        }
        return number != null ? number.intValue() : 0;
    }

}
