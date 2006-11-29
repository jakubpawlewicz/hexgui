//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.util.*;

import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

//----------------------------------------------------------------------------

public final class GuiBoard
    extends JPanel
{
    /** Callback for clicks on a field. */
    public interface Listener
    {
	void fieldClicked(HexPoint point);
    }

    private static final int DEFAULT_WIDTH = 11;
    private static final int DEFAULT_HEIGHT = 11;    

    // FIXME: coordinate this with GuiMenuBar default!!
    private static final String DEFAULT_DRAW_TYPE = "Diamond";

    private static final int DEFAULT_PREFERRED_WIDTH = 800;
    private static final int DEFAULT_PREFERRED_HEIGHT = 600;

    private static final boolean DEFAULT_FLIPPED = false;


    /** Constructor. */
    public GuiBoard(Listener listener)
    {
	m_image = null;
	m_listener = listener;
	m_flipped = DEFAULT_FLIPPED;

	initSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

	setDrawType(DEFAULT_DRAW_TYPE);   
	setPreferredSize(new Dimension(DEFAULT_PREFERRED_WIDTH, 
				       DEFAULT_PREFERRED_HEIGHT));

	setLayout(new BoardLayout());
	m_boardPanel = new BoardPanel();
	add(m_boardPanel);

	MouseAdapter mouseAdapter = new MouseAdapter()
	{
	    public void mouseClicked(MouseEvent e)
	    {
		GuiField f = m_drawer.getFieldContaining(e.getPoint(), field);
		if (f == null) return;
		m_listener.fieldClicked(f.getPoint());
	    }
	};
	m_boardPanel.addMouseListener(mouseAdapter);

	setVisible(true);
    }

    public void setDrawType(String name)
    {
	if (name.equals("Go"))
	    m_drawer = new BoardDrawerGo(m_flipped);
	else if (name.equals("Diamond"))
	    m_drawer = new BoardDrawerDiamond(m_flipped);
	else if (name.equals("Flat")) 
	    m_drawer = new BoardDrawerFlat(m_flipped);
	else {
	    System.out.println("GuiBoard: unknown draw type '" + name + "'.");
	    m_drawer = new BoardDrawerDiamond(m_flipped);
	} 
    }

    public void setOrientation(String orient)
    {
	if (orient.equals("Black on top"))
	    m_flipped = false;
	else if (orient.equals("White on top"))
	    m_flipped = true;
	else {
	    m_flipped = false;
	    System.out.println("GuiBoard: unknown orientation '" + 
			       orient + "'.");
	}
	m_drawer.setFlipped(m_flipped);
    }

    public void initSize(int w, int h)
    {
	System.out.println("GuiBoard.initSize: " + w + " " + h);

	m_width = w; 
	m_height = h;
	m_size = new Dimension(m_width, m_height);

	field = new GuiField[w*h+4];
	for (int x=0; x<w*h; x++) {
	    field[x] = new GuiField(HexPoint.get(x % w, x / w));
	    field[x].setAttributes(GuiField.DRAW_CELL_OUTLINE);
	}

	field[w*h+0] = new GuiField(HexPoint.NORTH);
	field[w*h+1] = new GuiField(HexPoint.SOUTH);
	field[w*h+2] = new GuiField(HexPoint.WEST);
	field[w*h+3] = new GuiField(HexPoint.EAST);
	
	clearAll();
    }

    public void initSize(Dimension dim)
    {
	initSize(dim.width, dim.height);
    }

    public Dimension getBoardSize()
    {
	return m_size;
    }

    public void clearAll()
    {
	for (int x=0; x<field.length; x++) 
	    field[x].clear();

	getField(HexPoint.NORTH).setColor(HexColor.BLACK);
	getField(HexPoint.SOUTH).setColor(HexColor.BLACK);
	getField(HexPoint.WEST).setColor(HexColor.WHITE);
	getField(HexPoint.EAST).setColor(HexColor.WHITE);
    }

    public void setColor(HexPoint point, HexColor color)
    {
	GuiField f = getField(point);
	f.setColor(color);
    }

    public HexColor getColor(HexPoint point)
    {
	GuiField f = getField(point);
	return f.getColor();
    }

    public GuiField getField(HexPoint point)
    {
	for (int x=0; x<field.length; x++) 
	    if (field[x].getPoint().equals(point)) 
		return field[x];
	assert(false);
	return null;
    }

    //------------------------------------------------------------
    private class BoardPanel
	extends JPanel
    {
	public BoardPanel()
	{
	    setFocusable(true);
	}

	public void paintComponent(Graphics graphics)
	{
	    int width = getWidth();
	    int height = getHeight();
// 	    System.out.println("GuiBoard.paintComponent "
// 			       + graphics.getClipBounds().x + " "
// 			       + graphics.getClipBounds().y + " "
// 			       + graphics.getClipBounds().width + " "
// 			       + graphics.getClipBounds().height);

	    if (m_image == null) {
		//System.out.println("Creating new image...");
		m_image = createImage(width, height);
	    }

	    m_drawer.draw(m_image.getGraphics(), width, height, 
			  m_width, m_height, field);

	    graphics.drawImage(m_image, 0, 0, null);
	}

	public void setBounds(int x, int y, int w, int h)
	{
	    super.setBounds(x,y,w,h);
	    //System.out.println("Bounds: "+x+" "+y+" "+w+" "+h);
	    m_image = null;
	}
    }

    //------------------------------------------------------------

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private int m_width, m_height;
    private Dimension m_size;

    private boolean m_flipped;

    private Image m_image;
    private GuiField field[];

    private BoardDrawerBase m_drawer;
    private BoardPanel m_boardPanel;

    private Listener m_listener;
}

//----------------------------------------------------------------------------