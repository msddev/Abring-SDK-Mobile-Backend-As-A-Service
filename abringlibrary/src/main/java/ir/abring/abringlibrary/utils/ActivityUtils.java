package ir.abring.abringlibrary.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ir.abring.abringlibrary.R;

public final class ActivityUtils {

    public static void addFragmentToActivity(FragmentManager fragmentManager,
                                             Fragment fragment,
                                             int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     *
     * @param fragmentManager
     * @param fragment
     * @param frameId
     * @param TARGET_FRAGMENT_TAG go to this fragment
     * @param SOURCE_FRAGMENT_TAG back to this fragment
     */
    public static void replaceFragmentToActivity(FragmentManager fragmentManager,
                                                 Fragment fragment,
                                                 int frameId,
                                                 String TARGET_FRAGMENT_TAG,
                                                 String SOURCE_FRAGMENT_TAG) {

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(frameId, fragment, TARGET_FRAGMENT_TAG);
        transaction.addToBackStack(SOURCE_FRAGMENT_TAG);
        transaction.commit();
    }

    public static void replaceFragmentToActivity(FragmentManager fragmentManager,
                                                 Fragment fragment,
                                                 int frameId) {

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
