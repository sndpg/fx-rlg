package io.woof.rlg;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Utility class for loading .fxml from the classpath
 */
public class FxmlUtils {

    private FxmlUtils(){
        throw new IllegalStateException("this is a utility class which cannot be instantiated");
    }

    public static Parent loadFxml(String fxmlName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Thread.currentThread().getContextClassLoader().getResource("fxml/" + fxmlName + ".fxml"));
        return fxmlLoader.load();
    }
}
