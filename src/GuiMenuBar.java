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
	m_menuBar.add(createHelpMenu());
    }

    public JMenuBar getJMenuBar()
    {
	return m_menuBar;
    }

    private JMenu createFileMenu()
    {
	JMenu menu = new JMenu("File");

	JMenuItem item;
	item = new JMenuItem("Save game...");
	item.addActionListener(m_listener);
	item.setActionCommand("savegame");
 	menu.add(item);

	item = new JMenuItem("Load game...");
	item.addActionListener(m_listener);
	item.setActionCommand("loadgame");
 	menu.add(item);

 	menu.addSeparator();
	
	item = new JMenuItem("Exit");
	item.addActionListener(m_listener);
	item.setActionCommand("shutdown");
	menu.add(item);

	return menu;
    }

    private JMenu createHelpMenu()
    {
	JMenu menu = new JMenu("Help");

	JMenuItem item;
	item = new JMenuItem("About HexGui...");
	item.addActionListener(m_listener);
	item.setActionCommand("about");
	menu.add(item);

	return menu;
    }

    private ActionListener m_listener;
    private JMenuBar m_menuBar;

}
