package salesapp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import salesapp.service.Service;

import java.io.IOException;

public class RegisterController {
    private Service srv;
    public Hyperlink switchToLoginBtn;

    public void setSrv(Service srv) {
        this.srv = srv;
    }

    public void initialize() {
        switchToLoginBtn.setOnAction(e -> switchToLogin());
    }

    private void switchToLogin() {
        Scene scene;

        try {
            FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/gui/login-window.fxml"));
            scene = new Scene(loginLoader.load());
            LoginController controller = loginLoader.getController();
            controller.setSrv(srv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        ((Stage) switchToLoginBtn.getScene().getWindow()).close();
    }
}
