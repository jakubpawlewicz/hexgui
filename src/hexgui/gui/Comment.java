//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;

/** Displays comment for current node. */
public class Comment 
    extends JScrollPane
            //    implements DocumentListener
{
   
    public Comment()
    {
        m_textPane = new JTextPane();
        //setFocusTraversalKeys(m_textPane);
        //m_textPane.getDocument().addDocumentListener(this);
        m_textPane.setFont(MONOSPACED_FONT);
        setViewportView(m_textPane);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //setPreferredSize(new Dimension(200, 400));
    }

    public void setText(String text)
    {
    }

    JTextPane m_textPane;

    private static final Font MONOSPACED_FONT = Font.decode("Monospaced");
}

//----------------------------------------------------------------------------
