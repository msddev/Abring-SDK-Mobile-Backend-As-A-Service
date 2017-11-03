package ir.abring.abringlibrary.interfaces;


public interface AbringCallBack<A,B> {
    void onSuccessful(A response);

    void onFailure(B response);
}
