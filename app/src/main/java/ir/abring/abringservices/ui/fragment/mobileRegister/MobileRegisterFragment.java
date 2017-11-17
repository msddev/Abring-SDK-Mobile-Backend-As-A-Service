package ir.abring.abringservices.ui.fragment.mobileRegister;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.abring.abringlibrary.abringclass.user.AbringMobileRegister;
import ir.abring.abringlibrary.abringclass.user.AbringRegister;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.Check;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MobileRegisterFragment extends BaseFragment implements View.OnClickListener {

    private static MobileRegisterFragment mInstance = null;


    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.inputlayoutMobile)
    TextInputLayout inputlayoutMobile;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.inputlayoutUsername)
    TextInputLayout inputlayoutUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.inputlayoutPassword)
    TextInputLayout inputlayoutPassword;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.inputlayoutName)
    TextInputLayout inputlayoutName;
    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    @BindView(R.id.linRegisterHolder)
    LinearLayout linRegisterHolder;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.inputlayoutCode)
    TextInputLayout inputlayoutCode;
    @BindView(R.id.linActiveHolder)
    LinearLayout linActiveHolder;
    Unbinder unbinder;

    private File file = null;
    private int REQUEST_EXTERNAL_STORAGE = 110;
    private boolean isActive = false;

    public MobileRegisterFragment() {
        // Required empty public constructor
    }

    public static synchronized MobileRegisterFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MobileRegisterFragment();
        }
        return mInstance;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mobile_register;
    }

    @Override
    protected void initViews(View rootView) {
        btnSave.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (!isActive) {

                    if (file != null) {
                        if (checkSelfPermission(mActivity,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                            mobileRegisterAction();
                        else
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_EXTERNAL_STORAGE);
                    } else
                        mobileRegisterAction();

                    break;
                } else {
                    verifyMobile();
                }

            case R.id.imgAvatar:
                file = null;
                ImagePicker.pickImage(this, "Select your image:", 100, false);
                break;
        }
    }

    private void mobileRegisterAction() {

        final AbringMobileRegister abringUser = new AbringMobileRegister
                .MobileRegisterBuilder()
                .setMobile(etMobile.getText().toString())
                .build();

        abringUser.register(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                //AbringRegisterModel register = (AbringRegisterModel) response;
                //Toast.makeText(mActivity, R.string.successful_responce, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "کد فعالسازی ارسال شد...", Toast.LENGTH_LONG).show();
                isActive = true;
            }

            @Override
            public void onFailure(Object response) {
                AbringApiError apiError = null;
                if (response instanceof AbringApiError)
                    apiError = (AbringApiError) response;
                Toast.makeText(mActivity,
                        Check.isEmpty(apiError.getMessage()) ? getString(R.string.failure_responce) : apiError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void verifyMobile() {
        AbringMobileRegister.mobileVerify(etCode.getText().toString(), new AbringCallBack<Object, Object>() {
            @Override
            public void onSuccessful(Object response) {
                Toast.makeText(getActivity(), "ثبت نام با موفقیت انجام شد", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Object response) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            // When an Image is picked
            if (requestCode == 100 && resultCode == Activity.RESULT_OK && null != data) {

                Bitmap bitmap = ImagePicker.getImageFromResult(mActivity, requestCode, resultCode, data);
                if (bitmap != null) {
                    imgAvatar.setImageBitmap(bitmap);
                }

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = mActivity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String mediaPath = cursor.getString(columnIndex);
                // Map is used to multipart the file using okhttp3.RequestBody
                file = new File(mediaPath);
                /*// Set the Image in ImageView for Previewing the Media
                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));*/
                cursor.close();

            } else {
                Toast.makeText(mActivity, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            mobileRegisterAction();
        }
    }
}
