package com.employee.recordsystem.ui.panels;

import com.employee.recordsystem.ui.MainFrame;
import com.employee.recordsystem.ui.service.AuthService;
import com.employee.recordsystem.dto.auth.JwtAuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginPanelTest {

    @Mock
    private MainFrame mainFrame;

    @Mock
    private AuthService authService;

    private LoginPanel loginPanel;

    @BeforeEach
    void setUp() {
        loginPanel = new LoginPanel(mainFrame);
        // Use reflection to inject mock AuthService
        try {
            java.lang.reflect.Field authServiceField = LoginPanel.class.getDeclaredField("authService");
            authServiceField.setAccessible(true);
            authServiceField.set(loginPanel, authService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSuccessfulLogin() throws IOException {
        // Given
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setUsername("testUser");
        response.setRole("ROLE_USER");
        when(authService.login(anyString(), anyString())).thenReturn(response);

        // When
        // Simulate button click
        Component[] components = loginPanel.getComponents();
        JButton loginButton = null;
        for (Component component : components) {
            if (component instanceof JButton) {
                loginButton = (JButton) component;
                break;
            }
        }
        assert loginButton != null;
        loginButton.doClick();

        // Then
        verify(mainFrame).setCurrentUser("testUser", "ROLE_USER");
        verify(mainFrame).showEmployeePanel();
    }

    @Test
    void testFailedLogin() throws IOException {
        // Given
        when(authService.login(anyString(), anyString()))
            .thenThrow(new IOException("Invalid credentials"));

        // When
        Component[] components = loginPanel.getComponents();
        JButton loginButton = null;
        for (Component component : components) {
            if (component instanceof JButton) {
                loginButton = (JButton) component;
                break;
            }
        }
        assert loginButton != null;
        loginButton.doClick();

        // Then
        verify(mainFrame, never()).setCurrentUser(anyString(), anyString());
        verify(mainFrame, never()).showEmployeePanel();
    }
}
