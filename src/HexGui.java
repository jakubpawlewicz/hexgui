import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

import hexgui.hex.*;

public class HexGui 
    extends JFrame 
    implements ActionListener 
{
    public HexGui()
    {
        super("HexGui");

	// Catch the close action and shutdown nicely
	addWindowListener(new java.awt.event.WindowAdapter() 
	    {
		public void windowClosing(WindowEvent winEvt) {
		    CmdShutdown();
		}
	    });
	
	m_menuBar = new GuiMenuBar(this);
	setJMenuBar(m_menuBar.getJMenuBar());

	m_board = new GuiBoard();
        getContentPane().add(m_board, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    //------------------------------------------------------------
    public void actionPerformed(ActionEvent e) 
    {
//      System.out.println("-----------------");
// 	System.out.println("Received Action Event: ");
// 	System.out.println(e.getActionCommand());
// 	System.out.println(e.paramString());

	String cmd = e.getActionCommand();
	if (cmd.equals("shutdown")) {
	    CmdShutdown();
	} else if (cmd.equals("newgame")) {
	    CmdNewGame();
	} else if (cmd.equals("savegame")) {
	    CmdSaveGame();
	} else if (cmd.equals("savegameas")) {
	    CmdSaveGame();
	} else if (cmd.equals("loadgame")) {
	    CmdLoadGame();
	} else if (cmd.equals("about")) {
	    CmdLoadGame();
	} else {
	    System.out.println("Unknown command: '" + cmd + "'.");
	}
    }

    //------------------------------------------------------------
    public void CmdShutdown()
    {
	System.out.println("Shuting down...");
	System.exit(0);
    }

    public void CmdNewGame()
    {
	System.out.println("newgame");
	Dimension dim = m_menuBar.getCurrentBoardSize();
	m_board.initSize(dim.width, dim.height);
        m_board.newGame();
	m_board.repaint();
    }

    public void CmdSaveGame()
    {

    }

    public void CmdLoadGame()
    {

    }

    public void CmdAbout()
    {

    }


    private GuiBoard m_board;
    private GuiMenuBar m_menuBar;
}
