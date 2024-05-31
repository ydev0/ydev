package org.openjfx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.http.HttpResponse;
import org.openjfx.components.FollowRecommendation;
import org.openjfx.components.Sidebar;
import org.openjfx.components.ThrdNode;
import org.openjfx.util.ImageHandler;
import org.openjfx.util.RequestHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    BorderPane profilePane;
    @FXML
    Sidebar sidebar;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Label usernameLabel;
    @FXML
    ImageView profilePicture;
    @FXML
    Label followersLabel;
    @FXML
    Label followeesLabel;
    @FXML
    VBox profileBox;
    @FXML
    FlowPane flowPane;

    User user = App.loggedUser;
    ImageHandler imageHandler = new ImageHandler();
    RequestHandler requestHandler = new RequestHandler();
    List<Thrd> threads = new ArrayList<>();
    List<User> followers = new ArrayList<>();
    List<User> followees = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(user.toString());
        sidebar.prefWidthProperty().bind(profilePane.widthProperty().divide(7));
        sidebar.prefHeightProperty().bind(profilePane.heightProperty());
        try {
            loadFollowers();
            loadFollowees();
            //loadThreads();
            //renderThreads();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profilePicture.setImage(imageHandler.imageDataToImage(user.getProfilePic()));
        usernameLabel.setText(user.getUsername());
        followersLabel.setText(followers.size() + "Seguidores");
        followeesLabel.setText(followees.size() + "Seguindo");
    }

    private void renderThreads() {
        if(threads == null || threads.isEmpty()) {
            return;
        }
        profileBox.getChildren().clear();
        for(Thrd thrd : threads){
            profileBox.getChildren().add(new ThrdNode(thrd));
            System.out.println(user);
        }
    }

    private void loadThreads() throws IOException {
        Gson gson = new Gson();
        HttpResponse response = requestHandler.sendRequest("user/t", "GET", new Object(), user);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        System.out.println("Threads: "+response.getEntity().getContent());
//        Type listType = new TypeToken<ArrayList<Thread>>(){}.getType();
//        followers = gson.fromJson(reader, listType);
    }

    private void loadFollowers() throws IOException {
        Gson gson = new Gson();
        HttpResponse response = requestHandler.sendRequest("user/followers", "GET", new Object(), user);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        System.out.println("Followers: "+response.getEntity().getContent());
//        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
//        followers = gson.fromJson(reader, listType);
    }

    private void loadFollowees() throws IOException {
        Gson gson = new Gson();
        HttpResponse response = requestHandler.sendRequest("user/followees", "GET", new Object(), user);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        System.out.println("Followees: "+response.getEntity().getContent());
//        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
//        followers = gson.fromJson(reader, listType);
    }
}
