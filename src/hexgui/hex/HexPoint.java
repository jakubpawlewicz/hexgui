//----------------------------------------------------------------------------
// $Id: 
//----------------------------------------------------------------------------
package hexgui.hex;

public final class HexPoint
{

    HexPoint()
    {
	x = y = 0;
    }

    HexPoint(int xp, int yp)
    {
	x = xp;
	y = yp;
    }

    public static String toString(int x, int y)
    {
	return "(" + x + ", " + y + ")";
    }

    public String toString()
    {
	return toString(x,y);
    }

    public int x;
    public int y;
}

//----------------------------------------------------------------------------