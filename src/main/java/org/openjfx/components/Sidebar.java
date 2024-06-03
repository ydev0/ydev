package org.openjfx.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.openjfx.App;
import org.openjfx.util.RequestHandler;
import org.openjfx.util.SceneSwitcher;
import java.io.IOException;

/**
 * A classe Sidebar representa uma barra lateral de navegação da interface de usuário.
 * Ela estende VBox e implementa SceneSwitcher para gerenciamento de cenas.
 */
public class Sidebar extends VBox implements SceneSwitcher {
    @FXML
    private Label logoLabel;
    @FXML
    private Button feedButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button threadButton;
    @FXML
    private Button logoutButton;

    /**
     * Construtor da classe Sidebar. Carrega o layout do FXML e aplica os estilos CSS.
     */
    public Sidebar() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/openjfx/Sidebar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/Sidebar.css").toExternalForm());
        try { fxmlLoader.load(); } catch (IOException e) { e.printStackTrace();}

        logoutButton.prefWidthProperty().bind(this.widthProperty());
        logoutButton.prefHeightProperty().bind(this.heightProperty().divide(15));

        threadButton.prefWidthProperty().bind(this.widthProperty());
        threadButton.prefHeightProperty().bind(this.heightProperty().divide(15));

        profileButton.prefWidthProperty().bind(this.widthProperty());
        profileButton.prefHeightProperty().bind(this.heightProperty().divide(15));

        feedButton.prefWidthProperty().bind(this.widthProperty());
        feedButton.prefHeightProperty().bind(this.heightProperty().divide(15));

        logoLabel.prefWidthProperty().bind(this.widthProperty());
        logoLabel.prefHeightProperty().bind(this.heightProperty().divide(10));
    }

    /**
     * Método para trocar para a cena do editor de threads.
     *
     * @param event O evento de ação.
     * @throws IOException Se ocorrer um erro de E/S ao carregar a cena.
     */
    public void switchThread(ActionEvent event) throws IOException {
        switchScene("ThreadEditor", "ThreadEditor");
    }

    /**
     * Método para trocar para a cena do feed.
     *
     * @param event O evento de ação.
     * @throws IOException Se ocorrer um erro de E/S ao carregar a cena.
     */
    public void switchFeed(ActionEvent event) throws IOException {
        switchScene("Feed", "Feed");
    }

    /**
     * Método para trocar para a cena do perfil do usuário.
     *
     * @param event O evento de ação.
     * @throws IOException Se ocorrer um erro de E/S ao carregar a cena.
     */
    public void switchProfile(ActionEvent event) throws IOException {
        App.pageUser = App.loggedUser;
        switchScene("profilePage", "Profile");
    }
    public void logout(ActionEvent event) throws IOException {
        App.loggedUser = null;
        switchScene("Login", "LoginSignup");
        new RequestHandler().sendRequest("user/logout", "POST", new Object(), App.loggedUser);
    }
}
