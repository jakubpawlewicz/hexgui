//----------------------------------------------------------------------------
// $Id$ 
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.game.Node;

import java.util.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

//----------------------------------------------------------------------------

public final class GuiToolBar
{
    public GuiToolBar(ActionListener listener, GuiPreferences preferences)
    {
	m_preferences = preferences;
	m_listener = listener;

	m_toolBar = new JToolBar();
	createToolBar();

	setVisible(m_preferences.getBoolean("gui-toolbar-visible"));
    }

    public JToolBar getJToolBar()
    {
	return m_toolBar;
    }

    public void setVisible(boolean visible)
    {
	m_toolBar.setVisible(visible);
	m_preferences.put("gui-toolbar-visible", visible);
    }

    public void enableStopButton(String actionCommand)
    {
	m_stop.setEnabled(true);
    }
    
    public void disableStopButton()
    {
	m_stop.setEnabled(false);
    }

    public void setProgramConnected(boolean f)
    {
	m_play.setEnabled(f);
    }
    
    public void updateButtonStates(Node node)
    {
	m_beginning.setEnabled(node.getParent() != null);
	m_back10.setEnabled(node.getParent() != null);
	m_back.setEnabled(node.getParent() != null);

	m_forward.setEnabled(node.getChild() != null);
	m_forward10.setEnabled(node.getChild() != null);
	m_end.setEnabled(node.getChild() != null);

	m_up.setEnabled(node.getNext() != null);
	m_down.setEnabled(node.getPrev() != null);

        m_swap.setEnabled(node.isSwapAllowed());
    }

    //----------------------------------------------------------------------

    private void createToolBar()
    {
	m_toolBar.add(makeButton("hexgui/images/filenew.png", 
				 "newgame",
				 "New Game",
				 "New"));

	m_toolBar.add(makeButton("hexgui/images/fileopen.png", 
				 "loadgame",
				 "Load Game",
				 "Load"));

	m_toolBar.add(makeButton("hexgui/images/filesave2.png", 
				 "savegame",
				 "Save Game",
				 "Save"));

	m_toolBar.addSeparator();

	
	m_beginning = makeButton("hexgui/images/beginning.png", 
				 "game_beginning",
				 "Game Start",
				 "Start");
	m_toolBar.add(m_beginning);
	
	m_back10 = makeButton("hexgui/images/backward10.png", 
			      "game_backward10",
			      "Go back ten moves",
			      "Back10");
	m_toolBar.add(m_back10);
    
	m_back = makeButton("hexgui/images/back.png", 
			    "game_back",
			    "Go back one move",
			    "Back");
	m_toolBar.add(m_back);

	m_forward = makeButton("hexgui/images/forward.png", 
			     "game_forward",
			     "Go forward one move",
			     "Forward");
	m_toolBar.add(m_forward);

	m_forward10 = makeButton("hexgui/images/forward10.png", 
				 "game_forward10",
				 "Go forward ten moves",
				 "Forward10");
	m_toolBar.add(m_forward10);

	m_end = makeButton("hexgui/images/end.png", 
			   "game_end",
			   "Go to end of game",
			   "End");
	m_toolBar.add(m_end);
    
	m_toolBar.addSeparator();

	m_up = makeButton("hexgui/images/up.png",
			  "game_up",
			  "Explore previous variation",
			  "Up");
	m_toolBar.add(m_up);
	
	m_down = makeButton("hexgui/images/down.png",
			  "game_down",
			  "Explore next variation",
			  "Down");
	m_toolBar.add(m_down);

	m_toolBar.addSeparator();

	m_swap = makeButton("hexgui/images/swap.png",
			    "game_swap",
			    "Play Swap Move",
			    "Swap");
	m_toolBar.add(m_swap);
	m_swap.setEnabled(false);

	m_toolBar.addSeparator();

	m_play = makeButton("hexgui/images/play.png",
			    "computer-move",
			    "Get computer move",
			    "Play");
	m_toolBar.add(m_play);
	m_play.setEnabled(false);

	m_stop = makeButton("hexgui/images/stop.png",
			    "stop",
			    "Stop Action",
			    "Stop");
	m_toolBar.add(m_stop);
	disableStopButton();
    }

    private JButton makeButton(String imageFile, String actionCommand,
			      String tooltip, String altText)
    {
	JButton button = new JButton();
	button.addActionListener(m_listener);
	button.setActionCommand(actionCommand);
	button.setToolTipText(tooltip);

        ClassLoader classLoader = getClass().getClassLoader();
	URL imageURL = classLoader.getResource(imageFile);
	if (imageURL != null) {
	    button.setIcon(new ImageIcon(imageURL, altText));
	} else {
	    button.setText(altText);
	    System.out.println("*** Resource not found: " + imageFile);
	}
	return button;
    }

    //----------------------------------------------------------------------

    private GuiPreferences m_preferences;
    private JToolBar m_toolBar;
    private ActionListener m_listener;

    private JButton m_beginning, m_back10, m_back;
    private JButton m_forward, m_forward10, m_end;
    private JButton m_up, m_down;
    private JButton m_play, m_stop, m_swap;
}

//----------------------------------------------------------------------------
