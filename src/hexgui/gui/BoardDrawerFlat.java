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

    protected static final double ASPECT_RATIO = 1.0/1.1547;

    public BoardDrawerFlat()
    {
	super();
	loadBackground("hexgui/images/wood.png");
	m_aspect_ratio = ASPECT_RATIO;
    }

    protected Point getLocation(int x, int y)
    {
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
	return h / ((bh+1)/2 + (bh/4) + 4);
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

    protected Polygon[] calcCellOutlines(GuiField field[])
    {
	Polygon outline[] = new Polygon[field.length];
        for (int x = 0; x < outline.length; x++) {
	    Point p = getLocation(field[x].getPoint());
	    outline[x] = Hexagon.createVerticalHexagon(p,
						       m_fieldWidth, 
						       m_fieldHeight);
        }	
	return outline;
    }

    protected void drawLabels(Graphics g, boolean alphatop)
    {
	String string;
	int xoffset,yoffset;
	g.setColor(Color.black);

	xoffset = m_fieldWidth/2;
	yoffset = 1;
	for (int x=0; x<m_bwidth; x++) {
	    if (alphatop)
		string = Character.toString((char)((int)'A' + x));
	    else
		string = Integer.toString(x+1);
	    drawLabel(g, getLocation(x, -1), string, xoffset);
	    drawLabel(g, getLocation(x-yoffset, m_bheight), string, xoffset);
	}
	xoffset = 0;
	yoffset = 0;
	for (int y=0; y<m_bheight; y++) {
	    if (!alphatop)
		string = Character.toString((char)((int)'A' + y));
	    else
		string = Integer.toString(y+1);
	    drawLabel(g, getLocation(-1, y), string, xoffset);
	    drawLabel(g, getLocation(m_bwidth, y-yoffset), string, xoffset);
	}
    }

    protected Polygon m_outline[];
}

//----------------------------------------------------------------------------
