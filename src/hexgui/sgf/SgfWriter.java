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
import java.util.Map;
import java.util.Iterator;

//----------------------------------------------------------------------------

/** Writes a in sgf format. */
public final class SgfWriter
{
    
    /** Write a game tree. */
    public SgfWriter(OutputStream out, Node root, GameInfo game)
    {
	m_out = new PrintStream(out);
	m_gameinfo = game;

	writeTree(root, true);
	m_out.print("\n");
	m_out.flush();
    }

    private void writeTree(Node root, boolean isroot)
    {
	m_out.print("(");
	writeNode(root, isroot);
	m_out.print(")");
    }

    private void writeNode(Node node, boolean isroot)
    {
	m_out.print(";");

	if (isroot) {
	    String value;

	    node.setSgfProperty("FF", "4");
	    node.setSgfProperty("AP", "HexGui:0.2");
	    node.setSgfProperty("GM", "11");
	    
	    Dimension dim = m_gameinfo.getBoardSize();
	    value = Integer.toString(dim.width);
	    if (dim.width != dim.height)
		value += ":" + Integer.toString(dim.height);
	    node.setSgfProperty("SZ", value);

	}

	Map map = node.getProperties();
	Iterator it = map.entrySet().iterator();
	while(it.hasNext()) {
	    Map.Entry e = (Map.Entry)it.next();
	    String key = (String)e.getKey();
	    String val = (String)e.getValue();
	    m_out.print(key + "[" + val + "]");
	}
	
	int num = node.numChildren();
	if (num == 0) return;

	if (num == 1) {
	    writeNode(node.getChild(), false);
	    return;
	} 
	
	for (int i=0; i<num; i++) 
	    writeTree(node.getChild(i), false);
	
    }

    private void printColor(HexColor color)
    {
	if (color == HexColor.BLACK)
	    m_out.print("B"); 
	else if (color == HexColor.WHITE)
	    m_out.print("W");
	else {
	    // FIXME: throw an error here!
	}
    }	

    private void printPoint(HexPoint point)
    {
	m_out.print(point.toString());
    }

    private PrintStream m_out;
    private GameInfo m_gameinfo;
}

//----------------------------------------------------------------------------