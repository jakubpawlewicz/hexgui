import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public abstract class BoardDrawerBase
{
    public BoardDrawerBase()
    {
	m_background = null;
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

    // FIXME!
    public Polygon createHorizontalHexagon(Point p, int width, int height)
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
    public abstract Point getLocation(int x, int y);

    public abstract Point getLocation(int pos);

    public abstract void computeFieldPlacement();

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
    protected void drawBackground(Graphics g)
    {
	if (m_background != null) 
	    g.drawImage(m_background, 0, 0, m_width, m_height, null);
    }

    protected void drawCells(Graphics g)
    {
	g.setColor(Color.black);
	for (int i=0; i<m_hexagon.length; i++)
	    g.drawPolygon(m_hexagon[i]);
    }

    protected void drawLabel(Graphics g, Point p, String string, int xoff)
    {
	FontMetrics fm = g.getFontMetrics();
	int width = fm.stringWidth(string);
	int height = fm.getAscent();
	int x = width/2;
	int y = height/2;
	g.drawString(string, p.x + xoff - x, p.y + y); 
    }

    protected abstract void drawLabels(Graphics g);

    protected void drawShadows(Graphics graphics, Field[] field)
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

    protected void drawFields(Graphics g, Field field[])
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

    protected Image m_background;
    protected int m_width;
    protected int m_height;

    protected int m_bwidth;
    protected int m_bheight;
    protected int m_marginX;
    protected int m_marginY;
    protected int m_fieldWidth;
    protected int m_fieldHeight;
    protected int m_vertStep;
    protected Polygon m_hexagon[];

    protected static final AlphaComposite COMPOSITE_3
        = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
}