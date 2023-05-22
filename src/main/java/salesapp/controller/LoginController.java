package salesapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import salesapp.domain.User;
import salesapp.service.Service;

import java.io.IOException;


public class LoginController {
    public TextField usernameField;
    public PasswordField passwordField;
    public Label errorLabel;
    public Button loginBtn;
    public Hyperlink switchToRegisterBtn;

    private Service srv;

    public void setSrv(Service srv) {
        this.srv = srv;
    }

    public void initialize() {
        loginBtn.setOnAction((e) -> login());
    }

    private void login() {
        System.out.println("MERGE");
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            return;
        }

        User loggedUser = new User("", "", username, password, false);
        try {
            loggedUser = srv.attemptLogin(loggedUser);
            openMainWindow(loggedUser);
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void openMainWindow(User user) {
        Scene scene;
        MainWindowController.setSrv(srv);
        MainWindowController.setCurrentUser(user);

        if (user.isAdmin()) {
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/gui/admin-window.fxml"));
                scene = new Scene(loginLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/gui/sales-agent-window.fxml"));
                scene = new Scene(loginLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Stage stage = new Stage();
        stage.setTitle("Main window");
        stage.setScene(scene);
        stage.show();
    }
}
