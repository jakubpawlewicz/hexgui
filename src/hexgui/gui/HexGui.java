//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.game.Node;
import hexgui.game.GameInfo;
import hexgui.sgf.SgfWriter;
import hexgui.sgf.SgfReader;

import java.io.*;
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
		    cmdShutdown();
		}
	    });
	
	m_menubar = new GuiMenuBar(this);
	setJMenuBar(m_menubar.getJMenuBar());

	m_toolbar = new GuiToolBar(this);
        getContentPane().add(m_toolbar.getJToolBar(), BorderLayout.NORTH);

	m_guiboard = new GuiBoard(this);
        getContentPane().add(m_guiboard, BorderLayout.CENTER);

	cmdNewGame();

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
	    cmdShutdown();
	} 
	//
	// file/help commands
	//
	else if (cmd.equals("newgame")) {
	    cmdNewGame();
	} else if (cmd.equals("savegame")) {
	    cmdSaveGame();
	} else if (cmd.equals("savegameas")) {
	    cmdSaveGameAs();
	} else if (cmd.equals("loadgame")) {
	    cmdLoadGame();
	} else if (cmd.equals("about")) {
	    cmdAbout();
	} 
	//
	// gui commands
	//
	else if (cmd.equals("gui_board_draw_type")) {
	    cmdGuiBoardDrawType();
	} 
	//
        // game navigation commands  
	//
        else if (cmd.equals("game_beginning")) {
	    backward(1000);
	} else if (cmd.equals("game_backward10")) {
	    backward(10);
	} else if (cmd.equals("game_back")) {
	    backward(1);
	} else if (cmd.equals("game_forward")) {
	    forward(1);
	} else if (cmd.equals("game_forward10")) {
	    forward(10);
	} else if (cmd.equals("game_end")) {
	    forward(1000);
	} else if (cmd.equals("game_up")) {
	    if (m_current.getNext() != null) {
		HexPoint point = m_current.getMove().getPoint();
		m_guiboard.setColor(point, HexColor.EMPTY);

		m_current = m_current.getNext();

		HexColor color = m_current.getMove().getColor();
		point = m_current.getMove().getPoint();
		m_guiboard.setColor(point, color);
		m_tomove = color.otherColor();
		
		m_guiboard.repaint();
		m_toolbar.updateButtonStates(m_current);
	    }
	} else if (cmd.equals("game_down")) {
	    if (m_current.getPrev() != null) {
		HexPoint point = m_current.getMove().getPoint();
		m_guiboard.setColor(point, HexColor.EMPTY);

		m_current = m_current.getPrev();

		HexColor color = m_current.getMove().getColor();
		point = m_current.getMove().getPoint();
		m_guiboard.setColor(point, color);
		m_tomove = color.otherColor();

		m_guiboard.repaint();
		m_toolbar.updateButtonStates(m_current);
	    }
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
    private void cmdShutdown()
    {
	if (gameChanged() && !askSaveGame())
	    return;

	System.out.println("Shutting down...");
	System.exit(0);
    }

    private void cmdNewGame()
    {
	if (gameChanged() && !askSaveGame())
	    return;

	String size = m_menubar.getSelectedBoardSize();
	Dimension dim = new Dimension(-1,-1);
	if (size.equals("Other...")) {
	    size = BoardSizeDialog.show(this, m_guiboard.getBoardSize());
	    if (size == null) return;
	}

	try {
	    StringTokenizer st = new StringTokenizer(size);
	    int w = Integer.parseInt(st.nextToken());
	    st.nextToken();
	    int h = Integer.parseInt(st.nextToken());
	    dim.setSize(w,h);
	}
	catch (Throwable t) {
	    showError("Size should be in format 'w x h'.");
	    return;
	}

	if (dim.width < 1 || dim.height < 1) {
	    showError("Invalid board size.");
	} else {
	    m_tomove = HexColor.BLACK;
	    m_root = new Node();
	    m_current = m_root;
	    m_gameinfo = new GameInfo();
	    m_gameinfo.setBoardSize(dim);
	    m_file = null;
	    setGameChanged(false);
	    setFrameTitle();
	    
	    m_guiboard.initSize(dim.width, dim.height);
	    m_guiboard.repaint();
	    m_toolbar.updateButtonStates(m_current);
	}
    }

    private boolean cmdSaveGame()
    {
	if (m_file == null) 
	    m_file = showSaveAsDialog();
	
	if (m_file != null) {
	    System.out.println("Saving to file: " + m_file.getName());
	    if (save(m_file)) {
		setGameChanged(false);
		setFrameTitle();
		return true;
	    }
	}
	return false;
    }

    private boolean cmdSaveGameAs()
    {
	File file = showSaveAsDialog();
	if (file == null) 
	    return false;

	m_file = file;
	return cmdSaveGame();
    }

    private void cmdLoadGame()
    {
	if (gameChanged() && !askSaveGame())
	    return;

	File file = showOpenDialog();
	if (file == null) return;

	System.out.println("Loading sgf from file: " + file.getName());
	SgfReader sgf = load(file);
	if (sgf != null) {
	    m_root = sgf.getGameTree();
	    m_gameinfo = sgf.getGameInfo();
	    m_current = m_root;

	    m_guiboard.initSize(m_gameinfo.getBoardSize());
	    forward(1000);

	    m_file = file;
	    setGameChanged(false);
	    setFrameTitle();
	}
    }

    private void cmdAbout()
    {
	
    }

    //------------------------------------------------------------

    private void cmdGuiBoardDrawType()
    {
	String type = m_menubar.getCurrentBoardDrawType();
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
	    Move move = new Move(point, m_tomove);
	    Node node = new Node(move);
	    m_current.addChild(node);
	    m_current = node;

	    m_guiboard.setColor(point, m_tomove);
	    m_tomove = m_tomove.otherColor();
	    setGameChanged(true);
	    setFrameTitle();

	    m_guiboard.repaint();
	    m_toolbar.updateButtonStates(m_current);
	}
    }

    private void forward(int n)
    {
	for (int i=0; i<n; i++) {
	    Node child = m_current.getChild();
	    if (child == null) break;

	    Move move = child.getMove();
	    m_guiboard.setColor(move.getPoint(), move.getColor());
	    m_current = child;
	    m_tomove = move.getColor().otherColor();
	}
	m_guiboard.repaint();
	m_toolbar.updateButtonStates(m_current);
    }

    private void backward(int n)
    {
	for (int i=0; i<n; i++) {
	    if (m_current == m_root) break;

	    Move move = m_current.getMove();
	    m_guiboard.setColor(move.getPoint(), HexColor.EMPTY);

	    m_current = m_current.getParent();
	}
	m_guiboard.repaint();
	m_toolbar.updateButtonStates(m_current);
	    
	if (m_current == m_root) 
	    m_tomove = HexColor.BLACK;
	else
	    m_tomove = m_current.getMove().getColor().otherColor();
    }

    private void setGameChanged(boolean changed) 
    {
	m_gameChanged = changed;
    }

    private boolean gameChanged()
    {
	return m_gameChanged;
    }

    private void setFrameTitle()
    {
	String filename = "untitled";
	if (m_file != null) filename = m_file.getName();
	if (gameChanged()) filename = filename + "*";
	setTitle("HexGui - " + filename);
    }

    /** Returns false if action was aborted. */
    private boolean askSaveGame()
    {
	Object options[] = {"Save", "Discard", "Cancel"};
	int n = JOptionPane.showOptionDialog(this,
					     "Game has changed.  Save changes?",
					     "Save Game?",
					     JOptionPane.YES_NO_CANCEL_OPTION,
					     JOptionPane.QUESTION_MESSAGE,
					     null,
					     options,
					     options[0]);
	if (n == 0) {
	    if (cmdSaveGame()) 
		return true;
	    return false;
	} else if (n == 1) {
	    return true;
	}
	return false;
    }

    /** Save game to file.
	@return true If successful. 
    */
    private boolean save(File file)
    {
	FileOutputStream out;
	try {
	    out = new FileOutputStream(file);
	}
	catch (FileNotFoundException e) {
	    showError("File not found!");
	    return false;
	}
	
	new SgfWriter(out, m_root, m_gameinfo);
	return true;
    }

    /* Load game from file. */
    private SgfReader load(File file)
    {
	FileInputStream in;
	try {
	    in = new FileInputStream(file);
	}
	catch(FileNotFoundException e) {
	    showError("File not found!");
	    return null;
	}

	SgfReader sgf;
	try {
	    sgf = new SgfReader(in);
	}
	catch (SgfReader.SgfError e) {
	    showError("Error reading SGF file:\n \"" + e.getMessage() + "\"");
	    return null;
	}
	
	return sgf;
    }

    /** Show a simple error message dialog. */
    private void showError(String msg)
    {
	JOptionPane.showMessageDialog(this, msg, "Error",
				      JOptionPane.ERROR_MESSAGE);
    }
    
    /** Show save dialog, return File of selected filename.  
	@return null If aborted.
    */
    private File showSaveAsDialog()
    {
	// FIXME: use most recent path here
	JFileChooser fc = new JFileChooser("../games/");
	if (m_file != null) fc.setSelectedFile(m_file);
	int ret = fc.showSaveDialog(this);
	if (ret == JFileChooser.APPROVE_OPTION)
	    return fc.getSelectedFile();
	return null;
    }

    /** Show open dialog, return File of selected filename.  
	@return null If aborted.
    */
    private File showOpenDialog()
    {
	// FIXME: use most recent path here
	JFileChooser fc = new JFileChooser("../games/");
	int ret = fc.showOpenDialog(this);
	if (ret == JFileChooser.APPROVE_OPTION)
	    return fc.getSelectedFile();
	return null;
    }


    private GuiBoard m_guiboard;
    private GuiToolBar m_toolbar;
    private GuiMenuBar m_menubar;

    private Node m_root;
    private Node m_current;
    private GameInfo m_gameinfo;
    private HexColor m_tomove;
    private boolean m_gameChanged;
    private File m_file;

}

//----------------------------------------------------------------------------
