//----------------------------------------------------------------------------
// $Id$ 
//----------------------------------------------------------------------------

package hexgui.hex;

//----------------------------------------------------------------------------

/** Move.
    Contains a HexPoint and a HexColor.  To construct a swap moves or
    other special moves use the appropriate HexPoint.  Immutable.
*/
public final class Move
{
    /** Constructs a move with the given point and color.
	@param p location of move
	@param c black or white.
    */
    public Move(HexPoint p, HexColor c)
    {
	m_point = p;
	m_color = c;
    }

    /** Returns the point of this move. 
	@return HexPoint of the location.
    */
    public HexPoint getPoint()
    {
	return m_point;
    }

    /** Returns the color of the move.
	@return HexColor of the move (WHITE or BLACK).
    */
    public HexColor getColor()
    {
	return m_color;
    }

    private final HexPoint m_point;
    private final HexColor m_color;
}

//----------------------------------------------------------------------------