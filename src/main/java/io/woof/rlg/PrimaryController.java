package io.woof.rlg;

import io.woof.rlg.concurrent.CompletableFutureCollection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static io.woof.rlg.FxmlUtils.loadFxml;
import static java.time.temporal.ChronoUnit.SECONDS;

public class PrimaryController {
    private static final Duration DEFAULT_TIMER_DURATION = Duration.ofSeconds(5L);
    private static final String DEFAULT_TIMER_FORMAT = "mm:ss";
    private static final String START = "Start";
    private static final String STOP = "Stop";

    @FXML
    private Pane mainView;

    @FXML
    private Label mainLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Button startStop;

    private final CompletableFutureCollection<Void> mainLabelCountdown = new CompletableFutureCollection<>(2);

    private final CompletableFutureCollection<Void> timeLabelCountdown = new CompletableFutureCollection<>();

    @FXML
    public void initialize() {
        mainLabel.setText("Hi!");
        resetTimerLabel();
    }

    private void resetTimerLabel() {
        timerLabel.setText(DurationFormatUtils.formatDuration(DEFAULT_TIMER_DURATION.toMillis(), DEFAULT_TIMER_FORMAT));
    }

    @FXML
    private void startStop() {
        String text = startStop.getText();

        if (text.equalsIgnoreCase(START)) {
            startStop.setText(STOP);

            mainLabel.setText("Hi!");
            resetTimerLabel();
            //            letterCountdown.add(
            //                    CompletableFuture.runAsync(() -> Platform.runLater(() -> label1.setText("after 2
            //                    sec.")),
            //                            CompletableFuture.delayedExecutor(2L, TimeUnit.SECONDS)),
            //                    CompletableFuture.runAsync(() -> Platform.runLater(() -> {
            //                        label1.setText("after 4 sec.");
            //                        startStop.setText(START);
            //                    }), CompletableFuture.delayedExecutor(4L, TimeUnit.SECONDS)));
            startCountdown(mainLabelCountdown, mainLabel, Duration.ofSeconds(3L), "s", () -> {
                mainLabel.setText("OI");
                startCountdown(timeLabelCountdown, timerLabel, DEFAULT_TIMER_DURATION, "mm:ss",
                        () -> {
                            Media alarm = new Media(ClassLoader.getSystemResource("media/alarm1.mp3").toString());
                            MediaPlayer mediaPlayer = new MediaPlayer(alarm);
                            mediaPlayer.play();
                            startStop.setText(START);
                        });
            });
        } else {
            startStop.setText(START);
            if (!mainLabelCountdown.isDone() || !timeLabelCountdown.isDone()) {
                mainLabelCountdown.cancelAll();
                timeLabelCountdown.cancelAll();
                mainLabel.setText("Hi!");
                resetTimerLabel();
            }
        }
    }

    @FXML
    private void openPreferences() throws IOException {
        mainView.getChildren().clear();
        mainView.getChildren().add(loadFxml("preferences"));
    }

    private CompletableFutureCollection<Void> startCountdown(Label label, Duration from, String format,
            Runnable finalizerOperation) {
        return startCountdown(new CompletableFutureCollection<>(), label, from, format, finalizerOperation);
    }

    private CompletableFutureCollection<Void> startCountdown(CompletableFutureCollection<Void> completableFutures,
            Label label, Duration from, String format, Runnable finalizerOperation) {
        long totalSeconds = from.get(SECONDS);

        for (long l = totalSeconds; l >= 0; l--) {
            var currentSecond = l;
            completableFutures.add(
                    CompletableFuture.runAsync(() -> Platform.runLater(
                            () -> {
                                label.setText(DurationFormatUtils.formatDuration(currentSecond * 1000L, format));
                                if (currentSecond == 0L) {
                                    finalizerOperation.run();
                                }
                            }),
                            CompletableFuture.delayedExecutor(totalSeconds - l, TimeUnit.SECONDS)));
        }
        return completableFutures;
    }

}
