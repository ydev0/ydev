package org.openjfx.components;

import com.ydev00.model.thread.Thrd;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.openjfx.App;
import org.openjfx.util.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class CommentNode extends VBox {
    private Label usernameLabel;
    private Label textLabel;
    private Label titleLabel;
    private FlowPane flowPane;
    private Button likeButton = new Button("Like");
    private Button unlikeButton = new Button("Unlike");
    private List<Thrd> comments = new ArrayList<>();

    private RequestHandler requestHandler = new RequestHandler();

    public CommentNode(Thrd thread) {
        this.setId("threadNode");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        usernameLabel = new Label(thread.getUser().getUsername());
        titleLabel = new Label(thread.getArticle().getTitle());
        titleLabel.setId("titleLabel");
        textLabel = new Label(thread.getText());
        textLabel.setId("textLabel");

        flowPane = new FlowPane();
        flowPane.getChildren().add(likeButton);
        flowPane.getChildren().add(unlikeButton);

        this.getChildren().add(usernameLabel);
        this.getChildren().add(titleLabel);
        this.getChildren().add(textLabel);
        this.getChildren().add(flowPane);

        likeButton.setOnMouseClicked(e -> {
            requestHandler.sendRequest("t/like", "POST", new Thrd(thread.getId()) , App.loggedUser);
        });
        likeButton.setOnMouseClicked(e -> {
            requestHandler.sendRequest("t/unlike", "POST", new Thrd(thread.getId()) , App.loggedUser);
        });
    }
}
