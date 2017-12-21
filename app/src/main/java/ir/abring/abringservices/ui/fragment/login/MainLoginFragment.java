package ir.abring.abringservices.ui.fragment.login;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.user.AbringLogin;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringActivityUtils;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;

public class MainLoginFragment extends BaseFragment implements View.OnClickListener {

    private static MainLoginFragment mInstance = null;

    @BindView(R.id.btnUserUI)
    Button btnUserUI;
    @BindView(R.id.btnAbringUI)
    Button btnAbringUI;
    @BindView(R.id.tvResult)
    TextView tvResult;

    public MainLoginFragment() {
    }

    public static synchronized MainLoginFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainLoginFragment();
        }
        return mInstance;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_layout;
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

                LoginFragment fragment = new LoginFragment();

                AbringActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        fragment,
                        R.id.mainframe,
                        "LoginFragment",
                        "MainLoginFragment");
                break;

            case R.id.btnAbringUI:
                showDialog();
                break;
        }
    }

    private void showDialog() {

        AbringLogin.showDialog(mActivity.getSupportFragmentManager(), mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                AbringRegisterModel register = (AbringRegisterModel) response;
            }

            @Override
            public void onFailure(Object response) {
                AbringApiError apiError = (AbringApiError) response;
                Toast.makeText(mActivity,
                        AbringCheck.isEmpty(apiError.getMessage()) ? getString(R.string.abring_failure_responce) : apiError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
