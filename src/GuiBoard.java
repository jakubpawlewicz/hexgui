
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

public class GuiBoard
    extends JPanel
{
    public GuiBoard()
    {

	initSize(11, 11);

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
	for (int x=0; x<w*h+4; x++) 
	    field[x] = new Field();

	clearAll();

	m_toMove = HexColor.BLACK;
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
	   //  System.out.println("GuiBoard.paintComponent "
// 			       + graphics.getClipBounds().x + " "
// 			       + graphics.getClipBounds().y + " "
// 			       + graphics.getClipBounds().width + " "
// 			       + graphics.getClipBounds().height);

	    Image image = createImage(width, height);
	    m_drawer.draw(image.getGraphics(), width, height, 
			  m_width, m_height, field);
	    graphics.drawImage(image, 0, 0, null);
	}

	public void actionPerformed(ActionEvent e) 
	{
	}

    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private int m_width;
    private int m_height;
    private int m_north, m_south, m_east, m_west;

    private HexColor m_toMove;
    private Field field[];

    private BoardDrawerBase m_drawer;
    private BoardPanel m_boardPanel;
}
