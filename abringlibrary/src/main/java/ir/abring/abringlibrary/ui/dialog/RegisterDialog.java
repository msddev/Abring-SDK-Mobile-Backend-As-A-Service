package ir.abring.abringlibrary.ui.dialog;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.base.BaseDialogFragment;
import ir.abring.abringlibrary.utils.Check;
import ir.abring.abringlibrary.utils.CheckPattern;

public class RegisterDialog extends BaseDialogFragment
        implements View.OnClickListener {

    private static RegisterDialog instance = null;
    private static boolean name;
    private static boolean email;
    private static boolean phone;
    private static boolean avatar;
    private static OnFinishListener mListener;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAvatar;
    private Button btnOK;
    private Button btnCancel;

    private String mUsername;
    private String mPassword;
    private String mName;
    private String mEmail;
    private String mPhone;
    private String mAvatar;

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

        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (!name) etName.setVisibility(View.GONE);
        if (!email) etEmail.setVisibility(View.GONE);
        if (!phone) etPhone.setVisibility(View.GONE);
        if (!avatar) etAvatar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int i = view.getId();
        if (i == R.id.btnOK) {

            if (checkValidation()) {
                mListener.onFinishDialog(etUsername.getText().toString().trim(),
                        etPassword.getText().toString().trim(),
                        etName.getText().toString().trim(),
                        etPhone.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etAvatar.getText().toString().trim()
                        );
            }

        } else if (i == R.id.btnCancel) {
            dismiss();
        }
        startActivity(intent);
        dismiss();
    }

    private boolean checkValidation() {
        boolean isValid = true;

        if (Check.isEmpty(etUsername.getText().toString().trim())) {
            etUsername.setError("نام کاربری نامعتبر است!");
            etUsername.setFocusable(true);
            isValid = false;
        } else if (Check.isEmpty(etPassword.getText().toString().trim())) {
            etPassword.setError("کلمه عبور نامعتبر است!");
            etPassword.setFocusable(true);
            isValid = false;
        } else if (name && Check.isEmpty(etName.getText().toString().trim())) {
            etName.setError("نام و نام خانوادگی نامعتبر است!");
            etName.setFocusable(true);
            isValid = false;
        } else if (phone) {
            if (!CheckPattern.isValidPhone(etPhone.getText().toString().trim())) {
                etPhone.setError("شماره تلفن نامعتبر است!");
                etPhone.setFocusable(true);
                isValid = false;
            }
        } else if (email) {
            if (!CheckPattern.isValidEmail(etEmail.getText().toString().trim())) {
                etEmail.setError("آدرس ایمیل نامعتبر است!");
                etEmail.setFocusable(true);
                isValid = false;
            }
        }

        return isValid;
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
