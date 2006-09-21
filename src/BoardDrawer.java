import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class BoardDrawer
{
    public BoardDrawer()
    {
	loadBackground();
    }

    public void loadBackground()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("images/wood.png");
        if (url == null) {
	    System.out.println("BoardDrawer: couldn't find background image!");
            m_image = null;            
	} else {
	    m_image = new ImageIcon(url).getImage();
	}
    }

    public void setAntiAliasing(Graphics g)
    {
	if (g instanceof Graphics2D) {
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				 RenderingHints.VALUE_ANTIALIAS_ON);
	}
    }

    public static double TAN60DEG = 1.732050808;

    public Polygon createVerticalHexagon(Point p, int width, int height)
    {
	int xpoints[] = new int[6];
	int ypoints[] = new int[6];

	int sx = (int)(0.5 * width);
        int sy = (int)(sx / TAN60DEG);
	int ly = (int)(0.5 * height);
	
	xpoints[0] = 0;   ypoints[0] = -ly;
	xpoints[1] = -sx; ypoints[1] = -sy;
	xpoints[2] = -sx; ypoints[2] = +sy;
	xpoints[3] = 0;   ypoints[3] = +ly;
	xpoints[4] = +sx; ypoints[4] = +sy;
	xpoints[5] = +sx; ypoints[5] = -sy;

	Polygon ret = new Polygon(xpoints, ypoints, 6);
	ret.translate(p.x, p.y);

	return ret;
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

    //------------------------------------------------------------
    public int getShadowOffset()
    {
        return (m_fieldWidth  - 2*Field.getStoneMargin(m_fieldWidth)) / 12;
    }

    public Field getFieldContaining(Point p, Field field[])
    {
	if (field.length != m_hexagon.length) {
	    System.out.println("Fields differ in size!");
	    return null;
	}

	for (int x=0; x<field.length; x++) {
	    if (m_hexagon[x].contains(p)) 
		return field[x];
	}
	return null;
    }

    //------------------------------------------------------------
    private void drawBackground(Graphics g)
    {
	if (m_image != null) 
	    g.drawImage(m_image, 0, 0, m_width, m_height, null);
    }

    private void drawCells(Graphics g)
    {
	g.setColor(Color.black);
	for (int i=0; i<m_hexagon.length; i++)
	    g.drawPolygon(m_hexagon[i]);
    }

    private void drawLabels(Graphics g)
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

    private void drawLabel(Graphics g, Point p, String string, int xoff)
    {
	FontMetrics fm = g.getFontMetrics();
	int width = fm.stringWidth(string);
	int height = fm.getAscent();
	int x = width/2;
	int y = height/2;
	g.drawString(string, p.x + xoff - x, p.y + y); 
    }

    private void drawShadows(Graphics graphics, Field[] field)
    {
        if (m_fieldWidth <= 5)
            return;
        Graphics2D graphics2D =
            graphics instanceof Graphics2D ? (Graphics2D)graphics : null;
        if (graphics2D == null)
            return;
        graphics2D.setComposite(COMPOSITE_3);
        int size = m_fieldWidth - 2 * Field.getStoneMargin(m_fieldWidth);
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

    private void drawFields(Graphics g, Field field[])
    {
	for (int x=0; x<field.length; x++) {
	    Point p = getLocation(x);
	    field[x].draw(g, p.x, p.y, m_fieldWidth, m_fieldHeight);
	}
    }

    public void draw(Graphics g, int w, int h, int bx, int by, Field field[])
    {
	m_width = w;
	m_height = h;

	m_bwidth = bx;
	m_bheight = by;

	setAntiAliasing(g);

	computeFieldPlacement();

	drawBackground(g);
	drawCells(g);
	drawLabels(g);
	drawShadows(g, field);

	drawFields(g, field);
    }

    private Image m_image;
    private int m_width;
    private int m_height;

    private int m_bwidth;
    private int m_bheight;
    private int m_marginX;
    private int m_marginY;
    private int m_fieldWidth;
    private int m_fieldHeight;
    private int m_vertStep;
    private Polygon m_hexagon[];

    private static final AlphaComposite COMPOSITE_3
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
}
