package com.example.calorietracker;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private final BluetoothSocket bt_Socket;
    private final InputStream myInStream;
    private final OutputStream myOutStream;
    public static final int RESPONSE_MESSAGE = 10;
    Handler myHandler;

    public ConnectedThread(BluetoothSocket socket, Handler uih)
    {
        bt_Socket = socket; //the BT socket passed from main
        InputStream tmpIn = null;   //data that is coming to Android
        OutputStream tmpOut = null; //data that is leaving Android
        this.myHandler = uih;   //handler that was passed from main
        Log.d(TAG, "ConnectedThread: Creating thread");

        try
        {
            Log.d(TAG, "ConnectedThread: Putting the input and output streams in temps");
            tmpIn = socket.getInputStream();    //trying to get output stream from BT device
            tmpOut = socket.getOutputStream();  //trying to get output stream from the Android device
        }
        catch(IOException e)
        {
            Log.d(TAG, "ConnectedThread: Exception input/output sockets: " + e.getMessage());
        }

        myInStream = tmpIn;
        myOutStream = tmpOut;

        try
        {
            Log.d(TAG, "ConnectedThread: Trying to flush");
            myOutStream.flush();
            //When we give any command, the streams of that command are stored in the memory
            // location called buffer(a temporary memory location) in our computer. When all the
            // temporary memory location is full then we use flush(), which flushes all the streams
            // of data and executes them completely and gives a new space to new streams in buffer
            // temporary location
        }
        catch (IOException e)
        {
            Log.d(TAG, "ConnectedThread: Exception Flush: " + e.getMessage());
            return;
        }
    }

    public void run()   //doesn't need to be invoked, does it automatically (its an override)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(myInStream));
        //Reads text from a character-input stream, buffering characters so as to
        // provide for the efficient reading of characters, arrays, and lines
        //Basically you do not need to write the actual buffer out like you did in the
        //first version of the code

        Log.d(TAG, "run: Starting Run thread");
        while(true)
        {
            try{
                // bytes = mmInStream.read(buffer) ==> EQUIVALENT BELLOW
                String resp = br.readLine();    //reads the line of text
                Message msg = new Message();
                msg.what = RESPONSE_MESSAGE;    //what links to the other class
                msg.obj = resp; //the object will hold the actual data from the InputStream
                myHandler.sendMessage(msg); //put message at the rear of the queue
                //using the handler, we are able to send the message from here into the UI
                //using the send message. We are then able to go into the main and then
                //"handle the message" in order to be able to work with it
            }
            catch(IOException e)
            {
                Log.d(TAG, "run: Exception: " + e.getMessage());
                break;
            }
        }
        Log.d(TAG, "run: Loop ended");
    }

    //needs to be in bytes, or else the BT device will
    // not be able to understand using the data using the 9600 baud rate
    public void write(byte[] bytes)
    {
        try
        {
            Log.d(TAG, "write: Writing bytes");
            myOutStream.write(bytes);
            //send the character GW so that the BT device knows to
            // send the values only when requested
        }
        catch(IOException e)
        {
            Log.d(TAG, "write: exception: " + e.getMessage());
        }
    }
}
