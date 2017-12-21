package ir.abring.abringservices.ui.fragment.checkUpdate;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.app.AbringCheckUpdate;
import ir.abring.abringlibrary.abringclass.user.AbringLogin;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringActivityUtils;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;
import ir.abring.abringservices.ui.fragment.login.LoginFragment;

public class MainCheckUpdateFragment extends BaseFragment implements View.OnClickListener {

    private static MainCheckUpdateFragment mInstance = null;

    @BindView(R.id.btnUserUI)
    Button btnUserUI;
    @BindView(R.id.btnAbringUI)
    Button btnAbringUI;
    @BindView(R.id.tvResult)
    TextView tvResult;

    private AbringCheckUpdateModel mUpdateApp;

    public MainCheckUpdateFragment() {
    }

    public static synchronized MainCheckUpdateFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainCheckUpdateFragment();
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

                AbringCheckUpdate.check(mActivity, new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {
                        mUpdateApp = (AbringCheckUpdateModel) response;
                        Toast.makeText(mActivity,
                                "عملیات با موفقیت انجام شد",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Object response) {
                        AbringApiError apiError = (AbringApiError) response;
                        Toast.makeText(mActivity,
                                AbringCheck.isEmpty(apiError.getMessage()) ? getString(R.string.abring_failure_responce) : apiError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.btnAbringUI:
                showDialog();
                break;
        }
    }

    private void showDialog() {

        AbringCheckUpdate.showDialog(mActivity.getSupportFragmentManager(), mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                mUpdateApp = (AbringCheckUpdateModel) response;
                Toast.makeText(mActivity,
                        "عملیات با موفقیت انجام شد",
                        Toast.LENGTH_SHORT).show();
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
