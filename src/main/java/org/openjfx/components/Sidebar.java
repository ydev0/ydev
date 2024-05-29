package org.openjfx.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.openjfx.App;
import org.openjfx.util.SceneSwitcher;

import java.io.IOException;
import java.net.URL;

public class Sidebar extends VBox implements SceneSwitcher {
    @FXML
    VBox sidebar;
    @FXML
    Label logoLabel;
    @FXML
    Button feedButton;
    @FXML
    Button profileButton;
    @FXML
    Button articleButton;

    public Sidebar() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/openjfx/Sidebar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Sidebar.css").toExternalForm());

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        articleButton.prefWidthProperty().bind(this.widthProperty());
        articleButton.prefHeightProperty().bind(this.widthProperty().divide(14));
        profileButton.prefWidthProperty().bind(this.widthProperty());
        profileButton.prefHeightProperty().bind(this.widthProperty().divide(14));
        feedButton.prefWidthProperty().bind(this.widthProperty());
        feedButton.prefHeightProperty().bind(this.widthProperty().divide(14));
    }

    public void switchThread(ActionEvent event){
        switchScene("ThreadEditor");
        App.scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/ThreadEditor.css").toExternalForm());
    }

    public void switchFeed(ActionEvent event){
        switchScene("Feed");
        App.scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Feed.css").toExternalForm());
    }
}
