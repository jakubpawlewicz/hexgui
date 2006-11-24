//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.game.Node;

import java.util.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

//----------------------------------------------------------------------------

/** The parent HexGui object.
    Basically does everything.
*/
public final class HexGui 
    extends JFrame 
    implements ActionListener, GuiBoard.Listener
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

	m_guiboard = new GuiBoard(this);
        getContentPane().add(m_guiboard, BorderLayout.CENTER);

	CmdNewGame();

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

	} else if (cmd.equals("game_up")) {

	} else if (cmd.equals("game_down")) {

	} else if (cmd.equals("stop")) {

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

	m_tomove = HexColor.BLACK;
	m_guiboard.initSize(dim.width, dim.height);
	m_guiboard.repaint();
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
	m_guiboard.setDrawType(type);
	m_guiboard.repaint();
    }

    //------------------------------------------------------------

    /** Callback from GuiBoard. 
	Handle a mouse click.
    */
    public void fieldClicked(HexPoint point)
    {
	if (m_guiboard.getColor(point) == HexColor.EMPTY) {
	    m_guiboard.setColor(point, m_tomove);
	    m_guiboard.repaint();                   // FIXME: remove me!
	    m_tomove = m_tomove.otherColor();
	}
    }

    public void forward(int n)
    {
	
    }

    private GuiBoard m_guiboard;
    private GuiToolBar m_toolBar;
    private GuiMenuBar m_menuBar;

    private Node m_root;
    private Node m_current;
    private HexColor m_tomove;
}

//----------------------------------------------------------------------------