//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import javax.swing.*;          
import java.awt.*;
import java.awt.event.*;

/** Dialog for entering a local program name. */
public final class LocalProgramDialog
{
    /** Run dialog.
        @return command to run.
	if user aborted.
    */
    public static String show(Component parent)
    {
	String value = "./htpwolve";
        String ret = JOptionPane.showInputDialog(parent, 
						 "Command to execute", 
						 value);
        return ret;
    }

    /** Make constructor unavailable; class is for namespace only. */
    private LocalProgramDialog()
    {
    }
}

