package org.openjfx;

import com.ydev00.model.user.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.Gson;

public class LoginController implements Initializable {
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public String emailInput;
    @FXML
    public String passwordInput;

    public void login(ActionEvent event) throws IOException {
        emailInput = emailField.getText();
        passwordInput = passwordField.getText();

        User user = new User(emailInput, passwordInput);
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:8080/user/login");
        StringEntity stringEntity = new StringEntity(gson.toJson(user));
        request.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(request);
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