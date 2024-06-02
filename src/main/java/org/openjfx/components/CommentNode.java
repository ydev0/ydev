package org.openjfx.components;

import com.google.gson.Gson;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.apache.http.HttpResponse;
import org.openjfx.App;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CommentNode extends VBox implements SceneSwitcher {
    private Label usernameLabel;
    private Label textLabel;
    private FlowPane flowPane;
    private Button likeButton = new Button("Like");
    private Button unlikeButton = new Button("Unlike");
    private List<Thrd> comments = new ArrayList<>();

    private RequestHandler requestHandler = new RequestHandler();

    public CommentNode(Thrd thread) {
        this.setId("commentNode");
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(10);

        usernameLabel = new Label(thread.getUser().getUsername());
        usernameLabel.setId("usernameLabel");
        textLabel = new Label(thread.getText());
        textLabel.setId("textLabel");

        flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.getChildren().add(likeButton);
        flowPane.getChildren().add(unlikeButton);

        this.getChildren().add(usernameLabel);
        this.getChildren().add(textLabel);
        this.getChildren().add(flowPane);

        likeButton.setOnMouseClicked(e -> {
            requestHandler.sendRequest("t/like", "POST", new Thrd(thread.getId()) , App.loggedUser);
        });
        likeButton.setOnMouseClicked(e -> {
            requestHandler.sendRequest("t/unlike", "POST", new Thrd(thread.getId()) , App.loggedUser);
        });
        usernameLabel.setOnMouseClicked(e -> {
            HttpResponse response = requestHandler.sendRequest("user/"+thread.getUser().getUsername(), "GET", new Object(), App.loggedUser);
            Reader reader = null;
            try {
                reader = new InputStreamReader(response.getEntity().getContent());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            User userResponse = new Gson().fromJson(reader, User.class);

            if(userResponse.getId() == 0){
                System.out.println("User not found");
                return;
            }
            App.pageUser = userResponse;
            try {
                switchScene("profilePage", "Profile");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
