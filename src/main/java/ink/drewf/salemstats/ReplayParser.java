package ink.drewf.salemstats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        messages.add("Players in this game: ");
        for (Element element : elements)
        {
            // Player data
            if(element.text().endsWith(" -"))
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

            // Day/night headers
            // These aren't always logged. Not sure why.
            if(element.text().startsWith("Day") || element.text().startsWith("Night"))
            {
                String rawString = element.text();
                if(rawString.startsWith("Day"))
                {
                    String dayNum = rawString.substring(rawString.indexOf(" "));
                    messages.add("DAY" + dayNum);
                }
                if(rawString.startsWith("Night"))
                {
                    String nightNum = rawString.substring(rawString.indexOf(" "));
                    messages.add("NIGHT" + nightNum);
                }
                messages.add("---------------------------");
            }

            // Status messages
            // Surely there's a better way of doing this. Just need to figure it out
            if(element.attributes().toString().contains("background-color"))
            {
                String rawText = element.text();
                if(!rawText.contains("very easy to modify") && !rawText.contains("TubaAntics and Curtis")
                        && !rawText.equals("PLAYER INFO") && !rawText.matches("(Day|Night) [0-9]+"))
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
