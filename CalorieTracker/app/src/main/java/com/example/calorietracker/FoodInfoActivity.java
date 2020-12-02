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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.calorietracker.volley.VolleyRequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import callbacks.IVolleyRequestCallback;

public class FoodInfoActivity extends AppCompatActivity {

    ImageView ivExit;
    TextView tvFood;
    TextView tvServingSizeUnits;
    Spinner spinServingSize;
    EditText etAmount;
    Button btnAddToDiary;
    JSONObject jsonFoodObj;
    ImageView ivFoodScale;

    private static final String TAG = "MainActivity";   //used to debug the state changes
    public final static String MODULE_MAC = "00:14:03:05:F0:F6";
    //The MAC address of the specific BT module that we are using
    //Got this by using the first version of this code
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    //the UUID for the HC05 BT module
    public final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter myBluetoothAdapter; //global bluetooth adapter
    BluetoothSocket mySocket;
    //socket that holds the BT device that is being connected to server socket
    BluetoothDevice bluetoothDevice;    //the actual BT device that is being connected
    ConnectedThread connectedThread = null; //object of the connected thread class
    public Handler mHandler;    //allows you to send and process messages associated with Threads

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);

        ivExit = findViewById(R.id.fia_exit);
        tvFood = findViewById(R.id.fia_food);
        spinServingSize = findViewById(R.id.fia_spin_serving_size);
        tvServingSizeUnits = findViewById(R.id.fia_serving_size_units);
        etAmount = findViewById(R.id.fia_amount);
        btnAddToDiary = findViewById(R.id.fia_add_to_diary);

        final String fdcId = getIntent().getStringExtra("fdcId");
        final String user_id = getIntent().getStringExtra("_id");
        final String prevApplicationContext = getIntent().getStringExtra("prevViewName");

        ivFoodScale = findViewById(R.id.fia_weight_btn);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();  //get default adapter

        ToggleBluetooth();

        assert prevApplicationContext != null;
        if(prevApplicationContext.equals("AddFoodActivity"))
        {
            btnAddToDiary.setText("Add Ingredient");
        }

        getFoodData(fdcId);

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodInfoActivity.super.onBackPressed();
            }
        });

        ivFoodScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mySocket.isConnected() && connectedThread != null)
                {
                    //when you click the button, it will send the characters GW to BT device
                    String  sendText = "GW";
                    Log.d(TAG, "getWeight(): telling the bluetooth to send data");
                    connectedThread.write(sendText.getBytes());
                }
                else
                {
                    Log.d(TAG, "getWeight(): Something went wrong ");
                }
            }
        });

        btnAddToDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jsonFoodObj != null){

                    double dServingSize = Double.parseDouble(spinServingSize.getSelectedItem().toString());
                    double dAmount = 0;
                    double dEnergy = 0; // id 1008 - "name": "Energy"
                    double dProtein = 0;    // id 1003 - "name": "Protein"
                    double dCarbs = 0;      // id 1005 - "name": "Carbohydrate, by difference"
                    double dFat = 0;        // id 1004 - "name": "Total lipid (fat)"
                    double dFiber = 0;      // id 1079 - "name": "Fiber, total dietary"

                    if ( etAmount.getText().toString().equals("")){
                        Toast.makeText(FoodInfoActivity.this, "Invalid Amount", Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        dAmount = Double.parseDouble(etAmount.getText().toString());

                        // FORMULA FOR CALCULATING NUTRIENT RATIOS:
                        // (Nutrient Amount)*(Amount entered by user)*(Serving Size option chosen by user) / (Serving Size) = x Serving Size Units

                        if(prevApplicationContext.equals("AddFoodActivity"))
                            addToIngredient(dAmount, dEnergy, dProtein, dCarbs, dFat, dServingSize, user_id, fdcId);
                        else
                            addToDiary(dAmount, dEnergy, dProtein, dCarbs, dFat, dServingSize, user_id, fdcId);

                    }

                }
                else {
                    // Error Occurred
                }
            }
        });

    }

    protected void getFoodData(String fdcId){

        JSONObject jsonFood = new JSONObject();
        try
        {
            jsonFood.put("fdcId", fdcId);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/food/fooddata",
                jsonFood,
                this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                          System.out.println(result.toString());
                          jsonFoodObj = result;
                          tvFood.setText(result.getString("description"));

                          try {
                            populateServingSizeSpinner(result);

                          }catch (JSONException e){
                              e.printStackTrace();
                          }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

    public void populateServingSizeSpinner(JSONObject result) throws JSONException {

        ArrayList<String> alServingSizes = new ArrayList<>();

        if(result.has("servingSize"))
        {
            alServingSizes.add(result.getString("servingSize"));
        }

        alServingSizes.add("1");
        tvServingSizeUnits.setText(result.getString("servingSizeUnit"));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, alServingSizes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinServingSize.setAdapter(spinnerAdapter);

    }

    public void addToDiary(double dAmount, double dEnergy, double dProtein, double dCarbs, double dFat, double dServingSize, final String user_id, String fdcId)
    {
        JSONObject jsonFood = new JSONObject();

        try
        {

//                            JSONArray arrTemp = jsonFoodObj.getJSONArray("foodNutrients");
//                            JSONObject jsonFoodNutrientObj = null;
//                            JSONObject jsonEnergyObj;
//
//                            for (int i = 0; i < arrTemp.length(); i++){
//                                jsonFoodNutrientObj = (JSONObject) arrTemp.get(i);
//                                jsonEnergyObj = jsonFoodNutrientObj.getJSONObject("nutrient"); // returns nutrient Object
//
//                                System.out.println(jsonEnergyObj.getString("id"));
//
//                                switch(jsonEnergyObj.getString("id")) {
//                                    case "1008":
//                                        dEnergy = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1003":
//                                        dProtein = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1005":
//                                        dCarbs = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1004":
//                                        dFat = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    case "1079":
//                                        dFiber = Double.parseDouble(jsonFoodNutrientObj.getString("amount")); // returns kcal per serving size
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }

            JSONObject jsonLabelNutrientObj = jsonFoodObj.getJSONObject("labelNutrients");

            JSONObject jsonNutrient = jsonLabelNutrientObj.getJSONObject("calories");
            dEnergy = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("protein");
            dProtein = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("carbohydrates");
            dCarbs = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("fat");
            dFat = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonFood.put("fdcId", fdcId);
            jsonFood.put("date", Calendar.getInstance().getTime());
            jsonFood.put("user_id", user_id);
            jsonFood.put("amount", dAmount);
            jsonFood.put("servingsize", dServingSize);
            jsonFood.put("servingunits", jsonFoodObj.getString("servingSizeUnit"));
            jsonFood.put("description", jsonFoodObj.getString("description"));
            jsonFood.put("brandowner", jsonFoodObj.getString("brandOwner"));

            jsonFood.put("energy", (dEnergy*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize")))); // Nutrient Id for Energy is 1008 -> see nutrient.csv
            jsonFood.put("protein", (dProtein*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonFood.put("carbs", (dCarbs*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonFood.put("fat", (dFat*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
//                            jsonFood.put("fiber", (dFiber*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        // Store data in database:

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/food/savediary",
                jsonFood,
                FoodInfoActivity.this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println(result.toString());
                        Intent intent = new Intent(FoodInfoActivity.this, HomeActivity.class);
                        intent.putExtra("_id",user_id);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });
    }

    public void addToIngredient(double dAmount, double dEnergy, double dProtein, double dCarbs, double dFat, double dServingSize, final String user_id, String fdcId)
    {
        final String recipeId = getIntent().getStringExtra("recipe_id");
        // Save the recipe to the database

        JSONObject jsonIngredient = new JSONObject();

        try
        {

            JSONObject jsonLabelNutrientObj = jsonFoodObj.getJSONObject("labelNutrients");

            JSONObject jsonNutrient = jsonLabelNutrientObj.getJSONObject("calories");
            dEnergy = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("protein");
            dProtein = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("carbohydrates");
            dCarbs = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonNutrient = jsonLabelNutrientObj.getJSONObject("fat");
            dFat = Double.parseDouble(jsonNutrient.getString("value")); // returns kcal per serving size

            jsonIngredient.put("fdcId", fdcId);
            jsonIngredient.put("recipe_id", recipeId);
            jsonIngredient.put("user_id", user_id);
            jsonIngredient.put("amount", dAmount);
            jsonIngredient.put("servingsize", dServingSize);
            jsonIngredient.put("servingunits", jsonFoodObj.getString("servingSizeUnit"));
            jsonIngredient.put("description", jsonFoodObj.getString("description"));
            jsonIngredient.put("brandowner", jsonFoodObj.getString("brandOwner"));

            jsonIngredient.put("energy", (dEnergy*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize")))); // Nutrient Id for Energy is 1008 -> see nutrient.csv
            jsonIngredient.put("protein", (dProtein*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonIngredient.put("carbs", (dCarbs*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
            jsonIngredient.put("fat", (dFat*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
//                            jsonFood.put("fiber", (dFiber*dAmount*dServingSize/Double.parseDouble(jsonFoodObj.getString("servingSize"))));
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        // Store data in database:

        VolleyRequestContainer.request(
                Request.Method.POST,
                "/ingredient/addingredient",
                jsonIngredient,
                FoodInfoActivity.this,
                new IVolleyRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        Intent intent = new Intent(FoodInfoActivity.this, AddIngredientActivity.class);
                        intent.putExtra("_id",user_id);
                        intent.putExtra("recipe_id",recipeId);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String result) {
                        // Failed
                    }
                });

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
            BluetoothSocket tmp = null;
            bluetoothDevice = myBluetoothAdapter.getRemoteDevice(MODULE_MAC);
            //will get the BT device using the hardware address (since it is known)
            try {
                Log.d(TAG, "initiateBluetoothProcess: Creating a socket");
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

                    etAmount.setText(txt);
                }
            }
        };
        Log.d(TAG, "initiateBluetoothProcess: Creating and Running thread");
        connectedThread = new ConnectedThread(mySocket, mHandler);
        //create the ConnectedThread object using the socket and the handler
        connectedThread.start();    //initiates the connected thread
    }

}