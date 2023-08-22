/*
 * This is the subpanel that displays the data in the GeneratorsPanel.
 * The panel uses a GridBagLayout, adding every new component vertically down.
 * The added components are JPanel with FlowLayout for each row.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GeneratorsPanel extends JPanel {

    Config config = new Config();

    HashMap<String, Component> deleteButtonsMap = new HashMap<>();

    GeneratorsPanel(boolean displayExpense) {

        this.setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.gridx = 0;  // makes it so every added panel is added vertically.
        this.setBackground(config.PANEL_BACKGROUND_COLOR);


        // COLUMNS ROW
        RowPanel rowPanelColumns = new RowPanel();
        rowPanelColumns.setBackground(config.PANEL_BACKGROUND_COLOR);

        ArrayList<Component> headerRowComponents = config.makeDataHeaderRowComponents();  // makes a list containing all the components.

        Component headerCategoryLabel = headerRowComponents.get(1);
        headerCategoryLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));
        rowPanelColumns.add(headerCategoryLabel);

        Component headerValueLabel = headerRowComponents.get(2);
        headerValueLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));
        rowPanelColumns.add(headerValueLabel);

        Component headerFrequencyLabel = headerRowComponents.get(3);
        headerFrequencyLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));
        rowPanelColumns.add(headerFrequencyLabel);

        Component headerTerminatesLabel = headerRowComponents.get(4);
        headerTerminatesLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));
        rowPanelColumns.add(headerTerminatesLabel);

        Component headerDeleteLabel = headerRowComponents.get(5);
        rowPanelColumns.add(headerDeleteLabel);

        this.add(rowPanelColumns, layoutConstraints);


        // DATA ROWS
        ArrayList<DataEntry> dataList = AppFrame.dataList;
        Collections.sort(dataList, Comparator.comparing(DataEntry::getCategory));  // sorting alphabetically by category.

        boolean noDataFound = true;
        for (DataEntry dataEntry : dataList) {

            if (dataEntry.isRecurring && dataEntry.isExpense == displayExpense) {

                noDataFound = false;

                JLabel spacerLabel = config.makeDataSpacerLabel();
                spacerLabel.setPreferredSize(new Dimension(config.SCROLLABLE_DISPLAY_WIDTH, 1));

                RowPanel rowPanelDataEntry = new RowPanel();
                rowPanelDataEntry.setBackground(config.PANEL_BACKGROUND_COLOR);
                ArrayList<Component> dataRowComponents = config.makeDataDisplayRowComponents(dataEntry);

                Component dataCategoryLabel = dataRowComponents.get(1);
                dataCategoryLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));

                Component dataValueLabel = dataRowComponents.get(2);
                dataValueLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));

                Component dataFrequencyLabel = dataRowComponents.get(3);
                dataFrequencyLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));

                Component dataTerminatesLabel = dataRowComponents.get(4);
                dataTerminatesLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 4, config.DATA_ROW_HEIGHT));

                rowPanelDataEntry.add(dataCategoryLabel);
                rowPanelDataEntry.add(dataValueLabel);
                rowPanelDataEntry.add(dataFrequencyLabel);
                rowPanelDataEntry.add(dataTerminatesLabel);

                // CREATE AND PAINT DELETE BUTTON
                JButton deleteDataEntryButton = new JButton("X");
                deleteDataEntryButton.setMargin(new Insets(0, 0, 0, 0));
                deleteDataEntryButton.setFont(config.PRIMARY_FONT);
                deleteDataEntryButton.setForeground(Color.RED);
                deleteDataEntryButton.setFocusable(false);
                deleteDataEntryButton.setBorderPainted(false);
                deleteDataEntryButton.setBackground(config.PANEL_BACKGROUND_COLOR);
                deleteDataEntryButton.setName("deleteButton" + dataEntry.getId());
                deleteDataEntryButton.setActionCommand("delete" + dataEntry.getId());
                deleteDataEntryButton.setPreferredSize(new Dimension(config.DELETE_BUTTON_WIDTH, config.DATA_ROW_HEIGHT));
                rowPanelDataEntry.add(deleteDataEntryButton);

                // ADD DELETE BUTTON TO HASHMAP
                deleteButtonsMap.put(deleteDataEntryButton.getName(), deleteDataEntryButton);

                // ADD ROWPANEL TO SUBPANEL
                this.add(spacerLabel, layoutConstraints);
                this.add(rowPanelDataEntry, layoutConstraints);

            }

        }

        // HANDLING NO DATA
        if (noDataFound) {
            JLabel nullLabel = new JLabel("No Data", SwingConstants.CENTER);
            nullLabel.setFont(config.PRIMARY_FONT);
            nullLabel.setForeground(config.SECONDARY_TEXT_COLOR);       
            nullLabel.setPreferredSize(new Dimension(config.SCROLLABLE_DISPLAY_WIDTH, config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP) - config.DATA_ROW_HEIGHT));
            this.add(nullLabel, layoutConstraints);
        }

        // ADD PANEL FILLER TO ALIGN TO TOP OF JSCROLLPANE WHEN NOT OVERFLOWING
        int remainingVerticalSpace = (config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)) - (this.getPreferredSize().height);
        if (remainingVerticalSpace > 0) {
            JPanel panelFillerLabel = new JPanel();
            panelFillerLabel.setBackground(config.VIEW_GENERATORS_PANEL_COLOR);
            panelFillerLabel.setPreferredSize(new Dimension(config.SCROLLABLE_DISPLAY_WIDTH, remainingVerticalSpace));
            this.add(panelFillerLabel, layoutConstraints);
        }

    }

}