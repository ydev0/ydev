package org.openjfx.util;

import javafx.scene.Scene;
import org.openjfx.App;

import java.io.IOException;

/**
 * Interface responsável por trocar cenas em uma aplicação JavaFX.
 */
public interface SceneSwitcher {

    /**
     * Troca a cena atual para uma nova cena especificada pelo nome da cena e o nome do arquivo CSS.
     *
     * @param sceneName O nome da nova cena a ser exibida.
     * @param CSSName O nome do arquivo CSS a ser aplicado à nova cena.
     * @throws IOException Se ocorrer um erro ao carregar a nova cena.
     */
    default void switchScene(String sceneName, String CSSName) throws IOException {
        App.setRoot(sceneName);
        App.scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/"+CSSName+".css").toExternalForm());
    }
}
