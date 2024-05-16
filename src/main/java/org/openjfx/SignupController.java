
package org.openjfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


public class SignupController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button imageButton;

    public String emailInput;
    public String passwordInput;
    public String usernameInput;
    public File profilePic;

    public void signup(ActionEvent event){
        emailInput = emailField.getText();
        passwordInput = passwordField.getText();
        usernameInput = usernameField.getText();
        try {
            FileInputStream fileInputStream = new FileInputStream(profilePic);
            byte[] byteArray = new byte[(int) profilePic.length()];
            fileInputStream.read(byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchScene(ActionEvent event){
        try{
            Main.setRoot("Login");
        } catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void chooseImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha uma foto de perfil");
        profilePic = fileChooser.showOpenDialog(emailField.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}