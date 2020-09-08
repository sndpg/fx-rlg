package io.woof.rlg;

import io.woof.rlg.concurrent.CompletableFutureCollection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.woof.rlg.FxmlUtils.loadFxml;

public class PrimaryController {

    private static final String START = "Start";
    private static final String STOP = "Stop";

    @FXML
    private Pane mainView;

    @FXML
    private Label label1;

    @FXML
    private Button startStop;

    private CompletableFutureCollection<Void> letterCountdown = new CompletableFutureCollection<>(2);

    @FXML
    private void startStop() {
        String text = startStop.getText();

        if (text.equalsIgnoreCase(START)) {
            startStop.setText(STOP);

            label1.setText("Hi!");
            letterCountdown.add(CompletableFuture.runAsync(() -> Platform.runLater(() -> label1.setText("after 2 sec.")),
                            CompletableFuture.delayedExecutor(2L, TimeUnit.SECONDS)),
                    CompletableFuture.runAsync(() -> Platform.runLater(() -> {
                        label1.setText("after 4 sec.");
                        startStop.setText(START);
                    }), CompletableFuture.delayedExecutor(4L, TimeUnit.SECONDS)));
        } else {
            startStop.setText(START);
            if (letterCountdown != null && !letterCountdown.isDone()){
                letterCountdown.cancelAll();
                label1.setText("Hi!");
            }
        }
    }

    @FXML
    private void openPreferences() throws IOException {
        mainView.getChildren().clear();
        mainView.getChildren().add(loadFxml("preferences"));
    }

}
