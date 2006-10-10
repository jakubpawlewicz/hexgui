
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

public class GuiBoard
    extends JPanel
{
    public GuiBoard()
    {
	m_image = null;

	initSize(11, 11);
        newGame();

	m_drawer = new BoardDrawerDiamond();
	setPreferredSize(new Dimension(800, 600));

	setLayout(new BoardLayout());
	m_boardPanel = new BoardPanel();
	add(m_boardPanel);

	MouseAdapter mouseAdapter = new MouseAdapter()
	    {
		public void mouseClicked(MouseEvent e)
		{
		    Field f = m_drawer.getFieldContaining(e.getPoint(), field);
		    if (f == null) return;
		    if (f.getColor() == HexColor.EMPTY) {
			f.setColor(m_toMove);
			m_toMove = m_toMove.otherColor();
			repaint();
		    }
		}
	    };
	m_boardPanel.addMouseListener(mouseAdapter);

	setVisible(true);
    }

    public void initSize(int w, int h)
    {
	System.out.println("GuiBoard.initSize: " + w + " " + h);

	m_width = w; 
	m_height = h;

	m_north = w*h;
	m_south = m_north+1;
	m_east = m_south+1;
	m_west = m_east+1;

	field = new Field[w*h+4];
	for (int x=0; x<w*h+4; x++) {
	    field[x] = new Field();
	    if (x < w*h) field[x].setAttributes(Field.DRAW_CELL_OUTLINE);
	}
    }

    public void clearAll()
    {
	for (int x=0; x<field.length; x++) 
	    field[x].clear();

	field[m_north].setColor(HexColor.BLACK);
	field[m_south].setColor(HexColor.BLACK);
	field[m_west].setColor(HexColor.WHITE);
	field[m_east].setColor(HexColor.WHITE);
    }

    public void newGame()
    {
        clearAll();
	m_toMove = HexColor.BLACK;
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
	    System.out.println("GuiBoard.paintComponent "
			       + graphics.getClipBounds().x + " "
			       + graphics.getClipBounds().y + " "
			       + graphics.getClipBounds().width + " "
			       + graphics.getClipBounds().height);

	    if (m_image == null) {
		System.out.println("Creating new image...");
		m_image = createImage(width, height);
	    }

	    m_drawer.draw(m_image.getGraphics(), width, height, 
			  m_width, m_height, field);

	    graphics.drawImage(m_image, 0, 0, null);
	}

	public void setBounds(int x, int y, int w, int h)
	{
	    super.setBounds(x,y,w,h);
	    System.out.println("Bounds: "+x+" "+y+" "+w+" "+h);
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
    private int m_north, m_south, m_east, m_west;

    private Image m_image;
    private HexColor m_toMove;
    private Field field[];

    private BoardDrawerBase m_drawer;
    private BoardPanel m_boardPanel;
}
