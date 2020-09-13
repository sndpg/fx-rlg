package io.woof.rlg;

import io.woof.rlg.model.LetterGenerator;
import io.woof.rlg.model.ModelContextHolder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TableView<LetterProperty> lettersTable;

    private Set<String> selectedCharacters;

    @FXML
    private void initialize() {
        selectedCharacters = ModelContextHolder.getModelContext()
                .getLetterGenerator()
                .getAllowedCharacters()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet());

        ObservableList<LetterProperty> letterProperties =
                FXCollections.observableList(LetterGenerator.getDefaultCharacters()
                        .stream()
                        .map(character -> {
                            String currentLetter = character.toString();
                            return new LetterProperty(currentLetter, selectedCharacters.contains(currentLetter));
                        })
                        .collect(Collectors.toList()));

        lettersTable.setItems(letterProperties);
        lettersTable.setEditable(true);

        TableColumn<LetterProperty, String> charactersColumn = new TableColumn<>();
        charactersColumn.setCellValueFactory(cellData -> cellData.getValue().letterProperty());

        CheckBox selectAllCheckBox = new CheckBox();
        setSelectAllCheckBoxState(letterProperties, selectAllCheckBox);

        selectAllCheckBox.setOnAction(
                actionEvent -> {
                    CheckBox target = (CheckBox) actionEvent.getTarget();
                    letterProperties.forEach(letterProperty -> {
                        boolean selected = target.isSelected();
                        letterProperty.setSelected(selected);
                        if (selected) {
                            selectedCharacters.add(letterProperty.getLetter());
                        } else {
                            selectedCharacters.remove(letterProperty.getLetter());
                        }
                    });
                });

        ContextMenu lettersTableContextMenu = createLettersTableContextMenu(letterProperties, selectAllCheckBox);
        charactersColumn.setContextMenu(lettersTableContextMenu);

        TableColumn<LetterProperty, Boolean> checkBoxColumn = new TableColumn<>();

        checkBoxColumn.setGraphic(selectAllCheckBox);
        checkBoxColumn.setContextMenu(lettersTableContextMenu);
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        checkBoxColumn.setCellFactory(tableColumn -> {
            CheckBoxTableCell<LetterProperty, Boolean> checkBoxTableCell = new CheckBoxTableCell<>();
            checkBoxTableCell.addEventHandler(ActionEvent.ACTION, actionEvent -> {
                CheckBox target = (CheckBox) actionEvent.getTarget();
                CheckBoxTableCell<LetterProperty, Boolean> cell =
                        ((CheckBoxTableCell<LetterProperty, Boolean>) target.getParent());
                boolean isSelected = cell.isSelected();
                String value = charactersColumn.getCellObservableValue(cell.getIndex()).getValue();
                if (isSelected) {
                    selectedCharacters.add(value);
                } else {
                    selectedCharacters.remove(value);
                }
                setSelectAllCheckBoxState(letterProperties, selectAllCheckBox);
            });
            return checkBoxTableCell;
        });

        lettersTable.getColumns().add(checkBoxColumn);
        lettersTable.getColumns().add(charactersColumn);
    }

    @FXML
    private void closePreferences() throws IOException {
        var parent = (Pane) mainAnchorPane.getParent();
        parent.getChildren().clear();
        parent.getChildren().add(loadFxml("primary"));
    }

    @FXML
    private void applyPreferences() throws IOException {
        var parent = (Pane) mainAnchorPane.getParent();
        ModelContextHolder.getModelContext().setLetterGenerator(new LetterGenerator(
                selectedCharacters.stream()
                        .map(s -> s.charAt(0))
                        .collect(Collectors.toSet())));
        parent.getChildren().clear();
        parent.getChildren().add(loadFxml("primary"));
    }

    private ContextMenu createLettersTableContextMenu(ObservableList<LetterProperty> letterProperties,
            CheckBox selectAllCheckBox) {
        var contextMenu = new ContextMenu();

        var selectAll = new MenuItem();
        selectAll.setText("select all");
        selectAll.addEventHandler(ActionEvent.ACTION,
                event -> {
                    letterProperties.forEach(letterProperty -> {
                        letterProperty.setSelected(true);
                        selectedCharacters.add(letterProperty.getLetter());
                    });
                    setSelectAllCheckBoxState(letterProperties, selectAllCheckBox);
                });

        var deselectAll = new MenuItem();
        deselectAll.setText("deselect all");
        deselectAll.addEventHandler(ActionEvent.ACTION,
                event -> {
                    letterProperties.forEach(letterProperty -> {
                        letterProperty.setSelected(false);
                        selectedCharacters.remove(letterProperty.getLetter());
                    });
                    setSelectAllCheckBoxState(letterProperties, selectAllCheckBox);
                });

        var invert = new MenuItem();
        invert.setText("invert");
        invert.addEventHandler(ActionEvent.ACTION, event -> {
            letterProperties.forEach(
                    letterProperty -> {
                        boolean newSelectedState = !letterProperty.isSelected();
                        letterProperty.setSelected(newSelectedState);
                        if (newSelectedState) {
                            selectedCharacters.add(letterProperty.getLetter());
                        } else {
                            selectedCharacters.remove(letterProperty.getLetter());
                        }
                    });
            setSelectAllCheckBoxState(letterProperties, selectAllCheckBox);
        });

        var menuItems = contextMenu.getItems();
        menuItems.add(selectAll);
        menuItems.add(deselectAll);
        menuItems.add(new SeparatorMenuItem());
        menuItems.add(invert);

        return contextMenu;
    }

    private void setSelectAllCheckBoxState(ObservableList<LetterProperty> letterProperties, CheckBox checkBox) {
        if (letterProperties.stream().allMatch(LetterProperty::isSelected)) {
            checkBox.setSelected(true);
        } else if (letterProperties.stream().noneMatch(LetterProperty::isSelected)) {
            checkBox.setSelected(false);
        } else {
            checkBox.setIndeterminate(true);
        }
    }

    private static class LetterProperty {
        private final StringProperty letter;
        private final BooleanProperty selected;

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
