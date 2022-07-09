package com.mycompany.Controllers;

import com.mycompany.Controllers.util.DialogMessageFactory;
import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.CustomExceptions.CustomIOException;
import com.mycompany.DAO.CostumerDAO;
import com.mycompany.DAO.ItemDAO;
import com.mycompany.DAO.OrderDAO;
import com.mycompany.DAO.ServiceDAO;
import com.mycompany.IO.TemplateIO;
import com.mycompany.IO.WriterIO;
import com.mycompany.Models.ItemWithService;
import com.mycompany.Models.OrderWithUser;
import com.mycompany.Swing.OrderDetailsDialog;
import com.mycompany.Swing.OrdersDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlling Order Dialogs
 */
public class OrderController {

    private DefaultTableModel orderTable;
    private DefaultTableModel itemTable;
    private DialogMessageFactory dialogMessageFactory;
    private OrdersDialog orderDialog;
    private OrderDAO orderDao;
    private ItemDAO itemDao;
    private CostumerDAO costumerDao;
    private ServiceDAO serviceDao;
    private static final String TEMPLATE_NAME = "template.txt";

    public OrderController(Connection connection) {
        orderTable = new DefaultTableModel(new String[]{"ID", "Costumer ID", "Costumer Name", "Date", "Total Price"}, 0);
        itemTable = new DefaultTableModel(new String[]{"Service", "Square Meter", "Price"}, 0);
        dialogMessageFactory = new DialogMessageFactory();
        try {
            orderDao = new OrderDAO(connection);
            itemDao = new ItemDAO(connection);
            costumerDao = new CostumerDAO(connection);
            serviceDao = new ServiceDAO(connection);
        } catch (CustomDatabaseException e) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            e.printStackTrace();
        }
        fillOrderTable();
        initController();
    }

    /**
     * Init orderDialog
     */
    private void initController() {
        orderDialog = new OrdersDialog(orderTable);
        orderDialog.setLocationRelativeTo(null);
        orderDialog.getContentPane().setBackground(Color.DARK_GRAY);
        orderDialog.setTitle("Orders");
        orderDialog.getBtView().addActionListener(e -> initOrderDetailsDialog());
        orderDialog.getBtDelete().addActionListener(e -> deleteOrder());
        orderDialog.getBtSaveToFile().addActionListener(e -> collectToWriter());

        orderDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    orderDao.close();
                    itemDao.close();
                    costumerDao.close();
                    serviceDao.close();
                } catch (CustomDatabaseException e) {
                    dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
                    e.printStackTrace();
                }
            }
        });
        orderDialog.setModal(true);
        orderDialog.setVisible(true);
    }

    /**
     * Get all order with them costumer name by id of costumer and fill the orderTable
     */
    private void fillOrderTable() {
        try {
            for (OrderWithUser orderWithUser : orderDao.findAllWithUser()) {
                orderTable.addRow(new String[]{
                        orderWithUser.getOrder().getId().toString(),
                        String.valueOf(orderWithUser.getOrder().getCostumerId()),
                        orderWithUser.getCostumer().getName(),
                        orderWithUser.getOrder().getDate().toString(),
                        formatPrice(orderDao.totalPrice(orderWithUser.getOrder().getId()))
                });
            }
        } catch (CustomDatabaseException ex) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            ex.printStackTrace();
        }
    }

    /**
     * Initialize a new dialog for order details by index of orderTable and
     * fill the service table with services of order
     */
    private void initOrderDetailsDialog() {
        if (orderDialog.getjTable1().getSelectedRow() != -1) {
            fillItemTableByOrderIndex();
            OrderDetailsDialog dialog = new OrderDetailsDialog(new javax.swing.JFrame(), itemTable);
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            dialog.setTitle("Order Details");
            orderDialog.setModal(true);
            dialog.setVisible(true);
        }
    }

    /**
     * Delete from orderTable by orderTable index
     * Refresh orderTable rows
     */
    private void deleteOrder() {
        if (orderDialog.getjTable1().getSelectedRow() != -1) {
            int orderId = Integer.parseInt((String) orderTable.getValueAt(orderDialog.getjTable1().getSelectedRow(), 0));
            try {
                itemDao.delete(orderId);
                orderDao.delete(orderId);
                orderTable.setNumRows(0);
                fillOrderTable();
            } catch (CustomDatabaseException ex) {
                dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Format int price to String format
     * @param price order items price
     */
    private String formatPrice(int price) {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(price);
    }

    /**
     * Fill item itemTable by index of orderTable
     */
    private void fillItemTableByOrderIndex() {
        try {
            itemTable.setRowCount(0);
            List<ItemWithService> itemWithServicesList = itemDao.findByIdWithService(Integer.parseInt((String) orderTable.getValueAt(orderDialog.getjTable1().getSelectedRow(), 0)));
            for (ItemWithService itemWithService : itemWithServicesList) {

                int price = (int) (itemWithService.getService().getPrice() * itemWithService.getItem().getSquareMeter());
                itemTable.addRow(new String[]{
                        itemWithService.getService().getName(),
                        String.valueOf(itemWithService.getItem().getSquareMeter()),
                        formatPrice((price))});
            }
        } catch (CustomDatabaseException ex) {
            dialogMessageFactory.getDialogMessage("Database error!", DialogMessageFactory.MessageType.ERROR);
            ex.printStackTrace();
        }
    }

    /**
     * Collect order details for writerIO to write in file by index of orderTable
     * Write to file
     */
    private void collectToWriter() {

        int selectedIndex = orderDialog.getjTable1().getSelectedRow();
        if (selectedIndex != -1) {
            List<String[]> itemList = new ArrayList<>();
            fillItemTableByOrderIndex();
            for (int i = 0; i < itemTable.getRowCount(); i++) {
                String[] item = new String[3];
                item[0] = (String) itemTable.getValueAt(i, 0);
                item[1] = (String) itemTable.getValueAt(i, 1);
                item[2] = (String) itemTable.getValueAt(i, 2);
                itemList.add(item);
            }
            String[] order = new String[4];
            order[0] = (String) orderTable.getValueAt(selectedIndex, 0);
            order[1] = (String) orderTable.getValueAt(selectedIndex, 2);
            order[2] = (String) orderTable.getValueAt(selectedIndex, 3);
            order[3] = (String) orderTable.getValueAt(selectedIndex, 4);


            TemplateIO template;
            WriterIO writerIo = null;

            try {
                template =new TemplateIO(TEMPLATE_NAME);
                writerIo = new WriterIO(template, itemList, order);
            } catch (CustomIOException ex) {
                dialogMessageFactory.getDialogMessage("Cannot read company or template file!", DialogMessageFactory.MessageType.ERROR);
                ex.printStackTrace();
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(writerIo.getFileName()));

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    writerIo.writeToFile(file.getPath());
                } catch (CustomIOException ex) {
                    dialogMessageFactory.getDialogMessage("Writing file error!", DialogMessageFactory.MessageType.ERROR);
                    ex.printStackTrace();
                }
            }

        }
    }

}
