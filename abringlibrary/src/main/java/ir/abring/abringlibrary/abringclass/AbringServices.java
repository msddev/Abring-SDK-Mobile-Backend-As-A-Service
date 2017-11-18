package ir.abring.abringlibrary.abringclass;

import com.orhanobut.hawk.Hawk;
import ir.abring.abringlibrary.AbringConstant;
import ir.abring.abringlibrary.models.abringregister.AbringResult;

public class AbringServices {

    public static Object getUser() {
        Object user = null;
        if (Hawk.contains(AbringConstant.ABRING_USER_INFO))
            user = Hawk.get(AbringConstant.ABRING_USER_INFO, null);
        return user;
    }

    public static void setUser(AbringResult result) {
        Hawk.put(AbringConstant.ABRING_USER_INFO, result);
        Hawk.put(AbringConstant.ABRING_TOKEN, result.getToken());
    }
}
