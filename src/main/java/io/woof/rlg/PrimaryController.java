package io.woof.rlg;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private BorderPane mainView;

    @FXML
    private void openPreferences() throws IOException {
        mainView.setCenter(RlgApplication.loadFxml("preferences"));
    }
}
