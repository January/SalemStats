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
        Elements content = doc.getElementsByClass(playerName);
        List<String> messages = new ArrayList<String>();
        for(Element tc : content)
        {
            String number = tc.previousElementSibling().text();
            String messageText = tc.nextElementSibling().text();
            messages.add(number + " " + playerName + messageText);
        }
        return messages;
    }

}
