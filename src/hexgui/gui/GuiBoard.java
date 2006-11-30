//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.util.*;

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

//----------------------------------------------------------------------------

/** Gui Board. */
public final class GuiBoard
    extends JPanel
{
    /** Callback for clicks on a field. */
    public interface Listener
    {
	void fieldClicked(HexPoint point);
    }

    private static final int DEFAULT_WIDTH = 11;
    private static final int DEFAULT_HEIGHT = 11;    

    // FIXME: coordinate this with GuiMenuBar default!!
    private static final String DEFAULT_DRAW_TYPE = "Diamond";

    private static final int DEFAULT_PREFERRED_WIDTH = 800;
    private static final int DEFAULT_PREFERRED_HEIGHT = 600;

    private static final boolean DEFAULT_FLIPPED = false;


    /** Constructor. */
    public GuiBoard(Listener listener)
    {
	m_image = null;
	m_listener = listener;
	m_flipped = DEFAULT_FLIPPED;

	initSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

	setDrawType(DEFAULT_DRAW_TYPE);   
	setPreferredSize(new Dimension(DEFAULT_PREFERRED_WIDTH, 
				       DEFAULT_PREFERRED_HEIGHT));

	setLayout(new BoardLayout());
	m_boardPanel = new BoardPanel();
	add(m_boardPanel);

	MouseAdapter mouseAdapter = new MouseAdapter()
	{
	    public void mouseClicked(MouseEvent e)
	    {
		GuiField f = m_drawer.getFieldContaining(e.getPoint(), m_field);
		if (f == null) return;
		m_listener.fieldClicked(f.getPoint());
	    }
	};
	m_boardPanel.addMouseListener(mouseAdapter);

	setVisible(true);
    }

    /** Sets the type of board drawer to use.  If <code>name</code> is
	not one of the values because "Diamond" is is used.
	@param name one of ("Diamond", "Flat", "Go"). 
    */
    public void setDrawType(String name)
    {
	if (name.equals("Go"))
	    m_drawer = new BoardDrawerGo();
	else if (name.equals("Diamond"))
	    m_drawer = new BoardDrawerDiamond();
	else if (name.equals("Flat")) 
	    m_drawer = new BoardDrawerFlat();
	else {
	    System.out.println("GuiBoard: unknown draw type '" + name + "'.");
	    m_drawer = new BoardDrawerDiamond();
	} 
    }

    /** Sets whether black on letters is on top or if white on 
	numbers is on top.  If string is invalid defaults to black on top.
	@param orient either "Black on top" or "White on top". 
    */
    public void setOrientation(String orient)
    {
	if (orient.equals("Black on top"))
	    m_flipped = false;
	else if (orient.equals("White on top"))
	    m_flipped = true;
	else {
	    m_flipped = false;
	    System.out.println("GuiBoard: unknown orientation '" + 
			       orient + "'.");
	}
    }

    /** Creates a board of the given dimensions.
	@param w width of the board in cells
	@param h height of the board in cells
    */
    public void initSize(int w, int h)
    {
	System.out.println("GuiBoard.initSize: " + w + " " + h);

	m_width = w; 
	m_height = h;
	m_size = new Dimension(m_width, m_height);

	m_field = new GuiField[w*h+4];
	for (int x=0; x<w*h; x++) {
	    m_field[x] = new GuiField(HexPoint.get(x % w, x / w));
	    m_field[x].setAttributes(GuiField.DRAW_CELL_OUTLINE);
	}

	m_field[w*h+0] = new GuiField(HexPoint.NORTH);
	m_field[w*h+1] = new GuiField(HexPoint.SOUTH);
	m_field[w*h+2] = new GuiField(HexPoint.WEST);
	m_field[w*h+3] = new GuiField(HexPoint.EAST);
	
	clearAll();
    }

    /** Creates a board with the given dimensions.
	Convience function.  
	@param dim dimension of the board
	@see initSize(int, int)
    */
    public void initSize(Dimension dim)
    {
	initSize(dim.width, dim.height);
    }

    /** Gets the size of the board.
	@return size of the board as a Dimension.
    */
    public Dimension getBoardSize()
    {
	return m_size;
    }

    /** Clears all marks and stones from the board. */
    public void clearAll()
    {
	for (int x=0; x<m_field.length; x++) 
	    m_field[x].clear();

	getField(HexPoint.NORTH).setColor(HexColor.BLACK);
	getField(HexPoint.SOUTH).setColor(HexColor.BLACK);
	getField(HexPoint.WEST).setColor(HexColor.WHITE);
	getField(HexPoint.EAST).setColor(HexColor.WHITE);
    }

    /** Sets the given point to the given color.
	@param point the point
	@param color the color to set it to.
    */
    public void setColor(HexPoint point, HexColor color)
    {
	GuiField f = getField(point);
	f.setColor(color);
    }

    /** Gets the color of the specified point.
	@param point the point whose color we with to obtain.
	@return the color of <code>point</code>
    */
    public HexColor getColor(HexPoint point)
    {
	GuiField f = getField(point);
	return f.getColor();
    }

    /** Gets the field at the specified point. */
    public GuiField getField(HexPoint point)
    {
	for (int x=0; x<m_field.length; x++) 
	    if (m_field[x].getPoint() == point) 
		return m_field[x];
	assert(false);
	return null;
    }

    //------------------------------------------------------------

    private GuiField[] flipFields(GuiField field[])
    {
	GuiField out[] = new GuiField[field.length];
	for (int i=0; i<field.length; i++) {
	    HexPoint p = field[i].getPoint();
	    out[i] = new GuiField(field[i]);
	    if (p == HexPoint.NORTH)
		out[i].setPoint(HexPoint.WEST);
	    else if (p == HexPoint.WEST)
		out[i].setPoint(HexPoint.NORTH);
	    else if (p == HexPoint.EAST)
		out[i].setPoint(HexPoint.SOUTH);
	    else if (p == HexPoint.SOUTH)
		out[i].setPoint(HexPoint.EAST);
	    else {
		out[i].setPoint(HexPoint.get(p.y, p.x));
	    }	    
	}
	return out;
    }

    private class BoardPanel
	extends JPanel
    {
	public BoardPanel()
	{
	    setFocusable(true);
	}

	public void paintComponent(Graphics graphics)
	{
	    int w = getWidth();
	    int h = getHeight();

	    if (m_image == null) {
		m_image = createImage(w, h);
	    }

	    int bw = m_width;
	    int bh = m_height;
	    GuiField ff[] = m_field;
	    if (m_flipped) {
		bw = m_height;
		bh = m_width;
		ff = flipFields(m_field);
	    }

	    m_drawer.draw(m_image.getGraphics(), w, h, bw, bh, m_flipped, ff);
	    graphics.drawImage(m_image, 0, 0, null);
	}

	public void setBounds(int x, int y, int w, int h)
	{
	    super.setBounds(x, y, w, h);
	    m_image = null;
	}
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private int m_width, m_height;
    private Dimension m_size;
    private boolean m_flipped;

    private Image m_image;
    private GuiField m_field[];

    private BoardDrawerBase m_drawer;
    private BoardPanel m_boardPanel;

    private Listener m_listener;
}

//----------------------------------------------------------------------------