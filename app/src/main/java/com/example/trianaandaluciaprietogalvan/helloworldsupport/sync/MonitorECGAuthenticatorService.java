package com.example.trianaandaluciaprietogalvan.helloworldsupport.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MonitorECGAuthenticatorService extends Service {

    private MonitorECGAuthenticator mMonitorECGAuthenticator;

    @Override
    public void onCreate() {
        mMonitorECGAuthenticator = new MonitorECGAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMonitorECGAuthenticator.getIBinder();
    }
}
