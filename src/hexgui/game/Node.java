//----------------------------------------------------------------------------
// $Id$ 
//----------------------------------------------------------------------------

package hexgui.game;

import hexgui.hex.HexColor;
import hexgui.hex.Move;

import java.util.Map;
import java.util.TreeMap;

//----------------------------------------------------------------------------

/** Node in a game tree.
    Stores the move and other properties related to this move.
    Can have any number of children.
*/
public class Node
{
    /** Constructor.
	Create an empty node with a null move.
    */
    public Node()
    {
	this(null);
    }

    /** Constructor.
	@param move Move to initialize the node with.
    */
    public Node(Move move)
    {
	m_property = new TreeMap();
	setMove(move);
    }

    public void setMove(Move move) { 
	m_move = move; 
	if (move == null) return;
	if (move.getColor() == HexColor.WHITE) {
	    m_property.put("W", move.getPoint().toString());
	    m_property.remove("B");
	} else {
	    m_property.put("B", move.getPoint().toString());
	    m_property.remove("W");
	}
    }
    public Move getMove() { return m_move; }

    public void setParent(Node parent) { m_parent = parent; }
    public Node getParent() { return m_parent; }

    public void setPrev(Node prev) { m_prev = prev; }
    public Node getPrev() { return m_prev; }

    public void setNext(Node next) { m_next = next; }
    public Node getNext() { return m_next; }

    public void setFirstChild(Node child) { m_child = child; }

    /** Get the number of children. */
    public int numChildren()
    {
	int num = 0;
	Node cur = m_child;
	while (cur != null) {
	    num++;
	    cur = cur.getNext();
	}	
	return num;
    }

    /** Add a child.
        @param child Node to be added to end of list.
    */     
    public void addChild(Node child) 
    {
	Node cur = m_child;

	if (cur == null) {
	    m_child = child;
	    child.setPrev(null);
	} else {
	    while (cur.getNext() != null)
		cur = cur.getNext();
	    cur.setNext(child);
	    child.setPrev(cur);
	}
	child.setNext(null);
	child.setParent(this);
    }

    /** Get the nth child. 
	@param n The child number to return.
	@return  Node of the nth child, null if n < 0 or n > numChildren().
    */
    public Node getChild(int n) 
    {
	Node cur = m_child;
	for (int i=0; cur != null; i++) {
	    if (i == n) return cur;
	    cur = cur.getNext();
	}
	return null;
    }

    /** Get the first child. */
    public Node getChild() { return getChild(0); }

    /** Remove this subtree. */
    public void removeSelf()
    {
	Node prev = getPrev();
	Node next = getNext();
	if (m_prev == null) {
	    Node parent = getParent();
	    if (parent != null) getParent().setFirstChild(next);
	    next.setPrev(null);
	} else {
	    prev.setNext(next);
	    next.setPrev(prev);
	}
    }

    //----------------------------------------------------------------------
    
    public void setSgfProperty(String key, String value)
    {
	m_property.put(key, value);
    }

    public String getSgfProperty(String key)
    {
	// FIXME: this generates a compiler warning.  How to fix?
	return (String)m_property.get(key);
    }

    public Map getProperties()
    {
	return m_property;
    }

    public void setComment(String comment) { setSgfProperty("C", comment); }
    public String getComment() { return getSgfProperty("C"); }

    //----------------------------------------------------------------------

    private TreeMap m_property;

    private Move m_move;
    private Node m_parent, m_prev, m_next, m_child;
}

//----------------------------------------------------------------------------