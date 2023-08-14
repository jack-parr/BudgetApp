import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

public class GraphPanel extends JPanel {

    final int MARGIN = 40;

    int graphWidth;
    int graphHeight;

    GraphPanel() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphWidth = getWidth() - 2*MARGIN;
        graphHeight = getHeight() - 2*MARGIN;

        g2d.setStroke(new BasicStroke(5));
        g2d.draw(new Line2D.Double(transformXCoord(0), transformYCoord(0), transformXCoord(0), transformYCoord(graphHeight)));
        g2d.draw(new Line2D.Double(transformXCoord(0), transformYCoord(0), transformXCoord(graphWidth), transformYCoord(0)));

    }

    public double transformXCoord(double xCoord) {
        return MARGIN + xCoord;
    }

    public double transformYCoord(double yCoord) {
        return MARGIN + (graphHeight - yCoord);
    }

}