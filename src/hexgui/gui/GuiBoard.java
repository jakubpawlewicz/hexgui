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

public class GuiBoard
    extends JPanel
{
    /** Callback for clicks on a field. */
    public interface Listener
    {
	void fieldClicked(HexPoint point);
    }

    /** Constructor. */
    public GuiBoard(Listener listener)
    {
	m_image = null;

	m_listener = listener;

	initSize(11, 11);

	setDrawType("Diamond");   // FIXME: use preferences
	setPreferredSize(new Dimension(800, 600));

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
	if (name.equals("Diamond"))
	    m_drawer = new BoardDrawerDiamond();
	else if (name.equals("Flat")) 
	    m_drawer = new BoardDrawerFlat();
	else {
	    System.out.println("GuiBoard: unknown draw type '" + name + "'.");
	    m_drawer = new BoardDrawerDiamond();
	} 
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

    public int DEFAULT_WIDTH = 11;
    public int DEFAULT_HEIGHT = 11;    

    private int m_width, m_height;
    private Dimension m_size;

    private Image m_image;
    private GuiField field[];

    private BoardDrawerBase m_drawer;
    private BoardPanel m_boardPanel;

    private Listener m_listener;
}

//----------------------------------------------------------------------------