package org.openjfx;

import com.google.gson.Gson;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import com.ydev00.util.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.http.HttpResponse;
import org.openjfx.components.Sidebar;
import org.openjfx.components.ThreadComponent;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class FeedController implements Initializable, SceneSwitcher {
    @FXML
    BorderPane feedPane;
    @FXML
    VBox feedBox;
    @FXML
    VBox recommendationBox;
    @FXML
    Sidebar sidebar;
    List<Thrd> feed = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sidebar.prefWidthProperty().bind(feedPane.widthProperty().divide(8));
        sidebar.prefHeightProperty().bind(feedPane.widthProperty());
        try {
            loadFeed();
            renderFeed();
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
            feedBox.getChildren().add(new ThreadComponent(thrd));
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
}
