//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.util;

import hexgui.hex.HexPoint;
import hexgui.hex.HexColor;
import hexgui.hex.VC;
import hexgui.util.Pair;

import java.io.StringReader;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Vector;

public final class StringUtils
{
    /** Converts all whitespace characters to a single ' '. */
    public static String cleanWhiteSpace(String str)
    {
        StringReader reader = new StringReader(str);
        StringBuilder ret = new StringBuilder();

        boolean white = false;
        while (true) {
            int c;
            try {
                c = reader.read();
            }
            catch (Throwable t) {
                System.out.println("Something bad happened!");
                break;
            }

            if (c == -1) break;
            if (c == ' ' || c == '\n' || c == '\t') {
                if (!white) ret.append(" ");
                white = true;
            } else {
                white = false;
                ret.append((char)c);
            }
        }
        return ret.toString();
    }

    public static Vector<HexPoint> parsePointList(String str)
    {
	Vector<HexPoint> ret = new Vector<HexPoint>();
        String cleaned = cleanWhiteSpace(str.trim());
	String[] pts = cleaned.split(" ");
	for (int i=0; i<pts.length; i++) {
	    HexPoint p = HexPoint.get(pts[i].trim());
	    ret.add(p);
	}
	return ret;
    }

    public static Vector<String> parseStringList(String str)
    {
	Vector<String> ret = new Vector<String>();
        String cleaned = cleanWhiteSpace(str.trim());
	String[] strs = cleaned.split(" ");
	for (int i=0; i<strs.length; i++) {
	    String cur = strs[i].trim();
	    ret.add(cur);
	}
	return ret;
    }

    public static Vector<Pair<String, String> > parseStringPairList(String str)
    {
	Vector<Pair<String, String> > ret = new Vector<Pair<String, String> >();
        String cleaned = cleanWhiteSpace(str.trim());
	String[] strs = cleaned.split(" ");
	for (int i=0; i<strs.length; i+=2) {
	    String c1 = strs[i].trim();
            String c2 = strs[i+1].trim();
	    ret.add(new Pair<String, String>(c1, c2));
	}
	return ret;
    }

    public static Vector<VC> parseVCList(String str)
    {
        Vector<VC> ret = new Vector<VC>();
        String cleaned = cleanWhiteSpace(str.trim());
	String[] vcs = cleaned.split(" ");

        for (int i=0, j=0; i<vcs.length; i+=j) {
            HexPoint from, to;
            HexColor color;
            int type;
            String carrier;
            Vector<HexPoint> key = new Vector<HexPoint>();
            String source;

            try {
                from = HexPoint.get(vcs[i+0]);
                to = HexPoint.get(vcs[i+1]);
                color = HexColor.get(vcs[i+2]);
                type = Integer.parseInt(vcs[i+3]);
                source = vcs[i+4];
                carrier = vcs[i+5];
                for (j=6; j<6+type; j++) {
                    HexPoint p = HexPoint.get(vcs[i+j]);
                    key.add(p);
                }

            }
            catch(Throwable t) {
                System.out.println("Exception occured while parsing VC!");
                return ret;                
            }

            ret.add(new VC(from, to, color, type, carrier, key, source));
        }
        return ret;
    }

    public static String reverse(String str)
    {
        StringBuilder ret = new StringBuilder();
        for (int i=str.length()-1; i>=0; i--) {
            ret.append(str.charAt(i));
        }
        return ret.toString();
    }
}

//----------------------------------------------------------------------------