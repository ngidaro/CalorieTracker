package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

public class InitiateBluetooth extends AppCompatActivity {

    private static final String TAG = "InitiateBluetooth";   //used to debug the state changes
    public final static String MODULE_MAC = "00:14:03:05:F0:F6";
    //The MAC address of the specific BT module that we are using
    //Got this by using the first version of this code
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //the UUID for the HC05 BT module
    public final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter myBluetoothAdapter; //global bluetooth adapter
    static BluetoothSocket mySocket;
    //socket that holds the BT device that is being connected to server socket
    BluetoothDevice bluetoothDevice;    //the actual BT device that is being connected
    static ConnectedThread connectedThread = null; //object of the connected thread class
    @SuppressLint("StaticFieldLeak")
    static TextView messageReceived;   //Where the value will be displayed
    static String text;
    Button weight_button;   //fetches the value that will be displayed
    public Handler mHandler;    //allows you to send and process messages associated with Threads

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String user_id = getIntent().getStringExtra("_id");

        //initializing the xml file to the main code
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  //get default adapter
        ToggleBluetooth();

        Intent intent = new Intent(InitiateBluetooth.this, HomeActivity.class);
        intent.putExtra("_id",user_id);
        startActivity(intent);
    }

    void ToggleBluetooth()
    {
        Log.d(TAG, "checkUserBT: Checking if BT is on");
        if(!myBluetoothAdapter.isEnabled()) //checks to see if BT is turned off
        {
            Log.d(TAG, "checkUserBT: Turning on BT");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            //will only send the signal to turn on BT if when you open the code and it is off
        }
        else
        {
            Log.d(TAG, "checkUserBT: BT is already on");
            initiateBluetoothProcess();
            Log.d(TAG, "checkUserBT: Stating initiateBluetoothProcess();");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT)
        {
            initiateBluetoothProcess();
        }
    }

    public void initiateBluetoothProcess() {
        Log.d(TAG, "initiateBluetoothProcess: Starting");
        if (myBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "initiateBluetoothProcess: Attempt to connect to bluetooth module");
            //attempt to connect to bluetooth module
            bluetoothDevice = myBluetoothAdapter.getRemoteDevice(MODULE_MAC);
            //will get the BT device using the hardware address (since it is known)
            try {
                Log.d(TAG, "initiateBluetoothProcess: Creating a socket");
                BluetoothSocket tmp = null;
                tmp = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                //will open communication with the BT device
                mySocket = tmp;
                mySocket.connect(); //connects to the BT device
                Log.d(TAG, "initiateBluetoothProcess: Connected to: " + bluetoothDevice.getName());
            }
            catch (IOException e)
            {
                Log.d(TAG, "initiateBluetoothProcess: Catching exception: " + e.getMessage());
                try {
                    Log.d(TAG, "initiateBluetoothProcess: Attempting to close socket");
                    mySocket.close();
                } catch (IOException c) {
                    Log.d(TAG, "initiateBluetoothProcess: Failed to close socket");
                    return;
                }
            }
        }

        mHandler = new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(msg.what == ConnectedThread.RESPONSE_MESSAGE)    //makes sure that it matches
                {
                    String txt = (String) msg.obj;

                    InitiateBluetooth.text = txt;

                    //retrieves the data form the ConnectedThread input stream
//                    if(InitiateBluetooth.messageReceived.getText().toString().length() >= 30)
//                    {
//                        InitiateBluetooth.messageReceived.setText(txt);
//                        InitiateBluetooth.messageReceived.append(txt);
                        //this is what will change / update the text view
//                    }
//                    else
//                    {
//                        InitiateBluetooth.messageReceived.append("\n" + txt);
//                    }
                }
            }
        };
        Log.d(TAG, "initiateBluetoothProcess: Creating and Running thread");
        connectedThread = new ConnectedThread(mySocket, mHandler);
        //create the ConnectedThread object using the socket and the handler
        connectedThread.start();    //initiates the connected thread
    }
}