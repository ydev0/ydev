package org.openjfx.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.openjfx.util.SceneSwitcher;
import java.io.IOException;

public class Sidebar extends VBox implements SceneSwitcher {
    @FXML
    private Label logoLabel;
    @FXML
    private Button feedButton;
    @FXML
    private Button profileButton;
    @FXML
    Button threadButton;

    public Sidebar() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/openjfx/Sidebar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Sidebar.css").toExternalForm());
        try { fxmlLoader.load(); } catch (IOException e) { e.printStackTrace();}

        threadButton.prefWidthProperty().bind(this.widthProperty());
        threadButton.prefHeightProperty().bind(this.heightProperty().divide(15));

        profileButton.prefWidthProperty().bind(this.widthProperty());
        profileButton.prefHeightProperty().bind(this.heightProperty().divide(15));

        feedButton.prefWidthProperty().bind(this.widthProperty());
        feedButton.prefHeightProperty().bind(this.heightProperty().divide(15));

        logoLabel.prefWidthProperty().bind(this.widthProperty());
        logoLabel.prefHeightProperty().bind(this.heightProperty().divide(10));
    }

    public void switchThread(ActionEvent event) throws IOException {
        switchScene("ThreadEditor", "ThreadEditor");
    }

    public void switchFeed(ActionEvent event) throws IOException {
        switchScene("Feed", "Feed");
    }
    public void switchProfile(ActionEvent event) throws IOException {
        switchScene("profilePage", "Profile");
    }
}
