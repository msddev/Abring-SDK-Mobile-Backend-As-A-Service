package ir.abring.abringlibrary.ui.dialog;

import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;
import com.tonyodev.fetch.request.RequestInfo;

import java.io.File;
import java.util.List;

import ir.abring.abringlibrary.R;
import ir.abring.abringlibrary.base.AbringBaseDialogFragment;
import ir.abring.abringlibrary.interfaces.AbringCallBack;
import ir.abring.abringlibrary.utils.AbringFileUtil;

public class AbringCheckUpdateDialog extends AbringBaseDialogFragment
        implements View.OnClickListener, FetchListener {

    private static AbringCheckUpdateDialog instance = null;

    private TextView tvDownloadPercent;
    private TextView tvDownloadPerSize;
    private TextView tvDownloadStatus;
    private RelativeLayout relDownloadProgress;
    private ProgressBar progressBar;
    private Button btnCancel;
    private Button btnOK;

    private boolean isRetry;
    private static String mUrl;
    private static File mfilePath;
    private static File mDownloadDir;
    private static String mFileName;

    private static long downloadId = -1;
    private static Request request;
    private Fetch fetch;

    private static AbringCallBack abringCallBack;

    public AbringCheckUpdateDialog() {
    }

    public static synchronized AbringCheckUpdateDialog getInstance(AbringCallBack<Object, Object> mCallBack) {
        abringCallBack = mCallBack;

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
        if (getArguments() != null)
            mUrl = getArguments().getString("url", null);

    }

    @Override
    protected void initViews(View view) {

        tvDownloadPercent = (TextView) view.findViewById(R.id.tvDownloadPercent);
        tvDownloadPerSize = (TextView) view.findViewById(R.id.tvDownloadPerSize);
        tvDownloadStatus = (TextView) view.findViewById(R.id.tvDownloadStatus);
        relDownloadProgress = (RelativeLayout) view.findViewById(R.id.relDownloadProgress);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOK = (Button) view.findViewById(R.id.btnOK);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        mFileName = getString(R.string.app_name).toLowerCase() + ".apk";

        mDownloadDir = AbringFileUtil.createDirectoryInStorage(getString(R.string.app_name));
        mfilePath = new File(mDownloadDir, mFileName);
        /*filePath = AbringFileUtil.getSaveDir(getString(R.string.app_name)) +
                getString(R.string.app_name).toLowerCase() + ".apk";*/
        setupDownloadManager();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnOK) {
            if (isRetry) {

                isRetry = false;
                fetch.retry(downloadId);

            } else {
                fetch.pause(downloadId);
                dismiss();
            }

        }
    }

    private void setupDownloadManager() {
        fetch = Fetch.newInstance(getContext());
        fetch.removeAll();
        fetch.removeRequests();

        if (mfilePath.exists())
            mfilePath.delete();

        request = new Request(mUrl, mDownloadDir.toString(), mFileName);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                downloadId = fetch.enqueue(request);
                fetch.addFetchListener(AbringCheckUpdateDialog.this);
            }
        }, 1000);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (downloadId != -1) {
            RequestInfo info = fetch.get(downloadId);

            if (info != null)
                setProgressView(info.getStatus(), info.getProgress(), 0, 0);

            fetch.addFetchListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fetch != null)
            fetch.removeFetchListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetch != null)
            fetch.release();
    }

    @Override
    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {

        if (downloadId == id) {

            if (status == Fetch.STATUS_ERROR)
                showDownloadErrorMessage(error);
            else
                setProgressView(status, progress, downloadedBytes, fileSize);
        }
    }

    private void setProgressView(int status, int progress, long downloadedBytes, long fileSize) {
        switch (status) {

            case Fetch.STATUS_QUEUED:
                tvDownloadStatus.setText(getString(R.string.abring_download_queued));
                btnOK.setText(getString(R.string.abring_exit));
                if (!progressBar.isIndeterminate())
                    progressBar.setIndeterminate(true);

                break;

            case Fetch.STATUS_DOWNLOADING:
                if (progressBar.isIndeterminate())
                    progressBar.setIndeterminate(false);

                if (progress == -1)
                    tvDownloadStatus.setText(getString(R.string.abring_connecting));
                else {
                    progressBar.setProgress(progress);
                    tvDownloadStatus.setText(getString(R.string.abring_downloading));
                    tvDownloadPerSize.setText(AbringFileUtil.getDownloadPerSize(downloadedBytes, fileSize));
                    tvDownloadPercent.setText(String.valueOf(progress).concat(" %"));
                }

                break;

            case Fetch.STATUS_DONE:

                progressBar.setIndeterminate(true);
                tvDownloadStatus.setText(getString(R.string.abring_complete));
                tvDownloadPerSize.setText(AbringFileUtil.getDownloadPerSize(downloadedBytes, fileSize));
                tvDownloadPercent.setText("100 %");

                if (mfilePath.isFile() && mfilePath.exists()) {
                    AbringFileUtil.installAppN(getActivity(), mfilePath);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }

                abringCallBack.onSuccessful(null);
                break;
        }
    }

    private void showDownloadErrorMessage(int error) {
        Toast.makeText(getContext(), getString(R.string.abring_failure_responce), Toast.LENGTH_SHORT).show();

        if (mfilePath.exists())
            mfilePath.delete();

        progressBar.setIndeterminate(true);
        tvDownloadStatus.setText(R.string.abring_download_error);
        btnOK.setText(R.string.abring_retry_download);
        isRetry = true;
    }
}
