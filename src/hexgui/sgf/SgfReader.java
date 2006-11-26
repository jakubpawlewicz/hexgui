//----------------------------------------------------------------------------
// $Id$ 
//----------------------------------------------------------------------------

package hexgui.sgf;

import hexgui.hex.HexColor;
import hexgui.hex.HexPoint;
import hexgui.hex.Move;
import hexgui.game.Node;

import java.io.*;

//----------------------------------------------------------------------------

/** SGF reader. */
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

	try {
	    findGameTree();
	    m_gametree = parseGameTree(null);
	}
	catch (IOException e) {
	    throw new SgfError("Error occured during read.");
	}
    }

    public Node getGameTree()
    {
	return m_gametree;
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
	String name, value;

	name = m_tokenizer.sval;
	
	int ttype = m_tokenizer.nextToken();
	if (ttype != '[')
	    throw new SgfError("Expected '[' not found in property!");
	
	ttype = m_tokenizer.nextToken();
	switch(ttype) { 
	case '\"':
	case StreamTokenizer.TT_WORD:
	    value = m_tokenizer.sval;
	    break;
	default:
	    throw new SgfError("Unknown property type!");
	}
	
	ttype = m_tokenizer.nextToken();
	if (ttype != ']')
	    throw new SgfError("Closing ']' not found in property!");

	System.out.println(name + "[" + value + "]");
	if (name.equals("W")) {
	    HexPoint point = new HexPoint(value);
	    node.setMove(new Move(point, HexColor.WHITE));
	} else if (name.equals("B")) {
	    HexPoint point = new HexPoint(value);
	    node.setMove(new Move(point, HexColor.BLACK));
	}
    }
    
    private StreamTokenizer m_tokenizer;
    private LineNumberReader m_reader;
    private Node m_gametree;
}

//----------------------------------------------------------------------------