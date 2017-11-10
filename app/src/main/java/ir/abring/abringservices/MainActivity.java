package ir.abring.abringservices;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.abring.abringlibrary.abringclass.AbringUserRegister;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.register.Register;
import ir.abring.abringlibrary.models.register.Result;
import ir.abring.abringlibrary.network.ApiError;
import ir.abring.abringlibrary.utils.Check;

public class MainActivity extends AppCompatActivity {

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
    @BindView(R.id.etAdatar)
    EditText etAdatar;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.openDialog)
    Button openDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Result result = (Result) AbringUserRegister.getUser();
        Log.d("USER", Check.isEmpty(result.getToken()) ? "null" : result.getToken());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbringUserRegister abringUser = new AbringUserRegister
                        .RegisterBuilder()
                        .setUsername(etUsername.getText().toString())
                        .setPassword(etPassword.getText().toString())
                        .build();

                abringUser.register(MainActivity.this, new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {
                        Register register = (Register) response;
                        Toast.makeText(MainActivity.this, R.string.successful_responce, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Object response) {
                        ApiError apiError = null;
                        if (response instanceof ApiError)
                            apiError = (ApiError) response;
                        Toast.makeText(MainActivity.this,
                                Check.isEmpty(apiError.getMessage()) ? getString(R.string.failure_responce) : apiError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AbringUserRegister abringUser = new AbringUserRegister
                        .DialogBuilder()
                        .setName(true)
                        .setPhone(true)
                        .build();

                abringUser.showDialog(getSupportFragmentManager(), MainActivity.this, new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {
                        Register register = (Register) response;
                        Toast.makeText(MainActivity.this, R.string.successful_responce, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Object response) {
                        ApiError apiError = (ApiError) response;
                        Toast.makeText(MainActivity.this,
                                Check.isEmpty(apiError.getMessage()) ? getString(R.string.failure_responce) : apiError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
