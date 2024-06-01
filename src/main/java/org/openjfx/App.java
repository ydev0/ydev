package org.openjfx;

import com.ydev00.model.user.User;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class App extends Application {
    public static User loggedUser;
    public static User pageUser;
    public static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            scene = new Scene(loadFXML("Login"));
            scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/LoginSignup.css").toExternalForm());
            stage.setScene(scene);

            stage.setMaximized(true);
            stage.setResizable(false);
            stage.show();
        } catch(IOException | NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void launchApp(){
        launch();
    }

}
