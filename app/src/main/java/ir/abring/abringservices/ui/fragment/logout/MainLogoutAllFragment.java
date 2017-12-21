package ir.abring.abringservices.ui.fragment.logout;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.user.AbringLogout;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;

public class MainLogoutAllFragment extends BaseFragment implements View.OnClickListener {

    private static MainLogoutAllFragment mInstance = null;

    @BindView(R.id.btnUserUI)
    Button btnUserUI;
    @BindView(R.id.btnAbringUI)
    Button btnAbringUI;
    @BindView(R.id.tvResult)
    TextView tvResult;

    public MainLogoutAllFragment() {
    }

    public static synchronized MainLogoutAllFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainLogoutAllFragment();
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
                logoutAction();
                break;

            case R.id.btnAbringUI:
                showDialog();
                break;
        }
    }

    private void logoutAction() {
        AbringLogout.logoutAll(getActivity(), new AbringCallBack() {
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

        AbringLogout.showDialogLogoutAll(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
            }

            @Override
            public void onFailure(Object response) {
            }
        });
    }
}
