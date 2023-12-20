package ink.drewf.salemstats;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class GuiController
{
    @FXML
    private Label fileName;
    public TextArea chatText;
    private final ReplayParser rp = new ReplayParser();

    @FXML
    protected void loadFile() throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        File replay = fileChooser.showOpenDialog(new Stage());

        TextInputDialog getName = new TextInputDialog();
        getName.setHeaderText("What name were you using in that game?");
        getName.showAndWait();
        String playerName = getName.getEditor().getText();

        List<String> messages = rp.parseReplay(replay, playerName);

        fileName.setText("Loaded file " + replay.getName());
        StringBuilder messageString = new StringBuilder();

        for(String m : messages)
        {
            messageString.append(m).append("\n");
        }

        chatText.setText(messageString.toString());
    }
}