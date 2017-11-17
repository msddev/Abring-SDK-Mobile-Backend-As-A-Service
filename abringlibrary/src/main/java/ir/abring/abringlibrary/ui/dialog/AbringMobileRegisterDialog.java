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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mvc.imagepicker.ImagePicker;

import java.io.File;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.utils.Check;
import ir.abring.abringlibrary.utils.CheckPattern;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class AbringMobileRegisterDialog extends AbringBaseDialogFragment
        implements View.OnClickListener {

    private static AbringMobileRegisterDialog instance = null;
    private static boolean username;
    private static boolean password;
    private static boolean deviceId;
    private static boolean name;
    private static boolean avatar;
    private static OnRegisterFinishListener mRegisterListener;
    private static OnActiveFinishListener mActiveListener;

    private static boolean isActive = false;

    private TextInputLayout inputlayoutMobile;
    private TextInputLayout inputlayoutUsername;
    private TextInputLayout inputlayoutPassword;
    private TextInputLayout inputlayoutName;
    private TextInputLayout inputlayoutCode;

    private LinearLayout linRegisterHolder;
    private LinearLayout linActiveHolder;

    private ProgressBar progressBar;

    private EditText etMobile;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etName;
    private EditText etCode;
    private ImageView imgAvatar;
    private Button btnOK;
    private Button btnCancel;

    private File file;
    private int REQUEST_EXTERNAL_STORAGE = 110;

    public AbringMobileRegisterDialog() {
    }

    public static synchronized AbringMobileRegisterDialog getInstance(boolean isUserName,
                                                                      boolean isPassword,
                                                                      boolean isDeviceId,
                                                                      boolean isName,
                                                                      boolean isAvatar,
                                                                      OnRegisterFinishListener onFinishListener) {
        isActive = false;
        username = isUserName;
        password = isPassword;
        deviceId = isDeviceId;
        name = isName;
        avatar = isAvatar;
        mRegisterListener = onFinishListener;

//        if (instance == null) {
//            instance = new AbringMobileRegisterDialog();
//        }
        return new AbringMobileRegisterDialog();
    }

    public static synchronized AbringMobileRegisterDialog getInstance(OnActiveFinishListener onFinishListener) {
        isActive = true;
        mActiveListener = onFinishListener;

//        if (instance == null) {
//            instance = new AbringMobileRegisterDialog();
//        }
        return new AbringMobileRegisterDialog();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dialog_mobile_register;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected void initViews(View view) {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        linRegisterHolder = (LinearLayout) view.findViewById(R.id.linRegisterHolder);
        linActiveHolder = (LinearLayout) view.findViewById(R.id.linActiveHolder);

        if (!isActive) {
            etMobile = (EditText) view.findViewById(R.id.etMobile);
            etUsername = (EditText) view.findViewById(R.id.etUsername);
            etPassword = (EditText) view.findViewById(R.id.etPassword);
            etName = (EditText) view.findViewById(R.id.etName);
            imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
            imgAvatar.setOnClickListener(this);

            inputlayoutMobile = (TextInputLayout) view.findViewById(R.id.inputlayoutMobile);
            inputlayoutUsername = (TextInputLayout) view.findViewById(R.id.inputlayoutUsername);
            inputlayoutPassword = (TextInputLayout) view.findViewById(R.id.inputlayoutPassword);
            inputlayoutName = (TextInputLayout) view.findViewById(R.id.inputlayoutName);

            if (!username) inputlayoutUsername.setVisibility(View.GONE);
            if (!password) inputlayoutPassword.setVisibility(View.GONE);
            if (!name) inputlayoutName.setVisibility(View.GONE);
            if (!avatar) imgAvatar.setVisibility(View.GONE);
            linActiveHolder.setVisibility(View.GONE);

        } else {
            etCode = (EditText) view.findViewById(R.id.etCode);
            linRegisterHolder.setVisibility(View.GONE);
            linActiveHolder.setVisibility(View.VISIBLE);
        }

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnOK) {

            progressBar.setVisibility(View.VISIBLE);

            if (!isActive) {

                if (checkRegisterValidation()) {
                    mRegisterListener.onFinishDialog(etMobile.getText().toString().trim(),
                            etUsername.getText().toString().trim(),
                            etPassword.getText().toString().trim(),
                            "Generate device id later",
                            etName.getText().toString().trim(),
                            file
                    );
                } else
                    progressBar.setVisibility(View.GONE);

            } else {
                if (checkActiveValidation()) {
                    mActiveListener.onFinishDialog(etCode.getText().toString().trim());
                } else
                    progressBar.setVisibility(View.GONE);
            }

        } else if (i == R.id.btnCancel) {
            dismiss();
        } else if (i == R.id.imgAvatar) {
            file = null;
            ImagePicker.pickImage(this, "تصویر خود را انتخاب کنید:", 100, false);
        }
    }

    private boolean checkRegisterValidation() {
        boolean isValid = true;

        if (etMobile.getText().toString().trim().length() != 11 ||
                !CheckPattern.isValidPhone(etMobile.getText().toString().trim())) {
            setupView(etMobile, "شماره موبایل نامعتبر است!");
            isValid = false;
        } else if (Check.isEmpty(etUsername.getText().toString().trim())) {
            setupView(etUsername, "نام کاربری نامعتبر است!");
            isValid = false;
        } else if (Check.isEmpty(etPassword.getText().toString().trim())) {
            setupView(etPassword, "کلمه عبور نامعتبر است!");
            isValid = false;
        } else if (name && Check.isEmpty(etName.getText().toString().trim())) {
            setupView(etName, "نام و نام خانوادگی نامعتبر است!");
            isValid = false;
        }


        return isValid;
    }

    private boolean checkActiveValidation() {
        boolean isValid = true;

        if (Check.isEmpty(etCode.getText().toString().trim())) {
            setupView(etCode, "کد فعال سازی نامعتبر است!");
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

    public interface OnRegisterFinishListener {
        void onFinishDialog(String mobile,
                            String userName,
                            String password,
                            String deviceId,
                            String name,
                            File avatar);
    }

    public interface OnActiveFinishListener {
        void onFinishDialog(String code);
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
