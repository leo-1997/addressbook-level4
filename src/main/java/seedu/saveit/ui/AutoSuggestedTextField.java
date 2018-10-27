package seedu.saveit.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import seedu.saveit.logic.Logic;
import seedu.saveit.model.Issue;

/**
 * The TextField component which supports auto key word suggestion
 */
public class AutoSuggestedTextField extends TextField {

    private static ContextMenu popUpWindow;
    private static TreeSet<String> storageSet;
    private static Logic logic;
    private List<String> keyWords = new ArrayList<>();

    public AutoSuggestedTextField() {
        super();
    }

    /**
     * Adds new listener to the text field to handle the key suggestion
     */
    public void initialise(Logic logic) {
        this.logic = logic;
        for (Issue issue : logic.getFilteredIssueList()) {
            this.keyWords.add(issue.getStatement().issue);
        }
        popUpWindow = new ContextMenu();
        storageSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        addAllKeyWord();
        this.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (getText().length() == 0 || !getText().contains("find")) {
                    popUpWindow.hide();
                } else {
                    showResult(AutoSuggestedTextField.this);
                }
            }
        });
    }

    /**
     * Updates the keywords stored in the class whenever there is a change on issue list in the storage.
     */
    public void update(Logic logic) {
        this.logic = logic;
        keyWords.clear();
        for (Issue issue : logic.getFilteredIssueList()) {
            this.keyWords.add(issue.getStatement().issue);
        }
        addAllKeyWord();
    }

    /**
     * Analyses the input string and suggests the key words
     */
    private void showResult(TextField textField) {
        String mainText = textField.getText();
        String text;
        int whiteSpaceIndex = mainText.indexOf(" ") + 1;
        if (whiteSpaceIndex != -1) {
            text = mainText.substring(whiteSpaceIndex, mainText.length()).trim();
        } else {
            text = mainText.trim();
        }
        LinkedList<String> searchResult = new LinkedList<>();
        searchResult.addAll(storageSet.subSet(text, text + Character.MAX_VALUE));
        if (searchResult.size() > 0 && text.length() > 0) {
            //hide the suggestion window if the issue statement is already entered completely
            if (searchResult.size() == 1 && searchResult.get(0).equals(text)) {
                popUpWindow.hide();
            } else {
                int maxNum = 8;
                int count = Math.min(searchResult.size(), maxNum);
                List<CustomMenuItem> menuItems = new LinkedList<>();
                for (int i = 0; i < count; i++) {
                    final String result = searchResult.get(i);
                    final String previousText = textField.getText();
                    Label entryLabel = new Label(result);
                    textField.requestFocus();
                    CustomMenuItem item = new CustomMenuItem(entryLabel, true);
                    int initIndex = whiteSpaceIndex;
                    item.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            textField.setText(previousText.substring(0, initIndex) + result);
                            textField.positionCaret(textField.getLength());
                            popUpWindow.hide();
                        }
                    });
                    menuItems.add(item);
                }
                popUpWindow.getItems().clear();
                popUpWindow.getItems().addAll(menuItems);
                popUpWindow.hide();
                popUpWindow.show(textField, Side.BOTTOM, (double) textField.getCaretPosition() * 8, 0);
            }
        } else {
            popUpWindow.hide();
        }
    }

    /**
     * Adds all the stored key words to the treeSet
     */
    private void addAllKeyWord() {
        storageSet.clear();
        for (String str : keyWords) {
            storageSet.add(str);
        }
    }
}
