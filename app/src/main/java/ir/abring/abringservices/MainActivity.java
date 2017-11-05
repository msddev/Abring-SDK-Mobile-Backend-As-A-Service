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
                        Log.d("fdg", "onSuccessful: ");
                        Toast.makeText(MainActivity.this, "با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.d("fdg", "onSuccessful: ");
                        Toast.makeText(MainActivity.this, "رخداد خطا", Toast.LENGTH_LONG).show();
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
                        Log.d("dfds", "onSuccessful: ");
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.d("dfds", "onFailure: ");
                    }
                });
            }
        });
    }
}
