package ir.abring.abringlibrary.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.abringclass.user.AbringLogin;
import ir.abring.abringlibrary.abringclass.user.AbringMobileRegister;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringCheckPattern;

public class AbringMobileRegisterDialog extends AbringBaseDialogFragment implements
        View.OnClickListener {

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
    private TextView tvResendActiveCode;
    private ImageView imgAvatar;

    private Button btnOK;
    private Button btnCancel;
    private Button btnGuest;

    private File file;

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

        return new AbringMobileRegisterDialog();
    }

    public static synchronized AbringMobileRegisterDialog getInstance(OnActiveFinishListener onFinishListener) {
        isActive = true;
        mActiveListener = onFinishListener;

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

        //close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnGuest = (Button) view.findViewById(R.id.btnGuest);
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
            tvResendActiveCode = (TextView) view.findViewById(R.id.tvResendActiveCode);
            linRegisterHolder.setVisibility(View.GONE);
            linActiveHolder.setVisibility(View.VISIBLE);

            tvResendActiveCode.setOnClickListener(this);
        }

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnGuest.setOnClickListener(this);
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
            ImagePicker.pickImage(this, getString(R.string.abring_select_image), 100, false);
        } else if (i == R.id.tvResendActiveCode) {
            progressBar.setVisibility(View.VISIBLE);
            AbringMobileRegister.mobileResendCode(new AbringCallBack<Object, Object>() {
                @Override
                public void onSuccessful(Object response) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.abring_send_new_code), Toast.LENGTH_SHORT).show();
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

    private boolean checkRegisterValidation() {
        boolean isValid = true;

        if (etMobile.getText().toString().trim().length() != 11 ||
                !AbringCheckPattern.isValidPhone(etMobile.getText().toString().trim())) {
            setupView(etMobile, getString(R.string.abring_mobile_not_valid));
            isValid = false;
        } else if (username) {
            if (!AbringCheck.isEmpty(etUsername.getText().toString().trim())) {
                if (etUsername.getText().toString().length() < 3) {
                    setupView(etUsername, getString(R.string.abring_username_not_valid));
                    isValid = false;
                }
            }
        } else if (password) {
            if (!AbringCheck.isEmpty(etPassword.getText().toString().trim())) {
                if (etPassword.getText().toString().length() < 3) {
                    setupView(etPassword, getString(R.string.abring_password_not_valid));
                    isValid = false;
                }
            }
        } else if (name) {
            if (!AbringCheck.isEmpty(etName.getText().toString().trim())) {
                if (etName.getText().toString().length() < 3) {
                    setupView(etName, getString(R.string.abring_name_not_valid));
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    private boolean checkActiveValidation() {
        boolean isValid = true;

        if (AbringCheck.isEmpty(etCode.getText().toString().trim())) {
            setupView(etCode, getString(R.string.abring_active_code_not_valid));
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
                Toast.makeText(getActivity(), getString(R.string.abring_image_not_selected), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.abring_image_selected_wrong), Toast.LENGTH_LONG).show();
        }
    }
}
