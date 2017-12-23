package ir.abring.abringlibrary.utils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AbringUtils {

    // This method  converts String to RequestBody
    public static RequestBody toRequestBody(String string) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), string);
        return body;
    }

    // This method  converts File to MultipartBody.Part
    public static MultipartBody.Part toMultipartBody( String name, File image) {

        // Parsing any Media type file
        MultipartBody.Part avatarToUpload = null;
        if (image != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), image);
            avatarToUpload = MultipartBody.Part.createFormData(name, image.getName(), requestBody);
        }
        return avatarToUpload;
    }
}
