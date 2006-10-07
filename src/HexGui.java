import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

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
	//System.out.println("Received Action Event: ");
	//System.out.println(e.getActionCommand());
	//System.out.println(e.paramString());

	String cmd = e.getActionCommand();
	if ("shutdown" == cmd) {
	    CmdShutdown();
	} else if ("newgame" == cmd) {
	    CmdNewGame();
	} else if ("savegame" == cmd) {
	    CmdSaveGame();
	} else if ("loadgame" == cmd) {
	    CmdLoadGame();
	} else if ("about" == cmd) {
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
	System.out.println("Newgame");
	m_board.clearAll();
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
