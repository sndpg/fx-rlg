package io.woof.rlg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static io.woof.rlg.FxmlUtils.loadFxml;

public class RlgApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(loadFxml("primary"));
        primaryStage.setScene(scene);
        primaryStage.setTitle("RLG");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
