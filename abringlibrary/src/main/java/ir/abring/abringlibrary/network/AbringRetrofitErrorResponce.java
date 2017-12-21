package ir.abring.abringlibrary.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import ir.abring.abringlibrary.Abring;
import ir.abring.abringlibrary.R;
import retrofit2.Response;

public class AbringRetrofitErrorResponce {

    private Context mContext;
    private AbringApiError errorResponse = new AbringApiError();

    public Object getMessage(Object error, Context context) {

        mContext = context;
        Log.d("R_Error", String.valueOf(error));

        if (String.valueOf(error).equals(Abring.getContext().getString(R.string.abring_no_connect_to_internet))) {
            errorResponse.setMessage(context.getResources().getString(R.string.abring_no_connect_to_internet));
            return errorResponse;
        } else if (error instanceof SocketTimeoutException) {
            errorResponse.setMessage(context.getResources().getString(R.string.abring_SERVER_TIMEOUT));
            return errorResponse;
        } else if (isNetworkProblem(error)) {
            errorResponse.setMessage(context.getResources().getString(R.string.abring_no_connect_to_server));
            return errorResponse;
        } else {
            return handleServerError(error);
        }
    }

    private static boolean isNetworkProblem(Object error) {
        return (error instanceof ConnectException) || (error instanceof NetworkErrorException);
    }

    private Object handleServerError(Object error) {
        Response response;
        try {
            response = (Response) error;
            Log.d("R_Error_Code", String.valueOf(error));
        } catch (ClassCastException e) {
            e.printStackTrace();
            return mContext.getResources().getString(R.string.abring_SERVER_ERROR);
        }

        try {

            Gson gson = new Gson();
            errorResponse = gson.fromJson(
                    response.errorBody().string(),
                    AbringApiError.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        switch (response.code()) {
            case 400: // not found
                return errorResponse;
            case 404:
                errorResponse.setMessage(mContext.getResources().getString(R.string.abring_ERROR_PAGE_NOT_FOUND));
                return errorResponse;
            case 405: // server error
            case 500:
                errorResponse.setMessage(mContext.getResources().getString(R.string.abring_no_connect_to_server));
                return errorResponse;

            case 422: // validation

            case 401: // autorization - refresh token
                return errorResponse;
            //return mActivity.getResources().getString(R.string.ERROR_AUTOROZATION);

            default:
                errorResponse.setMessage(mContext.getResources().getString(R.string.abring_no_connect_to_server));
                return errorResponse;
        }
    }
}
