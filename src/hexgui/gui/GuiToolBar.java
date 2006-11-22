//----------------------------------------------------------------------------
// $Id$ 
//----------------------------------------------------------------------------

package hexgui.gui;

import java.util.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public final class GuiToolBar
{
    public GuiToolBar(ActionListener listener)
    {
	m_toolBar = new JToolBar();
	m_listener = listener;
	createToolBar();
    }

    public JToolBar getJToolBar()
    {
	return m_toolBar;
    }

    public void createToolBar()
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

	m_toolBar.add(makeButton("hexgui/images/beginning.png", 
				 "game_beginning",
				 "Game Start",
				 "Start"));

	m_toolBar.add(makeButton("hexgui/images/backward10.png", 
				 "game_backward10",
				 "Go back ten moves",
				 "Back10"));

	m_toolBar.add(makeButton("hexgui/images/back.png", 
				 "game_back",
				 "Go back one move",
				 "Back"));

	m_toolBar.add(makeButton("hexgui/images/forward.png", 
				 "game_forward",
				 "Go forward one move",
				 "Forward"));

	m_toolBar.add(makeButton("hexgui/images/forward10.png", 
				 "game_forward10",
				 "Go forward ten moves",
				 "Forward10"));

	m_toolBar.add(makeButton("hexgui/images/end.png", 
				 "game_end",
				 "Go to end of game",
				 "End"));

	m_toolBar.addSeparator();

	m_up = makeButton("hexgui/images/up.png",
			  "game_up",
			  "Explore previous variation",
			  "Up");
	m_toolBar.add(m_up);
	enableUp(false);
	
	m_down = makeButton("hexgui/images/down.png",
			  "game_down",
			  "Explore next variation",
			  "Down");
	m_toolBar.add(m_down);
	enableDown(false);

	m_toolBar.addSeparator();

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

    public void enableUp(boolean s) { m_up.setEnabled(s); }
    public void enableDown(boolean s) { m_down.setEnabled(s); }

    public void enableStopButton(String actionCommand)
    {
	m_stop.setEnabled(true);
    }
    
    public void disableStopButton()
    {
	m_stop.setEnabled(false);
    }

    //----------------------------------------------------------------------

    private JToolBar m_toolBar;
    private ActionListener m_listener;

    private JButton m_up;
    private JButton m_down;
    private JButton m_stop;
    
}

//----------------------------------------------------------------------------
