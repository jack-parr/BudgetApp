/*
 * Contains all the configuration variables.
 */

import java.awt.*;

public class Config {

    public final int DISPLAY_WIDTH = 600;
    public final int DISPLAY_HEIGHT = 600;
    public final int MENU_HEIGHT = 50;
    public final int PANEL_HEIGHT = DISPLAY_HEIGHT - MENU_HEIGHT;

    public final Color SUMMARY_COLOR = Color.gray;
    public final Color INCOME_COLOR = Color.blue;
    public final Color EXPENSES_COLOR = new Color(60, 60, 60);
    public final Color SAVINGS_COLOR = Color.red;
    public final Color CHART_COLOR = Color.orange;

    public final Color PRIMARY_TEXT_COLOR = new Color(235, 235, 235);
    public final Font PRIMARY_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 16);
    final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
    public final Font MENU_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);
    public final Font HEADING_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 24);

    public final String DATA_FILEPATH = "tempDataOut.csv";
    
}
