package ir.abring.abringservices.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.AbringServices;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseActivity;
import ir.abring.abringservices.ui.activity.register.MainRegisterActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnMobileRegister)
    Button btnMobileRegister;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnLogout)
    Button btnLogout;

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        setupBack("سرویس ابرینگ");
        btnRegister.setOnClickListener(this);
        btnMobileRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnRegister:
                intent = new Intent(this, MainRegisterActivity.class);
                intent.putExtra("ActionName", "Register");
                startActivity(intent);
                break;
            case R.id.btnMobileRegister:
                intent = new Intent(this, MainRegisterActivity.class);
                intent.putExtra("ActionName", "MobileRegister");
                startActivity(intent);
                break;
            case R.id.btnLogin:
                intent = new Intent(this, MainRegisterActivity.class);
                intent.putExtra("ActionName", "Login");
                startActivity(intent);
                break;
            case R.id.btnLogout:
                intent = new Intent(this, MainRegisterActivity.class);
                intent.putExtra("ActionName", "Logout");
                startActivity(intent);
                break;
        }
    }
}
