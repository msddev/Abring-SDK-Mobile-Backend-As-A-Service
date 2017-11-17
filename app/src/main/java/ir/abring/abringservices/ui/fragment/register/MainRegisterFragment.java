package ir.abring.abringservices.ui.fragment.register;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.user.AbringRegister;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.ActivityUtils;
import ir.abring.abringlibrary.utils.Check;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;

public class MainRegisterFragment extends BaseFragment implements View.OnClickListener {

    private static MainRegisterFragment mInstance = null;

    @BindView(R.id.btnUserUI)
    Button btnUserUI;
    @BindView(R.id.btnAbringUI)
    Button btnAbringUI;
    @BindView(R.id.tvResult)
    TextView tvResult;

    public MainRegisterFragment() {
    }

    public static synchronized MainRegisterFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainRegisterFragment();
        }
        return mInstance;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_register;
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

                RegisterFragment fragment = RegisterFragment.getInstance();

                ActivityUtils.replaceFragmentToActivity(getFragmentManager(),
                        fragment,
                        R.id.mainframe,
                        "RegisterFragment",
                        "MainRegisterFragment");
                break;

            case R.id.btnAbringUI:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        AbringRegister abringUser = new AbringRegister
                .DialogBuilder()
                .setName(true)
                .setPhone(true)
                .setAvatar(true)
                .build();

        abringUser.showDialog(mActivity.getSupportFragmentManager(), mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                AbringRegisterModel register = (AbringRegisterModel) response;
            }

            @Override
            public void onFailure(Object response) {
                AbringApiError apiError = (AbringApiError) response;
            }
        });
    }
}
