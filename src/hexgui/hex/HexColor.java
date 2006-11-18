//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------
package hexgui.hex;

public final class HexColor
{
    public static final HexColor EMPTY;
    public static final HexColor WHITE;
    public static final HexColor BLACK;

    public String toString()
    {
	return m_string;
    }

    public HexColor otherColor()
    {
	return m_otherColor;
    }

    private final String m_string;
    private HexColor m_otherColor;

    static 
    {
	BLACK = new HexColor("black");
	WHITE = new HexColor("white");
	EMPTY = new HexColor("empty");
	BLACK.setOtherColor(WHITE);
	WHITE.setOtherColor(BLACK);
	EMPTY.setOtherColor(EMPTY);
    }

    private HexColor(String string)
    {
	m_string = string;
    }
    private void setOtherColor(HexColor color)
    {
	m_otherColor = color;
    }
}
