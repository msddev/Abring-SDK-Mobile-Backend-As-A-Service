package ir.abring.abringservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ir.abring.abringlibrary.abringclass.AbringUser;
import ir.abring.abringlibrary.interfaces.AbringCallBack;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AbringUser abringUser = new AbringUser
                .RegisterBuilder()
                .setUsername("SdkTest")
                .setPassword("123456")
                .build();

        abringUser.register(new AbringCallBack() {
            @Override
            public void onSuccessful(Object response) {

            }

            @Override
            public void onFailure(Object response) {

            }
        });
    }
}
