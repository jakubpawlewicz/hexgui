//----------------------------------------------------------------------------
// $Id: 
//----------------------------------------------------------------------------
package hexgui.hex;

public final class Move
{
    public Move()
    {
	m_point = null;
    	m_color = null;
    }

    public Move(HexPoint p, HexColor c)
    {
	m_point = p;
	m_color = c;
    }

    public HexPoint getPoint()
    {
	return m_point;
    }

    public HexColor getColor()
    {
	return m_color;
    }

    public static String toString(HexPoint p, HexColor c)
    {
	return "[" + p.toString() + ", " + c.toString() + "]";
    }

    public String toString()
    {
	return toString(m_point, m_color);
    }

    private HexPoint m_point;
    private HexColor m_color;

}

//----------------------------------------------------------------------------