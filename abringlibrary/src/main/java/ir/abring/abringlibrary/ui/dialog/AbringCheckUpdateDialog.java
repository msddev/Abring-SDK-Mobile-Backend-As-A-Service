package ir.abring.abringlibrary.ui.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.utils.AbringNetworkUtil;

public class AbringCheckUpdateDialog extends AbringBaseDialogFragment
        implements View.OnClickListener {

    private static AbringCheckUpdateDialog instance = null;
    private static OnFinishListener mListener;

    private Button btnOK;
    private Button btnCancel;

    public AbringCheckUpdateDialog() {
    }

    public static synchronized AbringCheckUpdateDialog getInstance(OnFinishListener onFinishListener) {
        mListener = onFinishListener;

        if (instance == null) {
            instance = new AbringCheckUpdateDialog();
        }
        return instance;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dialog_check_update;
    }

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected void initViews(View view) {
        btnOK = (Button) view.findViewById(R.id.btnOK);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.btnOK) {

            if (AbringNetworkUtil.isNetworkConnected(getContext()))
                checkUpdateAction();
            else
                Toast.makeText(getActivity(), getString(R.string.abring_no_connect_to_internet), Toast.LENGTH_SHORT).show();

        } else if (i == R.id.btnCancel) {
            dismiss();
        }
    }

    private void checkUpdateAction() {
        /*progressBar.setVisibility(View.VISIBLE);

        if (checkValidation()) {
            mListener.onFinishDialog(etUsername.getText().toString().trim(),
                    etPassword.getText().toString().trim(),
                    etName.getText().toString().trim(),
                    etPhone.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    file
            );
        } else
            progressBar.setVisibility(View.GONE);*/
    }

    public interface OnFinishListener {
        void onFinishDialog();

    }
}
