package ink.drewf.salemstats;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

        /*TextInputDialog getName = new TextInputDialog();
        getName.setHeaderText("What name were you using in that game?");
        getName.showAndWait();
        String playerName = getName.getEditor().getText();*/

        // Check to ensure we're loading a valid log file
        String content = Files.readString(Path.of(replay.getAbsolutePath()), StandardCharsets.ISO_8859_1);
        if(!content.contains("TubaAntics and Curtis"))
        {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setHeaderText("That's not a proper game log. Try again?");
            warning.show();
        }
        else
        {
            List<String> messages = rp.parseReplay(replay);

            fileName.setText("Loaded file " + replay.getName());
            StringBuilder messageString = new StringBuilder();

            for(String m : messages)
            {
                messageString.append(m).append("\n");
            }

            chatText.setText(messageString.toString().trim());
        }


    }

}