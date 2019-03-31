package cn.iotzone.mindlocal.base;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.trello.rxlifecycle3.components.RxActivity;

import androidx.annotation.Nullable;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends RxActivity {
    private CompositeDisposable mDisposables;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            Window localWindow = getWindow();
            localWindow.addFlags(Integer.MIN_VALUE);
            localWindow.getDecorView().setSystemUiVisibility(8192);
        }
    }

    protected abstract void initViews();

    @Override
    protected void onStop() {
        super.onStop();
        if (mDisposables != null) {
            mDisposables.dispose();
            mDisposables = null;
        }

    }

    /**
     * 管理订阅状态
     *
     * @return CompositeDisposable
     */
    public CompositeDisposable getDisposables() {
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        return mDisposables;
    }

}
