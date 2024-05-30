package org.openjfx.components;

import com.ydev00.model.thread.Thrd;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.IOException;

public class ThrdNode extends VBox {
    @FXML
    Label usernameLabel;
    @FXML
    Label textLabel;
    @FXML
    Label titleLabel;
    @FXML
    WebView articleView;
    @FXML
    FlowPane flowPane;
    @FXML
    Button likeButton;
    @FXML
    Label likeLabel;
    @FXML
    Button commentButton;
    @FXML
    Label commentLabel;
    Thrd thread;

    public ThrdNode(Thrd thread){
        this.thread = thread;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/openjfx/Thread.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        textLabel.setText(thread.getText());
        titleLabel.setText(thread.getArticle().getTitle());
        articleView.getEngine().load(thread.getArticle().getMarkdown());
    }
}
