package ir.abring.abringlibrary.utils;

import android.util.Patterns;

public class AbringCheckPattern {

    public static boolean isValidEmail(CharSequence target) {
        if (AbringCheck.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPhone(CharSequence target) {
        if (AbringCheck.isEmpty(target)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }
}
