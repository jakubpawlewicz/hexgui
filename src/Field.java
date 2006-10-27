
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import hexgui.hex.*;

public class Field
{
    public static int getStoneMargin(int width)
    {
	return width / 17 + 3;
    }
    
    //------------------------------------------------------------
    public Field()
    {
	clear();
	clearAttributes();
    }

    public static final int DRAW_CELL_OUTLINE = 1;

    public void clearAttributes() 
    {
	m_attributes = 0;
    }

    public int getAttributes()
    {
	return m_attributes;
    }

    public void setAttributes(int f)
    {
	m_attributes |= f;
    }

    public void setColor(HexColor c)
    {
	m_color = c;
    }

    public HexColor getColor()
    {
	return m_color;
    }

    public void clear()
    {
	setColor(HexColor.EMPTY);
    }

    private RadialGradientPaint getPaint(int size,
                                         Color colorNormal,
                                         Color colorBright)
    {
        RadialGradientPaint paint;
        int paintSize;
        int radius = Math.max(size / 3, 1);
        int center = size / 3;
        Point2D.Double centerPoint =
            new Point2D.Double(center, center);
        Point2D.Double radiusPoint =
            new Point2D.Double(radius, radius);
        paint = new RadialGradientPaint(centerPoint, colorBright,
                                        radiusPoint, colorNormal);
        return paint;
    }

    //------------------------------------------------------------
    void draw(Graphics g, int x, int y, int w, int h)
    {
	if (!g.hitClip(x, y, w, h))
            return;

	m_width = w;
	m_height = h;
	m_margin = getStoneMargin(m_width);

	m_graphics = g.create(x-w/2,y-h/2,w,h);
	if (m_graphics instanceof Graphics2D)
	    m_graphics2D = (Graphics2D)m_graphics;
	else 
	    m_graphics2D = null;
	
	if (m_color == HexColor.EMPTY) return;

	if (m_color == HexColor.WHITE) 
	    drawStone(COLOR_STONE_WHITE, COLOR_STONE_WHITE_BRIGHT);
	else if (m_color == HexColor.BLACK)
	    drawStone(COLOR_STONE_BLACK, COLOR_STONE_BLACK_BRIGHT);

    }
    
    void drawStone(Color normal, Color bright)
    {
	if (m_graphics2D != null) {
	    RadialGradientPaint paint = getPaint(m_width, normal, bright);
	    m_graphics2D.setPaint(paint);
	} else {
	    m_graphics.setColor(normal);
	}

	m_graphics.fillOval(m_margin, m_margin,
			    m_width - 2*m_margin, m_height - 2*m_margin);

	m_graphics.setPaintMode();
    }

    private int m_width;
    private int m_height;
    private int m_margin;
    private HexColor m_color;
    private int m_attributes;
    private Graphics m_graphics;
    private Graphics2D m_graphics2D;

    private static final Color COLOR_STONE_BLACK = Color.decode("#030303");
    private static final Color COLOR_STONE_BLACK_BRIGHT = Color.decode("#666666");
    private static final Color COLOR_STONE_WHITE = Color.decode("#d7d0c9");
    private static final Color COLOR_STONE_WHITE_BRIGHT
	//        = Color.decode("#f6eee6");
        = Color.decode("#ffffff");

}
