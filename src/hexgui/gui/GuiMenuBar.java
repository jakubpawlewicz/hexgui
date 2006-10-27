//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import java.util.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

public class GuiMenuBar
{
    public GuiMenuBar(ActionListener listener)
    {
	m_menuBar = new JMenuBar();

	m_listener = listener;
	m_menuBar.add(createFileMenu());
	m_menuBar.add(createGameMenu());
        m_menuBar.add(createEditMenu());
	m_menuBar.add(createViewMenu());
	m_menuBar.add(createHelpMenu());
    }

    public JMenuBar getJMenuBar()
    {
	return m_menuBar;
    }

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
	
	item = new JMenuItem("Exit");
	item.setMnemonic(KeyEvent.VK_X);
	item.addActionListener(m_listener);
	item.setActionCommand("shutdown");
	menu.add(item);

	return menu;
    }

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

	item = new JMenuItem("Resign");
	menu.add(item);

	return menu;
    }

    private JMenu createEditMenu()
    {
	JMenu menu = new JMenu("Edit");
	menu.setMnemonic(KeyEvent.VK_E);

	JMenu size = createBoardSizeMenu();
	menu.add(size);

	return menu;
    }

    private JMenu createBoardSizeMenu()
    {
        JMenu menu = new JMenu("Board Size");
	m_bsGroup = new ButtonGroup();

	JRadioButtonMenuItem item;
	item = new JRadioButtonMenuItem("14 x 14");
	item.addActionListener(m_listener);
	item.setActionCommand("newgame");
	m_bsGroup.add(item);
	menu.add(item);

	item = new JRadioButtonMenuItem("11 x 11");
	item.addActionListener(m_listener);
	item.setActionCommand("newgame");
	item.setSelected(true);
	m_bsGroup.add(item);
	menu.add(item);

	item = new JRadioButtonMenuItem("8 x 8");
	item.addActionListener(m_listener);
	item.setActionCommand("newgame");
	item.setSelected(true);
	m_bsGroup.add(item);
	menu.add(item);

	item = new JRadioButtonMenuItem("7 x 7");
	item.addActionListener(m_listener);
	item.setActionCommand("newgame");
	item.setSelected(true);
	m_bsGroup.add(item);
	menu.add(item);

	menu.addSeparator();

	item = new JRadioButtonMenuItem("Other...");
	item.addActionListener(m_listener);
	item.setActionCommand("newgame");
	item.setSelected(true);
	m_bsGroup.add(item);
	menu.add(item);

	return menu;
    }
	
    public Dimension getCurrentBoardSize()
    {
	Enumeration e = m_bsGroup.getElements();
	AbstractButton b = (AbstractButton)e.nextElement();
	while (!b.isSelected() && e.hasMoreElements()) { 
	    b = (AbstractButton)e.nextElement();
	}

	Dimension ret = new Dimension();
	if (b.getText().equals("14 x 14"))
	    ret.setSize(14,14);
	else if (b.getText().equals("11 x 11")) 
	    ret.setSize(11,11);
	else if (b.getText().equals("7 x 7"))
	    ret.setSize(7,7);
	else if (b.getText().equals("Other...")) {
	    ret.setSize(4,3);
	}
	    
	return ret;
    }	

    private JMenu createViewMenu()
    {
	JMenu menu = new JMenu("View");
	menu.setMnemonic(KeyEvent.VK_V);

	JMenu view = createBoardViewMenu();
	menu.add(view);

	return menu;
    }

    private JMenu createBoardViewMenu()
    {
        JMenu menu = new JMenu("Board Type");
	m_btGroup = new ButtonGroup();

	JRadioButtonMenuItem item;
	item = new JRadioButtonMenuItem("Diamond");
	item.addActionListener(m_listener);
	item.setActionCommand("gui_board_draw_type");
	item.setSelected(true);
	m_btGroup.add(item);
	menu.add(item);

	item = new JRadioButtonMenuItem("Flat");
	item.addActionListener(m_listener);
	item.setActionCommand("gui_board_draw_type");
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
    
    private ActionListener m_listener;
    private JMenuBar m_menuBar;

    private ButtonGroup m_bsGroup;   // board sizes
    private ButtonGroup m_btGroup;   // board view types (diamond, flat, etc)
}
