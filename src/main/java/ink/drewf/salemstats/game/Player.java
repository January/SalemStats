package ink.drewf.salemstats.game;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Arrays;
import java.util.List;

public class Player
{
    // Town role groupings
    private final List<String> townInvestigative = Arrays.asList("Coroner", "Investigator", "Lookout", "Psychic", "Seer",
            "Sheriff", "Spy", "Tracker");
    private final List<String> townProtective = Arrays.asList("Bodyguard", "Cleric", "Trapper", "Crusader");
    private final List<String> townKilling = Arrays.asList("Vigilante", "Veteran", "Trickster", "Deputy");
    private final List<String> townSupport = Arrays.asList("Amnesiac", "Admirer", "Tavern Keeper", "Retributionist");
    private final List<String> townPower = Arrays.asList("Jailor", "Mayor", "Monarch", "Prosecutor");

    // Neutral role groupings
    private final List<String> neutralEvil = Arrays.asList("Jester", "Executioner", "Pirate", "Doomsayer");
    private final List<String> neutralKilling = Arrays.asList("Arsonist", "Serial Killer", "Shroud", "Werewolf");
    private final List<String> neutralApoc = Arrays.asList("Plaguebearer", "Berserker", "Soul Collector", "Baker");

    // Coven role groupings
    private final List<String> covenPower = Arrays.asList("Coven Leader", "Hex Master", "Witch");
    private final List<String> covenKilling = Arrays.asList("Conjurer", "Ritualist", "Jinx");
    private final List<String> covenDeception = Arrays.asList("Medusa", "Illusionist", "Dreamweaver", "Enchanter");
    private final List<String> covenUtility = Arrays.asList("Potion Master", "Necromancer", "Poisoner", "Voodoo Master", "Wildling");

    private final SimpleIntegerProperty number;
    private final SimpleStringProperty name;
    private final SimpleStringProperty username;
    private final SimpleStringProperty role;
    private SimpleStringProperty faction;
    private SimpleStringProperty subfaction;
    public Player(int number, String name, String username, String role)
    {
        this.number = new SimpleIntegerProperty(number);
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
        findFaction(role);
    }

    private void findFaction(String role)
    {
        // There's a better way to do all this, surely
        if(townInvestigative.contains(role))
        {
            this.faction = new SimpleStringProperty("Town");
            this.subfaction = new SimpleStringProperty("Investigative");
        }
        else if(townProtective.contains(role))
        {
            this.faction = new SimpleStringProperty("Town");
            this.subfaction = new SimpleStringProperty("Protective");
        }
        else if(townKilling.contains(role))
        {
            this.faction = new SimpleStringProperty("Town");
            this.subfaction = new SimpleStringProperty("Killing");
        }
        else if(townSupport.contains(role))
        {
            this.faction = new SimpleStringProperty("Town");
            this.subfaction = new SimpleStringProperty("Support");
        }
        else if(townPower.contains(role))
        {
            this.faction = new SimpleStringProperty("Town");
            this.subfaction = new SimpleStringProperty("Power");
        }
        else if(neutralEvil.contains(role))
        {
            this.faction = new SimpleStringProperty("Neutral");
            this.subfaction = new SimpleStringProperty("Evil");
        }
        else if(neutralKilling.contains(role))
        {
            this.faction = new SimpleStringProperty("Neutral");
            this.subfaction = new SimpleStringProperty("Killing");
        }
        else if(neutralApoc.contains(role))
        {
            this.faction = new SimpleStringProperty("Neutral");
            this.subfaction = new SimpleStringProperty("Apocalypse");
        }
        else if(covenPower.contains(role))
        {
            this.faction = new SimpleStringProperty("Coven");
            this.subfaction = new SimpleStringProperty("Power");
        }
        else if(covenKilling.contains(role))
        {
            this.faction = new SimpleStringProperty("Coven");
            this.subfaction = new SimpleStringProperty("Killing");
        }
        else if(covenDeception.contains(role))
        {
            this.faction = new SimpleStringProperty("Coven");
            this.subfaction = new SimpleStringProperty("Deception");
        }
        else if(covenUtility.contains(role))
        {
            this.faction = new SimpleStringProperty("Coven");
            this.subfaction = new SimpleStringProperty("Utility");
        }
        else
        {
            // Cursed Soul, Vampire
            this.faction = new SimpleStringProperty(role);
            this.subfaction = new SimpleStringProperty("N/A");
        }
    }

    public int getNumber()
    {
        return number.get();
    }

    public void setNumber(int number)
    {
        this.number.set(number);
    }

    public String getName()
    {
        return name.get();
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public String getUsername()
    {
        return username.get();
    }

    public void setUsername(String username)
    {
        this.username.set(username);
    }

    public String getRole()
    {
        return role.get();
    }

    public void setRole(String role)
    {
        this.role.set(role);
    }

    public String getFaction()
    {
        return faction.get();
    }

    public void setFaction(String faction)
    {
        this.faction.set(faction);
    }

    public String getSubfaction()
    {
        return subfaction.get();
    }

    public void setSubfaction(String subfaction)
    {
        this.subfaction.set(subfaction);
    }
}
