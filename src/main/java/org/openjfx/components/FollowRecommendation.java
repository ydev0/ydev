package org.openjfx.components;
import com.google.gson.Gson;
import com.ydev00.model.user.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.http.HttpResponse;
import org.openjfx.App;
import org.openjfx.util.ImageHandler;
import org.openjfx.util.RequestHandler;

import javafx.scene.image.ImageView;
import org.openjfx.util.SceneSwitcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * A classe FollowRecommendation representa um componente de interface de usuário para exibir
 * recomendações de usuários a serem seguidos. Ela estende HBox e implementa SceneSwitcher
 * para gerenciamento de cenas.
 */
public class FollowRecommendation extends HBox implements SceneSwitcher {
    private RequestHandler requestHandler = new RequestHandler();
    private ImageHandler imageHandler = new ImageHandler();

    private ImageView imageView = new ImageView();
    private Label usernameLabel;
    private Button followButton;

    /**
     * Constrói um FollowRecommendation para um determinado usuário.
     *
     * @param user O objeto User contendo os dados do usuário a ser recomendado.
     */
    public FollowRecommendation(User user){
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setId("followRecommendation");

        usernameLabel = new Label(user.getUsername());
        this.getChildren().add(usernameLabel);
        usernameLabel.setId("usernameLabel");

        followButton = new Button("Seguir");
        this.getChildren().add(followButton);
        followButton.setId("followButton");

        followButton.setOnAction(e -> {
            requestHandler.sendRequest("user/follow","POST", user, App.loggedUser);
        });

        usernameLabel.setOnMouseClicked(e -> {
           HttpResponse response = requestHandler.sendRequest("user/"+user.getUsername(), "GET", new Object(), App.loggedUser);
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

        if(user.getProfilePic().getImage() != null)
            imageView.setImage((imageHandler.imageDataToImage(user.getProfilePic())));
    }
}
