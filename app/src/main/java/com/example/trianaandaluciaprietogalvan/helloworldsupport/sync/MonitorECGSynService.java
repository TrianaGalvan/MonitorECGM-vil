package com.example.trianaandaluciaprietogalvan.helloworldsupport.sync;
import 	android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by trianaandaluciaprietogalvan on 05/04/16.
 */
public class MonitorECGSynService extends Service {
    // Storage for an instance of the sync adapter
    private static MonitorECGSync sMonitorECGSync = null;
    // Object to use as a thread-safe lock
    private static final Object sMonitoECGSyncLock = new Object();

    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sMonitoECGSyncLock) {
            if (sMonitorECGSync == null) {
                sMonitorECGSync = new MonitorECGSync(getApplicationContext(), true);
            }
        }

    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
          /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sMonitorECGSync.getSyncAdapterBinder();
    }
}
