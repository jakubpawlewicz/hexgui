//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import java.io.*;
import java.util.*;
import javax.swing.*;          
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;

//----------------------------------------------------------------------------

public class StatusBar
    extends JPanel
{
    public StatusBar()
    {
        super();
        setMinimumSize(new Dimension(0,25));
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        m_message = new JLabel();
        m_message.setHorizontalTextPosition(JLabel.LEFT);
        add(m_message);

        setMessage("Ready");
        setVisible(true);
    }

    public void setMessage(String msg)
    {
        m_message.setText(msg);
    }

    JLabel m_message;
}

//----------------------------------------------------------------------------
