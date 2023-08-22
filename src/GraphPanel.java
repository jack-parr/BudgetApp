/*
 * This is the panel that plots the graph shown in SummaryPanel.
 */

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

    Config config = new Config();

    final float MARGIN = 60;
    final float Y_TICK_MULTIPLE = 100;

    Graphics2D g2d;
    float graphWidth;
    float graphHeight;
    float graphDateStep;
    float graphZeroCoord;
    float graphPoundStep;
    LinkedHashSet<LocalDate> graphDates;
    ArrayList<Float> adjustedValuesList;
    float graphStartValue = 0;
    HashMap<LocalDate, Float> savingsMap;
    HashMap<LocalDate, Float> graphMap = new HashMap<>();

    LocalDate startDate;
    LocalDate endDate;

    GraphPanel(String startDate, String endDate) {

        this.setBackground(config.PANEL_BACKGROUND_COLOR);

        this.startDate = LocalDate.parse(startDate, config.DATE_TIME_FORMATTER);
        this.endDate = LocalDate.parse(endDate, config.DATE_TIME_FORMATTER);

        savingsMap = calculateCummulative();

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphWidth = getWidth() - 2*MARGIN;
        graphHeight = getHeight() - 2*MARGIN;

        // CALCULATE GRAPH PARAMETERS
        for (LocalDate date : savingsMap.keySet()) {
            if (date.isBefore(startDate)) {
                graphStartValue = savingsMap.get(date);
            }
            else {break;}  // breaks at first one that is not before startDate, since dates are in chronological order.
        }
        graphDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toCollection(LinkedHashSet::new));  // creates set of relevant dates for selectedPeriod.
        Set<LocalDate> datasetDates = savingsMap.keySet();  // creates a set of dates from the dataset.
        datasetDates.retainAll(graphDates);  // intersection.
        adjustedValuesList = new ArrayList<>();
        datasetDates.forEach(date -> adjustedValuesList.add(savingsMap.get(date) - graphStartValue));  // extracting all relevant values from savingsMap.

        float graphMaxValue = Math.max(Collections.max(adjustedValuesList), 0) + 10;  // maximum value on the graph.
        float graphMinValue = Math.min(Collections.min(adjustedValuesList), 0) - 20;  // minimum value on the graph.
        float graphRange = graphMaxValue - graphMinValue;  // absolute range of values on the graph.
        graphZeroCoord = (Math.abs(graphMinValue) / graphRange) * graphHeight;  // yCoord of zero line.
        graphPoundStep = graphHeight / graphRange;  // £1 in graph coord units.
        graphDateStep = graphWidth / (graphDates.size() - 1);  // 1 day in graph coord units. The minus 1 is because it plots the first point at xCoord = 0.

        // DRAW GRAPH SKELETON
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(config.PRIMARY_TEXT_COLOR);
        g2d.draw(new Line2D.Float(transformXCoord(0), transformYCoord(0), transformXCoord(0), transformYCoord(graphHeight)));  // y-axis.
        g2d.draw(new Line2D.Float(transformXCoord(0), transformYCoord(0), transformXCoord(graphWidth), transformYCoord(0)));  // x-axis.
        drawRotate(g2d, transformXCoord(-40), transformYCoord(graphHeight/3), -90, "Cummulative Savings (£)");
        
        // DRAW ZERO LINE
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.lightGray);
        g2d.draw(new Line2D.Float(transformXCoord(-28), transformYCoord(graphZeroCoord), transformXCoord(graphWidth), transformYCoord(graphZeroCoord)));
        g2d.drawString("0", transformXCoord((float) -15), transformYCoord(graphZeroCoord + 2));

        // DRAW VALUE GRID LINES
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(config.PRIMARY_TEXT_COLOR);
        float yTickStep = (float) ((Math.ceil((graphRange / 10) / Y_TICK_MULTIPLE)) * Y_TICK_MULTIPLE) * graphPoundStep;  // coord step for each y-tick.
        float yTickCoord = (float) (graphZeroCoord - (Math.floor(graphZeroCoord / yTickStep) * yTickStep));  // starting at lowest y-tick on graph.
        while (yTickCoord < graphHeight) {  // stops when highest y-tick is processed.

            if (yTickCoord != graphZeroCoord) {

                g2d.draw(new Line2D.Float(transformXCoord(-28), transformYCoord(yTickCoord), transformXCoord(graphWidth), transformYCoord(yTickCoord)));
                int yTickValue = Math.round((yTickCoord - graphZeroCoord) / graphPoundStep);
                g2d.drawString(Integer.toString(yTickValue), transformXCoord((float) (-15 + (-6.5 * Math.floor(Math.log10(Math.abs(yTickValue)))))), transformYCoord(yTickCoord + 2));  // moves x-coord based on number of digits so the labels right-align with the axis.
            
            }

            yTickCoord += yTickStep;

        }

        // DRAWING DATA
        float prevXCoord = 0;
        float prevYCoord = graphZeroCoord;  // starts at £0.
        float currXCoord = 0;
        float currYValue = 0;  // needs to be converted into a cartesian coord.
        float currYCoord;
        for (LocalDate date : graphDates) {

            if (date.isAfter(LocalDate.now())) {break;}  // stops drawing if it passes current date.

            Object extractedPoint = savingsMap.get(date);

            if (extractedPoint != null) {
                currYValue = (float) extractedPoint - graphStartValue;  // updates yValue from savingsMap, with the start value considered.
            }
            
            currYCoord = graphZeroCoord + (currYValue * graphPoundStep);  // convert value into cartesian Coord.
            graphMap.put(date, currYCoord);

            // DRAWING DAY TICKS
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.lightGray);
            g2d.draw(new Line2D.Float(transformXCoord(currXCoord), transformYCoord(graphHeight), transformXCoord(currXCoord), transformYCoord(-4)));

            // DRAWING WEEK TICKS
            if (date.getDayOfWeek().toString() == "MONDAY") {
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(config.SECONDARY_TEXT_COLOR);
                g2d.draw(new Line2D.Float(transformXCoord(currXCoord), transformYCoord(graphHeight), transformXCoord(currXCoord), transformYCoord(-8)));
            }

            // DRAWING NEW MONTH TICKS
            if (date.getDayOfMonth() == 1) {
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(config.PRIMARY_TEXT_COLOR);
                g2d.draw(new Line2D.Float(transformXCoord(currXCoord), transformYCoord((float) -0.9* MARGIN), transformXCoord(currXCoord), transformYCoord(graphHeight)));
                g2d.setColor(config.PRIMARY_TEXT_COLOR);
                drawRotate(g2d, transformXCoord(currXCoord + 2), transformYCoord(-10), 90, date.getMonth().toString().substring(0, 3) + " " + Integer.toString(date.getYear()).substring(2));
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

        g2d.dispose();

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