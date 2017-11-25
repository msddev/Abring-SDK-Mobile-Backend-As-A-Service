package ir.abring.abringservices.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    protected FragmentActivity mActivity;
    protected Unbinder unbinder;
//    private EventBus bus = EventBus.getDefault();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeView();
    }

    protected abstract void initBeforeView();

    protected abstract int getContentViewId();

    protected abstract void initViews(View rootView);

    @Override
    public void onDestroy() {
        unbinder.unbind();
//        bus.unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
//        bus.register(this);
        initViews(rootView);
        return rootView;
    }

}
