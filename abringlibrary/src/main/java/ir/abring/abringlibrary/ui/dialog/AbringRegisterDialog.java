package ir.abring.abringlibrary.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mvc.imagepicker.ImagePicker;
import java.io.File;
import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.user.AbringLogin;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringCheckPattern;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class AbringRegisterDialog extends AbringBaseDialogFragment
        implements View.OnClickListener {

    private static AbringRegisterDialog instance = null;
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

    private ProgressBar progressBar;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private ImageView imgAvatar;
    private Button btnOK;
    private Button btnCancel;
    private Button btnGuest;

    private File file;

    public AbringRegisterDialog() {
    }

    public static synchronized AbringRegisterDialog getInstance(boolean isName,
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
            instance = new AbringRegisterDialog();
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

        //close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);

        inputlayoutUsername = (TextInputLayout) view.findViewById(R.id.inputlayoutUsername);
        inputlayoutPassword = (TextInputLayout) view.findViewById(R.id.inputlayoutPassword);
        inputlayoutName = (TextInputLayout) view.findViewById(R.id.inputlayoutName);
        inputlayoutEmail = (TextInputLayout) view.findViewById(R.id.inputlayoutEmail);
        inputlayoutPhone = (TextInputLayout) view.findViewById(R.id.inputlayoutPhone);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnGuest = (Button) view.findViewById(R.id.btnGuest);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        btnGuest.setOnClickListener(this);

        if (!name) inputlayoutName.setVisibility(View.GONE);
        if (!email) inputlayoutEmail.setVisibility(View.GONE);
        if (!phone) inputlayoutPhone.setVisibility(View.GONE);
        if (!avatar) imgAvatar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnOK) {
            registerAction();
        } else if (i == R.id.btnCancel) {
            dismiss();
        } else if (i == R.id.imgAvatar) {
            file = null;
            ImagePicker.pickImage(this, getString(R.string.abring_select_image), 100, false);
        } else if (i == R.id.btnGuest) {
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
        }
    }

    private void registerAction() {
        progressBar.setVisibility(View.VISIBLE);

        if (checkValidation()) {
            mListener.onFinishDialog(etUsername.getText().toString().trim(),
                    etPassword.getText().toString().trim(),
                    etName.getText().toString().trim(),
                    etPhone.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    file
            );
        } else
            progressBar.setVisibility(View.GONE);
    }

    private boolean checkValidation() {
        boolean isValid = true;

        if (AbringCheck.isEmpty(etUsername.getText().toString().trim())) {
            setupView(etUsername, getString(R.string.abring_username_not_valid));
            isValid = false;
        } else if (AbringCheck.isEmpty(etPassword.getText().toString().trim())) {
            setupView(etPassword, getString(R.string.abring_password_not_valid));
            isValid = false;
        } else if (name && AbringCheck.isEmpty(etName.getText().toString().trim())) {
            setupView(etName, getString(R.string.abring_name_not_valid));
            isValid = false;
        } else if (phone) {
            if (etPhone.getText().toString().trim().length() != 11 ||
                    !AbringCheckPattern.isValidPhone(etPhone.getText().toString().trim())) {
                setupView(etPhone, getString(R.string.abring_phone_not_valid));
                isValid = false;
            }
        } else if (email) {
            if (AbringCheck.isEmpty(etEmail.getText().toString().trim()) ||
                    !AbringCheckPattern.isValidEmail(etEmail.getText().toString().trim())) {
                setupView(etEmail, getString(R.string.abring_email_not_valid));
                isValid = false;
            }
        } /*else if (avatar) {
            if (file == null) {
                setupView(etEmail, "تصویر انتخاب نشده است!");
                isValid = false;
            }
        }*/

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
                            File avatar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            // When an Image is picked
            if (requestCode == 100 && resultCode == Activity.RESULT_OK && null != data) {

                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);
                if (bitmap != null) {
                    imgAvatar.setImageBitmap(bitmap);
                }

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn,
                        null,
                        null,
                        null);

                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String mediaPath = cursor.getString(columnIndex);
                file = new File(mediaPath);
                /*// Set the Image in ImageView for Previewing the Media
                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));*/
                cursor.close();

            } else {
                Toast.makeText(getActivity(), getString(R.string.abring_image_not_selected), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.abring_image_selected_wrong), Toast.LENGTH_LONG).show();
        }
    }
}
