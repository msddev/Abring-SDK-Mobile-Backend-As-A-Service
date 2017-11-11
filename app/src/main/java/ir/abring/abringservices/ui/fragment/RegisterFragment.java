package ir.abring.abringservices.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

import java.io.File;
import java.util.regex.Matcher;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.AbringUserRegister;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringregister.AbringRegister;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.Check;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    private static RegisterFragment mInstance = null;

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    @BindView(R.id.btnSave)
    Button btnSave;

    private File file = null;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static synchronized RegisterFragment getInstance() {
        if (mInstance == null) {
            mInstance = new RegisterFragment();
        }
        return mInstance;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_register;
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
                saveAction();
                break;

            case R.id.imgAvatar:
                file = null;
                ImagePicker.pickImage(this, "Select your image:", 100, false);
                break;
        }
    }

    private void saveAction() {

        AbringUserRegister abringUser = new AbringUserRegister
                .RegisterBuilder()
                .setUsername(etUsername.getText().toString())
                .setPassword(etPassword.getText().toString())
                .setAvatar(file)
                .build();

        abringUser.register(mActivity, new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {
                AbringRegister register = (AbringRegister) response;
                Toast.makeText(mActivity, R.string.successful_responce, Toast.LENGTH_SHORT).show();
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
}
