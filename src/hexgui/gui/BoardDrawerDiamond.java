package hexgui.gui;

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class BoardDrawerDiamond extends BoardDrawerBase
{
    public BoardDrawerDiamond()
    {
	super();
	loadBackground("images/wood.png");
    }

    //------------------------------------------------------------
    public Point getLocation(int x, int y)
    {
	Point ret = new Point();
	ret.x = m_marginX + (y + x)*m_horizStep;
	ret.y = m_marginY + (m_bheight/2)*m_fieldHeight + (y - x)*m_fieldHeight/2;
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
	int sx;

	m_hexagon = new Polygon[m_bwidth*m_bheight+4];

	m_fieldHeight = m_height / (m_bheight + 2);
	m_fieldWidth  = m_width / (m_bwidth + 2 + (m_bheight-1)/2);

	if (m_fieldHeight >= (int)(m_fieldWidth * (1.0 / ASPECT_RATIO))) {
		m_fieldHeight = (int)(m_fieldWidth * (1.0 / ASPECT_RATIO));
	} else if (m_fieldHeight < (int)(m_fieldWidth * ASPECT_RATIO)) {
		m_fieldWidth = (int)(m_fieldHeight*ASPECT_RATIO);
	}

	if ((m_fieldHeight & 1) != 0) m_fieldHeight++;
	if ((m_fieldWidth & 1) != 0) m_fieldWidth++;

	sx = (int)((0.5 * m_fieldHeight / TAN60DEG)+0.5);
	m_horizStep = sx + m_fieldWidth/2;
	
	int bw = (m_bwidth+m_bheight-1)*m_horizStep;
	int bh = m_bheight*m_fieldHeight;

	m_marginX = (m_width - bw) / 2 + m_fieldWidth/2;
	m_marginY = (m_height - bh) / 2 + m_fieldHeight/2;

        for (int i = 0; i < m_hexagon.length; i++) {
	    Point p = getLocation(i);
	    m_hexagon[i] = createHorizontalHexagon(p, m_fieldWidth, m_fieldHeight);
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

    protected int m_horizStep;

    public static double ASPECT_RATIO = 1.0;
}
