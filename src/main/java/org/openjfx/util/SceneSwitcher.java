package org.openjfx.util;

import javafx.scene.Scene;
import org.openjfx.App;

import java.io.IOException;

public interface SceneSwitcher {
    default void switchScene(String sceneName, String CSSName) throws IOException {
        App.setRoot(sceneName);
        App.scene.getStylesheets().add(getClass().getResource("/org/openjfx/CSS/"+CSSName+".css").toExternalForm());
    }
}
