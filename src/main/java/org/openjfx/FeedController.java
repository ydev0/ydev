package org.openjfx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.http.HttpResponse;
import org.openjfx.components.FollowRecommendation;
import org.openjfx.components.Sidebar;
import org.openjfx.components.ThrdNode;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para o feed de threads e recomendações de usuários.
 * Carrega e exibe as threads e recomendações na interface do usuário.
 */
public class FeedController implements Initializable, SceneSwitcher {
    @FXML
    private FlowPane topPane;
    @FXML
    private BorderPane feedPane;
    @FXML
    private VBox feedBox;
    @FXML
    private VBox recommendationBox;
    @FXML
    private Sidebar sidebar;
    @FXML
    private ScrollPane scrollPane;

    private RequestHandler requestHandler = new RequestHandler();
    private Gson gson = new Gson();

    private List<Thrd> feed = new ArrayList<>();
    private List<User> recommendations = new ArrayList<>();


    /**
     * Inicializa o controlador com as configurações necessárias.
     *
     * @param url O URL da localização.
     * @param resourceBundle O recurso utilizado.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sidebar.prefWidthProperty().bind(feedPane.widthProperty().divide(7));
        sidebar.prefHeightProperty().bind(feedPane.heightProperty());

        topPane.prefWidthProperty().bind(feedPane.widthProperty());
        topPane.prefHeightProperty().bind(feedPane.heightProperty().divide(20));

        recommendationBox.prefWidthProperty().bind(feedPane.widthProperty().divide(6));
        recommendationBox.prefHeightProperty().bind(feedPane.heightProperty().multiply(19).divide(20));

        feedBox.prefWidthProperty().bind(scrollPane.widthProperty());

        try {
            loadRecommendations();
            loadFeed();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        renderRecommendations();
        renderFeed();
    }

    /**
     * Carrega o feed de threads do servidor.
     *
     * @throws IOException Se ocorrer um erro de entrada/saída.
     */
    private void loadFeed() throws IOException {
        HttpResponse response = requestHandler.sendRequest("feed", "GET", new Object(), App.loggedUser);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        Type listType = new TypeToken<ArrayList<Thrd>>(){}.getType();
        feed = gson.fromJson(reader, listType);
    }

    /**
     * Carrega as recomendações de usuários do servidor.
     *
     * @throws IOException Se ocorrer um erro de entrada/saída.
     */
    private void loadRecommendations() throws IOException {
        HttpResponse response = requestHandler.sendRequest("getAll", "GET", new Object(), null);
        Reader reader = new InputStreamReader(response.getEntity().getContent());
        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        recommendations = gson.fromJson(reader, listType);
    }


    /**
     * Renderiza as recomendações de usuários na interface do usuário.
     */
    private void renderRecommendations()  {
        for(User user : recommendations){
            FollowRecommendation recommendation = new FollowRecommendation(user);
            recommendation.prefWidthProperty().bind(feedPane.widthProperty());
            recommendation.prefHeightProperty().bind(feedPane.heightProperty().divide(15));
            recommendationBox.getChildren().add(recommendation);
        }
    }

    /**
     * Renderiza o feed de threads na interface do usuário.
     */
    private void renderFeed() {
        for(Thrd thrd : feed){
            if(thrd.getArticle() != null) {
                ThrdNode thrdNode = new ThrdNode(thrd);
                feedBox.getChildren().add(thrdNode);
            }
        }
    }

}
