package ink.drewf.salemstats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ink.drewf.salemstats.game.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReplayParser
{
    public List<String> parseReplay(File replay) throws IOException
    {
        Document doc = Jsoup.parse(replay, "UTF-8", "");
        Elements elements = doc.body().select("*");

        List<String> messages = new ArrayList<>();

        for (Element element : elements)
        {
            // Player data
            if(element.text().endsWith(" -") && element.text().startsWith("["))
            {
                String rawString = element.text();
                String rawNumber = rawString.substring(rawString.indexOf('[') + 1, rawString.indexOf(']'));
                int playerNum = Integer.parseInt(rawNumber);
                String playerName = rawString.substring(rawString.indexOf(" ") + 1, rawString.indexOf("-") - 1);
                String playerRole = element.nextElementSibling().text();
                String playerUsername = "";
                for(Element elem : element.nextElementSiblings())
                {
                    if(elem.text().contains("(Username: "))
                    {
                        String rawUsername = elem.text();
                        playerUsername = rawUsername.substring(rawUsername.indexOf(":") + 2, rawUsername.indexOf(")"));
                        break;
                    }
                }
                GuiController.addPlayer(new Player(playerNum, playerName, playerUsername, playerRole));
            }

            // Status messages
            // Surely there's a better way of doing this. Just need to figure it out
            if(element.attributes().toString().contains("background-color"))
            {
                String rawText = element.text();
                if(rawText.matches("(Day|Night) [0-9]+"))
                {
                    // Day/night headers
                    // These aren't always logged. Not sure why.
                    if(rawText.startsWith("Day"))
                    {
                        String dayNum = rawText.substring(rawText.indexOf(" "));
                        messages.add("DAY" + dayNum);
                    }
                    if(rawText.startsWith("Night"))
                    {
                        String nightNum = rawText.substring(rawText.indexOf(" "));
                        messages.add("NIGHT" + nightNum);
                    }
                    messages.add("---------------------------");
                }
                else if(!rawText.contains("very easy to modify") && !rawText.contains("TubaAntics and Curtis")
                        && !rawText.equals("PLAYER INFO"))
                {
                    messages.add(rawText);
                }
            }

            // Chat messages
            if(element.text().startsWith(":"))
            {
                // If the message is styled that means we're dead
                if(element.hasAttr("style"))
                {
                    String number = element.parent().previousElementSibling().text();
                    // Dead chat names replace spaces with hyphens, undo that
                    String pNameRaw = element.previousElementSibling().text();
                    String playerName = pNameRaw.replace("-", " ");
                    String messageText = element.text();
                    messages.add(number + " (Dead) " + playerName + messageText);
                }
                else
                {
                    Element nameElement = element.previousElementSibling();
                    String playerNum = nameElement.previousElementSibling().text();
                    String playerName = nameElement.text();
                    String playerMsg = element.text();
                    messages.add(playerNum + " " + playerName + playerMsg);
                }
            }
        }
        return messages;
    }
}
