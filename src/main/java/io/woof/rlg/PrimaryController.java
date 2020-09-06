package io.woof.rlg;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.woof.rlg.FxmlUtils.loadFxml;

public class PrimaryController {

    @FXML
    private BorderPane mainView;

    @FXML
    private Label label1;

    @FXML
    private void test() {
        label1.setText("Hi!");

        CompletableFuture.delayedExecutor(2L, TimeUnit.SECONDS)
                .execute(() -> Platform.runLater(() -> label1.setText("after 2 sec.")));
        CompletableFuture.delayedExecutor(4L, TimeUnit.SECONDS)
                .execute(() -> Platform.runLater(() -> label1.setText("after 4 sec.")));
    }

    @FXML
    private void openPreferences() throws IOException {
        mainView.setCenter(loadFxml("preferences"));
    }
}
