//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.HexColor;
import hexgui.hex.HexPoint;

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

//----------------------------------------------------------------------------

/** Base class for board drawing.

    <p>Board drawers are responsible for drawing the background,
    labels, field outlines, and stone shadows.  In addition, they are
    also responsible for determining the the actual position of each
    field in the window.  Field contents (i.e. stones, markers,
    numerical values, etc) are not drawn, they are drawn with the
    GuiField class.

    <p>Board sizes supported are <code>m x n</code> where
    <code>m</code> and <code>n</code> range from 1 to 26.  By default,
    black connects top and bottom and should be labeled with letters.
    White connects left and right and should be labeled with numbers.
    An option to display White on the top is provided.
*/
public abstract class BoardDrawerBase
{
    public BoardDrawerBase(boolean flipped)
    {
	m_background = null;
	m_flipped = flipped;
	m_aspect_ratio = 1.0;
    }

    /** Loads the image in <code>filename</code> and sets it as the background.
	If <code>filename</code> does not exist no background image is 
	displayed.  Image will be scaled to fit the window.
	@param filename filename of the image to use as a background.
    */
    public void loadBackground(String filename)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(filename);
        if (url == null) {
	    System.out.println("loadBackground: could not load '" + 
			       filename + "'!");
            m_background = null;            
	} else {
	    m_background = new ImageIcon(url).getImage();
	}
    }

    /** Sets whether White connects top and bottom or not.
	If <code>f</code> is <code>true</code> then White should connect
        top and bottom, otherwise Black connects top and bottom.
	@param f  whether White connects top and bottom or not
    */
    public void setFlipped(boolean f) { m_flipped = f; }

    /** Gets the field containing the specified point.
	@param p the point
	@param field the set of fields to search through.
	@return the field in the set that p is in or <code>null</code> if
                p is not in any field.
    */
    public abstract GuiField getFieldContaining(Point p, GuiField field[]);

    /** Draws the board.
	The size of the region to draw to, the size of the board, and the
	field to draw must be given.  The position of each field is 
	then calculated and the board drawn. 

	@param g graphics context to draw to
	@param w the width of the region to draw in
	@param h the height of the region to draw in
	@param bx the width of the board (in fields)
	@param by the height of the board (in fields)
    */
    public void draw(Graphics g, 
		     int w, int h, int bx, int by, 
		     GuiField field[])
    {
	m_width = w;
	m_height = h;

	m_bwidth = bx;
	m_bheight = by;

	computeFieldPlacement();
	initDrawCells(field);

	setAntiAliasing(g);
	drawBackground(g);
	drawCells(g, field);
	drawLabels(g);
	drawShadows(g, field);
	drawFields(g, field);
    }

    //------------------------------------------------------------

    /** Returns the location in the window of the field with
	coordinates <code>(x,y)</code>.  Coordinates increase to the
	right and down, with the top left of the board having
	coordinates <code>(0,0)</code>.  Negative values are acceptable.
	@param x the x coordinate of the field.
	@param y the y coordinate of the field.
	@return the center of the field at <code>(x,y)</code>.
    */
    protected abstract Point getLocation(int x, int y);

    /** Returns the location of the field with HexPoint pos. */
    protected Point getLocation(HexPoint pos)
    {
	if (pos == HexPoint.NORTH) {
	    return getLocation(m_bwidth/2+1, -2);
	} else if (pos == HexPoint.SOUTH) { 
	    return getLocation(m_bwidth/2-1, m_bheight+1);
	} else if (pos == HexPoint.EAST) { 
	    return getLocation(m_bwidth+1, m_bheight/2-1);
	} else if (pos == HexPoint.WEST) { 
	    return getLocation(-2, m_bheight/2+1);
	}
	return getLocation(pos.x, pos.y);
    }

    /** Calculates the width of a field given the dimensions of the
	window and board.
	@param w width of window
	@param h height of window
	@param bw width of board
	@param bh height of board
    */
    protected abstract int calcFieldWidth(int w, int h, int bw, int bh);

    /** Calculates the height of a field given the dimensions of the
	window and board.
	@see calcFieldWidth
    */
    protected abstract int calcFieldHeight(int w, int h, int bw, int bh);

    
    protected abstract int calcStepSize();

    /** Calculates the width of the board in pixels. 
	@requires calcFieldWidth and calcFieldHeight to have been called.
    */
    protected abstract int calcBoardWidth();
    
    /** Calculates the height of the board in pixels.
	@requires calcFieldWidth and calcFieldHeight to have been called.
    */
    protected abstract int calcBoardHeight();

    /** Perfroms any necessary initializations for drawing the
	outlines of the fields.
	@param the fields it will need to draw
    */
    protected abstract void initDrawCells(GuiField field[]);

    /** Draws the outlines of the given fields. 
	@param g graphics context to draw to.
	@param field the list of fields to draw.
    */
    protected abstract void drawCells(Graphics g, GuiField field[]);

    protected void computeFieldPlacement()
    {
	m_fieldWidth = calcFieldWidth(m_width, m_height, m_bwidth, m_bheight);
	m_fieldHeight = calcFieldHeight(m_width, m_height, m_bwidth, m_bheight);

	if (m_fieldHeight >= (int)(m_fieldWidth/m_aspect_ratio)) {
	    m_fieldHeight = (int)(m_fieldWidth/m_aspect_ratio);
	} else {
	    m_fieldWidth = (int)(m_fieldHeight*m_aspect_ratio);
	}

	// If field dimensions are not even then the inner cell lines
	// on the board can be doubled up.  
	// FIXME: lines still get doubled up...why?
	if ((m_fieldWidth & 1) != 0) m_fieldWidth--;
	if ((m_fieldHeight & 1) != 0) m_fieldHeight--;

	m_fieldRadius = (m_fieldWidth < m_fieldHeight) ? 
                         m_fieldWidth : m_fieldHeight;

	m_step = calcStepSize();

	int bw = calcBoardWidth();
	int bh = calcBoardHeight();
	m_marginX = (m_width - bw)/2 + m_fieldWidth/2;
	m_marginY = (m_height - bh)/2 + m_fieldHeight/2;
    }

    //------------------------------------------------------------

    protected int getShadowOffset()
    {
        return (m_fieldRadius - 2*GuiField.getStoneMargin(m_fieldRadius)) / 12;
    }

    protected void drawBackground(Graphics g)
    {
	if (m_background != null) 
	    g.drawImage(m_background, 0, 0, m_width, m_height, null);
    }

    protected void drawLabel(Graphics g, Point p, String string, int xoff)
    {
	FontMetrics fm = g.getFontMetrics();
	int width = fm.stringWidth(string);
	int height = fm.getAscent();
	int x = width/2;
	int y = height/2;
	g.drawString(string, p.x + xoff - x, p.y + y); 
    }

    protected abstract void drawLabels(Graphics g);

    protected void drawShadows(Graphics graphics, GuiField[] field)
    {
        if (m_fieldRadius <= 5)
            return;
        Graphics2D graphics2D =
            graphics instanceof Graphics2D ? (Graphics2D)graphics : null;
        if (graphics2D == null)
            return;
        graphics2D.setComposite(COMPOSITE_3);
        int size = m_fieldRadius - 2 * GuiField.getStoneMargin(m_fieldRadius);
        int offset = getShadowOffset();
        for (int pos = 0; pos < field.length; pos++) {
	    if (field[pos].getColor() == HexColor.EMPTY)
		continue;
	    Point location = getLocation(field[pos].getPoint());
	    graphics.setColor(Color.black);
	    graphics.fillOval(location.x - size / 2 + offset,
			      location.y - size / 2 + offset,
			      size, size);
	}
        graphics.setPaintMode();
    }

    protected void drawFields(Graphics g, GuiField field[])
    {
	for (int x=0; x<field.length; x++) {
	    Point p = getLocation(field[x].getPoint());
	    field[x].draw(g, p.x, p.y, m_fieldWidth, m_fieldHeight);
	}
    }

    protected void setAntiAliasing(Graphics g)
    {
	if (g instanceof Graphics2D) {
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
	}
    }

    protected Image m_background;
    protected boolean m_flipped;

    protected double m_aspect_ratio;

    protected int m_width, m_height;
    protected int m_bwidth, m_bheight;
    protected int m_marginX, m_marginY;
    protected int m_fieldWidth, m_fieldHeight, m_fieldRadius, m_step;

    protected static final AlphaComposite COMPOSITE_3
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);

}

//----------------------------------------------------------------------------
