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
    private RequestHandler requestHandler = new RequestHandler();
    private ImageHandler imageHandler = new ImageHandler();

    private ImageView imageView = new ImageView();
    private Label usernameLabel;
    private Button followButton;

    public FollowRecommendation(User user){
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setId("followRecommendation");

        usernameLabel = new Label(user.getUsername());
        this.getChildren().add(usernameLabel);
        usernameLabel.setId("usernameLabel");

        followButton = new Button("Seguir");
        this.getChildren().add(followButton);
        followButton.setId("followButton");

        followButton.setOnAction(e -> {
            requestHandler.sendRequest("user/follow","POST", user, App.loggedUser);
        });
        if(user.getProfilePic().getImage() != null)
            imageView.setImage((imageHandler.imageDataToImage(user.getProfilePic())));
    }
}
