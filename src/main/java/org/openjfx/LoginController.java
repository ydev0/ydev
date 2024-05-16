
package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;


public class LoginController implements Initializable {
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public String emailInput;
    public String passwordInput;

    public void login(ActionEvent event){
        emailInput = emailField.getText();
        passwordInput = passwordField.getText();
        System.out.println(emailInput + "\n" + passwordInput);
    }
    
    public void switchScene(ActionEvent event){
        try{
            Main.setRoot("Signup");
        } catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}