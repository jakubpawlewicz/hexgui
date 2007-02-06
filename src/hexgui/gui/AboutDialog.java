//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.version.Version;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;          
import javax.swing.border.EtchedBorder;
import javax.swing.text.html.HTMLEditorKit;

//----------------------------------------------------------------------------

/** Shows info about HexGui. */
public class AboutDialog 
    extends JDialog implements ActionListener
{
    /** Display model about dialog. */
    public AboutDialog(Frame owner)
    {
        super(owner, true);
        setTitle("About HexGui");
        //setPreferredSize(new Dimension(400,300));
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel tp = new JPanel();
        tp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        String about = "<html><body>" 
            + "<h3>HexGui v" + Version.id +  "</h3>"
            + "(C)opyright 2007, Broderick Arneson.<br><br>" 
            + "build: " + Version.build + ", "
            + Version.date + "<br>"
            + "<p><b>HexGui</b> is full of Hexy Goodness!"
            + "<br><br>"
            + "</body></html>";

        JEditorPane text = new JEditorPane();
        text.setEditable(false);
        text.setEditorKit(new HTMLEditorKit());
        text.setText(about);
            
        tp.add(text);

        JButton button = new JButton("OK");
        button.setActionCommand("OK");
        button.addActionListener(this);

        panel.add(tp);
        panel.add(button);
        add(panel);

        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        setVisible(false);
    }
}

//----------------------------------------------------------------------------
