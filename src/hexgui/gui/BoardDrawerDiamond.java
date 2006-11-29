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
    public BoardDrawerDiamond(boolean flipped)
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
	ret.x = m_marginX + (y + x)*m_step;
	ret.y = m_marginY + (m_bheight/2)*m_fieldHeight 
	        + (y - x)*m_fieldHeight/2;
	return ret;
    }

    protected Dimension calcFieldSize(int w, int h, int bw, int bh)
    {
	Dimension ret = new Dimension();
	ret.width = h / (bh + 2);
	ret.height = w / (bw + 2 + (bh-1)/2);
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
	return m_bheight*m_fieldHeight;
    }

    protected Polygon createOutlinePolygon(Point pos)
    {
	return Hexagon.createHorizontalHexagon(pos, 
					       m_fieldWidth, 
					       m_fieldHeight);
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
}

//----------------------------------------------------------------------------
