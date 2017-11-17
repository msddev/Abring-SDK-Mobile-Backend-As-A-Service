package ir.abring.abringlibrary.network;

import android.accounts.AuthenticatorException;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import ir.abring.abringlibrary.R;
import retrofit2.Response;

public class AbringRetrofitErrorHelper {

    private Context mContext;
    private AbringApiError errorResponse;

    public String getMessage(Object error, Context context) {

        mContext = context;
        Log.d("R_Error", String.valueOf(error));

        if (error instanceof SocketTimeoutException) {
            return context.getResources().getString(R.string.abring_SERVER_TIMEOUT);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.abring_no_connect_to_server);
        } else {
            return handleServerError(error);
        }
    }

    private static boolean isNetworkProblem(Object error) {
        return (error instanceof ConnectException) || (error instanceof NetworkErrorException);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof AuthenticatorException);
    }

    private String handleServerError(Object res) {
        Response response;
        try {
            response = (Response) res;
            Log.d("R_Error_Code", String.valueOf(res));
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
                return errorResponse.getMessage();
            case 404:
                return mContext.getResources().getString(R.string.abring_ERROR_PAGE_NOT_FOUND);
            case 405: // server error
            case 500:
                return mContext.getResources().getString(R.string.abring_no_connect_to_server);

            case 422: // validation

            case 401: // autorization - refresh token
                /*Intent intent = new Intent(mContext, LoginRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);*/
                return errorResponse.getMessage();
            //return mActivity.getResources().getString(R.string.ERROR_AUTOROZATION);

            default:
                return mContext.getResources().getString(R.string.abring_no_connect_to_server);
        }
    }
}
