package ir.abring.abringservices.ui.fragment.logout;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.abringclass.user.AbringLogin;
import ir.abring.abringlibrary.abringclass.user.AbringLogout;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringActivityUtils;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;
import ir.abring.abringservices.ui.activity.MainActivity;
import ir.abring.abringservices.ui.fragment.login.LoginFragment;

public class MainLogoutFragment extends BaseFragment implements View.OnClickListener {

    private static MainLogoutFragment mInstance = null;

    @BindView(R.id.btnUserUI)
    Button btnUserUI;
    @BindView(R.id.btnAbringUI)
    Button btnAbringUI;
    @BindView(R.id.tvResult)
    TextView tvResult;

    public MainLogoutFragment() {
    }

    public static synchronized MainLogoutFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainLogoutFragment();
        }
        return mInstance;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_logout;
    }

    @Override
    protected void initViews(View rootView) {
        btnUserUI.setOnClickListener(this);
        btnAbringUI.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUserUI:
                logoutAction();
                break;

            case R.id.btnAbringUI:
                showDialog();
                break;
        }
    }

    private void logoutAction() {
        AbringLogout.logout(getActivity(), new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                Toast.makeText(getActivity(), "خروج کاربری با موفقیت انجام شد", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Object response) {
                AbringApiError apiError = (AbringApiError) response;
                Toast.makeText(getActivity(),
                        AbringCheck.isEmpty(apiError.getMessage()) ? getString(R.string.abring_failure_responce) : apiError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {

        AbringLogout.showDialog(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
            }

            @Override
            public void onFailure(Object response) {
            }
        });
    }
}
