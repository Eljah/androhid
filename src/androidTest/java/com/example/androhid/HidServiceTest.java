package com.example.androhid;

import android.bluetooth.BluetoothAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class HidServiceTest {
    @Test
    public void bluetoothAdapterAvailable() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        assertNotNull(adapter);
    }

    @Test
    public void serviceCanBeCreated() {
        HidService service = Robolectric.buildService(HidService.class).create().get();
        assertNotNull(service);
    }
}
