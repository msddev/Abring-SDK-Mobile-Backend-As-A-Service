package ir.abring.abringlibrary.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.utils.Check;
import ir.abring.abringlibrary.utils.CheckPattern;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;

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

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);

        if (!name) inputlayoutName.setVisibility(View.GONE);
        if (!email) inputlayoutEmail.setVisibility(View.GONE);
        if (!phone) inputlayoutPhone.setVisibility(View.GONE);
        if (!avatar) imgAvatar.setVisibility(View.GONE);
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
                        file
                );
            } else
                progressBar.setVisibility(View.GONE);


        } else if (i == R.id.btnCancel) {
            dismiss();
        } else if (i == R.id.imgAvatar) {
            file = null;
            ImagePicker.pickImage(this, "Select your image:", 100, false);
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
        } else if (avatar) {
            if (file == null) {
                setupView(etEmail, "تصویر انتخاب نشده است!");
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
                Toast.makeText(getActivity(), "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
