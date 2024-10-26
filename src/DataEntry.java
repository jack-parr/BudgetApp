import java.time.LocalDate;

class DataEntry {
   String id;
   boolean isRecurring;
   boolean isExpense;
   String frequency;
   LocalDate startDate;
   LocalDate endDate;
   LocalDate nextDueDate;
   String category;
   float value;
   int sortCode;

   public DataEntry(String id, boolean isExpense, boolean isRecurring, String frequency, LocalDate startDate, LocalDate endDate, LocalDate nextDueDate, String category, float value) {
      this.id = id;
      this.isExpense = isExpense;
      this.isRecurring = isRecurring;
      this.frequency = frequency;
      this.startDate = startDate;
      this.endDate = endDate;
      this.nextDueDate = nextDueDate;
      this.category = category;
      this.value = value;
      this.sortCode = this.makeSortCode(startDate, category);
   }

   private int makeSortCode(LocalDate date, String category) {
      int day = date.getDayOfMonth();
      int firstLetter = Character.getNumericValue(category.charAt(0));
      String var10000 = Integer.toString(32 - day);
      int sortCode = Integer.parseInt(var10000 + Integer.toString(firstLetter));
      return sortCode;
   }

   protected String getId() {
      return this.id;
   }

   protected void setId(String id) {
      this.id = id;
   }

   protected boolean getIsExpense() {
      return this.isExpense;
   }

   protected void setIsExpense(boolean isExpense) {
      this.isExpense = isExpense;
   }

   protected boolean getIsRecurring() {
      return this.isRecurring;
   }

   protected void setIsRecurring(boolean isRecurring) {
      this.isRecurring = isRecurring;
   }

   protected String getFrequency() {
      return this.frequency;
   }

   protected void setFrequency(String frequency) {
      this.frequency = frequency;
   }

   protected LocalDate getStartDate() {
      return this.startDate;
   }

   protected void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
   }

   protected LocalDate getEndDate() {
      return this.endDate;
   }

   protected void setEndDate(LocalDate endDate) {
      this.endDate = endDate;
   }

   protected LocalDate getNextDueDate() {
      return this.nextDueDate;
   }

   protected void setNextDueDate(LocalDate nextDueDate) {
      this.nextDueDate = nextDueDate;
   }

   protected String getCategory() {
      return this.category;
   }

   protected void setCategory(String category) {
      this.category = category;
   }

   protected float getValue() {
      return this.value;
   }

   protected void setValue(float value) {
      this.value = value;
   }

   protected int getSortCode() {
      return this.sortCode;
   }

   protected void setSortCode(int sortCode) {
      this.sortCode = sortCode;
   }

   public String toString() {
      String var10000 = this.id;
      return "DataEntry [id=" + var10000 + ", isExpense=" + this.isExpense + ", isRecurring=" + this.isRecurring + ", frequency=" + this.frequency + ", startDate=" + String.valueOf(this.startDate) + ", endDate=" + String.valueOf(this.endDate) + ", nextDueDate=" + String.valueOf(this.nextDueDate) + ", category=" + this.category + ", value=" + this.value + ", sortCode=" + this.sortCode + "]";
   }
}
