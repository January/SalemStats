package ink.drewf.salemstats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ink.drewf.salemstats.game.Death;
import ink.drewf.salemstats.game.Event;
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

        // Presently gamelogs don't always log the day/night
        // In fact, it's only like 1/6 of the time
        List<String> currentPhase = new ArrayList<>(Arrays.asList(new String[]{"Unknown"}));

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
                        String trimmedUsername = rawUsername.substring(rawUsername.indexOf("(Username: "));
                        playerUsername = trimmedUsername.substring(11, trimmedUsername.indexOf(")"));
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

                // Day/night headers
                // These aren't always logged by the gamelogs mod. Not sure why.
                if(rawText.matches("(Day|Night) [0-9]+"))
                {
                    if(rawText.startsWith("Day"))
                    {
                        String dayNum = rawText.substring(rawText.indexOf(" "));
                        String currentDay = "DAY" + dayNum;
                        messages.add(currentDay);
                        currentPhase.add(currentDay);
                    }
                    if(rawText.startsWith("Night"))
                    {
                        String nightNum = rawText.substring(rawText.indexOf(" "));
                        String currentNight = "NIGHT" + nightNum;
                        messages.add(currentNight);
                        currentPhase.add(currentNight);
                    }
                    messages.add("---------------------------");
                }
                else if(!rawText.contains("very easy to modify") && !rawText.contains("TubaAntics and Curtis") && !rawText.equals("PLAYER INFO"))
                {
                    String eventType = "";

                    // Major events
                    if(rawText.endsWith("They must be a Ritualist!"))
                        eventType = "Ritualist guessed wrong";
                    else if(rawText.endsWith(", Destroyer of Worlds and Horseman of the Apocalypse!"))
                        eventType = "Death transformation";
                    else if(rawText.endsWith("A famine has begun!"))
                        eventType = "Famine transformation";
                    else if(rawText.startsWith("A Plague has consumed the Town,"))
                        eventType = "Pestilence transformation";
                    else if(rawText.endsWith("Cry 'Havoc!', and let slip the dogs of war."))
                        eventType = "War transformation";

                    if(!eventType.isEmpty())
                    {
                        GuiController.addEvent(new Event(currentPhase.get(currentPhase.size() - 1), eventType));
                    }

                    // Deaths
                    if(rawText.contains("died last night.") || rawText.contains("died today.") || rawText.contains("has accomplished their goal as") )
                    {
                        int deathPhase = currentPhase.size() - 1;
                        String deadPlayer = "";
                        List<String> deathCauses = new ArrayList<>();
                        if(rawText.contains("died last night."))
                        {
                            if(!currentPhase.get(deathPhase).equals("Unknown"))
                            {
                                deathPhase--;
                            }
                            deadPlayer = rawText.substring(0, rawText.indexOf(" died last night."));
                        }
                        else if(rawText.contains("has accomplished their goal as"))
                        {
                            if(!currentPhase.get(deathPhase).equals("Unknown"))
                            {
                                deathPhase--;
                            }
                            deadPlayer = rawText.substring(0, rawText.indexOf(" has accomplished their goal as"));
                            GuiController.addDeath(new Death(currentPhase.get(deathPhase), deadPlayer, List.of("Won & left town")));
                        }
                        else
                        {
                            deadPlayer = rawText.substring(0, rawText.indexOf(" died today."));
                        }
                        if(!rawText.contains("has accomplished their goal as"))
                        {
                            for(Element e : element.nextElementSiblings())
                            {
                                String rawMsg = e.text();
                                if(rawMsg.startsWith("They were ") || rawMsg.endsWith("convicted and executed.") || rawMsg.contains("disconnected from life.")
                                        || rawMsg.contains("lost a duel with a") || rawMsg.contains("visited a Serial Killer.")
                                        || rawMsg.endsWith("to the noose."))
                                {
                                    // Ones that will look awkward if not high-priority
                                    if(rawMsg.contains("died while defending their target."))
                                    {
                                        deathCauses.add("Defending target");
                                    }

                                    // Evil causes
                                    if(rawMsg.contains("by the Coven."))
                                    {
                                        deathCauses.add("Coven");
                                    }
                                    if(rawMsg.contains("by a Jinx."))
                                    {
                                        deathCauses.add("Jinx");
                                    }
                                    if(rawMsg.contains("by a Conjurer."))
                                    {
                                        deathCauses.add("Conjurer");
                                    }
                                    if(rawMsg.contains("a Serial Killer."))
                                    {
                                        deathCauses.add("Serial Killer");
                                    }
                                    if(rawMsg.contains("by a Werewolf."))
                                    {
                                        deathCauses.add("Werewolf");
                                    }
                                    if(rawMsg.contains("by a Ritualist"))
                                    {
                                        deathCauses.add("Ritualist");
                                    }
                                    if(rawMsg.contains("by a Shroud."))
                                    {
                                        deathCauses.add("Shroud");
                                    }
                                    if(rawMsg.contains("by an Executioner."))
                                    {
                                        deathCauses.add("Torment");
                                    }
                                    if(rawMsg.contains("lost a duel"))
                                    {
                                        deathCauses.add("Pirate");
                                    }
                                    if(rawMsg.contains("by a Jester."))
                                    {
                                        deathCauses.add("Jester");
                                    }
                                    if(rawMsg.contains("by a Doomsayer"))
                                    {
                                        deathCauses.add("Doomsayer");
                                    }
                                    if(rawMsg.contains("by a Berserker"))
                                    {
                                        deathCauses.add("Berserker");
                                    }
                                    if(rawMsg.contains("by War,"))
                                    {
                                        deathCauses.add("War");
                                    }
                                    if(rawMsg.contains("to a pestilence"))
                                    {
                                        deathCauses.add("Pestilence");
                                    }
                                    if(rawMsg.contains("died of starvation"))
                                    {
                                        deathCauses.add("Famine");
                                    }
                                    if(rawMsg.contains("by an Arsonist"))
                                    {
                                        deathCauses.add("Arsonist");
                                    }
                                    if(rawMsg.contains("disconnected from life."))
                                    {
                                        deathCauses.add("Disconnected");
                                    }

                                    // Town causes
                                    if(rawMsg.endsWith("convicted and executed.") || rawMsg.endsWith("to the noose."))
                                    {
                                        deathCauses.add("Hanged");
                                    }
                                    if(rawMsg.endsWith("were Prosecuted."))
                                    {
                                        deathCauses.add("Prosecutor");
                                    }
                                    if(rawMsg.contains("by a Trapper."))
                                    {
                                        deathCauses.add("Trapper");
                                    }
                                    if(rawMsg.contains("by a Vigilante."))
                                    {
                                        deathCauses.add("Vigilante");
                                    }
                                    if(rawMsg.contains("by a Veteran."))
                                    {
                                        deathCauses.add("Veteran");
                                    }
                                    if(rawMsg.contains("by a Bodyguard."))
                                    {
                                        deathCauses.add("Bodyguard");
                                    }
                                    if(rawMsg.contains("by a Trickster."))
                                    {
                                        deathCauses.add("Trickster");
                                    }
                                    if(rawMsg.contains("by the Jailor."))
                                    {
                                        deathCauses.add("Jailor");
                                    }
                                    if(rawMsg.contains("by a Crusader"))
                                    {
                                        deathCauses.add("Crusader");
                                    }
                                    if(rawMsg.contains("by a Deputy."))
                                    {
                                        deathCauses.add("Deputy");
                                    }
                                    GuiController.addDeath(new Death(currentPhase.get(deathPhase), deadPlayer, deathCauses));
                                    break;
                                }
                            }
                        }
                    }
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
