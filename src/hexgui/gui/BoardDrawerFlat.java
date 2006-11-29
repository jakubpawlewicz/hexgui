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

// FIXME: board not centered correctly if flipped and non square!
public class BoardDrawerFlat extends BoardDrawerBase
{

    protected static final double ASPECT_RATIO = 1.1547;

    public BoardDrawerFlat(boolean flipped)
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
	Point ret = new Point();
	ret.x = m_marginX + y*m_fieldWidth/2 + x*m_fieldWidth;
	ret.y = m_marginY + y*m_step;
	return ret;
    }

    protected int calcFieldWidth(int w, int h, int bw, int bh)
    {
	return w / (bw + (bh-1)/2 + 2);
    }

    protected int calcFieldHeight(int w, int h, int bw, int bh)
    {
	return h / ((bh+1)/2 + (bh/4) + 3);
    }

    protected int calcStepSize()
    {
	return m_fieldHeight/4 + m_fieldHeight/2;
	
    }
    protected int calcBoardWidth()
    {
	return m_bwidth*m_fieldWidth + (m_bheight-1)*m_fieldWidth/2;
    }

    protected int calcBoardHeight()
    {
	return m_fieldHeight*(m_bheight+1)/2 
	    +  m_fieldHeight*m_bheight/4;
    }

    protected void initDrawCells(GuiField field[])
    {
	m_outline = new Polygon[field.length];
        for (int x = 0; x < m_outline.length; x++) {
	    Point p = getLocation(field[x].getPoint());
	    m_outline[x] = Hexagon.createVerticalHexagon(p,
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
	int xoffset,yoffset;
	g.setColor(Color.black);

	char c = 'A';
	xoffset = (m_flipped) ? 0 : m_fieldWidth/2;
	yoffset = (m_flipped) ? 0 : 1;
	for (int x=0; x<m_bwidth; x++) {
	    String string = Character.toString(c);
	    drawLabel(g, getLocation(x, -1), string, xoffset);
	    drawLabel(g, getLocation(x-yoffset, m_bheight), string, xoffset);
	    c++;
	}
	int n = 1;
	xoffset = (m_flipped) ? m_fieldWidth / 2 : 0;	
	yoffset = (m_flipped) ? 1 : 0;
	for (int y=0; y<m_bheight; y++) {
	    String string = Integer.toString(n);
	    drawLabel(g, getLocation(-1, y), string, xoffset);
	    drawLabel(g, getLocation(m_bwidth, y-yoffset), string, xoffset);
	    n++;
	}
    }

    protected Polygon m_outline[];
}

//----------------------------------------------------------------------------
