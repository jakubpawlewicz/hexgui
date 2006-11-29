//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

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

    public Point getLocation(int x, int y)
    {
	if (m_flipped) {
	    int temp = x;
	    x = y;
	    y = temp;
	}

	Point ret = new Point();
	ret.x = m_marginX + y*m_fieldWidth/2 + x*m_fieldWidth;
	ret.y = m_marginY + y*m_vertStep;
	return ret;
    }

    public Point getLocation(int pos)
    {
	Point ret = new Point();
	int size = m_bwidth*m_bheight;
	if (pos == size) { // north
	    ret = getLocation(m_bwidth/2+1, -2);
	} else if (pos == size+1) { // south
	    ret = getLocation(m_bwidth/2-1, m_bheight+1);
	} else if (pos == size+2) { // east 
	    ret = getLocation(m_bwidth+1, m_bheight/2-1);
	} else if (pos == size+3) { // west
	    ret = getLocation(-2, m_bheight/2+1);
	} else {
	    ret = getLocation(pos % m_bwidth, pos / m_bwidth);
	}
	return ret;
    }

    public void computeFieldPlacement()
    {
	m_hexagon = new Polygon[m_bwidth*m_bheight+4];

	m_fieldWidth  = (2*m_width) / (2*m_bwidth + m_bheight + 3);
	m_fieldHeight = (2*m_height - m_bheight*m_fieldWidth) 
	                   /  (m_bheight + 1);

	// Note: if field dimensions are not even then the inner cell lines
	// on the board can be doubled up.  
	if ((m_fieldWidth & 1) != 0) m_fieldWidth++;
	if ((m_fieldHeight & 1) != 0) m_fieldHeight++;
	
	if (m_fieldHeight >= (int)(m_fieldWidth * (1.0 / ASPECT_RATIO))) {
		m_fieldHeight = (int)(m_fieldWidth * (1.0 / ASPECT_RATIO));
	} else {
		m_fieldWidth = (int)(m_fieldHeight*ASPECT_RATIO);
	}

	m_fieldRadius = (m_fieldWidth < m_fieldHeight) ? 
	    m_fieldWidth : m_fieldHeight;

	int sy = (m_fieldHeight/2)/2;
	m_vertStep = sy + m_fieldHeight/2;

	int bw = m_bwidth*m_fieldWidth + (m_bheight-1)*m_fieldWidth/2;
	int bh = m_fieldHeight*(m_bheight+1)/2 
	           + (m_bheight/2)*(2*sy);

	m_marginX = (m_width - bw) / 2 + m_fieldWidth/2;
	m_marginY = (m_height - bh) / 2 + m_fieldHeight/2;

        for (int i = 0; i < m_hexagon.length; i++) {
	    Point p = getLocation(i);
	    m_hexagon[i] = createVerticalHexagon(p, m_fieldWidth, 
						    m_fieldHeight);
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

    protected int m_vertStep;
}

//----------------------------------------------------------------------------
