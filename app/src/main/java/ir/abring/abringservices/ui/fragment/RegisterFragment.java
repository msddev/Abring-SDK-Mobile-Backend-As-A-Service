package ir.abring.abringservices.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;

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

    Bitmap bitmap;

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
                bitmap = null;
                ImagePicker.pickImage(this, "Select your image:");
                break;
        }
    }

    private void saveAction() {
        AbringUserRegister abringUser = new AbringUserRegister
                .RegisterBuilder()
                .setUsername(etUsername.getText().toString())
                .setPassword(etPassword.getText().toString())
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
        bitmap = ImagePicker.getImageFromResult(mActivity, requestCode, resultCode, data);
        if (bitmap != null) {
            imgAvatar.setImageBitmap(bitmap);
        }
    }
}
