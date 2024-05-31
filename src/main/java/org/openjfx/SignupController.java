
package org.openjfx;

import com.google.gson.Gson;
import com.ydev00.model.user.User;
import com.ydev00.model.image.Image;
import com.ydev00.model.image.ImageData;
import org.apache.commons.io.FilenameUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.http.HttpResponse;
import org.openjfx.util.ImageHandler;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

public class SignupController implements Initializable, SceneSwitcher {
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

    public User signup(ActionEvent event) throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        ImageHandler imageHandler = new ImageHandler();
        Gson gson = new Gson();

        emailInput = emailField.getText();
        passwordInput = passwordField.getText();
        usernameInput = usernameField.getText();

        if(emailInput.isEmpty() || passwordInput.isEmpty() || usernameInput.isEmpty() || profilePic == null){
            System.out.println("Empty fields");
            return null;
        }

        Image profileImg = imageHandler.fileToImage(profilePic);

        User user = new User(usernameInput, emailInput, passwordInput, profileImg);
        HttpResponse response = requestHandler.sendRequest("signup", "POST", user, App.loggedUser);
        Reader reader = new InputStreamReader(response.getEntity().getContent());

        User userResponse = gson.fromJson(reader, User.class);

        if(userResponse.getId() == 0){
            System.out.println("User not created");
            return null;
        }

        System.out.println(userResponse.getUsername() + " " + userResponse.getEmail() + " " + userResponse.getPassword() + " " + userResponse.getId());
        System.out.println(response.getStatusLine().getStatusCode());
        App.loggedUser = userResponse;
        switchScene("Feed");
        App.scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Feed.css").toExternalForm());
        return userResponse;
    }

    public void switchLogin(ActionEvent event){
        switchScene("Login");
    }

    public void chooseImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha uma foto de perfil");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        profilePic = fileChooser.showOpenDialog(emailField.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}