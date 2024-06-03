package org.openjfx;

import com.google.gson.Gson;
import com.ydev00.model.user.User;
import com.ydev00.model.image.Image;
import java.io.*;
import java.net.URL;
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

    private RequestHandler requestHandler = new RequestHandler();
    private ImageHandler imageHandler = new ImageHandler();
    private Gson gson = new Gson();

    public void signup(ActionEvent event) throws IOException {
        emailInput = emailField.getText();
        passwordInput = passwordField.getText();
        usernameInput = usernameField.getText();

        if(emailInput.isEmpty() || passwordInput.isEmpty() || usernameInput.isEmpty() || profilePic == null){
            System.out.println("Empty fields");
            return;
        }

        Image profileImg = imageHandler.fileToImage(profilePic);
        User user = new User(usernameInput, emailInput, passwordInput, profileImg);

        HttpResponse response = requestHandler.sendRequest("signup", "POST", user, null);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        User userResponse = gson.fromJson(reader, User.class);

        if(userResponse.getId() == 0){
            System.out.println("User not created");
            return;
        }

        response = requestHandler.sendRequest("login", "POST", userResponse, null);
        reader = new InputStreamReader(response.getEntity().getContent());
        userResponse = gson.fromJson(reader, User.class);

        if(userResponse.getId() == 0){
            System.out.println("User not logged in");
            return;
        }

        App.loggedUser = userResponse;

        switchScene("Feed", "Feed");
    }

    public void switchLogin(ActionEvent event) throws IOException {
        switchScene("Login", "LoginSignup");
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