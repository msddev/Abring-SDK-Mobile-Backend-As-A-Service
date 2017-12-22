package ir.abring.abringservices.ui.fragment.dynamicRequest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.abringclass.app.AbringCheckUpdate;
import ir.abring.abringlibrary.abringclass.app.AbringDynamicRequest;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;

public class MainDynamicRequestFragment extends BaseFragment implements View.OnClickListener {

    private static MainDynamicRequestFragment mInstance = null;

    @BindView(R.id.btnUserUI)
    Button btnUserUI;
    @BindView(R.id.btnAbringUI)
    Button btnAbringUI;
    @BindView(R.id.tvResult)
    TextView tvResult;

    private AbringCheckUpdateModel mUpdateApp;

    public MainDynamicRequestFragment() {
    }

    public static synchronized MainDynamicRequestFragment getInstance() {
        if (mInstance == null) {
            mInstance = new MainDynamicRequestFragment();
        }
        return mInstance;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_layout;
    }

    @Override
    protected void initViews(View rootView) {
        btnUserUI.setOnClickListener(this);
        btnAbringUI.setOnClickListener(this);
        btnAbringUI.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUserUI:

                //String URL = "http://ws.v3.abring.ir/index.php?r=app/check-update&app=ir.iranplays.tootak&variable=update";
                String URL = "app/check-update"; //send url after ?r= to first &

                Map<String, String> params = new HashMap<>();
                params.put("variable", "update");

                AbringDynamicRequest mAbring = new AbringDynamicRequest
                        .DynamicRequestBuilder()
                        .setUrl(URL)
                        .setParameters(params)
                        .build();

                mAbring.request(getActivity(), new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {

                        Toast.makeText(mActivity,
                                "عملیات با موفقیت انجام شد",
                                Toast.LENGTH_SHORT).show();

                        if (response != null) {
                            Gson gson = new Gson();
                            AbringCheckUpdateModel m = gson.fromJson(
                                    response.toString(),
                                    AbringCheckUpdateModel.class);
                        }
                    }

                    @Override
                    public void onFailure(Object response) {
                        AbringApiError apiError = (AbringApiError) response;
                        Toast.makeText(mActivity,
                                AbringCheck.isEmpty(apiError.getMessage()) ? getString(R.string.abring_failure_responce) : apiError.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
                break;

            case R.id.btnAbringUI:

                break;
        }
    }
}