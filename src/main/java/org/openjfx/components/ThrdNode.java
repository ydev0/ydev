package org.openjfx.components;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.*;
import org.apache.http.HttpResponse;
import org.openjfx.App;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;
import org.w3c.dom.Text;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ThrdNode extends VBox implements SceneSwitcher {
    private Label usernameLabel;
    private Label textLabel;
    private Label titleLabel;
    private FlowPane flowPane;
    private Button commentButton = new Button("Comentar");
    private Button likeButton = new Button("Like");
    private Button unlikeButton = new Button("Unlike");
    private WebView webView;
    private List<Thrd> comments = new ArrayList<>();

    private RequestHandler requestHandler = new RequestHandler();
    private HttpResponse response;

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
        flowPane.getChildren().add(unlikeButton);
        flowPane.getChildren().add(commentButton);

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

        commentButton.setOnMouseClicked(e -> {
            response = requestHandler.sendRequest("t/"+thread.getId(), "GET", new Object(), App.loggedUser);
            Reader reader;
            try {
                reader = new InputStreamReader(response.getEntity().getContent());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Type listType = new TypeToken<ArrayList<Thrd>>(){}.getType();
            comments = new Gson().fromJson(reader, listType);
            System.out.println("Comment: " + comments);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(this.getScene().getWindow());
            VBox dialogVbox = new VBox(20);

            TextArea textArea = new TextArea();
            dialogVbox.getChildren().add(textArea);

            Button sendButton = new Button("Enviar");
            sendButton.setOnAction(event -> {
                Thrd comment = new Thrd(textArea.getText());
                response = requestHandler.sendRequest("t/comment/"+thread.getId(), "POST", comment, App.loggedUser);
                textArea.setText("");
            });
            dialogVbox.getChildren().add(sendButton);

            Separator separator = new Separator();
            dialogVbox.getChildren().add(separator);

            Iterator<Thrd> iterator = comments.iterator();
            iterator.next();
            for(; iterator.hasNext(); ) {
                CommentNode comment = new CommentNode(iterator.next());
                comment.prefWidthProperty().bind(dialogVbox.widthProperty().divide(1.5));
                dialogVbox.getChildren().add(comment);
                separator = new Separator();
                dialogVbox.getChildren().add(separator);
            }

            Scene dialogScene = new Scene(dialogVbox, 960, 600);

            dialog.setScene(dialogScene);
            dialog.setResizable(false);
            dialog.show();
        });

        titleLabel.setOnMouseClicked(e -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(this.getScene().getWindow());

            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(webView);

            Separator separator = new Separator();
            dialogVbox.getChildren().add(separator);

            Scene dialogScene = new Scene(dialogVbox, 960, 600);

            dialog.setScene(dialogScene);
            dialog.setResizable(false);
            dialog.show();
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
