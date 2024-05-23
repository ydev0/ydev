
package org.openjfx;

import com.google.gson.Gson;
import com.ydev00.model.user.User;
import com.ydev00.model.image.Image;
import com.ydev00.model.image.ImageData;
import org.apache.commons.io.FilenameUtils;
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
import org.openjfx.util.RequestHandler;

import javax.sql.rowset.serial.SerialBlob;

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

    public User signup(ActionEvent event) throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();

        emailInput = emailField.getText();
        passwordInput = passwordField.getText();
        usernameInput = usernameField.getText();
        String imageType = FilenameUtils.getExtension(String.valueOf(profilePic));
        System.out.println(imageType);

        List<Integer> binaryData = new ArrayList<>();
        InputStream inputStream = new FileInputStream(profilePic);
        for(int i = 0; i <  profilePic.length(); i++){
            binaryData.add(inputStream.read());
        }
        ImageData profileImgData = new ImageData(binaryData, false);
        Image profileImg = new Image(imageType, profileImgData);


        User user = new User(usernameInput, emailInput, passwordInput, profileImg);
        HttpResponse response = requestHandler.sendRequest("signup", "POST", user, null);

        Reader reader = new InputStreamReader(response.getEntity().getContent());
        User userResponse = gson.fromJson(reader, User.class);

        if(userResponse.getId() == 0){
            System.out.println("User not created");
            return null;
        }

        System.out.println(userResponse.getUsername() + " " + userResponse.getEmail() + " " + userResponse.getPassword() + " " + userResponse.getId());
        System.out.println(response.getStatusLine().getStatusCode());

        return userResponse;

    }

    public void switchScene(ActionEvent event){
        try{
            Main.setRoot("Login");
        } catch(IOException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
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