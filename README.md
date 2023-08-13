Budget and expenses tracking application.
Data is stored in variables 'dataList' and 'listsHashMap' within AppFrame.java.
AppFrame acts as the actionListener for all components. Uses actionCommands to identify what is being requested.
Make sure all action command checks happen AFTER source checks, and happen in decreasing string length order.

- Set and track savings goals.
- Manage income.
- Manage fixed expenses, eg rent bills subscriptions.
- Be able to input expenses by date and category.
- See an overview of total income vs expenses by month.
- Visualise data by month.

# TO DO:
- Format ViewDataPanel.
- Format AddNewDataPanel.
- Format GeneratorsPanel.

- Code SummaryPanel.
- Add deleting capability to AppFrame.checkRecurringEntryGenerators (this should only occur if the check for a due entry generation comes back false).