//----------------------------------------------------------------------------
// $Id$ 
//----------------------------------------------------------------------------

package hexgui.sgf;

import hexgui.hex.HexColor;
import hexgui.hex.HexPoint;
import hexgui.hex.Move;
import hexgui.game.Node;
import hexgui.game.GameInfo;

import java.io.*;
import java.awt.Dimension;
import java.lang.StringBuilder;
import java.lang.NumberFormatException;


//----------------------------------------------------------------------------

/** SGF reader. 
    NOTE: Uses StringBuilder which requires Java 1.5
*/
public final class SgfReader
{
    /** Sgf exception. */
    public static class SgfError
	extends Exception
    {
	public SgfError(String message)
	{
	    super(message);
	}
    }

    /** Constructor. 
	Parse the input stream in sgf format. 
    */
    public SgfReader(InputStream in) throws SgfError
    {
	InputStreamReader reader = new InputStreamReader(in);
	m_reader = new LineNumberReader(reader);
	m_tokenizer = new StreamTokenizer(m_reader);
	m_gameinfo = new GameInfo();

	try {
	    findGameTree();
	    m_gametree = parseGameTree(null);
	    verifyGameInfo();
	}
	catch (IOException e) {
	    throw new SgfError("Error occured during read.");
	}
    }

    public Node getGameTree()
    {
	return m_gametree;
    }

    public GameInfo getGameInfo()
    {
	return m_gameinfo;
    }

    //------------------------------------------------------------

    private void findGameTree() throws SgfError, IOException
    {
	while (true) {
	    int ttype = m_tokenizer.nextToken();
	    if (ttype == StreamTokenizer.TT_EOF)
		throw new SgfError("No game tree found!");
	    
	    if (ttype == '(') {
		m_tokenizer.pushBack();
		break;
	    }		
	}
    }

    private Node parseGameTree(Node parent) throws SgfError, IOException
    {
	int ttype = m_tokenizer.nextToken();
	if (ttype != '(') 
	    throw new SgfError("Error at head of game tree!");

	Node node = parseNode(parent);
	
	ttype = m_tokenizer.nextToken();
	if (ttype != ')') 
	    throw new SgfError("Game tree not closed!");

	return node;
    }

    private Node parseNode(Node parent) throws SgfError, IOException
    {
	int ttype = m_tokenizer.nextToken();
	if (ttype != ';') 
	    throw new SgfError("Error at head of node!");

	Node node = new Node();
	node.setParent(parent);
	if (parent != null) 
	    parent.addChild(node);

	boolean done = false;
	while (!done) {
	    ttype = m_tokenizer.nextToken();
	    switch(ttype) {
	    case '(':
		m_tokenizer.pushBack();
		parseGameTree(node);
		break;

	    case ';':
		m_tokenizer.pushBack();
		parseNode(node);
		done = true;
		break;

	    case ')':
		m_tokenizer.pushBack();
		done = true;
		break;

	    case StreamTokenizer.TT_WORD:
		parseProperty(node);
		break;

	    case StreamTokenizer.TT_EOF:
		throw new SgfError("Unexpected EOF in node!");

	    default:
		throw new SgfError("Error in SGF file.");
	    }
	}

	return node;
    }

    private void parseProperty(Node node) throws SgfError, IOException
    {
	String name = m_tokenizer.sval;
	String value = parseValue();

	System.out.println(name + "[" + value + "]");

	if (name.equals("W")) {
	    HexPoint point = new HexPoint(value);
	    node.setMove(new Move(point, HexColor.WHITE));
	} else if (name.equals("B")) {
	    HexPoint point = new HexPoint(value);
	    node.setMove(new Move(point, HexColor.BLACK));
	} else if (name.equals("C")) {
	    node.setComment(value);
	} else if (name.equals("SZ")) {
	    int x,y;	    
	    Dimension dim = new Dimension();
	    String sp[] = value.split(":");
	    
	    try {
		if (sp.length == 1) {
		    x = Integer.parseInt(sp[0]);
		    dim.setSize(x,x);
		} else if (sp.length == 2) {
		    x = Integer.parseInt(sp[0]);
		    y = Integer.parseInt(sp[1]);
		    dim.setSize(x,y);
		} else {
		    throw new SgfError("Too many arguments in SZ property!");
		}
	    }
	    catch (NumberFormatException e) {
		throw new SgfError("Error in SZ property!");
	    }

	    m_gameinfo.setBoardSize(dim);
	    
	} else {
	    System.out.println("Unsupported property '" + name + "'");
	}
    }

    private String parseValue() throws SgfError, IOException
    {
	int ttype = m_tokenizer.nextToken();
	if (ttype != '[')
	    throw new SgfError("Property missing opening '['.");

	StringBuilder sb = new StringBuilder(256);
	boolean quoted = false;
	while (true) {
	    int c = m_reader.read();
	    if (c < 0)
		throw new SgfError("Property runs to EOF.");

	    if (!quoted) {
		if (c == ']') break;
		if (c == '\\') 
		    quoted = true;
		else {
		    if (c != '\r' && c != '\n')
			sb.append((char)c);
		}
	    } else {
		quoted = false;
		sb.append(c);
	    }
	}

	return sb.toString();
    }

    //----------------------------------------------------------------------

    private void verifyGameInfo() throws SgfError
    {
	if (m_gameinfo.getBoardSize() == null) 
	    throw new SgfError("Missing SZ property.");
    }
    
    private StreamTokenizer m_tokenizer;
    private LineNumberReader m_reader;
    private Node m_gametree;
    private GameInfo m_gameinfo;
}

//----------------------------------------------------------------------------