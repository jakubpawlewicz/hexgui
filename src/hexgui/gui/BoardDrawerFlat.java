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

public class BoardDrawerFlat extends BoardDrawerBase
{
    public BoardDrawerFlat(boolean flipped)
    {
	super(flipped);
	loadBackground("hexgui/images/wood.png");
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

    protected Dimension calcFieldSize(int w, int h, int bw, int bh)
    {
	Dimension ret = new Dimension();
	ret.width = (2*w) / (2*bw + bh + 3);
	ret.height = (2*h - bh*ret.width) /  (bh + 1);
	return ret;
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
	    + (m_bheight/2)*(2*(m_fieldHeight/4));
    }

    protected Polygon createOutlinePolygon(Point pos)
    {
	return Hexagon.createVerticalHexagon(pos, 
					     m_fieldWidth, 
					     m_fieldHeight);
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
}

//----------------------------------------------------------------------------
