//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import java.util.*;
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

	m_toolBar = new GuiToolBar(this);
        getContentPane().add(m_toolBar.getJToolBar(), BorderLayout.NORTH);

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

	//
	// system commands
	//
	if (cmd.equals("shutdown")) {
	    CmdShutdown();
	} 
	//
	// file/help commands
	//
	else if (cmd.equals("newgame")) {
	    CmdNewGame();
	} else if (cmd.equals("savegame")) {
	    CmdSaveGame();
	} else if (cmd.equals("savegameas")) {
	    CmdSaveGame();
	} else if (cmd.equals("loadgame")) {
	    CmdLoadGame();
	} else if (cmd.equals("about")) {
	    CmdLoadGame();
	} 
	//
	// gui commands
	//
	else if (cmd.equals("gui_board_draw_type")) {
	    CmdGuiBoardDrawType();
	} 
	//
        // game navigation commands  
	//
        else if (cmd.equals("game_beginning")) {

	} else if (cmd.equals("game_backward10")) {

	} else if (cmd.equals("game_back")) {

	} else if (cmd.equals("game_forward")) {

	} else if (cmd.equals("game_forward10")) {

	} else if (cmd.equals("game_end")) {

	}
	//
	// unknown command
	//
	else {
	    System.out.println("Unknown command: '" + cmd + "'.");
	}
    }

    //------------------------------------------------------------
    public void CmdShutdown()
    {
	System.out.println("Shutting down...");
	System.exit(0);
    }

    public void CmdNewGame()
    {
	System.out.println("newgame");

	String size = m_menuBar.getSelectedBoardSize();
	Dimension dim = new Dimension(-1,-1);
	if (size.equals("Other...")) {
	    size = BoardSizeDialog.show(this);
	}

	StringTokenizer st = new StringTokenizer(size);
	if (st.countTokens() == 3) {
	    int w,h;
	    w = Integer.parseInt(st.nextToken());
	    st.nextToken();
	    h = Integer.parseInt(st.nextToken());
	    dim.setSize(w,h);
	}
	if (dim.width == -1) {
	    JOptionPane.showMessageDialog(this,
					  "Invalid board size.",
					  "Invalid Board Size",
					  JOptionPane.ERROR_MESSAGE);
	    return;
	}

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

    //------------------------------------------------------------

    public void CmdGuiBoardDrawType()
    {
	String type = m_menuBar.getCurrentBoardDrawType();
	System.out.println(type);
	m_board.setDrawType(type);
	m_board.repaint();
    }


    private GuiBoard m_board;
    private GuiToolBar m_toolBar;
    private GuiMenuBar m_menuBar;
}
