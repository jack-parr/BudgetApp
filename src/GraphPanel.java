/*
 * This is the panel that plots the graph shown in SummaryPanel.
 */

import javax.swing.JPanel;

import java.awt.BasicStroke;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphPanel extends JPanel {

    final int MARGIN = 40;

    int graphWidth;
    int graphHeight;
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

        // DRAW GRAPH SKELETON
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(new Line2D.Double(transformXCoord(0), transformYCoord(0), transformXCoord(0), transformYCoord(graphHeight)));
        g2d.draw(new Line2D.Double(transformXCoord(0), transformYCoord(0), transformXCoord(graphWidth), transformYCoord(0)));
        // draw ticks and grid based on number of plot points needed by selectedPeriod.
        // one plot point for each day of the selectedPeriod.

        // PLOT POINTS
        // calculate the difference between the min plot point needed and the max plot point needed.
        LocalDate startDate = LocalDate.now().minusMonths(6);
        LocalDate endDate = LocalDate.now();
        Set<LocalDate> setOfDates = startDate.datesUntil(endDate).collect(Collectors.toSet());  // creates set of relevant dates for selectedPeriod.

        Set<LocalDate> test = savingsMap.keySet();  // creates a set of dates that have data.
        test.retainAll(setOfDates);  // intersection.
        ArrayList<Float> valuesList = new ArrayList<>();
        test.forEach(date -> valuesList.add(savingsMap.get(date)));  // extracting all relevant values from savingsMap.
        //System.out.println(Collections.max(valuesList));
        //System.out.println(Collections.min(valuesList));

        // cycle over set of dates.
        // for each date, try to extract a new plot value. If this fails, plot the previous value again.

        
        // PLOT SAVINGS GOAL IF NEEDED

    }

    public double transformXCoord(double xCoord) {
        // Transforms a conventional xCoord into the value needed for the g2d.draw()
        return MARGIN + xCoord;
    }

    public double transformYCoord(double yCoord) {
        // Transforms a conventional yCoord into the value needed for the g2d.draw()
        return MARGIN + (graphHeight - yCoord);
    }

    public LinkedHashMap<LocalDate, Float> calculateCummulative() {

        // Calculates the cummulative savings..

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