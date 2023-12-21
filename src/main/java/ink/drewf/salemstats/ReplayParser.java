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
                String playerNum = rawString.substring(0, rawString.indexOf(']') + 1);
                String playerName = rawString.substring(rawString.indexOf(" ") + 1, rawString.indexOf("-") - 1);
                String playerRole = element.nextElementSibling().text();
                String playerUsername = "";
                for(Element elem : element.nextElementSiblings())
                {
                    if(elem.text().startsWith("(Username: "))
                    {
                        String rawUsername = elem.text();
                        playerUsername = rawUsername.substring(rawUsername.indexOf(":") + 2, rawUsername.indexOf(")"));
                        break;
                    }
                }
                messages.add(playerNum + " " + playerName + " (" + playerUsername + ") -- " + playerRole);
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
