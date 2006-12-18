//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.htp.HtpController;

import javax.swing.*;          
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/** Non-modal dialog displaying the communication between HexGui and a 
    HTP compatible program. */
public class HtpShell 
    extends JDialog implements HtpController.IOInterface
{
    public HtpShell(JFrame owner)
    {
	super(owner, "HexGui: Shell");
	m_editor = new JEditorPane();
	m_editor.setEditable(false);
	m_editor.setFont(new Font("monospaced", Font.PLAIN, 12));

	m_text = m_editor.getDocument();

	m_scrollpane = new JScrollPane(m_editor);
	m_scrollpane.setVerticalScrollBarPolicy(
		     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
// 	m_scrollpane.setHorizontalScrollBarPolicy(
// 		     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	Dimension size = owner.getSize();
	m_scrollpane.setPreferredSize(new Dimension(400, size.height));
	m_scrollpane.setMinimumSize(new Dimension(400, 200));
	setLocation(size.width, 0);

	getContentPane().add(m_scrollpane, BorderLayout.CENTER);
	pack();
	
	setVisible(true);
    }

    public void appendText(String text)
    {
	try {
	    m_text.insertString(m_text.getLength(), text, null);
	} 
	catch (BadLocationException e) {
	    System.out.println("Bad location!");
	}
    }

    /** HtpController.IOInterface */
    public void sentCommand(String str)
    {
	appendText(str);
    }

    public void receivedResponse(String str)
    {
	appendText(str);
    }
   
    JEditorPane m_editor;
    JScrollPane m_scrollpane;
    Document m_text;
}

