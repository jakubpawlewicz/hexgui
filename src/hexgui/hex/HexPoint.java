//----------------------------------------------------------------------------
// $Id: 
//----------------------------------------------------------------------------

package hexgui.hex;

//----------------------------------------------------------------------------

public final class HexPoint
{
    public static final HexPoint NORTH;
    public static final HexPoint SOUTH;
    public static final HexPoint WEST;
    public static final HexPoint EAST;


    public HexPoint()
    {
	x = y = 0;
    }

    public HexPoint(int x, int y)
    {
	this.x = x;
	this.y = y;
    }

    public static String toString(int x, int y)
    {
	if      (x ==    0 && y == -100) return "north";
	else if (x ==    0 && y ==  100) return "south";
	else if (x == -100 && y ==    0) return "west";
	else if (x ==  100 && y ==    0) return "east";

	char c = (char)((int)'a' + x);
	return "" + c + (y+1);
    }

    public String toString()
    {
	return toString(x,y);
    }

    static 
    {
	NORTH = new HexPoint(0, -100);
	SOUTH = new HexPoint(0,  100);
	WEST = new HexPoint(-100, 0);
	EAST = new HexPoint( 100, 0);
    }


    public int x;
    public int y;
}

//----------------------------------------------------------------------------