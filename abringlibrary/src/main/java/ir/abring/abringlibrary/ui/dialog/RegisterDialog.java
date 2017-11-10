package ir.abring.abringlibrary.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.base.BaseDialogFragment;
import ir.abring.abringlibrary.utils.Check;
import ir.abring.abringlibrary.utils.CheckPattern;
import ir.abring.abringlibrary.utils.UIUtil;

public class RegisterDialog extends BaseDialogFragment
        implements View.OnClickListener {

    private static RegisterDialog instance = null;
    private static boolean name;
    private static boolean email;
    private static boolean phone;
    private static boolean avatar;
    private static OnFinishListener mListener;

    private TextInputLayout inputlayoutUsername;
    private TextInputLayout inputlayoutPassword;
    private TextInputLayout inputlayoutName;
    private TextInputLayout inputlayoutEmail;
    private TextInputLayout inputlayoutPhone;
    private TextInputLayout inputlayoutAvatar;

    private ProgressBar progressBar;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAvatar;
    private Button btnOK;
    private Button btnCancel;

    public RegisterDialog() {
    }

    public static synchronized RegisterDialog getInstance(boolean isName,
                                                          boolean isAvatar,
                                                          boolean isEmail,
                                                          boolean isPhone,
                                                          OnFinishListener onFinishListener) {
        name = isName;
        avatar = isAvatar;
        email = isEmail;
        phone = isPhone;
        mListener = onFinishListener;

        if (instance == null) {
            instance = new RegisterDialog();
        }
        return instance;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dialog_register;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected void initViews(View view) {
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etAvatar = (EditText) view.findViewById(R.id.etAvatar);

        inputlayoutUsername = (TextInputLayout) view.findViewById(R.id.inputlayoutUsername);
        inputlayoutPassword = (TextInputLayout) view.findViewById(R.id.inputlayoutPassword);
        inputlayoutName = (TextInputLayout) view.findViewById(R.id.inputlayoutName);
        inputlayoutEmail = (TextInputLayout) view.findViewById(R.id.inputlayoutEmail);
        inputlayoutPhone = (TextInputLayout) view.findViewById(R.id.inputlayoutPhone);
        inputlayoutAvatar = (TextInputLayout) view.findViewById(R.id.inputlayoutAvatar);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (!name) inputlayoutName.setVisibility(View.GONE);
        if (!email) inputlayoutEmail.setVisibility(View.GONE);
        if (!phone) inputlayoutPhone.setVisibility(View.GONE);
        if (!avatar) inputlayoutAvatar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnOK) {

            progressBar.setVisibility(View.VISIBLE);

            if (checkValidation()) {
                mListener.onFinishDialog(etUsername.getText().toString().trim(),
                        etPassword.getText().toString().trim(),
                        etName.getText().toString().trim(),
                        etPhone.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etAvatar.getText().toString().trim()
                );
            } else
                progressBar.setVisibility(View.GONE);


        } else if (i == R.id.btnCancel) {
            dismiss();
        }
    }

    private boolean checkValidation() {
        boolean isValid = true;

        if (Check.isEmpty(etUsername.getText().toString().trim())) {
            setupView(etUsername, "نام کاربری نامعتبر است!");
            isValid = false;
        } else if (Check.isEmpty(etPassword.getText().toString().trim())) {
            setupView(etPassword, "کلمه عبور نامعتبر است!");
            isValid = false;
        } else if (name && Check.isEmpty(etName.getText().toString().trim())) {
            setupView(etName, "نام و نام خانوادگی نامعتبر است!");
            isValid = false;
        } else if (phone) {
            if (etPhone.getText().toString().trim().length() != 11 ||
                    !CheckPattern.isValidPhone(etPhone.getText().toString().trim())) {
                setupView(etPhone, "شماره تلفن نامعتبر است!");
                isValid = false;
            }
        } else if (email) {
            if (Check.isEmpty(etEmail.getText().toString().trim()) ||
                    !CheckPattern.isValidEmail(etEmail.getText().toString().trim())) {
                setupView(etEmail, "آدرس ایمیل نامعتبر است!");
                isValid = false;
            }
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
        void onFinishDialog(String userName,
                            String password,
                            String name,
                            String phone,
                            String email,
                            String avatar);
    }
}
