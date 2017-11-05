package ir.abring.abringlibrary.utils;

import android.util.Patterns;

public class CheckPattern {

    public static boolean isValidEmail(CharSequence target) {
        if (Check.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPhone(CharSequence target) {
        if (Check.isEmpty(target)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }
}
