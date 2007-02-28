//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.game.Node;

import java.util.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

//----------------------------------------------------------------------------

/** Menu bar. */
public final class GuiMenuBar
{
    public GuiMenuBar(ActionListener listener, GuiPreferences preferences)
    {
	m_preferences = preferences;

	m_menuBar = new JMenuBar();

	m_listener = listener;
	m_menuBar.add(createFileMenu());
	m_menuBar.add(createGameMenu());
        m_menuBar.add(createEditMenu());
	m_menuBar.add(createViewMenu());
	m_menuBar.add(createHelpMenu());

	setProgramConnected(false);
    }

    public JMenuBar getJMenuBar()
    {
	return m_menuBar;
    }

    public void setProgramConnected(boolean f)
    {
	m_connect_remote.setEnabled(!f);
	m_connect_local.setEnabled(!f);
	m_disconnect.setEnabled(f);
        m_genmove.setEnabled(f);

	if (f == false) {
	    setShellVisible(false);
	    m_shell_visible.setEnabled(false);
	    setAnalyzeVisible(false);
	    m_analyze_visible.setEnabled(false);
	} else {
	    m_shell_visible.setEnabled(true);
            m_analyze_visible.setEnabled(true);

	    setShellVisible(m_preferences.
			    getBoolean("shell-show-on-connect"));
            setAnalyzeVisible(m_preferences.
                              getBoolean("analyze-show-on-connect"));
	}
    }

    public void updateMenuStates(Node current)
    {
        m_swap.setEnabled(current.isSwapAllowed());
    }

    //----------------------------------------------------------------------

    private JMenu createFileMenu()
    {
	JMenu menu = new JMenu("File");
	menu.setMnemonic(KeyEvent.VK_F);

	JMenuItem item;
	item = new JMenuItem("Open...");
	item.setMnemonic(KeyEvent.VK_O);
	item.addActionListener(m_listener);
	item.setActionCommand("loadgame");
 	menu.add(item);

	item = new JMenuItem("Save");
	item.setMnemonic(KeyEvent.VK_S);
	item.addActionListener(m_listener);
	item.setActionCommand("savegame");
	menu.add(item);

	item = new JMenuItem("Save As...");
	item.setMnemonic(KeyEvent.VK_A);
	item.addActionListener(m_listener);
	item.setActionCommand("savegameas");
 	menu.add(item);

 	menu.addSeparator();
	item = new JMenuItem("Connect Remote Program...");
	item.addActionListener(m_listener);
	item.setActionCommand("connect-program");
	m_connect_remote = item;
 	menu.add(item);


	item = new JMenuItem("Connect Local Program...");
	item.addActionListener(m_listener);
	item.setActionCommand("connect-local-program");
	m_connect_local = item;
 	menu.add(item);

	item = new JMenuItem("Disconnect Program");
	item.addActionListener(m_listener);
	item.setActionCommand("disconnect-program");
	m_disconnect = item;
 	menu.add(item);

 	menu.addSeparator();
	
	item = new JMenuItem("Exit");
	item.setMnemonic(KeyEvent.VK_X);
	item.addActionListener(m_listener);
	item.setActionCommand("shutdown");
	menu.add(item);

	return menu;
    }

    //----------------------------------------------------------------------

    private JMenu createGameMenu()
    {
	JMenu menu = new JMenu("Game");
	menu.setMnemonic(KeyEvent.VK_G);

	JMenuItem item;
	item = new JMenuItem("New");
	item.setMnemonic(KeyEvent.VK_N);
	item.addActionListener(m_listener);
	item.setActionCommand("newgame");
 	menu.add(item);

	menu.addSeparator();
	
	JMenu submenu;
	submenu = createClockMenu();
	menu.add(submenu);

	menu.addSeparator();

        m_swap = new JMenuItem("Play Swap Move");
        m_swap.addActionListener(m_listener);
        m_swap.setActionCommand("game_swap");
        menu.add(m_swap);

        m_genmove = new JMenuItem("Generate Computer Move");
        m_genmove.addActionListener(m_listener);
        m_genmove.setActionCommand("genmove");
        menu.add(m_genmove);

	// FIXME: implement!!
	m_resign = new JMenuItem("Resign");
	menu.add(m_resign);

	return menu;
    }

    // FIXME: implement!
    private JMenu createClockMenu()
    {
	JMenu menu = new JMenu("Clock");
	JMenuItem item;
	
	item = new JMenuItem("Start");
	menu.add(item);

	item = new JMenuItem("Pause");
	menu.add(item);

	item = new JMenuItem("Resume");
	menu.add(item);

	return menu;
    }

    //----------------------------------------------------------------------

    private JMenu createEditMenu()
    {
	JMenu menu = new JMenu("Edit");
	menu.setMnemonic(KeyEvent.VK_E);

	JMenu size = createBoardSizeMenu();
	menu.add(size);

	menu.addSeparator();
	
	JMenuItem item;
	item = new JMenuItem("Preferences...");
	item.addActionListener(m_listener);
	item.setActionCommand("show-preferences");

	menu.add(item);

	return menu;
    }

    private JMenu createBoardSizeMenu()
    {
        JMenu menu = new JMenu("Board Size");
	m_bsGroup = new ButtonGroup();

	String sizes[] = new String[] 
	    {
		"19 x 19",
		"14 x 14",
		"11 x 11",
		"10 x 10",
		"9 x 9",
		"8 x 8",
		"7 x 7",
                "6 x 6", 
                "5 x 5",
                "4 x 4",
		"3 x 3"
	    };

	String preferred = m_preferences.get("gui-board-width") + " x "
	                 + m_preferences.get("gui-board-height");
	
	boolean found = false;
	JRadioButtonMenuItem item;
	for (int i=0; i<sizes.length; i++) {
	    item = new JRadioButtonMenuItem(sizes[i]);
	    item.addActionListener(m_listener);
	    item.setActionCommand("newgame");
	    if (preferred.equals(sizes[i])) {
		item.setSelected(true);
		found = true;
	    }
	    m_bsGroup.add(item);
	    menu.add(item);
	}

	if (!found) {
	    item = new JRadioButtonMenuItem(preferred);
	    item.addActionListener(m_listener);
	    item.setActionCommand("newgame");
	    item.setSelected(true);
	    m_bsGroup.add(item);
	    menu.add(item);
	}
	
	menu.addSeparator();

	item = new JRadioButtonMenuItem("Other...");
	item.addActionListener(m_listener);
	item.setActionCommand("newgame");
	item.setSelected(true);
	m_bsGroup.add(item);
	menu.add(item);

	return menu;
    }

    public String getSelectedBoardSize()
    {
	Enumeration e = m_bsGroup.getElements();
	AbstractButton b = (AbstractButton)e.nextElement();
	while (!b.isSelected() && e.hasMoreElements()) { 
	    b = (AbstractButton)e.nextElement();
	}

	return b.getText();
    }	

    //----------------------------------------------------------------------

    public boolean getToolbarVisible()
    {
	return m_toolbar_visible.getState();
    }

    public boolean getShellVisible()
    {
	return m_shell_visible.getState();
    }

    public void setShellVisible(boolean f) 
    {
	m_shell_visible.setState(f);
    }

    public boolean getAnalyzeVisible()
    {
        return m_analyze_visible.getState();
    }

    public void setAnalyzeVisible(boolean f)
    {
        m_analyze_visible.setState(f);
    }

    private JMenu createViewMenu()
    {
	JMenu menu = new JMenu("View");
	menu.setMnemonic(KeyEvent.VK_V);

	m_toolbar_visible = new JCheckBoxMenuItem("Show Toolbar");
	if (m_preferences.getBoolean("gui-toolbar-visible"))
	    m_toolbar_visible.setState(true);  
	m_toolbar_visible.addActionListener(m_listener);
	m_toolbar_visible.setActionCommand("gui_toolbar_visible");
	menu.add(m_toolbar_visible);

	m_shell_visible = new JCheckBoxMenuItem("Show Shell");
	m_shell_visible.addActionListener(m_listener);
	m_shell_visible.setActionCommand("gui_shell_visible");
	m_shell_visible.setEnabled(false);
	menu.add(m_shell_visible);

	m_analyze_visible = new JCheckBoxMenuItem("Show Analyze");
	m_analyze_visible.addActionListener(m_listener);
	m_analyze_visible.setActionCommand("gui_analyze_visible");
	m_analyze_visible.setEnabled(false);
	menu.add(m_analyze_visible);

	menu.addSeparator();

	JMenu view;
	view = createBoardViewMenu();
	menu.add(view);

	view = createOrientationViewMenu();
	menu.add(view);

	return menu;
    }

    private JMenu createBoardViewMenu()
    {
        JMenu menu = new JMenu("Board Type");
	m_btGroup = new ButtonGroup();

	String defaultType = m_preferences.get("gui-board-type");

	JRadioButtonMenuItem item;
	item = new JRadioButtonMenuItem("Diamond");
	item.addActionListener(m_listener);
	item.setActionCommand("gui_board_draw_type");
	if (defaultType.equals("Diamond"))
	    item.setSelected(true);
	m_btGroup.add(item);
	menu.add(item);

	item = new JRadioButtonMenuItem("Flat");
	item.addActionListener(m_listener);
	item.setActionCommand("gui_board_draw_type");
	if (defaultType.equals("Flat"))
	    item.setSelected(true);
	m_btGroup.add(item);
	menu.add(item);

	item = new JRadioButtonMenuItem("Go");
	item.addActionListener(m_listener);
	item.setActionCommand("gui_board_draw_type");
	if (defaultType.equals("Go"))
	    item.setSelected(true);
	m_btGroup.add(item);
	menu.add(item);

	return menu;
    }

    public String getCurrentBoardDrawType()
    {
	Enumeration e = m_btGroup.getElements();
	AbstractButton b = (AbstractButton)e.nextElement();
	while (!b.isSelected() && e.hasMoreElements()) { 
	    b = (AbstractButton)e.nextElement();
	}
	return b.getText();
    }	

    private JMenu createOrientationViewMenu()
    {
        JMenu menu = new JMenu("Board Orientation");
	m_orGroup = new ButtonGroup();

	String pref = m_preferences.get("gui-board-on-top");

	JRadioButtonMenuItem item;
	item = new JRadioButtonMenuItem("Black on top");
	item.addActionListener(m_listener);
	item.setActionCommand("gui_board_orientation");
	if (pref.equals("black")) item.setSelected(true);
	m_orGroup.add(item);
	menu.add(item);

	item = new JRadioButtonMenuItem("White on top");
	item.addActionListener(m_listener);
	item.setActionCommand("gui_board_orientation");
	if (pref.equals("white")) item.setSelected(true);
	m_orGroup.add(item);
	menu.add(item);

	return menu;
    }

    public String getCurrentBoardOrientation()
    {
	Enumeration e = m_orGroup.getElements();
	AbstractButton b = (AbstractButton)e.nextElement();
	while (!b.isSelected() && e.hasMoreElements()) { 
	    b = (AbstractButton)e.nextElement();
	}
	return b.getText();
    }	

    //----------------------------------------------------------------------

    private JMenu createHelpMenu()
    {
	JMenu menu = new JMenu("Help");
	menu.setMnemonic(KeyEvent.VK_H);

	JMenuItem item;
	item = new JMenuItem("About HexGui...");
	item.setMnemonic(KeyEvent.VK_A);
	item.addActionListener(m_listener);
	item.setActionCommand("about");
	menu.add(item);

	return menu;
    }

    private GuiPreferences m_preferences;
    private ActionListener m_listener;
    private JMenuBar m_menuBar;

    private JCheckBoxMenuItem m_toolbar_visible;
    private JCheckBoxMenuItem m_shell_visible;
    private JCheckBoxMenuItem m_analyze_visible;

    private JMenuItem m_connect_local, m_connect_remote, m_disconnect;

    private JMenuItem m_resign, m_swap, m_genmove;

    private ButtonGroup m_bsGroup;   // board sizes
    private ButtonGroup m_btGroup;   // board view types (diamond, flat, etc)
    private ButtonGroup m_orGroup;   // black on top, or white?
}
