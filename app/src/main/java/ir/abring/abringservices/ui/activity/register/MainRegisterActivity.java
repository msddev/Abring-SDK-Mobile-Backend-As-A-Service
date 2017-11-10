package ir.abring.abringservices.ui.activity.register;

import android.app.FragmentManager;
import android.widget.Button;

import butterknife.BindView;
import ir.abring.abringlibrary.utils.ActivityUtils;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseActivity;
import ir.abring.abringservices.ui.fragment.MainRegisterFragment;

public class MainRegisterActivity extends BaseActivity {

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_register;
    }

    @Override
    protected void initViews() {
        setupBack("ثبت نام");

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                MainRegisterFragment.getInstance(),
                R.id.mainframe);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
