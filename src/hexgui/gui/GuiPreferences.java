//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import java.io.*;
import java.util.*;
import java.util.prefs.*;

//----------------------------------------------------------------------------

/** GuiPreferences */
public final class GuiPreferences
{
    private static String[][] s_preflist = new String[][]
	{ 
	    {"gui-board-type",                    "Diamond"},
	    {"gui-board-on-top",                  "white"},
	    {"gui-board-pixel-width",             "800"},
	    {"gui-board-pixel-height",            "600"},
	    {"gui-board-width",                   "11"},
	    {"gui-board-height",                  "11"},

	    {"gui-toolbar-visible",               "true"},

	    {"shell-show-on-connect",             "true"},

            {"auto-respond",                      "true"},

	    {"path-local-program",                "./htpwolve"},
	    {"path-load-game",                    "../games/"},
	    {"path-save-game",                    "../games/"},

	    {"dummy-preference",                  ""}
	};
    

    public GuiPreferences(Class theClass)
    {
	m_preferences = Preferences.userNodeForPackage(theClass);
	
    }

    public String get(String name)
    {
	for (int i=0; i<s_preflist.length; i++) {
	    if (name.equals(s_preflist[i][0])) {
		return m_preferences.get(s_preflist[i][0], s_preflist[i][1]);
	    }
	}
	System.out.println("Unknown preference: " + name + 
			   ", returning 'unknown'.");
	return "unknown";
    }

    public int getInt(String name)
    { 
	return Integer.parseInt(get(name)); 
    }

    public boolean getBoolean(String name)
    {	
	return Boolean.parseBoolean(get(name));
    }


    public void put(String name, String value)
    {
	for (int i=0; i<s_preflist.length; i++) {
	    if (name.equals(s_preflist[i][0])) {
		m_preferences.put(s_preflist[i][0], value);
		return;
	    }
	}
	System.out.println("Unknown preference: " + name + 
			   ", nothing stored.");
    }

    public void put(String name, int value) 
    {
	put(name, Integer.toString(value));
    }

    public void put(String name, boolean value) 
    {
	put(name, Boolean.toString(value));
    }

    
    Preferences m_preferences;
}

