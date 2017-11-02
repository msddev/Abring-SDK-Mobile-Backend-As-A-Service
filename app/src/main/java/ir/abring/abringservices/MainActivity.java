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
                .setUsername("test")
                .setPassword("123456")
                .build();

        abringUser.register(new AbringCallBack() {
            /**
             * Successful response
             **/
            @Override
            public void onSuccessful() {
                Toast.makeText(MainActivity.this, "تست موفق", Toast.LENGTH_LONG).show();
            }

            /**
             * Failure response
             **/
            @Override
            public void onFailure() {

            }
        });
    }
}
