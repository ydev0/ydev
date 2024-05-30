package org.openjfx.components;
import com.ydev00.model.user.User;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.openjfx.App;
import org.openjfx.util.RequestHandler;

public class FollowRecommendation extends FlowPane {
    Label usernameLabel;
    Button followButton;
    RequestHandler requestHandler = new RequestHandler();

    public FollowRecommendation(User user){
        usernameLabel = new Label(user.getUsername());
        this.getChildren().add(usernameLabel);
        followButton = new Button("Follow");
        this.getChildren().add(followButton);
        followButton.setOnAction(e -> {
            requestHandler.sendRequest("user/follow","POST", user, App.loggedUser);

        });
    }
}
