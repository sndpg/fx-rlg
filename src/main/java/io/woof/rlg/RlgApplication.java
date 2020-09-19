package io.woof.rlg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static io.woof.rlg.FxmlUtils.loadFxml;

public class RlgApplication extends Application {

    private static final Image APP_ICON_36 =
            new Image(ClassLoader.getSystemResource("media/baseline_groups_black_36dp.png").toString());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(loadFxml("primary"));
        primaryStage.getIcons().add(APP_ICON_36);
        primaryStage.setScene(scene);
        primaryStage.setTitle("RLG");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
