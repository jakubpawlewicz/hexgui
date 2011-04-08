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

    public interface ColorToMoveCallback
    {
        HexColor getColorToMove();
    }

    public AnalyzeDialog(JFrame owner,
                         Callback callback,
                         SelectionCallback selection,
                         ColorToMoveCallback colorToMove,
                         StatusBar statusbar)
    {
	super(owner, "HexGui: Analyze");

        m_callback = callback;
        m_selection = selection;
        m_colorToMove = colorToMove;
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

        runpanel.add(m_run);

        panel.add(m_scrollpane);
        panel.add(runpanel);
        add(panel);

        pack();

	Dimension size = owner.getSize();
	setLocation(size.width, size.height);
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
        if (what.equals("run"))
        {
            int index = m_list.getSelectedIndex();
            if (index == -1) return;

            Vector<HexPoint> selected = m_selection.getSelectedCells();

            StringBuilder cmd = new StringBuilder();
            String name = m_commands.get(index);

            cmd.append(name);

            // commands that need only a point
            if (name.equals("group-get"))
            {
                if (selected.size() < 1)
                {
                    m_statusbar.setMessage("Please select a cell before " +
                                           "running.");
                    return;
                }
                HexPoint p = selected.get(0);
                cmd.append(" " + p.toString());
            }
            // commands that need AT LEAST one point
            else if (name.equals("encode-pattern"))
            {
                if (selected.size() < 1)
                {
                    m_statusbar.setMessage("Please select at least one cell " +
                                           "before running.");
                    return;
                }

                int i = 0;
                while (i < selected.size())
                {
                    HexPoint p = selected.get(i++);
                    cmd.append(" " + p.toString());
                }
            }
            // commands that need only a color
            else if (name.equals("compute-inferior") ||
		     name.equals("compute-fillin") ||
		     name.equals("compute-vulnerable") ||
		     name.equals("compute-reversible") ||
		     name.equals("compute-dominated") ||
                     name.equals("dfpn-get-bounds") ||
                     name.equals("dfpn-get-work") ||
                     name.equals("dfpn-solve-state") ||
                     name.equals("dfpn-solver-find-winning") ||
                     name.equals("dfs-solve-state") ||
                     name.equals("dfs-solver-find-winning") ||
                     name.equals("find-comb-decomp") || 
                     name.equals("find-split-decomp") || 
                     name.equals("vc-build") ||
                     name.equals("vc-get-mustplay") ||
		     name.equals("vc-maintain") ||
                     name.equals("eval-resist") ||
                     name.equals("eval-twod") ||
                     name.equals("shortest-paths") ||
                     name.equals("shortest-vc-paths"))
            {
                HexColor color = getSelectedColor();
                cmd.append(" " + color.toString());
            }
            // commands that need a point and a color
            else if (name.equals("vc-connected-to-full") ||
                     name.equals("vc-connected-to-semi"))
            {
                if (selected.size() < 1)
                {
                    m_statusbar.setMessage("Please select a cell before " +
                                           "running.");
                    return;
                }
                HexPoint p = selected.get(0);
                HexColor c = getSelectedColor();
                cmd.append(" " + p.toString());
                cmd.append(" " + c.toString());
            }
            // commands that need 2 points and a color
            else if (name.equals("vc-between-cells-full") ||
                     name.equals("vc-between-cells-semi") ||
                     name.equals("vc-intersection-full") ||
                     name.equals("vc-intersection-semi") ||
                     name.equals("vc-union-full") ||
                     name.equals("vc-union-semi"))
            {
                if (selected.size() < 2)
                {
                    m_statusbar.setMessage("Please select at least two " +
                                           "cells before running.");
                    return;
                }
                HexPoint p1 = selected.get(0);
                HexPoint p2 = selected.get(1);
                HexColor c = getSelectedColor();
                cmd.append(" " + p1.toString());
                cmd.append(" " + p2.toString());
                cmd.append(" " + c.toString());
            }
            cmd.append("\n");
            m_callback.analyzeCommand(cmd.toString());
        }
    }

    //------------------------------------------------------------

    private HexColor getSelectedColor()
    {
        return m_colorToMove.getColorToMove();
    }

    Callback m_callback;
    SelectionCallback m_selection;
    ColorToMoveCallback m_colorToMove;
    StatusBar m_statusbar;

    JList m_list;
    JScrollPane m_scrollpane;
    Vector<String> m_commands;

    JButton m_run;
}

//----------------------------------------------------------------------------
