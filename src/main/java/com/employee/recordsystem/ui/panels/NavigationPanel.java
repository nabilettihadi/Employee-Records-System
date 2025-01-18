package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.ui.MainFrame;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class NavigationPanel extends JPanel {
    private final MainFrame mainFrame;
    private JLabel userInfoLabel;

    public NavigationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createEtchedBorder());

        // User info panel at the top
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userInfoLabel = new JLabel("Not logged in");
        userPanel.add(userInfoLabel, BorderLayout.CENTER);
        add(userPanel, BorderLayout.NORTH);

        // Navigation buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addNavigationButton(buttonPanel, "Employees", e -> mainFrame.showEmployeePanel());
        addNavigationButton(buttonPanel, "Departments", e -> mainFrame.showDepartmentPanel());

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void addNavigationButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.addActionListener(listener);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    public void updateUserInfo(String username, String role) {
        userInfoLabel.setText(String.format("<html>User: %s<br>Role: %s</html>", username, role));
    }
}
