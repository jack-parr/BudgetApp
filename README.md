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
- JSCROLLPANE FOR EXPENSE PANEL.
- Add extra properties to DataEntry indicating if it is In or Out. Either string or int.
- Add extra input on newExpensePanel to select between In or Out.
- Change expenseSubPanel2 display to differentiate between income and expense data.
- Change way the sum handles the different signage of data values.
- Rename panels to reflect that they are simply data panels now instead of just for expenses.
- Begin working on handling subscription type incomes and expenses. 
    - These should be input with frequencies and start dates. Modify DataEntry object to be flexible.