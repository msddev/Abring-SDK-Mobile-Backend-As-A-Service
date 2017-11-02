package ir.abring.abringlibrary.utils;

public class ConvertDigits {

    public static String persianDigits(String string) {
        if (string != null) {
            string = string.replace("0", "۰");
            string = string.replace("1", "۱");
            string = string.replace("2", "۲");
            string = string.replace("3", "۳");
            string = string.replace("4", "۴");
            string = string.replace("5", "۵");
            string = string.replace("6", "۶");
            string = string.replace("7", "۷");
            string = string.replace("8", "۸");
            string = string.replace("9", "۹");
        }
        return string;
    }

    public static String latinDigits(String string) {
        if (string != null) {
            string = string.replace("۰", "0");
            string = string.replace("۱", "1");
            string = string.replace("۲", "2");
            string = string.replace("۳", "3");
            string = string.replace("۴", "4");
            string = string.replace("۵", "5");
            string = string.replace("۶", "6");
            string = string.replace("۷", "7");
            string = string.replace("۸", "8");
            string = string.replace("۹", "9");
        }
        return string;
    }
}
