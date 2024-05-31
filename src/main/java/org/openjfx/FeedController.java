package org.openjfx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ydev00.model.thread.Thrd;
import com.ydev00.model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
        System.out.println("loggedUser:"+App.loggedUser);
        sidebar.prefWidthProperty().bind(feedPane.widthProperty().divide(7));
        sidebar.prefHeightProperty().bind(feedPane.heightProperty());

        topPane.prefWidthProperty().bind(feedPane.widthProperty());
        topPane.prefHeightProperty().bind(feedPane.heightProperty().divide(20));

        recommendationBox.prefWidthProperty().bind(feedPane.widthProperty().divide(6));

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

        HttpResponse response = requestHandler.sendRequest("home/feed", "GET", new Object(), App.loggedUser);
        Reader reader = new InputStreamReader(response.getEntity().getContent());

        //Verifique o conteúdo JSON retornado
        BufferedReader bfr = new BufferedReader(reader);
        StringBuilder jsonContent = new StringBuilder();
        String line;
        while ((line = bfr.readLine()) != null) {
            jsonContent.append(line);
        }

        // Imprima o JSON para ver o que está sendo retornado
        System.out.println("Feed retornado: " + jsonContent.toString());

        // Verifique se é um array ou um objeto e processe adequadamente
        if (jsonContent.toString().trim().startsWith("[")) {
            Type listType = new TypeToken<ArrayList<Thrd>>(){}.getType();
            feed = gson.fromJson(jsonContent.toString(), listType);
        } else {
            // Processar como objeto ou log de erro
            System.err.println("Erro: JSON esperado como array, mas retornou um objeto.");
        }
    }


    private void loadRecommendations() throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        Gson gson = new Gson();

        HttpResponse response = requestHandler.sendRequest("getAll", "GET", new Object(), null);
        Reader reader = new InputStreamReader(response.getEntity().getContent());

        // Verifique o conteúdo JSON retornado
        BufferedReader bfr = new BufferedReader(reader);
        StringBuilder jsonContent = new StringBuilder();
        String line;
        while ((line = bfr.readLine()) != null) {
            jsonContent.append(line);
        }

        // Imprima o JSON para ver o que está sendo retornado
        System.out.println("Recomendações retornadas: " + jsonContent.toString());

        // Verifique se é um array ou um objeto e processe adequadamente
        if (jsonContent.toString().trim().startsWith("[")) {
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            recommendations = gson.fromJson(jsonContent.toString(), listType);
        } else {
            // Processar como objeto ou log de erro
            System.err.println("Erro: JSON esperado como array, mas retornou um objeto.");
        }
    }


    private void renderRecommendations() throws IOException {
        if(recommendations == null || recommendations.isEmpty()) {
            return;
        }
        recommendationBox.getChildren().clear();
        for(User user : recommendations){
            FollowRecommendation recommendation = new FollowRecommendation(user);
            recommendation.prefWidthProperty().bind(feedPane.widthProperty());
            recommendation.prefHeightProperty().bind(feedPane.heightProperty().divide(15));
            recommendationBox.getChildren().add(recommendation);
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
