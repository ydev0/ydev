package org.openjfx;

import com.google.gson.Gson;
import com.ydev00.model.user.User;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.http.HttpResponse;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;


public class LoginController implements Initializable, SceneSwitcher {
  @FXML
  public TextField emailField;
  @FXML
  public PasswordField passwordField;
  @FXML
  public String emailInput;
  @FXML
  public String passwordInput;

  public User login(ActionEvent event) throws IOException {
    RequestHandler requestHandler = new RequestHandler();
    Gson gson = new Gson();

    emailInput = emailField.getText();
    passwordInput = passwordField.getText();

    User user = new User(emailInput, passwordInput);
    HttpResponse response = requestHandler.sendRequest("login", "POST", user, null);


    Reader reader = new InputStreamReader(response.getEntity().getContent());
    User userResponse = gson.fromJson(reader, User.class);
    if(userResponse.getId() == 0){
      System.out.println("User not found");
      return null;
    }

    System.out.println(userResponse.getUsername() + " " + userResponse.getEmail() + " " + userResponse.getPassword() + " " + userResponse.getId());
    System.out.println(response.getStatusLine().getStatusCode());
    switchScene("Feed");
    App.scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Feed.css").toExternalForm());
    App.loggedUser = userResponse;
    return userResponse;
  }

  public void switchSignup(ActionEvent event) throws IOException {
    switchScene("Signup");
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

}