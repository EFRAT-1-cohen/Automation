package Pages;

public enum Menu {
    rights("מיצוי זכויות"),
    kitsAndHat("קצבאות והטבות"),
    paymentBituch("דמי ביטוח"),
    contact("יצירת קשר"),
  branches("סניפים"),
    payments("תשלומים"),
    service("שירות אישי");

    String mainMenuItem;

    Menu(String mainMenuItem) {
        this.mainMenuItem = mainMenuItem;
    }

    public String getMainMenuItem() {
        return mainMenuItem;
    }
}
