package io.woof.rlg;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Utility class for loading .fxml from the classpath
 */
public class FxmlUtils {

    private FxmlUtils() {
        throw new IllegalStateException("this is a utility class which cannot be instantiated");
    }

    public static Parent loadFxml(String fxmlName) throws IOException {
        Enumeration<URL> systemResources = ClassLoader.getSystemResources("fxml/" + fxmlName + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(systemResources.nextElement());
        return fxmlLoader.load();
    }
}
