/*
 * This is the panel that plots the graph shown in SummaryPanel.
 */

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphPanel extends JPanel {

    final float MARGIN = 60;

    float graphWidth;
    float graphHeight;
    HashMap<LocalDate, Float> savingsMap;

    GraphPanel() {

        savingsMap = calculateCummulative();
        // for (Object date : savingsMap.keySet().toArray()) {
        //     System.out.println();
        //     System.out.println(date);
        //     System.out.println(savingsMap.get(date));
        // }

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphWidth = getWidth() - 2*MARGIN;
        graphHeight = getHeight() - 2*MARGIN;

        // CALCULATE GRAPH PARAMETERS
        LocalDate startDate = LocalDate.now().minusMonths(6);
        LocalDate endDate = LocalDate.now().plusDays(1);
        LinkedHashSet<LocalDate> graphDates = startDate.datesUntil(endDate).collect(Collectors.toCollection(LinkedHashSet::new));  // creates set of relevant dates for selectedPeriod.
        Set<LocalDate> datasetDates = savingsMap.keySet();  // creates a set of dates from the dataset.
        datasetDates.retainAll(graphDates);  // intersection.
        ArrayList<Float> valuesList = new ArrayList<>();
        datasetDates.forEach(date -> valuesList.add(savingsMap.get(date)));  // extracting all relevant values from savingsMap.

        float graphMaxValue = Math.max(Collections.max(valuesList), 0);  // maximum value on the graph.
        float graphMinValue = Math.min(Collections.min(valuesList), -100);  // minimum value on the graph.
        float graphRange = graphMaxValue - graphMinValue;  // absolute range of values on the graph.
        float graphZeroCoord = (Math.abs(graphMinValue) / graphRange) * graphHeight;  // yCoord of zero line.
        float graphPoundStep = graphHeight / graphRange;  // £1 in graph coord units.
        float graphDateStep = graphWidth / graphDates.size();  // 1 day in graph coord units.

        // DRAW GRAPH SKELETON
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(new Line2D.Float(transformXCoord(0), transformYCoord(0), transformXCoord(0), transformYCoord(graphHeight)));
        g2d.draw(new Line2D.Float(transformXCoord(0), transformYCoord(0), transformXCoord(graphWidth), transformYCoord(0)));
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.blue);
        g2d.draw(new Line2D.Float(transformXCoord(0), transformYCoord(graphZeroCoord), transformXCoord(graphWidth), transformYCoord(graphZeroCoord)));

        // DRAWING
        float prevXCoord = 0;
        float prevYCoord = graphZeroCoord;  // starts at £0.
        float currXCoord = 0;
        float currYValue = 0;  // needs to be converted into a cartesian coord.
        float currYCoord;
        for (LocalDate date : graphDates) {

            Object extractedPoint = savingsMap.get(date);

            if (extractedPoint != null) {
                currYValue = (float) extractedPoint;  // updates yValue from savingsMap..
            }
            
            currYCoord = graphZeroCoord + (currYValue * graphPoundStep);  // convert value into cartesian Coord.

            // DRAWING NEW MONTH LINES
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.gray);
            if (date.getDayOfMonth() == 1) {
                g2d.draw(new Line2D.Float(transformXCoord(currXCoord), transformYCoord((float) -0.9* MARGIN), transformXCoord(currXCoord), transformYCoord(graphHeight)));
                drawRotate(g2d, transformXCoord(currXCoord + 2), transformYCoord(-10), 90, date.getMonth().toString().substring(0, 3) + " " + Integer.toString(date.getYear()).substring(2));
            }

            // DRAWING WEEK TICKS
            g2d.setStroke(new BasicStroke(1));
            if (date.getDayOfWeek().toString() == "MONDAY") {
                g2d.draw(new Line2D.Float(transformXCoord(currXCoord), transformYCoord(4), transformXCoord(currXCoord), transformYCoord(-4)));
            }

            // DRAWING DATA LINE
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.red);
            g2d.draw(new Line2D.Float(transformXCoord(prevXCoord), transformYCoord(prevYCoord), transformXCoord(currXCoord), transformYCoord(currYCoord)));

            prevYCoord = currYCoord;
            prevXCoord = currXCoord;
            currXCoord += graphDateStep;

        }

        // PLOT SAVINGS GOAL IF NEEDED

    }

    public float transformXCoord(float xCoord) {
        // Transforms a conventional xCoord into the value needed for the g2d.draw()
        return MARGIN + xCoord;
    }

    public float transformYCoord(float yCoord) {
        // Transforms a conventional yCoord into the value needed for the g2d.draw()
        return MARGIN + (graphHeight - yCoord);
    }

    public static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) {

        g2d.translate((float) x, (float) y);  // translate coord system.
        g2d.rotate(Math.toRadians(angle));  // rotate coord system.
        g2d.drawString(text, 0, 0);  // draw string with new coord system.
        g2d.rotate(-Math.toRadians(angle));  // reset rotation.
        g2d.translate(-(float) x, -(float) y);  // reset translation.

    }  

    public LinkedHashMap<LocalDate, Float> calculateCummulative() {

        // Calculates the cummulative savings.

        ArrayList<DataEntry> dataList = AppFrame.dataList;
        Collections.sort(dataList, Comparator.comparing(de -> de.getStartDate()));

        LinkedHashMap<LocalDate, Float> cummulativeMap = new LinkedHashMap<>();

        float savingSum = 0;
        int sign;
        for (DataEntry de : dataList) {

            if (!de.getIsRecurring()) {

                sign = de.getIsExpense() ? -1 : 1;  // true = -1, false = 1.
                savingSum += sign * de.getValue();

                cummulativeMap.put(de.getStartDate(), savingSum);

            }

        }

        return cummulativeMap;

    }

}