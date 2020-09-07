package io.woof.rlg;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static io.woof.rlg.FxmlUtils.loadFxml;

public class PreferencesController {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private void closePreferences() throws IOException {
        Pane parent = (Pane) mainAnchorPane.getParent();
        parent.getChildren().clear();
        parent.getChildren().add(loadFxml("primary"));

    }

    @FXML
    private void applyPreferences() throws IOException {
        Pane parent = (Pane) mainAnchorPane.getParent();
        parent.getChildren().clear();
        parent.getChildren().add(loadFxml("primary"));
    }
}
