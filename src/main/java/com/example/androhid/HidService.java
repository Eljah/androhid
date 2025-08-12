package com.example.androhid;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HidService extends Service implements BluetoothProfile.ServiceListener {
    private static final String TAG = "HidService";
    private BluetoothHidDevice hidDevice;
    private BluetoothAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final BluetoothHidDevice.Callback callback = new BluetoothHidDevice.Callback() {
        @Override
        public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
            Log.d(TAG, "App status changed: " + registered);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            adapter.getProfileProxy(this, this, BluetoothProfile.HID_DEVICE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (adapter != null && hidDevice != null) {
            hidDevice.unregisterApp();
            adapter.closeProfileProxy(BluetoothProfile.HID_DEVICE, hidDevice);
        }
        executor.shutdown();
        super.onDestroy();
    }

    @Override
    public void onServiceConnected(int profile, BluetoothProfile proxy) {
        if (profile == BluetoothProfile.HID_DEVICE) {
            hidDevice = (BluetoothHidDevice) proxy;
            BluetoothHidDeviceAppSdpSettings sdp = new BluetoothHidDeviceAppSdpSettings(
                    "HID Keyboard", "Keyboard", "Android",
                    BluetoothHidDevice.SUBCLASS1_COMBO, null);
            BluetoothHidDeviceAppQosSettings inQos = new BluetoothHidDeviceAppQosSettings(
                    BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT, 800, 9, 0, 11250, 11250);
            hidDevice.registerApp(sdp, null, inQos, executor, callback);
        }
    }

    @Override
    public void onServiceDisconnected(int profile) {
        if (profile == BluetoothProfile.HID_DEVICE) {
            hidDevice = null;
        }
    }
}
