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
    /** Shows the dialog. */
    public static Program show(Frame owner)
    {
        ChooseProgramDialog dialog 
            =  new ChooseProgramDialog(owner, "Choose program to connect");
        dialog.setVisible(true);
        Program program = dialog.getProgram();
        dialog.dispose();
        return program;
    }
}

//----------------------------------------------------------------------------
