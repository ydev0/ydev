package org.openjfx.components;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.*;
import org.apache.http.HttpResponse;
import org.openjfx.util.RequestHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ThrdNode extends VBox {
    private Label usernameLabel;
    private Label textLabel;
    private Label titleLabel;
    private Label likeLabel;
    private Label commentLabel;
    private FlowPane flowPane;
    private Button commentButton = new Button("Comentar");
    private Button likeButton = new Button("Curtir");
    private WebView webView;
    private List<Thrd> comments = new ArrayList<>();

    private RequestHandler requestHandler = new RequestHandler();

    public ThrdNode(Thrd thread){
        this.setId("threadNode");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        usernameLabel = new Label(thread.getUser().getUsername());
        titleLabel = new Label(thread.getArticle().getTitle());
        titleLabel.setId("titleLabel");
        textLabel = new Label(thread.getText());
        textLabel.setId("textLabel");

        webView = new WebView();
        webView.getEngine().loadContent(thread.getArticle().getMarkdown());

        flowPane = new FlowPane();
        flowPane.getChildren().add(likeButton);
        flowPane.getChildren().add(commentButton);

        this.getChildren().add(usernameLabel);
        this.getChildren().add(titleLabel);
        this.getChildren().add(textLabel);
        this.getChildren().add(flowPane);

        titleLabel.setOnMouseClicked(e ->{
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(this.getScene().getWindow());

            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(webView);
            Scene dialogScene = new Scene(dialogVbox, 960, 600);

            dialog.setScene(dialogScene);
            dialog.setResizable(false);
            dialog.show();

            HttpResponse response = requestHandler.sendRequest("getAll", "GET", new Object(), null);
            Reader reader = null;
            try {
                reader = new InputStreamReader(response.getEntity().getContent());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            comments = new Gson().fromJson(reader, listType);
        });
    }
}
