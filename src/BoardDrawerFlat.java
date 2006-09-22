import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class BoardDrawerFlat extends BoardDrawerBase
{
    public BoardDrawerFlat()
    {
	super();
	loadBackground();
    }

    public void loadBackground()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("images/wood.png");
        if (url == null) {
	    System.out.println("BoardDrawerFlat: couldn't find background image!");
            m_background = null;            
	} else {
	    m_background = new ImageIcon(url).getImage();
	}
    }

    //------------------------------------------------------------
    public Point getLocation(int x, int y)
    {
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

	// make them equal.  FIXME: is this ok?
	if (m_fieldHeight >= (int)(m_fieldWidth * 1.0)) {
		m_fieldHeight = (int)(m_fieldWidth * 1.0);
	} else if (m_fieldHeight < (int)(m_fieldWidth * 1.0)) {
		m_fieldWidth = (int)(m_fieldHeight*1.0);
	}

	int sy = (int)((0.5 * m_fieldWidth / TAN60DEG)+0.5);
	m_vertStep = sy + m_fieldHeight/2;

	int bw = m_bwidth*m_fieldWidth + (m_bheight-1)*m_fieldWidth/2;
	int bh = m_fieldHeight*(m_bheight+1)/2 
	           + (m_bheight/2)*(2*sy);

	m_marginX = (m_width - bw) / 2 + m_fieldWidth/2;
	m_marginY = (m_height - bh) / 2 + m_fieldHeight/2;

	// calculate hexagons
        for (int pos = 0; pos < m_hexagon.length; pos++) {
	    Point p = getLocation(pos);
	    m_hexagon[pos] = createVerticalHexagon(p, m_fieldWidth, m_fieldHeight);
        }	
    }

    protected void drawLabels(Graphics g)
    {
	int xoffset;
	g.setColor(Color.black);

	char c = 'A';
	xoffset = m_fieldWidth/2;
	for (int x=0; x<m_bwidth; x++) {
	    String string = Character.toString(c);
	    drawLabel(g, getLocation(x, -1), string, xoffset);
	    drawLabel(g, getLocation(x-1, m_bheight), string, xoffset);
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
