//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.htp.HtpController;

import javax.swing.*;          
import javax.swing.text.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

//----------------------------------------------------------------------------

/** Non-modal dialog displaying list of htp commands of the connected
    HTP compatible program. */
public class AnalyzeDialog 
    extends JDialog implements ActionListener, ListSelectionListener
{

    public interface Callback 
    {
	void analyzeCommand(String str);
    }

    public interface SelectionCallback
    {
        Vector<HexPoint> getSelectedCells();
    }


    public AnalyzeDialog(JFrame owner, 
                         Callback callback, 
                         SelectionCallback selection,
                         StatusBar statusbar)
    {
	super(owner, "HexGui: Analyze");

        m_callback = callback;
        m_selection = selection;
        m_statusbar = statusbar;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        m_list = new JList();
        m_list.addListSelectionListener(this);
        m_list.setDragEnabled(false);
        m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	m_scrollpane = new JScrollPane(m_list);
	m_scrollpane.setVerticalScrollBarPolicy(
		     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel runpanel = new JPanel();
        m_run = new JButton("Run");
        m_run.addActionListener(this);
        m_run.setActionCommand("run");
        
        m_color = new JComboBox(m_colors);
        m_color.setEditable(false);
        m_type = new JComboBox(m_types);
        m_type.setEditable(false);

        runpanel.add(m_run);
        runpanel.add(m_color);
        runpanel.add(m_type);

        panel.add(m_scrollpane);
        panel.add(runpanel);
        add(panel);

        pack();


	Dimension size = owner.getSize();
	setLocation(size.width, size.height);
        
        setVisible(true);
    }

    public void setCommands(Vector<String> commands)
    {
        m_commands = commands;
        m_list.setListData(commands);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        int index = m_list.getSelectedIndex();
    }

    public void actionPerformed(ActionEvent e)
    {
        String what = e.getActionCommand();
        if (what.equals("run")) {
            int index = m_list.getSelectedIndex();
            if (index == -1) return;

            Vector<HexPoint> selected = m_selection.getSelectedCells();

            StringBuilder cmd = new StringBuilder();
            String name = m_commands.get(index);

            cmd.append(name);
            
            // commands that need only a point
            if (name.equals("get_absorb_group")) {
                if (selected.size() < 1) {
                    m_statusbar.setMessage("Please select cell before running.");
                    return;
                }

                HexPoint p = selected.get(0);
                cmd.append(" " + p.toString());

            } 
            // commands that need only a color
            else if (name.equals("compute_dead_cells") ||
                     name.equals("find_sealed") ||
                     name.equals("strengthen_vcs") ||
                     name.equals("vc-build") ||
                     name.equals("vc-get-mustplay") ||
                     name.equals("eval_twod") ||
                     name.equals("eval_resist") ||
                     name.equals("eval_resist_delta")) {

                HexColor color = getSelectedColor();
                cmd.append(" " + color.toString());
                
            }  
            // commands that need a point, a color, and a vctype
            else if (name.equals("vc-connected-to")) {

                if (selected.size() < 1) {
                    m_statusbar.setMessage("Please select cell before running.");
                    return;
                }
                
                HexPoint p = selected.get(0);
                HexColor c = getSelectedColor();
                int t = getSelectedType();

                cmd.append(" " + p.toString());
                cmd.append(" " + c.toString());
                cmd.append(" " + t);

            } 
            // commands that need 2 points, a color, and a vc type
            else if (name.equals("vc-between-cells") ||
                     name.equals("vc-intersection") ||
                     name.equals("vc-union")) {
                
                if (selected.size() < 2) {
                    m_statusbar.setMessage("Please select at least two cells " + 
                                           "before running.");
                    return;
                }
                
                HexPoint p1 = selected.get(0);
                HexPoint p2 = selected.get(1);
                HexColor c = getSelectedColor();
                int t = getSelectedType();

                cmd.append(" " + p1.toString());
                cmd.append(" " + p2.toString());
                cmd.append(" " + c.toString());
                cmd.append(" " + t);
            } 

            cmd.append("\n");
            m_callback.analyzeCommand(cmd.toString());
        }
    }

    //------------------------------------------------------------

    private HexColor getSelectedColor()
    {
        if (m_colors[m_color.getSelectedIndex()].equals("white"))
            return HexColor.WHITE;
        return HexColor.BLACK;
    }

    private int getSelectedType()
    {
        return m_type.getSelectedIndex();
    }


    Callback m_callback;
    SelectionCallback m_selection;
    StatusBar m_statusbar;

    JList m_list;
    JScrollPane m_scrollpane;
    Vector<String> m_commands;
   
    JButton m_run;
    JComboBox m_color;
    JComboBox m_type;

    static final String m_colors[] = { "black", "white" };
    static final String m_types[] = {"full", "semi" };
}

//----------------------------------------------------------------------------
