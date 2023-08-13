/*
 * This is the panel used to contain a DataEntry when it is displayed in ViewDataPanel or GeneratorsPanel.
 */

import java.awt.FlowLayout;

import javax.swing.JPanel;

public class RowPanel extends JPanel {

    // This uses FlowLayout to construct every row added to the subpanel.

    Config config = new Config();

    RowPanel() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

    }

}