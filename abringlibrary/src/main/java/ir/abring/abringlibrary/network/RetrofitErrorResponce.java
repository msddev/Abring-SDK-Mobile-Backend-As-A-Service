package ir.abring.abringlibrary.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import ir.abring.abringlibrary.R;
import retrofit2.Response;

public class RetrofitErrorResponce {

    private Context mContext;
    private ApiError errorResponse;

    public Object getMessage(Object error, Context context) {

        mContext = context;
        Log.d("R_Error", String.valueOf(error));

        Response response;
        try {
            response = (Response) error;
            Log.d("R_Error_Code", String.valueOf(error));
        } catch (ClassCastException e) {
            e.printStackTrace();
            return mContext.getResources().getString(R.string.SERVER_ERROR);
        }

        try {

            Gson gson = new Gson();
            errorResponse = gson.fromJson(
                    response.errorBody().string(),
                    ApiError.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return errorResponse;
    }
}
