package com.mycompany.Swing;

import com.mycompany.Controllers.*;
import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.DAO.ConnectionFactory;

import java.awt.*;
import java.sql.Connection;

public class MainFrame extends javax.swing.JFrame {

    private Connection connection;

    public MainFrame() {
        initComponents();
        getContentPane().setBackground(Color.DARK_GRAY);
        setTitle("Menu");
        setLocationRelativeTo(null);

        try {
            connection = ConnectionFactory.getConnection();
        } catch (CustomDatabaseException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btNewOrder = new javax.swing.JButton();
        btOrders = new javax.swing.JButton();
        btServices = new javax.swing.JButton();
        btCostumers = new javax.swing.JButton();
        btMyCompany = new javax.swing.JButton();
        lbTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btNewOrder.setText("New Order");
        btNewOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNewOrderActionPerformed(evt);
            }
        });

        btOrders.setText("Orders");
        btOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOrdersActionPerformed(evt);
            }
        });

        btServices.setText("Services");
        btServices.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btServices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btServicesActionPerformed(evt);
            }
        });

        btCostumers.setText("Costumers");
        btCostumers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCostumersActionPerformed(evt);
            }
        });

        btMyCompany.setText("My Company");
        btMyCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMyCompanyActionPerformed(evt);
            }
        });

        lbTitle.setFont(new java.awt.Font("Rockwell", 3, 18)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(255, 255, 255));
        lbTitle.setText("FelújítLak Kft.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btServices, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btCostumers, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(btMyCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbTitle)
                .addGap(149, 149, 149))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btServices, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btCostumers, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btMyCompany)
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOrdersActionPerformed
        OrderController controller = new OrderController(connection);
    }//GEN-LAST:event_btOrdersActionPerformed

    private void btServicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btServicesActionPerformed
        ServiceController controller = new ServiceController(connection);
    }//GEN-LAST:event_btServicesActionPerformed

    private void btCostumersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCostumersActionPerformed
        CostumerController controller = new CostumerController(connection);
    }//GEN-LAST:event_btCostumersActionPerformed

    private void btMyCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMyCompanyActionPerformed
        CompanyController controller = new CompanyController();
    }//GEN-LAST:event_btMyCompanyActionPerformed

    private void btNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewOrderActionPerformed
        ItemController controller = new ItemController(connection);
    }//GEN-LAST:event_btNewOrderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCostumers;
    private javax.swing.JButton btMyCompany;
    private javax.swing.JButton btNewOrder;
    private javax.swing.JButton btOrders;
    private javax.swing.JButton btServices;
    private javax.swing.JLabel lbTitle;
    // End of variables declaration//GEN-END:variables
}
