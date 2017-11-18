package ir.abring.abringservices.ui.activity.register;

import android.app.FragmentManager;

import ir.abring.abringlibrary.utils.AbringActivityUtils;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseActivity;
import ir.abring.abringservices.ui.fragment.login.MainLoginrFragment;
import ir.abring.abringservices.ui.fragment.logout.MainLogoutFragment;
import ir.abring.abringservices.ui.fragment.mobileRegister.MainMobileRegisterFragment;
import ir.abring.abringservices.ui.fragment.register.MainRegisterFragment;

public class MainRegisterActivity extends BaseActivity {

    private String actionName;

    @Override
    protected void initBeforeView() {
        if (getIntent().hasExtra("ActionName"))
            actionName = getIntent().getStringExtra("ActionName");

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_register;
    }

    @Override
    protected void initViews() {

        if (actionName.equals("Register")) {
            setupBack("ثبت نام");
            AbringActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    MainRegisterFragment.getInstance(),
                    R.id.mainframe);
        } else if (actionName.equals("MobileRegister")) {
            setupBack("ثبت نام با موبایل");
            AbringActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    MainMobileRegisterFragment.getInstance(),
                    R.id.mainframe);
        } else if (actionName.equals("Login")) {
            setupBack("ورود");
            AbringActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    MainLoginrFragment.getInstance(),
                    R.id.mainframe);
        } else if (actionName.equals("Logout")) {
            setupBack("خروج کاربری");
            AbringActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    MainLogoutFragment.getInstance(),
                    R.id.mainframe);
        }
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
