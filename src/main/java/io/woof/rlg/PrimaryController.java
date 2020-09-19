package io.woof.rlg;

import io.vavr.control.Try;
import io.woof.rlg.concurrent.CompletableFutureCollection;
import io.woof.rlg.model.LetterGenerator;
import io.woof.rlg.model.ModelContextHolder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    private final CompletableFutureCollection<Void> mainLabelCountdown = new CompletableFutureCollection<>(2);
    private final CompletableFutureCollection<Void> timeLabelCountdown = new CompletableFutureCollection<>();

    @FXML
    private Pane mainView;

    @FXML
    private Label mainLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Button startStop;

    @FXML
    private TextField timerMinutes;

    @FXML
    private TextField timerSeconds;

    @FXML
    public void initialize() {
        timerMinutes.setTextFormatter(createTimerTextFormatter(60, "minutes"));
        timerSeconds.setTextFormatter(createTimerTextFormatter(59, "seconds"));

        timerMinutes.setText(String.valueOf(DEFAULT_TIMER_DURATION.toMinutesPart()));
        timerSeconds.setText(String.valueOf(DEFAULT_TIMER_DURATION.toSecondsPart()));

        mainLabel.setText("Hi!");
        resetTimerLabel();
    }

    @FXML
    private void startStop() {
        String text = startStop.getText();

        if (text.equalsIgnoreCase(START)) {
            LetterGenerator letterGenerator = ModelContextHolder.getModelContext().getLetterGenerator();
            if (!letterGenerator.hasNext()) {
                var alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("All letters have been consumed. A new round will be started.");
                alert.setOnCloseRequest(event -> revealNextLetterAndStartTimer(letterGenerator));
                alert.show();
                return;
            }
            revealNextLetterAndStartTimer(letterGenerator);
        } else {
            resetToStartState();
        }
    }

    @FXML
    private void resetModel() {
        ModelContextHolder.getModelContext().getLetterGenerator().reset();
        resetToStartState();
        initialize();
    }

    @FXML
    private void openPreferences() throws IOException {
        mainView.getChildren().clear();
        mainView.getChildren().add(loadFxml("preferences"));
    }

    private void resetToStartState() {
        startStop.setText(START);
        if (!mainLabelCountdown.isDone() || !timeLabelCountdown.isDone()) {
            mainLabelCountdown.cancelAll();
            timeLabelCountdown.cancelAll();
            mainLabel.setText("Hi!");
            resetTimerLabel();
        }
    }

    private void revealNextLetterAndStartTimer(LetterGenerator letterGenerator) {
        startStop.setText(STOP);

        mainLabel.setText("Hi!");
        resetTimerLabel();
        startCountdown(mainLabelCountdown, mainLabel, Duration.ofSeconds(3L), "s", () -> {
            mainLabel.setText(letterGenerator.next());
            startCountdown(timeLabelCountdown, timerLabel, getTimerInputAsDuration(), "mm:ss",
                    () -> {
                        Media alarm = new Media(ClassLoader.getSystemResource("media/alarm1.mp3").toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(alarm);
                        mediaPlayer.play();
                        startStop.setText(START);
                    });
        });
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


    private void resetTimerLabel() {
        timerLabel.setText(
                DurationFormatUtils.formatDuration(getTimerInputAsDuration().toMillis(), DEFAULT_TIMER_FORMAT));
    }

    private Duration getTimerInputAsDuration() {
        return Duration.ofMinutes(Try.of(() -> Long.parseLong(timerMinutes.getText())).getOrElse(0L))
                .plus(Duration.ofSeconds(Try.of(() -> Long.parseLong(timerSeconds.getText())).getOrElse(0L)));
    }

    private TextFormatter<TextFormatter.Change> createTimerTextFormatter(int maxValue,
            String propertyNameForInvalidInput) {
        return new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                String controlNewText = change.getControlNewText();

                // only digits allowed
                boolean validInput = controlNewText.equals("") ||
                        (controlNewText.matches("\\d+") && Integer.parseInt(controlNewText) <= maxValue);
                if (!validInput) {
                    var alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText(controlNewText + " is not a valid input for " + propertyNameForInvalidInput);
                    alert.show();
                    change.setText("");
                }
            }
            resetTimerLabel();
            return change;
        });
    }

}
