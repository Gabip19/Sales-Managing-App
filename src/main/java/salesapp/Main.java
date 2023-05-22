package salesapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import salesapp.controller.LoginController;
import salesapp.service.Service;

import java.io.IOException;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/gui/login-window.fxml"));
        Scene loginScene = new Scene(loginLoader.load());
        LoginController ctr = loginLoader.getController();
        ctr.setSrv(getService());

        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }

    static Service getService(){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        return context.getBean(Service.class);
    }
}