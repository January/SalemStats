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

        for (Element element : elements)
        {
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
