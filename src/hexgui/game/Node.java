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
    Stores moves and other properties related to this move.
*/
public class Node
{
    /** Initializes an empty node with a null move. */
    public Node()
    {
	this(null);
    }

    /** Constructs a new node with the specified move.
	@param move move to initialize the node with.
    */
    public Node(Move move)
    {
	m_property = new TreeMap();
	setMove(move);
    }

    public void setMove(Move move) { m_move = move;  }
    public Move getMove() { return m_move; }

    public void setParent(Node parent) { m_parent = parent; }
    public Node getParent() { return m_parent; }

    public void setPrev(Node prev) { m_prev = prev; }
    public Node getPrev() { return m_prev; }

    public void setNext(Node next) { m_next = next; }
    public Node getNext() { return m_next; }

    /** Sets the first child of this node.  
	This does not update the sibling pointers of the child.
    */
    public void setFirstChild(Node child) { m_child = child; }
    
    /** Returns the number of children of this node. */
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

    /** Adds a child to the end of the list of children. 
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

    /** Returns the nth child. 
	@param n The number of the child to return. 
	@return  The nth child or <code>null</code> that child does not exist.
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

    /** Returns the first child. 
	@return first child or <code>null</code> if no children.
    */
    public Node getChild() { return getChild(0); }

    /** Removes this node from the gametree. */
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

    /** Adds a property to this node. 
	Node properties are <code>(key, value)</code> pairs of strings.
	These properties will stored if the gametree is saved in SGF format.
	@param key name of the property
	@param value value of the property
    */
    public void setSgfProperty(String key, String value)
    {
	m_property.put(key, value);
    }

    /** Returns the value of a property. 
	@param key name of property
	@return value of <code>key</code> or <code>null</code> if key is
	not in the property list.
    */                
    public String getSgfProperty(String key)
    {
	// FIXME: this generates a compiler warning.  How to fix?
	return (String)m_property.get(key);
    }

    /** Returns a map of the current set of properties.
	@return Map containing the properties
    */
    public Map getProperties()
    {
	return m_property;
    }

    /** Sets the SGF Comment field of this node. */
    public void setComment(String comment) { setSgfProperty("C", comment); }
    
    public String getComment() { return getSgfProperty("C"); }

    //----------------------------------------------------------------------

    private TreeMap m_property;

    private Move m_move;
    private Node m_parent, m_prev, m_next, m_child;
}

//----------------------------------------------------------------------------