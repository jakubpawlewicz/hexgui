//----------------------------------------------------------------------------
// $Id$
//----------------------------------------------------------------------------

package hexgui.gui;

import hexgui.hex.*;
import hexgui.util.*;

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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYSplineRenderer;

import org.jfree.data.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;

//----------------------------------------------------------------------------

/** Non-modal dialog displaying graph of evaluation scores. */
public class EvalGraphDialog
    extends JDialog implements ActionListener
{
    public EvalGraphDialog(JFrame owner, 
                           Vector<Integer> movenum, 
                           Vector<Double> scores)
    {
	super(owner, "HexGui: Evaluation Graph");

	addWindowListener(new WindowAdapter() 
	    {
		public void windowClosing(WindowEvent winEvt) {
		    dispose();
		}
	    });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        XYSeries series1 = new XYSeries("Black");
        XYSeries series2 = new XYSeries("White");
        for (int i=0; i<scores.size(); ++i) {
            int move = movenum.get(i).intValue();
            double score = scores.get(i).doubleValue();
            if (score > 1.5) score = 1.5;
            if (score <-1.5) score = -1.5;
            if ((move % 2) == 0)
                series1.add(move, score);
            else
                series2.add(move, score);
        }
        XYSeriesCollection col = new XYSeriesCollection();
        col.addSeries(series1);
        col.addSeries(series2);
        
//         JFreeChart chart = ChartFactory.createXYLineChart
//             ("Evaluation Graph",  // Title
//              "Move",              // Y-Axis label
//              "Score",             // X-Axis label
//              col,           // Dataset
//              PlotOrientation.VERTICAL,
//              true ,               // Show legend
//              true,                // show tooltips
//              true                 // show urls
//              );

        JFreeChart chart = new JFreeChart(new XYPlot(col, 
                                                     new NumberAxis("Move"),
                                                     new NumberAxis("Score"),
                                                     new XYSplineRenderer()));

        BufferedImage image = chart.createBufferedImage(800,600);

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
