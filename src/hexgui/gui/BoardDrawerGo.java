//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

//----------------------------------------------------------------------------

public class BoardDrawerGo extends BoardDrawerBase
{
    public BoardDrawerGo(boolean flipped)
    {
	super(flipped);
	loadBackground("hexgui/images/wood.png");
    }

    // FIXME: do this better instead of relying on our encoding!!
    // FIXME: support clicking on edges!!
    public GuiField getFieldContaining(Point p, GuiField field[])
    {
	int w = m_fieldWidth/2;
	int h = m_fieldHeight/2;
	for (int y=0; y<m_bheight; y++) {
	    for (int x=0; x<m_bwidth; x++) {
		Point c = getLocation(x, y);
		int dx = Math.abs(p.x - c.x);
		int dy = Math.abs(p.y - c.y);
		if (dx <= w && dy <= h) 
		    return field[y*m_bheight + x];
	    }
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
	ret.x = m_marginX + x*m_fieldWidth;
	ret.y = m_marginY + y*m_fieldHeight;
	return ret;
    }

    // FIXME: center stones on even length sides!!
    protected Point getLocation(int pos)
    {
	int size = m_bwidth*m_bheight;
	if (pos == size) { // north
	    return getLocation(m_bwidth/2, -2);
	} else if (pos == size+1) { // south
	    return getLocation(m_bwidth/2, m_bheight+1);
	} else if (pos == size+2) { // east 
	    return getLocation(m_bwidth+1, m_bheight/2);
	} else if (pos == size+3) { // west
	    return getLocation(-2, m_bheight/2);
	}
	return getLocation(pos % m_bwidth, pos / m_bwidth);
    }

    protected Dimension calcFieldSize(int w, int h, int bw, int bh)
    {
	Dimension ret = new Dimension();
	ret.width = w / (bw + 4);
	ret.height = h / (bh + 4);
	return ret;
    }

    // FIXME: not needed... something is wrong with the api 
    protected int calcStepSize()
    {
	return 0;
	
    }
    protected int calcBoardWidth()
    {
	return m_bwidth*m_fieldWidth;
    }

    protected int calcBoardHeight()
    {
	return m_bheight*m_fieldHeight;
    }

    protected void initDrawCells()
    {
    }

    protected void drawCells(Graphics g, GuiField field[])
    {
	g.setColor(Color.black);
	Point p = getLocation(0,0);
	int x = p.x;
	int y = p.y;
	for (int i=0; i<m_bheight; i++) {
	    g.drawLine(x, 
		       y+i*m_fieldHeight, 
		       x+(m_bwidth-1)*m_fieldWidth, 
		       y+i*m_fieldHeight);
	}
	for (int i=0; i<m_bwidth; i++) {
	    g.drawLine(x + i*m_fieldWidth, 
		       y,
		       x + i*m_fieldWidth, 
		       y+(m_bheight-1)*m_fieldHeight);
	}
	for (int i=1; i<m_bheight; i++) {
	    g.drawLine(x, 
		       y + i*m_fieldHeight,
		       x + i*m_fieldWidth,   // FIXME: WRONG ON NON-SQUARE!
		       y);
	}
	for (int i=1; i<m_bwidth; i++) {
	    g.drawLine(x + i*m_fieldWidth,
		       y + (m_bheight-1)*m_fieldHeight,
		       x + (m_bwidth-1)*m_fieldWidth,
		       y + i*m_fieldHeight);
	}
    }

    protected void drawLabels(Graphics g)
    {
	int xoffset,yoffset;
	g.setColor(Color.black);

	char c = 'A';
	xoffset = yoffset = 0;
	for (int x=0; x<m_bwidth; x++) {
	    String string = Character.toString(c);
	    drawLabel(g, getLocation(x, -1), string, xoffset);
	    drawLabel(g, getLocation(x, m_bheight), string, xoffset);
	    c++;
	}
	int n = 1;
	xoffset = yoffset = 0;
	for (int y=0; y<m_bheight; y++) {
	    String string = Integer.toString(n);
	    drawLabel(g, getLocation(-1, y), string, xoffset);
	    drawLabel(g, getLocation(m_bwidth, y), string, xoffset);
	    n++;
	}
    }
}

//----------------------------------------------------------------------------
