package ir.abring.abringservices.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.abring.abringservices.R;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;

public abstract class BaseActivity extends ActivityManagePermission {

    protected Unbinder unbinder;
    protected MaterialDialog loadinDialog;
    protected Context mContext;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBeforeView();
        setContentView(getContentViewId());
        unbinder = ButterKnife.bind(this);
        mContext = this;
        initToolbar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();
    }

    protected abstract void initBeforeView();

    protected abstract int getContentViewId();

    protected abstract void initViews();

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    protected void setupBack(String title) {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void showLoadingDialog(String dialogText) {
        loadinDialog = new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_MatrialDialog))
                .contentGravity(GravityEnum.END)
                .content(dialogText)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }
}
