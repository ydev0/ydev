package org.openjfx.util;

import javafx.scene.Scene;
import org.openjfx.App;

import java.io.IOException;

public interface SceneSwitcher {
    default void switchScene(String sceneName){
        try{
            App.setRoot(sceneName);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
