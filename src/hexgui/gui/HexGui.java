//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.game.Node;
import hexgui.game.GameInfo;
import hexgui.sgf.SgfWriter;
import hexgui.sgf.SgfReader;
import hexgui.htp.HtpController;
import hexgui.htp.HtpError;
import hexgui.htp.StreamCopy;
import hexgui.version.Version;

import java.io.*;
import java.util.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.*;

//----------------------------------------------------------------------------

/** HexGui. */
public final class HexGui 
    extends JFrame 
    implements ActionListener, GuiBoard.Listener
{
    public HexGui()
    {
        super("HexGui");

	System.out.println("HexGui v" + Version.id + "; " + Version.date 
			   + "; build " + Version.build + "\n");
	
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
	if (cmd.equals("shutdown"))
	    cmdShutdown();
	else if (cmd.equals("connect-program"))
	    cmdConnectRemoteProgram();
	else if (cmd.equals("connect-local-program"))
	    cmdConnectLocalProgram();
	else if (cmd.equals("disconnect-program"))
	    cmdDisconnectProgram();
	//
	// file/help commands
	//
	else if (cmd.equals("newgame")) 
	    cmdNewGame();
	else if (cmd.equals("savegame"))
	    cmdSaveGame();
	else if (cmd.equals("savegameas"))
	    cmdSaveGameAs();
	else if (cmd.equals("loadgame"))
	    cmdLoadGame();
	else if (cmd.equals("about"))
	    cmdAbout();
	//
	// gui commands
	//
	else if (cmd.equals("gui_toolbar_visible"))
	    cmdGuiToolbarVisible();
	else if (cmd.equals("gui_board_draw_type"))
	    cmdGuiBoardDrawType();
	else if (cmd.equals("gui_board_orientation"))
	    cmdGuiBoardOrientation();
	//
        // game navigation commands  
	//
        else if (cmd.equals("game_beginning"))
	    backward(1000);
	else if (cmd.equals("game_backward10"))
	    backward(10);
	else if (cmd.equals("game_back"))
	    backward(1);
	else if (cmd.equals("game_forward"))
	    forward(1);
	else if (cmd.equals("game_forward10"))
	    forward(10);
	else if (cmd.equals("game_end"))
	    forward(1000);
	else if (cmd.equals("game_up"))
	    up();
	else if (cmd.equals("game_down")) 
	    down();
	else if (cmd.equals("computer-move")) 
	    cmdComputerMove();
	else if (cmd.equals("stop")) {

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

	if (m_white_process != null) {
	    System.out.println("Stopping [" + m_white_name + " " + 
			       m_white_version + "] process...");
	    m_white_process.destroy();
	}

	System.exit(0);
    }

    private void cmdConnectRemoteProgram()
    {
	int port = 20000;
	String hostname = "localhost";

	System.out.print("Connecting to HTP program at [" + 
			 hostname + "] on port " + port + "...");
	System.out.flush();

	try {
	    m_white_socket = new Socket(hostname, port);
	}
	catch (UnknownHostException e) {
	    showError("Unknown host: '" + e.getMessage() + "'");
	    return;
	}
	catch (IOException e) {
	    showError("Error creating socket: '" + e.getMessage() + "'");
	    return;
	}
	System.out.println("connected.");

	InputStream in;
	OutputStream out;
	try {
	    in = m_white_socket.getInputStream();
	    out = m_white_socket.getOutputStream();
	}
	catch (IOException e) {
	    showError("Error obtaing socket stream: " + e.getMessage());
	    m_white = null;
	    return;
	}
	startController(in, out);
	m_menubar.setProgramConnected(true);
    }

    private void cmdConnectLocalProgram()
    {
        String prog = LocalProgramDialog.show(this);
	if (prog == null) // user aborted
	    return;

	Runtime runtime = Runtime.getRuntime();
	String cmd = prog;
	System.out.println("Executing '" + cmd + "'...");
	try {
	    m_white_process = runtime.exec(cmd);
	}
	catch (Throwable e) {
	    showError("Error starting program: '" + e.getMessage() + "'");
	    return;
	}
    
	Process proc = m_white_process;
	Thread blah = new Thread(new StreamCopy(proc.getErrorStream(),
						System.out));
	blah.start();

	startController(proc.getInputStream(), proc.getOutputStream());
	m_menubar.setProgramConnected(true);
    }

    // FIXME: implement this
    private void cmdDisconnectProgram()
    {
	if (m_white == null) 
	    return;

	htpSendQuit();
	try {
	    if (m_white_process != null) {
		m_white_process.waitFor();
		m_white_process = null;
	    } 
	    if (m_white_socket != null) {
		m_white_socket.close();
		m_white_socket = null;
	    }
	    m_white = null;
	    m_menubar.setProgramConnected(false);
	}
	catch (Throwable e) {
	    showError("Error: " + e.getMessage());
	}
    }

    //------------------------------------------------------------

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
            htpBoardsize();
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

    private void cmdGuiToolbarVisible()
    {
	boolean visible = m_menubar.getToolbarVisible();
	m_toolbar.setVisible(visible);
    }

    private void cmdGuiBoardDrawType()
    {
	String type = m_menubar.getCurrentBoardDrawType();
	System.out.println(type);
	m_guiboard.setDrawType(type);
	m_guiboard.repaint();
    }

    private void cmdGuiBoardOrientation()
    {
	String type = m_menubar.getCurrentBoardOrientation();
	System.out.println(type);
	m_guiboard.setOrientation(type);
	m_guiboard.repaint();
    }

    //------------------------------------------------------------

    private void startController(InputStream in, OutputStream out)
    {
	System.out.print("Starting controller...");
	System.out.flush();
	m_white = new HtpController(in, out);
	System.out.println("success.");
	htpSendNameCommand();
	htpVersion();
	m_toolbar.enablePlayButton();
        cmdNewGame();
    }

    private void sendCommand(String cmd, Runnable callback)
    {
	if (m_white == null) 
	    return;

	try {
	    m_white.sendCommand(cmd, callback);
	}
	catch (HtpError e) {
            showError(e.getMessage());
	}
    }

    // FIXME: add callback?
    private void htpSendQuit()
    {
	sendCommand("quit\n", null);
    }

    // FIXME: handle errors!
    public void cbName()
    {
	String str = m_white.getResponse();
	m_white_name = str.trim();
    }

    private void htpSendNameCommand()
    {
	Runnable callback = new Runnable() 
	    { 
		public void run() { cbName(); } 
	    };
	sendCommand("name\n", callback);
    }

    // FIXME: handle errors!
    public void cbVersion()
    {
	String str = m_white.getResponse();
	m_white_version = str.trim();
    }

    private void htpVersion()
    {
	Runnable callback = new Runnable()
	    {
		public void run() { cbVersion(); }
	    };
	sendCommand("version\n", callback);
    }

    public void cbEmptyResponse()
    {

    }

    // FIXME: add a callback?
    private void htpPlay(Move move)
    {
	sendCommand("play " + move.getColor().toString() + 
		    " " + move.getPoint().toString() + 
		    "\n", 
		    null);
	sendCommand("showboard\n", null);
    }

    // FIXME: add a callback?
    private void htpUndo()
    {
	sendCommand("undo\n", null);
    }

    // FIXME: add a callback?
    private void htpBoardsize()
    {
        Dimension size = m_guiboard.getBoardSize();
        sendCommand("boardsize " + size.width + " " + size.height + "\n",
                    null);
        sendCommand("showboard\n", null);
    }

    public void cbComputerMove()
    {
	String str = m_white.getResponse();
	HexPoint point = HexPoint.get(str.trim());
	if (point == null) {
	    System.out.println("Invalid move!!");
	} else {
	    play(new Move(point, m_tomove));
	}
    }

    private void cmdComputerMove()
    {
	Runnable callback = new Runnable() 
	    { 
		public void run() { cbComputerMove(); } 
	    };
	sendCommand("genmove " + m_tomove.toString() + "\n", callback);
	sendCommand("showboard\n", null);
    }

    /** Callback from GuiBoard. 
	Handle a mouse click.
    */
    public void fieldClicked(HexPoint point)
    {
	if (m_guiboard.getColor(point) == HexColor.EMPTY) {
	    humanMove(new Move(point, m_tomove));
	}
    }

    public void humanMove(Move move)
    {
	play(move);
	htpPlay(move);
        if (!m_guiboard.isBoardFull())
            cmdComputerMove();
    }

    private void play(Move move)
    {
	Node node = new Node(move);
	m_current.addChild(node);
	m_current = node;

	m_guiboard.setColor(move.getPoint(), m_tomove);
	m_guiboard.setLastPlayed(move.getPoint());
	m_tomove = m_tomove.otherColor();
	setGameChanged(true);
	setFrameTitle();

	//m_guiboard.repaint();
	setLastPlayed();
	m_guiboard.paintImmediately();
	m_toolbar.updateButtonStates(m_current);	
    }

    private void forward(int n)
    {
	for (int i=0; i<n; i++) {
	    Node child = m_current.getChild();
	    if (child == null) break;

	    Move move = child.getMove();
	    m_guiboard.setColor(move.getPoint(), move.getColor());
	    htpPlay(move);

	    m_current = child;
	    m_tomove = move.getColor().otherColor();
	}
	setLastPlayed();
	m_guiboard.repaint();
	m_toolbar.updateButtonStates(m_current);
    }

    private void backward(int n)
    {
	for (int i=0; i<n; i++) {
	    if (m_current == m_root) break;

	    Move move = m_current.getMove();
	    m_guiboard.setColor(move.getPoint(), HexColor.EMPTY);
	    htpUndo();

	    m_current = m_current.getParent();
	}
	setLastPlayed();
	m_guiboard.repaint();
	m_toolbar.updateButtonStates(m_current);
	    
	if (m_current == m_root) 
	    m_tomove = HexColor.BLACK;
	else
	    m_tomove = m_current.getMove().getColor().otherColor();
    }

    private void up()
    {
	if (m_current.getNext() != null) {
	    HexPoint point = m_current.getMove().getPoint();
	    m_guiboard.setColor(point, HexColor.EMPTY);
	    htpUndo();
	    
	    m_current = m_current.getNext();
	    
	    HexColor color = m_current.getMove().getColor();
	    point = m_current.getMove().getPoint();
	    m_guiboard.setColor(point, color);
	    htpPlay(m_current.getMove());
	    m_tomove = color.otherColor();
	    
	    setLastPlayed();
	    m_guiboard.repaint();
	    m_toolbar.updateButtonStates(m_current);
	}
    }

    private void down()
    {
	if (m_current.getPrev() != null) {
	    HexPoint point = m_current.getMove().getPoint();
	    m_guiboard.setColor(point, HexColor.EMPTY);
	    htpUndo();
	    
	    m_current = m_current.getPrev();
	    
	    HexColor color = m_current.getMove().getColor();
	    point = m_current.getMove().getPoint();
	    m_guiboard.setColor(point, color);
	    htpPlay(m_current.getMove());
	    m_tomove = color.otherColor();
	    
	    setLastPlayed();
	    m_guiboard.repaint();
	    m_toolbar.updateButtonStates(m_current);
	}
    }

    //------------------------------------------------------------

    private void setLastPlayed()
    {
	if (m_current != m_root)
	    m_guiboard.setLastPlayed(m_current.getMove().getPoint());
	else 
	    m_guiboard.setLastPlayed(null);
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
	String name = "HexGui " + Version.id;
	if (m_white != null) 
	    name += " - [" + m_white_name + " " + m_white_version + "]";
	setTitle(name + " - " + filename);
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

    //------------------------------------------------------------

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

    private HtpController m_white;
    private String m_white_name;
    private String m_white_version;
    private Process m_white_process;
    private Socket m_white_socket;

    private File m_file;
}

//----------------------------------------------------------------------------
