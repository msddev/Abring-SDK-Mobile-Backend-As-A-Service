package ir.abring.abringservices.ui.fragment.login;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.user.AbringLogin;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.inputlayoutUsername)
    TextInputLayout inputlayoutUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.inputlayoutPassword)
    TextInputLayout inputlayoutPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initViews(View rootView) {
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                login();
                break;
        }
    }

    private void login() {

        final AbringLogin abringUser = new AbringLogin
                .LoginBuilder()
                .setUsername(etUsername.getText().toString())
                .setPassword(etPassword.getText().toString())
                .build();

        abringUser.login(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                Toast.makeText(getActivity(), "ورود با موفقیت انجام شد", Toast.LENGTH_LONG).show();
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
