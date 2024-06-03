package org.openjfx.components;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.ModUser;
import com.ydev00.model.user.User;
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
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A classe ThrdNode representa um componente de interface de usuário para exibir uma thread em uma discussão.
 * Ela estende VBox e implementa SceneSwitcher para gerenciamento de cenas.
 */
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

    /**
     * Constrói um ThrdNode para uma determinada thread.
     *
     * @param thread O objeto thread contendo dados da discussão.
     */
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
        flowPane.setAlignment(Pos.TOP_CENTER);
        flowPane.setHgap(10);
        flowPane.getChildren().add(likeButton);
        likeButton.setId("threadButton");
        flowPane.getChildren().add(unlikeButton);
        unlikeButton.setId("threadButton");
        flowPane.getChildren().add(commentButton);
        commentButton.setId("threadButton");

        if(App.loggedUser.isRoot()){
            Button deleteButton = new Button("Apagar");
            flowPane.getChildren().add(deleteButton);
            deleteButton.setId("deleteButton");
            deleteButton.setOnAction(event ->{
                requestHandler.sendRequest("/delete/t/"+thread.getId(), "DELETE", new Object(), App.loggedUser);
                try {
                    switchScene("Feed", "Feed");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        this.getChildren().add(usernameLabel);
        this.getChildren().add(titleLabel);
        this.getChildren().add(textLabel);
        this.getChildren().add(new Separator());
        this.getChildren().add(flowPane);

        likeButton.setOnMouseClicked(e -> {
            requestHandler.sendRequest("t/like", "POST", new Thrd(thread.getId()) , App.loggedUser);
        });
        unlikeButton.setOnMouseClicked(e -> {
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


            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(this.getScene().getWindow());
            VBox dialogVbox = new VBox(20);
            dialogVbox.setId("dialogVbox");

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
                comment.setId("commentNode");
                comment.prefWidthProperty().bind(dialogVbox.widthProperty().divide(1.5));
                dialogVbox.getChildren().add(comment);
                separator = new Separator();
                dialogVbox.getChildren().add(separator);
            }

            Scene dialogScene = new Scene(dialogVbox, 960, 600);
            dialogScene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Comments.css").toExternalForm());

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
