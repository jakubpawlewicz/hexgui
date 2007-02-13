//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.hex;

import java.util.Vector;

//----------------------------------------------------------------------------

/** VC.
    A connection between two cells. 
*/
public class VC
{
    /** Constructs a VC. */
    public VC(HexPoint from, HexPoint to, HexColor c, int type)
    {
        this(from, to, c, type, "", new Vector<HexPoint>());
    }

    public VC(HexPoint from, HexPoint to, HexColor c, int type,
              String carrier, Vector<HexPoint> key)
    {
        m_from = from;
        m_to = to;
        m_color = c;
        m_type = type;
        m_carrier = carrier;
        m_key = key;
    }

    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        ret.append(m_from.toString());
        ret.append(" ");
        ret.append(m_to.toString());
        ret.append(" ");
        ret.append(m_color.toString());
        ret.append(" ");
        ret.append(Integer.toString(m_type));
        ret.append(" ");
        ret.append(m_carrier);
        for (int i=0; i<m_key.size(); i++) {
            ret.append(" ");
            ret.append(m_key.get(i).toString());
        }
        return ret.toString();
    }

    public HexPoint getFrom()  { return m_from; }
    public HexPoint getTo()    { return m_to; }
    public HexColor getColor() { return m_color; }
    public int getType() { return m_type; }
    public String getCarrier() { return m_carrier; }
    public Vector<HexPoint> getKey() { return m_key; }

    private HexPoint m_from;
    private HexPoint m_to;
    private HexColor m_color;
    private int m_type;
    private String m_carrier;
    private Vector<HexPoint> m_key;
}

//----------------------------------------------------------------------------