//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;

import javax.swing.*;          
import javax.swing.text.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.util.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

//----------------------------------------------------------------------------

/** Non-modal dialog displaying graph of evaluation scores. */
public class EvalGraphDialog
    extends JDialog implements ActionListener
{
    public EvalGraphDialog(JFrame owner)
    {
	super(owner, "HexGui: Evalualtion Graph");

	addWindowListener(new WindowAdapter() 
	    {
		public void windowClosing(WindowEvent winEvt) {
		    dispose();
		}
	    });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        XYSeries series = new XYSeries("Average Size");
        series.add(20.0, 10.0);
        series.add(40.0, 20.0);
        series.add(70.0, 50.0);
        XYDataset xyDataset = new XYSeriesCollection(series);
        
        JFreeChart chart = ChartFactory.createXYLineChart
            ("Sample XY Chart",  // Title
             "Height",           // X-Axis label
             "Weight",           // Y-Axis label
             xyDataset,          // Dataset
             PlotOrientation.HORIZONTAL,
             true ,               // Show legend
             true,                // show tooltips
             true                 // show urls
             );

        BufferedImage image = chart.createBufferedImage(500,300);

        JLabel lblChart = new JLabel();
        lblChart.setIcon(new ImageIcon(image));

        panel.add(lblChart);
        add(panel);

        pack();

	Dimension size = owner.getSize();
	setLocation(0, size.height);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
    }
}

//----------------------------------------------------------------------------
