package com.employee.recordsystem.ui.dialogs;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class DeleteConfirmationDialog extends JDialog {
    private boolean confirmed = false;

    public DeleteConfirmationDialog(Frame owner, String itemType, String itemName) {
        super(owner, "Confirm Delete", true);
        setLayout(new MigLayout("fillx", "[]", "[]20[]"));

        // Warning message
        String message = String.format("<html><body>Are you sure you want to delete the %s:<br><b>%s</b>?<br><br>" +
            "This action cannot be undone.</body></html>", itemType, itemName);
        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(new Color(204, 0, 0));

        // Buttons
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(204, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Cancel");

        // Add components
        add(messageLabel, "wrap");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, "span, center");

        // Add actions
        deleteButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());

        // Set dialog properties
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
