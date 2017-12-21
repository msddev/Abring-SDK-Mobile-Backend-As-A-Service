package ir.abring.abringservices.ui.activity;

import ir.abring.abringlibrary.utils.AbringActivityUtils;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseActivity;
import ir.abring.abringservices.ui.fragment.MainFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        setupBack("سرویس ابرینگ");

        AbringActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                MainFragment.getInstance(),
                R.id.mainframe);
    }
}
