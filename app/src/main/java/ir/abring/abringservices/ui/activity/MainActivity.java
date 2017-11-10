package ir.abring.abringservices.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseActivity;
import ir.abring.abringservices.ui.activity.register.MainRegisterActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btnRegister)
    Button btnRegister;

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
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnRegister:
                intent = new Intent(this, MainRegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
