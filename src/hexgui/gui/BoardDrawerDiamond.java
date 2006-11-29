//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.util.Hexagon;

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

//----------------------------------------------------------------------------

public class BoardDrawerDiamond extends BoardDrawerBase
{

    protected static final double ASPECT_RATIO = 1.1547;

    public BoardDrawerDiamond(boolean flipped)
    {
	super(flipped);
	loadBackground("hexgui/images/wood.png");
	m_aspect_ratio = ASPECT_RATIO;
    }

    public GuiField getFieldContaining(Point p, GuiField field[])
    {
	for (int x=0; x<field.length; x++) {
	    if (m_outline[x].contains(p)) 
		return field[x];
	}
	return null;
    }

    protected Point getLocation(int x, int y)
    {
	if (m_flipped) {
	    int temp = x;
	    x = y;
	    y = temp;
	}

	// yoffset will be positive when bwidth > bheight (to push the
	// board down) and negative when bwidth < bheight (to lift it
	// up) because the a1 square (0,0) will not be 
	// in the center of the vertical space occupied by the board. 
	int yoffset = (m_bwidth - m_bheight)*m_fieldHeight/2;

	Point ret = new Point();
	ret.x = m_marginX + (y + x)*m_step;
	ret.y = m_marginY + yoffset + (m_bheight/2)*m_fieldHeight 
	        + (y - x)*m_fieldHeight/2;
	return ret;
    }

    protected Dimension calcFieldSize(int w, int h, int bw, int bh)
    {
	Dimension ret = new Dimension();
	ret.width = w / (bw + (bh-1)/2 + 2);
	ret.height = h / (bh + 2);
	return ret;
    }

    protected int calcStepSize()
    {
	return m_fieldWidth/4 + m_fieldWidth/2;
    }
    protected int calcBoardWidth()
    {
	return (m_bwidth+m_bheight-1)*m_step;
    }

    protected int calcBoardHeight()
    {
	return m_bheight*m_fieldHeight 
	    + (m_bwidth - m_bheight)*m_fieldHeight/2;
    }

    protected void initDrawCells(GuiField field[])
    {
	m_outline = new Polygon[field.length];
        for (int x = 0; x < m_outline.length; x++) {
	    Point p = getLocation(field[x].getPoint());
	    m_outline[x] = Hexagon.createHorizontalHexagon(p,
							   m_fieldWidth, 
							   m_fieldHeight);
        }	
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

    protected void drawLabels(Graphics g)
    {
	int xoffset;
	g.setColor(Color.black);

	char c = 'A';
	xoffset = 0;
	for (int x=0; x<m_bwidth; x++) {
	    String string = Character.toString(c);
	    drawLabel(g, getLocation(x, -1), string, xoffset);
	    drawLabel(g, getLocation(x, m_bheight), string, xoffset);
	    c++;
	}
	int n = 1;
	xoffset = 0;	
	for (int y=0; y<m_bheight; y++) {
	    String string = Integer.toString(n);
	    drawLabel(g, getLocation(-1, y), string, xoffset);
	    drawLabel(g, getLocation(m_bwidth, y), string, xoffset);
	    n++;
	}
    }

    protected Polygon m_outline[];
}

//----------------------------------------------------------------------------
