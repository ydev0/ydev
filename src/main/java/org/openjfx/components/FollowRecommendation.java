package org.openjfx.components;

import com.google.gson.Gson;
import com.ydev00.model.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.apache.http.HttpResponse;
import org.openjfx.App;
import javax.swing.*;
import javafx.event.ActionEvent;
import org.openjfx.util.RequestHandler;

import java.util.List;

public class FollowRecommendation extends FlowPane {
    @FXML
    FlowPane followPane;
    @FXML
    ImageView profilePic;
    @FXML
    Label usernameLabel;
    @FXML
    Button followButton;
    User user;

    public FollowRecommendation(User user){
        this.user = user;
        usernameLabel.setText(user.getUsername());
    }

    public void follow(ActionEvent e){
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();
        HttpResponse response = requestHandler.sendRequest("user/follow", "POST", user, App.loggedUser);
    }

}
