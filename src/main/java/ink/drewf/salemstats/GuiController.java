package ink.drewf.salemstats;

import ink.drewf.salemstats.game.Death;
import ink.drewf.salemstats.game.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    public TextArea chatText;

    // Players table
    @FXML
    private TableView<Player> gameTable;
    @FXML
    private TableColumn<Player, Integer> numberColumn;
    @FXML
    private TableColumn<Player, String> nameColumn;
    @FXML
    private TableColumn<Player, String> roleColumn;
    @FXML
    private TableColumn<Player, String> factionColumn;
    @FXML
    private TableColumn<Player, String> subfactionColumn;
    @FXML
    private TableColumn<Player, String> usernameColumn;

    // Deaths table
    @FXML
    private TableView<Death> deathTable;
    @FXML
    private TableColumn<Death, String> deathTimeColumn;
    @FXML
    private TableColumn<Death, String> deadPlayerColumn;
    @FXML
    private TableColumn<Death, String> killerColumn;

    private final ReplayParser rp = new ReplayParser();
    private static final ObservableList<Player> playerList = FXCollections.observableArrayList();

    public static void addPlayer(Player p)
    {
        playerList.add(p);
    }

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
            playerList.clear();
            List<String> messages = rp.parseReplay(replay);

            numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            roleColumn.setCellValueFactory(new PropertyValueFactory<>("Role"));
            factionColumn.setCellValueFactory(new PropertyValueFactory<>("Faction"));
            subfactionColumn.setCellValueFactory(new PropertyValueFactory<>("Subfaction"));
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));

            gameTable.setItems(playerList);

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