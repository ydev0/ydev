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

/**
 * Controlador para a tela de login.
 * Gerencia a autenticação de usuários e a navegação para outras telas.
 */
public class LoginController implements Initializable, SceneSwitcher {
  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private String emailInput;
  @FXML
  private String passwordInput;

  private RequestHandler requestHandler = new RequestHandler();
  private Gson gson = new Gson();

  /**
   * Realiza a ação de login quando o botão de login é clicado.
   *
   * @param event O evento de clique do botão.
   * @throws IOException Se ocorrer um erro de entrada/saída.
   */
  public void login(ActionEvent event) throws IOException {
    emailInput = emailField.getText();
    passwordInput = passwordField.getText();

    User user = new User(emailInput, passwordInput);
    HttpResponse response = requestHandler.sendRequest("login", "POST", user, null);

    Reader reader = new InputStreamReader(response.getEntity().getContent());
    User userResponse = gson.fromJson(reader, User.class);

    if(userResponse.getId() == 0){
      System.out.println("User not found");
      return;
    }

    App.loggedUser = userResponse;
    App.loggedUser.setAuth(true);

    switchScene("Feed", "Feed");
  }
  /**
   * Alterna para a tela de cadastro quando o botão de cadastro é clicado.
   *
   * @param event O evento de clique do botão.
   * @throws IOException Se ocorrer um erro de entrada/saída.
   */
  public void switchSignup(ActionEvent event) throws IOException {
    switchScene("Signup", "LoginSignup");
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

}