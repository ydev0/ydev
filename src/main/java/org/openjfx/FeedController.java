package org.openjfx;

import com.google.gson.Gson;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import com.ydev00.util.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.http.HttpResponse;
import org.openjfx.components.FollowRecommendation;
import org.openjfx.components.Sidebar;
import org.openjfx.components.ThrdNode;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FeedController implements Initializable, SceneSwitcher {
    @FXML
    FlowPane topPane;
    @FXML
    BorderPane feedPane;
    @FXML
    VBox feedBox;
    @FXML
    VBox recommendationBox;
    @FXML
    Sidebar sidebar;
    List<Thrd> feed = new ArrayList<>();
    List<User> recommendations = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sidebar.prefWidthProperty().bind(feedPane.widthProperty().divide(7));
        sidebar.prefHeightProperty().bind(feedPane.heightProperty());

        topPane.prefWidthProperty().bind(feedPane.widthProperty());
        topPane.prefHeightProperty().bind(feedPane.heightProperty().divide(20));

        try {
            loadFeed();
            renderFeed();
            loadRecommendations();
            renderRecommendations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderFeed() {
        if(feed == null || feed.size() == 0) {
            return;
        }
        feedBox.getChildren().clear();
        for(Thrd thrd : feed){
            feedBox.getChildren().add(new ThrdNode(thrd));
        }
    }

    private void loadFeed() throws IOException {
        feed.clear();
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();

        HttpResponse response = requestHandler.sendRequest("home/", "GET", feed, App.loggedUser);

        Reader reader = new InputStreamReader(response.getEntity().getContent());

        // Parse JSON response directly into a List of Thrd objects
        if(gson.fromJson(reader, Object.class) instanceof Message){
            Message message = gson.fromJson(reader, Message.class);
            System.out.println(message.toString());
        }
        else{
            feed = gson.fromJson(reader, ArrayList.class);
        }
    }

    private void loadRecommendations() throws IOException {
        recommendations.clear();
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();

        HttpResponse response = requestHandler.sendRequest("/getAll", "GET", recommendations, App.loggedUser);

        Reader reader = new InputStreamReader(response.getEntity().getContent());

        // Parse JSON response directly into a List of Thrd objects
        if(gson.fromJson(reader, Object.class) instanceof Message){
            Message message = gson.fromJson(reader, Message.class);
            System.out.println("Vazio");
        }
        else{
            recommendations = gson.fromJson(reader, ArrayList.class);
        }
    }

    private void renderRecommendations() throws IOException {
        if(recommendations == null || recommendations.size() == 0) {
            return;
        }
        recommendationBox.getChildren().clear();
        for(User user : recommendations){
            recommendationBox.getChildren().add(new FollowRecommendation(user));
        }
    }
}
