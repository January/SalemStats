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
    public List<String> parseReplay(File replay, String pNameRaw) throws IOException
    {
        Document doc = Jsoup.parse(replay, "UTF-8", "");

        // Convert spaces to dashes in line with replay classes
        String playerName = pNameRaw.replace(" ", "-");

        // TODO: Mark days!

        // Get messages from when the player was alive
        Elements content = doc.getElementsByClass(playerName);
        List<String> messages = new ArrayList<String>();
        for(Element tc : content)
        {
            String number = tc.previousElementSibling().text();
            String messageText = tc.nextElementSibling().text();
            messages.add(number + " " + pNameRaw + messageText);
        }

        // Get messages from when the player was dead
        // Doing this after alive messages is OK because old ret doesn't exist anymore, so there's no circumstance where
        // one can become alive again after dying
        Elements deadMessages = doc.select("[style*='color:#689194']");
        for(Element tc : deadMessages)
        {
            String text = tc.text();
            if(!text.startsWith(":") && !text.startsWith("[") && text.contains(playerName))
            {
                String number = tc.parent().previousElementSibling().text();
                String messageText = tc.nextElementSibling().text();
                messages.add(number + " (Dead) " + pNameRaw + messageText);
            }
        }

        return messages;
    }

}
