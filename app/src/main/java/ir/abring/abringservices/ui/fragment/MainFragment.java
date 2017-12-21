package ir.abring.abringservices.ui.fragment;

import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import ir.abring.abringlibrary.utils.AbringActivityUtils;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;
import ir.abring.abringservices.ui.fragment.checkUpdate.MainCheckUpdateFragment;
import ir.abring.abringservices.ui.fragment.login.MainLoginFragment;
import ir.abring.abringservices.ui.fragment.logout.MainLogoutAllFragment;
import ir.abring.abringservices.ui.fragment.logout.MainLogoutFragment;
import ir.abring.abringservices.ui.fragment.mobileRegister.MainMobileRegisterFragment;
import ir.abring.abringservices.ui.fragment.register.MainRegisterFragment;

public class MainFragment  extends BaseFragment
        implements View.OnClickListener  {

    private static MainFragment mInstance = null;

    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnMobileRegister)
    Button btnMobileRegister;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnLogoutAll)
    Button btnLogoutAll;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;

    public static synchronized MainFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainFragment();
        }
        return mInstance;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews(View rootView) {
        btnRegister.setOnClickListener(this);
        btnMobileRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnLogoutAll.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnRegister:

                AbringActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        MainRegisterFragment.getInstance(),
                        R.id.mainframe,
                        "MainRegisterFragment",
                        "MainFragment");

                break;
            case R.id.btnMobileRegister:

                AbringActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        MainMobileRegisterFragment.getInstance(),
                        R.id.mainframe,
                        "MainMobileRegisterFragment",
                        "MainFragment");
                break;
            case R.id.btnLogin:

                AbringActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        MainLoginFragment.getInstance(),
                        R.id.mainframe,
                        "MainLoginFragment",
                        "MainFragment");
                break;
            case R.id.btnLogout:

                AbringActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        MainLogoutFragment.getInstance(),
                        R.id.mainframe,
                        "MainLogoutFragment",
                        "MainFragment");
                break;
            case R.id.btnLogoutAll:

                AbringActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        MainLogoutAllFragment.getInstance(),
                        R.id.mainframe,
                        "MainLogoutAllFragment",
                        "MainFragment");
                break;

            case R.id.btnUpdate:

                AbringActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        MainCheckUpdateFragment.getInstance(),
                        R.id.mainframe,
                        "MainLogoutAllFragment",
                        "MainFragment");
                break;
        }
    }
}
