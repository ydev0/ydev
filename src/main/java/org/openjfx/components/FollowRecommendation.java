package org.openjfx.components;
import com.ydev00.model.user.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.openjfx.App;
import org.openjfx.util.ImageHandler;
import org.openjfx.util.RequestHandler;

import javafx.scene.image.ImageView;

public class FollowRecommendation extends HBox {
    RequestHandler requestHandler = new RequestHandler();
    ImageHandler imageHandler = new ImageHandler();
    ImageView imageView = new ImageView();
    Label usernameLabel;
    Button followButton;

    public FollowRecommendation(User user){
        this.setId("followRecommendation");
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        usernameLabel = new Label(user.getUsername());
        this.getChildren().add(usernameLabel);
        followButton = new Button("Follow");
        this.getChildren().add(followButton);

        usernameLabel.setId("usernameLabel");
        followButton.setId("followButton");

        followButton.setOnAction(e -> {
            requestHandler.sendRequest("user/follow","POST", user, App.loggedUser);

        });
        if(user.getProfilePic().getImage() != null){
            imageView.setImage((imageHandler.imageDataToImage(user.getProfilePic())));
        }
    }
}
