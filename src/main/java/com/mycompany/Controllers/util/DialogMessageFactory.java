package com.mycompany.Controllers.util;

import javax.swing.*;
import java.awt.*;

public class DialogMessageFactory {

    public enum MessageType {
        ERROR,
        INFO,
        WARNING
    }

    private JOptionPane jOptionPane;
    private JLabel label;
    private JPanel panel;
    private String errorImagePath = "/images/error.png";
    private String infoImagePath = "/images/info.png";
    private String warningImagePath = "/images/warning.png";

    public DialogMessageFactory () {
        panel = new JPanel();
        label = new JLabel();
        label.setOpaque(true);
        label.setFont(new Font("Serif", Font.BOLD, 14));
        jOptionPane = new JOptionPane();
    }

    public void getDialogMessage(String messageText, MessageType type) {
        label.setText(messageText);
        switch (type){
            case INFO:
                label.setForeground(Color.BLUE);
                label.setIcon(getIcon(infoImagePath));
                panel.add(label);
                jOptionPane.showMessageDialog(null, panel, "Info", JOptionPane.DEFAULT_OPTION);
                break;
            case ERROR:
                label.setForeground(Color.RED);
                label.setIcon(getIcon(errorImagePath));
                panel.add(label);
                jOptionPane.showMessageDialog(null, panel, "Error", JOptionPane.DEFAULT_OPTION);
                break;
            case WARNING:
                label.setForeground(Color.BLACK);
                label.setIcon(getIcon(warningImagePath));
                panel.add(label);
                jOptionPane.showMessageDialog(null, panel, "Warning", JOptionPane.DEFAULT_OPTION);
                break;
            default:
                break;
        }
    }

    private ImageIcon getIcon(String fileName)  {
        return new ImageIcon(this.getClass().getResource(fileName));
    }
}
