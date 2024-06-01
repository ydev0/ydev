package org.openjfx;import com.google.gson.Gson;import com.google.gson.reflect.TypeToken;import com.ydev00.model.thread.Thrd;import com.ydev00.model.user.User;import javafx.fxml.FXML;import javafx.fxml.Initializable;import javafx.scene.Scene;import javafx.scene.control.Label;import javafx.scene.control.Button;import javafx.scene.control.SplitPane;import javafx.scene.image.ImageView;import javafx.scene.layout.BorderPane;import javafx.scene.layout.FlowPane;import javafx.scene.layout.VBox;import javafx.stage.Modality;import javafx.stage.Stage;import org.apache.http.HttpResponse;import org.apache.http.util.EntityUtils;import org.openjfx.components.FollowRecommendation;import org.openjfx.components.Sidebar;import org.openjfx.components.ThrdNode;import org.openjfx.util.ImageHandler;import org.openjfx.util.RequestHandler;import org.openjfx.util.SceneSwitcher;import java.io.IOException;import java.io.InputStreamReader;import java.io.Reader;import java.lang.reflect.Type;import java.net.URL;import java.util.ArrayList;import java.util.List;import java.util.ResourceBundle;public class ProfileController implements Initializable, SceneSwitcher {    @FXML    private BorderPane profilePane;    @FXML    private FlowPane topPane;    @FXML    private Sidebar sidebar;    @FXML    private SplitPane splitPane;    @FXML    private VBox profileBox;    @FXML    private ImageView profilePicture;    @FXML    private Label usernameLabel;    @FXML    private Label followersLabel;    @FXML    private Label followeesLabel;    @FXML    private VBox threadBox;    private User user = App.pageUser;    private List<Thrd> threads = new ArrayList<>();    private List<User> followers = new ArrayList<>();    private List<User> followees = new ArrayList<>();        private ImageHandler imageHandler = new ImageHandler();    private RequestHandler requestHandler = new RequestHandler();    private Gson gson = new Gson();    @Override    public void initialize(URL url, ResourceBundle resourceBundle) {        if(App.loggedUser.isRoot() && App.loggedUser != user){            Button deleteButton = new Button("Apagar ");            profileBox.getChildren().add(deleteButton);            deleteButton.setId("deleteButton");            deleteButton.setOnAction(e ->{                try {                    switchScene("Feed", "Feed");                } catch (IOException ex) {                    throw new RuntimeException(ex);                }                requestHandler.sendRequest("delete/user", "DELETE", new User(user.getUsername()), App.loggedUser);            });        }        sidebar.prefWidthProperty().bind(profilePane.widthProperty().divide(7));        sidebar.prefHeightProperty().bind(profilePane.heightProperty().multiply(19).divide(20));        topPane.prefWidthProperty().bind(profilePane.widthProperty());        topPane.prefHeightProperty().bind(profilePane.heightProperty().divide(20));        threadBox.prefWidthProperty().bind(splitPane.widthProperty().divide(5).multiply(4));        profileBox.prefWidthProperty().bind(splitPane.widthProperty().divide(5));        try {            loadFollowers();            loadFollowees();            loadThreads();        } catch (IOException e) {            e.printStackTrace();        }        renderThreads();        profilePicture.setImage(imageHandler.imageDataToImage(user.getProfilePic()));        usernameLabel.setText(user.getUsername());        followersLabel.setText(followers.size() + (followers.size() == 1 ? " Seguidor": " Seguidores"));        followeesLabel.setText(followees.size() + " Seguindo");        followersLabel.setOnMouseClicked(e -> {            Stage dialog = new Stage();            dialog.initModality(Modality.APPLICATION_MODAL);            dialog.initOwner(followersLabel.getScene().getWindow());            VBox dialogVbox = new VBox(20);            for(User users : followers){                FollowRecommendation recommendation = new FollowRecommendation(users);                recommendation.prefWidthProperty().bind(dialogVbox.widthProperty());                recommendation.prefHeightProperty().bind(dialogVbox.heightProperty().divide(15));                dialogVbox.getChildren().add(recommendation);                System.out.println(users);            }            Scene dialogScene = new Scene(dialogVbox, 600, 375);            dialogScene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Users.css").toExternalForm());            dialog.setScene(dialogScene);            dialog.setResizable(false);            dialog.show();        });        followeesLabel.setOnMouseClicked(e -> {            Stage dialog = new Stage();            dialog.initModality(Modality.APPLICATION_MODAL);            dialog.initOwner(followeesLabel.getScene().getWindow());            VBox dialogVbox = new VBox(20);            for(User users : followees){                FollowRecommendation recommendation = new FollowRecommendation(users);                recommendation.prefWidthProperty().bind(dialogVbox.widthProperty());                recommendation.prefHeightProperty().bind(dialogVbox.heightProperty().divide(15));                dialogVbox.getChildren().add(recommendation);                System.out.println(users);            }            Scene dialogScene = new Scene(dialogVbox, 600, 375);            dialogScene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Users.css").toExternalForm());            dialog.setScene(dialogScene);            dialog.setResizable(false);            dialog.show();        });    }    private void renderThreads() {        for (Thrd thrd : threads) {            if(thrd.getArticle() != null) {                ThrdNode thrdNode = new ThrdNode(thrd);                thrdNode.prefWidthProperty().bind(threadBox.widthProperty().divide(1.25));                threadBox.getChildren().add(thrdNode);            }        }    }    private void loadThreads() throws IOException {        HttpResponse response = requestHandler.sendRequest("user/"+user.getUsername()+"/t", "GET", new Object() , App.loggedUser);        if (response.getStatusLine().getStatusCode() != 200) {            System.out.println("Failed to load threads: " + EntityUtils.toString(response.getEntity()));            return;        }        Reader reader = new InputStreamReader(response.getEntity().getContent());        Type listType = new TypeToken<ArrayList<Thrd>>() {}.getType();        threads = gson.fromJson(reader, listType);    }    private void loadFollowers() throws IOException {        HttpResponse response = requestHandler.sendRequest("user/"+user.getUsername()+"/getFollowers", "GET", new Object(), App.loggedUser);        if (response.getStatusLine().getStatusCode() != 200) {            System.out.println("Failed to load followers: " + EntityUtils.toString(response.getEntity()));            return;        }        Reader reader = new InputStreamReader(response.getEntity().getContent());        Type listType = new TypeToken<ArrayList<User>>() {}.getType();        followers = gson.fromJson(reader, listType);    }    private void loadFollowees() throws IOException {        HttpResponse response = requestHandler.sendRequest("user/"+user.getUsername()+"/getFollowees", "GET", new Object(), App.loggedUser);        if (response.getStatusLine().getStatusCode() != 200) {            System.out.println("Failed to load followees: " + EntityUtils.toString(response.getEntity()));            return;        }        Reader reader = new InputStreamReader(response.getEntity().getContent());        Type listType = new TypeToken<ArrayList<User>>() {}.getType();        followees = gson.fromJson(reader, listType);    }}