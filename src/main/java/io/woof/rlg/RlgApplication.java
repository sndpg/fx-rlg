package io.woof.rlg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RlgApplication extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        scene = new Scene(loadFxml("primary"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static Scene getScene(){
        return scene;
    }

    static void setRoot(String fxmlName) throws IOException {
        scene.setRoot(loadFxml(fxmlName));
    }


    static Parent loadFxml(String fxmlName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Thread.currentThread().getContextClassLoader().getResource("fxml/" + fxmlName + ".fxml"));
        return fxmlLoader.load();
    }
}
