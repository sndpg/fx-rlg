package io.woof.rlg;

import io.woof.rlg.model.LetterGenerator;
import io.woof.rlg.model.ModelContextHolder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static io.woof.rlg.FxmlUtils.loadFxml;

public class PreferencesController {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private AnchorPane letterSelectionAnchor;

    @FXML
    private void initialize() {
        Set<String> selectedCharacters =
                ModelContextHolder.getModelContext()
                        .getLetterGenerator()
                        .getAllowedCharacters()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.toSet());

        ObservableList<LetterProperty> letters = FXCollections.observableList(LetterGenerator.getDefaultCharacters()
                .stream()
                .map(character -> {
                    String currentLetter = character.toString();
                    return new LetterProperty(currentLetter, selectedCharacters.contains(currentLetter));
                })
                .collect(Collectors.toList()));

        letters.addListener((ListChangeListener<LetterProperty>) c -> {
            while (c.next()) {
                String changedLetter = c.getList().get(c.getFrom()).getLetter();
                if (letters.get(c.getFrom()).isSelected()) {
                    selectedCharacters.add(changedLetter);
                } else {
                    selectedCharacters.remove(changedLetter);
                }
            }
        });

        TableView<LetterProperty> tableView = new TableView<>();
        tableView.setItems(letters);
        tableView.setEditable(true);

        TableColumn<LetterProperty, String> charactersColumn = new TableColumn<>();
        charactersColumn.setCellValueFactory(cellData -> cellData.getValue().letter);

        TableColumn<LetterProperty, Boolean> column = new TableColumn<>();
        column.setCellValueFactory(cellData -> cellData.getValue().selected);
        column.setCellFactory(tableColumn -> new CheckBoxTableCell<>());

        tableView.getColumns().add(column);
        tableView.getColumns().add(charactersColumn);

        letterSelectionAnchor.getChildren().add(tableView);
    }

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

    private static class LetterProperty {
        private StringProperty letter;
        private BooleanProperty selected;

        public LetterProperty(String letter, boolean selected) {
            this.letter = new SimpleStringProperty(letter);
            this.selected = new SimpleBooleanProperty(selected);
        }

        public boolean isSelected() {
            return selected.get();
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        public String getLetter() {
            return letter.get();
        }

        public StringProperty letterProperty() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter.set(letter);
        }
    }
}
