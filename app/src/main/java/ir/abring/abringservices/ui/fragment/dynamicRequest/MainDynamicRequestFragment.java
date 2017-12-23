package ir.abring.abringservices.ui.fragment.dynamicRequest;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import ir.abring.abringlibrary.abringclass.app.AbringDynamicRequest;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.models.abringapp.AbringCheckUpdateModel;
import ir.abring.abringlibrary.models.abringregister.AbringRegisterModel;
import ir.abring.abringlibrary.network.AbringApiError;
import ir.abring.abringlibrary.utils.AbringCheck;
import ir.abring.abringlibrary.utils.AbringUtils;
import ir.abring.abringservices.R;
import ir.abring.abringservices.base.BaseFragment;
import okhttp3.RequestBody;

public class MainDynamicRequestFragment extends BaseFragment implements View.OnClickListener {

    private static MainDynamicRequestFragment mInstance = null;

    @BindView(R.id.btnPOST)
    Button btnPOST;
    @BindView(R.id.btnGET)
    Button btnGET;
    @BindView(R.id.tvResult)
    TextView tvResult;

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
        return R.layout.fragment_main_layout2;
    }

    @Override
    protected void initViews(View rootView) {
        btnPOST.setOnClickListener(this);
        btnGET.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String URL;

        switch (view.getId()) {
            case R.id.btnPOST:

                //String URL = "http://ws.v3.abring.ir/index.php?r=app/check-update&app=ir.iranplays.tootak&variable=update";
                URL = "player/register"; //send url after ?r= to first &

                Map<String, RequestBody> params1 = new HashMap<>();
                params1.put("username", AbringUtils.toRequestBody("test1100"));
                params1.put("password", AbringUtils.toRequestBody("123456"));

                AbringDynamicRequest mAbring1 = new AbringDynamicRequest
                        .DynamicRequestPostBuilder()
                        .setUrl(URL)
                        .setParameters(params1)
                        .build();

                mAbring1.request(getActivity(), new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {

                        Toast.makeText(mActivity,
                                "عملیات با موفقیت انجام شد",
                                Toast.LENGTH_SHORT).show();

                        if (response != null) {

                            tvResult.setText(response.toString());

                            Gson gson = new Gson();
                            AbringRegisterModel m1 = gson.fromJson(
                                    response.toString(),
                                    AbringRegisterModel.class);
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

            case R.id.btnGET:

                //String URL = "http://ws.v3.abring.ir/index.php?r=app/check-update&app=ir.iranplays.tootak&variable=update";
                URL = "app/check-update"; //send url after ?r= to first &

                Map<String, String> params2 = new HashMap<>();
                params2.put("variable", "update");

                AbringDynamicRequest mAbring2 = new AbringDynamicRequest
                        .DynamicRequestGetBuilder()
                        .setUrl(URL)
                        .setParameters(params2)
                        .build();

                mAbring2.request(getActivity(), new AbringCallBack() {
                    @Override
                    public void onSuccessful(Object response) {

                        Toast.makeText(mActivity,
                                "عملیات با موفقیت انجام شد",
                                Toast.LENGTH_SHORT).show();

                        if (response != null) {

                            tvResult.setText(response.toString());

                            Gson gson = new Gson();
                            AbringCheckUpdateModel m2 = gson.fromJson(
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
        }
    }
}