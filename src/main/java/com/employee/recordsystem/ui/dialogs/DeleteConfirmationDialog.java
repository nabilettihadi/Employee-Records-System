package com.employee.recordsystem.ui.dialogs;

import javax.swing.*;
import java.awt.*;

public class DeleteConfirmationDialog extends JDialog {
    private boolean confirmed = false;

    public DeleteConfirmationDialog(Frame owner, String itemName) {
        super(owner, "Confirm Delete", true);
        
        setLayout(new BorderLayout(10, 10));
        
        // Message panel
        JPanel messagePanel = new JPanel();
        messagePanel.add(new JLabel("Are you sure you want to delete " + itemName + "?"));
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");
        
        deleteButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        
        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
