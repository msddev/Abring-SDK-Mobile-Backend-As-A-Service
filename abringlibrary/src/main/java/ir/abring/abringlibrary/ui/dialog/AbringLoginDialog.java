package ir.abring.abringlibrary.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.utils.AbringCheck;

public class AbringLoginDialog extends AbringBaseDialogFragment
        implements View.OnClickListener {

    private static AbringLoginDialog instance = null;
    private static OnFinishListener mListener;
    private ProgressBar progressBar;
    private EditText etUsername;
    private EditText etPassword;

    private Button btnOK;
    private Button btnCancel;

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

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnOK) {

            progressBar.setVisibility(View.VISIBLE);

            if (checkValidation()) {
                mListener.onFinishDialog(etUsername.getText().toString().trim(),
                        etPassword.getText().toString().trim());
            } else
                progressBar.setVisibility(View.GONE);

        } else if (i == R.id.btnCancel) {
            dismiss();
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
