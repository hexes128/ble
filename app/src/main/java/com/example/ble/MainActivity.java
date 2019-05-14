package com.example.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.ble.R.layout.activity_main;





public class MainActivity extends AppCompatActivity {


    private Button scan;
    private Button connect;
    private ListView devicedata;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothDevice readyconnectdevice;
    private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
    private  ArrayAdapter   resultAdapter;
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        scan = findViewById(R.id.scan);

        connect = findViewById(R.id.connect);
        devicedata=findViewById(R.id.devicedata);
   resultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        devicedata.setAdapter(resultAdapter);

        if (!getPackageManager().hasSystemFeature(getPackageManager().FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getBaseContext(), "No_sup_ble", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bluetoothLeScanner.startScan(scanCallback);
                handler.postDelayed(runnable, 100000);
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bluetoothLeScanner.stopScan(scanCallback);
                BluetoothGatt mBluetoothGatt = readyconnectdevice.connectGatt(mContext, false, mGattCallback);
            }
        });

        devicedata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                bluetoothLeScanner.stopScan(scanCallback);
                readyconnectdevice = devices.get(position);
                Toast.makeText(getApplicationContext(),readyconnectdevice.getName()+"\n"+readyconnectdevice.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bluetoothLeScanner.stopScan(scanCallback);

        }
    };
    private Handler handler = new Handler();

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult results) {
            super.onScanResult(callbackType, results);
            BluetoothDevice mdevice = results.getDevice();


            if (!devices.contains(mdevice)) {
                devices.add(mdevice);

              String UUIDx = UUID.nameUUIDFromBytes(results.getScanRecord().getBytes()).toString();
                resultAdapter.add(mdevice.getName()+"\n"+mdevice.getAddress()+"\n"+UUIDx);
                resultAdapter.notifyDataSetChanged();

            }
            else {

            }
        }
    };
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {//连接状态改变
            if(newState==BluetoothGatt.STATE_CONNECTED){
                Log.e("onConnec中中中", "连接成功:");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {


            }



        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {

        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {//发送数据时调用

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {// Characteristic 改变，数据接收会调用

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {//descriptor写

        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {

        }

    };

}