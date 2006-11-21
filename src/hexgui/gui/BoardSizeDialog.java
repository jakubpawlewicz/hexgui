//----------------------------------------------------------------------------
// $Id: HexGui.java 30 2006-10-27 05:09:12Z broderic $
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

/** Dialog for entering a board size. */
public final class BoardSizeDialog
{
    /** Run dialog.
        @return Board dimensions as string in format "w x h"; returns "-1 x -1" 
	if aborted. */
    public static String show(Component parent)
    {
        String ret, value = "11 x 11";
        ret = JOptionPane.showInputDialog(parent, "Board size", value);
        if (ret == null) 
	    return "-1 x -1";
        return ret;
    }

    /** Make constructor unavailable; class is for namespace only. */
    private BoardSizeDialog()
    {
    }
}

