//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.HexColor;

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

//----------------------------------------------------------------------------

public abstract class BoardDrawerBase
{
    protected static final double ASPECT_RATIO = 1.1547;

    public BoardDrawerBase(boolean flipped)
    {
	m_background = null;
	m_flipped = flipped;
    }

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

    public void setFlipped(boolean f)
    {
	m_flipped = f;
    }

    public GuiField getFieldContaining(Point p, GuiField field[])
    {
	if (field.length != m_outline.length) {
	    System.out.println("Fields differ in size!");
	    return null;
	}

	for (int x=0; x<field.length; x++) {
	    if (m_outline[x].contains(p)) 
		return field[x];
	}
	return null;
    }

    public void draw(Graphics g, 
		     int w, int h, int bx, int by, 
		     GuiField field[])
    {
	m_width = w;
	m_height = h;

	m_bwidth = bx;
	m_bheight = by;

	computeFieldPlacement();

	setAntiAliasing(g);
	drawBackground(g);
	drawCells(g, field);
	drawLabels(g);
	drawShadows(g, field);
	drawFields(g, field);
    }

    //------------------------------------------------------------

    protected abstract Point getLocation(int x, int y);

    protected Point getLocation(int pos)
    {
	int size = m_bwidth*m_bheight;
	if (pos == size) { // north
	    return getLocation(m_bwidth/2+1, -2);
	} else if (pos == size+1) { // south
	    return getLocation(m_bwidth/2-1, m_bheight+1);
	} else if (pos == size+2) { // east 
	    return getLocation(m_bwidth+1, m_bheight/2-1);
	} else if (pos == size+3) { // west
	    return getLocation(-2, m_bheight/2+1);
	}
	return getLocation(pos % m_bwidth, pos / m_bwidth);
    }

    protected abstract Dimension calcFieldSize(int w, int h, int bw, int bh);
    protected abstract int calcStepSize();
    protected abstract int calcBoardWidth();
    protected abstract int calcBoardHeight();
    protected abstract Polygon createOutlinePolygon(Point pos);

    protected void computeFieldPlacement()
    {
	m_outline = new Polygon[m_bwidth*m_bheight+4];

	Dimension dim = calcFieldSize(m_width, m_height, m_bwidth, m_bheight);
	m_fieldWidth = dim.width;
	m_fieldHeight = dim.height;

	// Note: if field dimensions are not even then the inner cell lines
	// on the board can be doubled up.  
	if ((m_fieldWidth & 1) != 0) m_fieldWidth++;
	if ((m_fieldHeight & 1) != 0) m_fieldHeight++;
	
	if (m_fieldHeight >= (int)(m_fieldWidth/ASPECT_RATIO)) {
	    m_fieldHeight = (int)(m_fieldWidth/ASPECT_RATIO);
	} else {
	    m_fieldWidth = (int)(m_fieldHeight*ASPECT_RATIO);
	}

	m_fieldRadius = (m_fieldWidth < m_fieldHeight) ? 
	    m_fieldWidth : m_fieldHeight;

	m_step = calcStepSize();
	
	int bw = calcBoardWidth();
	int bh = calcBoardHeight();

	m_marginX = (m_width - bw) / 2 + m_fieldWidth/2;
	m_marginY = (m_height - bh) / 2 + m_fieldHeight/2;

        for (int i = 0; i < m_outline.length; i++) {
	    m_outline[i] = createOutlinePolygon(getLocation(i));
        }	
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

    protected void drawCells(Graphics g, GuiField field[])
    {
	g.setColor(Color.black);
	for (int i=0; i<m_outline.length; i++) {
	    if ((field[i].getAttributes() & GuiField.DRAW_CELL_OUTLINE) != 0) {
		g.drawPolygon(m_outline[i]);
	    }
	}
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
	    Point location = getLocation(pos);
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
	    Point p = getLocation(x);
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

    protected int m_width, m_height;
    protected int m_bwidth, m_bheight;
    protected int m_marginX, m_marginY;
    protected int m_fieldWidth, m_fieldHeight, m_fieldRadius, m_step;
    protected Polygon m_outline[];

    protected static final AlphaComposite COMPOSITE_3
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);

}

//----------------------------------------------------------------------------
