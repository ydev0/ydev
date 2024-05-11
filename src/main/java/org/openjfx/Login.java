package org.openjfx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 *
 * @author carlosenogs
 */
public class Login extends Application{
    
    public void start(Stage stage){
        try {
            Parent root = FXMLLoader.load(getClass.getResource("Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);   
            stage.show();       
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    
}
