package org.openjfx;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class Main extends Application{

    private static  Scene scene;

    @Override
    public void start(Stage stage) throws IOException{
        try {
            scene = new Scene(loadFXML("Signup"));
            scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/LoginSignup.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(false);
            stage.show();
        } catch(IOException | NullPointerException e){
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args) {
        launch();
    }


}
