package cn.iotzone.mindlocal.app;

import com.google.gson.Gson;

import androidx.multidex.MultiDexApplication;
import cn.iotzone.mindlocal.db.DaoMaster;
import cn.iotzone.mindlocal.db.DaoSession;

public class MainApplication extends MultiDexApplication {
    private static DaoSession daoSession;
    private static MainApplication sMainApplication;
    public static MainApplication get() {
        return sMainApplication;
    }
    private static Gson gson;
    @Override
    public void onCreate() {
        super.onCreate();
        sMainApplication = this;
        setupDatabase();
        gson = new Gson();
    }
    public static Gson getGson(){
        return  gson;
    }
    private void setupDatabase() {
        daoSession = new DaoMaster(new DaoMaster.DevOpenHelper(this, "devices.db", null).getWritableDatabase()).newSession();
    }
    public static DaoSession getDaoInstant() {
        return daoSession;
    }
}
