
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

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

    public void signup(ActionEvent event) throws IOException {
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
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("localhost:8080/user/signup");
        StringEntity stringEntity = new StringEntity(gson.toJson(user));
        request.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(request);


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
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        profilePic = fileChooser.showOpenDialog(emailField.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}