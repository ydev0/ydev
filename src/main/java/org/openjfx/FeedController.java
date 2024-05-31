package org.openjfx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.http.HttpResponse;
import org.openjfx.components.FollowRecommendation;
import org.openjfx.components.Sidebar;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;
import java.io.*;
import java.lang.reflect.Type;
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
            loadRecommendations();
            renderRecommendations();
            loadFeed();
            renderFeed();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFeed() throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();

        HttpResponse response = requestHandler.sendRequest("home", "GET", new Object(), App.loggedUser);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        BufferedReader bfr = new BufferedReader(reader);
        Type listType = new TypeToken<ArrayList<Thrd>>(){}.getType();
        feed = gson.fromJson(reader, listType);
    }

    private void loadRecommendations() throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();

        HttpResponse response = requestHandler.sendRequest("getAll", "GET", new Object(), null);
        Reader reader = new InputStreamReader(response.getEntity().getContent());

        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        recommendations = gson.fromJson(reader, listType);
    }

    private void renderRecommendations() throws IOException {
        if(recommendations == null || recommendations.isEmpty()) {
            return;
        }
        recommendationBox.getChildren().clear();
        for(User user : recommendations){
            recommendationBox.getChildren().add(new FollowRecommendation(user));
            System.out.println(user);
        }
    }

    private void renderFeed() {
        feedBox.getChildren().clear();
        for(Thrd thrd : feed){
            System.out.println(thrd);
        }
        System.out.println(feed.size());
    }

}
