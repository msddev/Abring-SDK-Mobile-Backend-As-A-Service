package ir.abring.abringlibrary.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.user.AbringLogin;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringNetworkUtil;

public class AbringLoginDialog extends AbringBaseDialogFragment
        implements View.OnClickListener {

    private static AbringLoginDialog instance = null;
    private static OnFinishListener mListener;
    private ProgressBar progressBar;
    private EditText etUsername;
    private EditText etPassword;

    private Button btnOK;
    private Button btnCancel;
    private Button btnGuest;

    public AbringLoginDialog() {
    }

    public static synchronized AbringLoginDialog getInstance(OnFinishListener onFinishListener) {

        mListener = onFinishListener;

        if (instance == null) {
            instance = new AbringLoginDialog();
        }
        return instance;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dialog_login;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected void initViews(View view) {

        //close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etPassword = (EditText) view.findViewById(R.id.etPassword);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnGuest = (Button) view.findViewById(R.id.btnGuest);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnGuest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnOK) {

            if (AbringNetworkUtil.isNetworkConnected(getContext())) {

                progressBar.setVisibility(View.VISIBLE);

                if (checkValidation()) {
                    mListener.onFinishDialog(etUsername.getText().toString().trim(),
                            etPassword.getText().toString().trim());
                } else
                    progressBar.setVisibility(View.GONE);

            } else
                Toast.makeText(getActivity(), getString(R.string.abring_no_connect_to_internet), Toast.LENGTH_SHORT).show();

        } else if (i == R.id.btnCancel) {
            dismiss();
        } else if (i == R.id.btnGuest) {

            if (AbringNetworkUtil.isNetworkConnected(getContext())) {

                progressBar.setVisibility(View.VISIBLE);
                AbringLogin.loginAsGuest(getActivity(), new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), getString(R.string.abring_login_successfull), Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Object response) {
                        progressBar.setVisibility(View.GONE);
                        AbringApiError apiError = (AbringApiError) response;

                        Toast.makeText(getActivity(),
                                AbringCheck.isEmpty(apiError.getMessage()) ? getString(R.string.abring_failure_responce) :
                                        apiError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else
                Toast.makeText(getActivity(), getString(R.string.abring_no_connect_to_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidation() {
        boolean isValid = true;

        if (AbringCheck.isEmpty(etUsername.getText().toString().trim())) {
            setupView(etUsername, getString(R.string.abring_username_not_valid));
            isValid = false;
        } else if (AbringCheck.isEmpty(etPassword.getText().toString().trim())) {
            setupView(etPassword, getString(R.string.abring_password_not_valid));
            isValid = false;
        }

        return isValid;
    }

    private void setupView(final EditText view, String msg) {
        view.setError(msg);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    public interface OnFinishListener {
        void onFinishDialog(String userName, String password);
    }
}
