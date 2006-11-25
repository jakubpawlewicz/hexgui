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

/** Writes a in sgf format. */
public final class SgfWriter
{
    
    /** Write a game tree. */
    public SgfWriter(OutputStream out, Node root)
    {
	m_out = new PrintStream(out);

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


	}

	if (node.getMove() != null) 
	    printMove(node.getMove());
	
	// FIXME: print other properties!


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

    private void printMove(Move move)
    {
	printColor(move.getColor());
	m_out.print("[");
	printPoint(move.getPoint());
	m_out.print("]");
    }


    private PrintStream m_out;
}

//----------------------------------------------------------------------------