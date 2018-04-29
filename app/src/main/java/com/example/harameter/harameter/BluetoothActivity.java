package com.example.harameter.harameter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


public class BluetoothActivity extends Activity {
    //private final String DEVICE_NAME="MyBTBee";
    //private final String DEVICE_ADDRESS="20:13:10:15:33:66";
    private final String DEVICE_NAME = "HC-05";

    private static final Random RANDOM = new Random();
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, stopButton, settingsclickable;
    //EditText editText;
    double thresh = .5;
    double streakTemp = 0;
    TextView textView, info, circleObject, mode;
    String difficulty, method, email;
    int accuracy, streak;
    double streakMax;
    double recentFail;
    boolean deviceConnected=false;
    //Thread thread;
    byte buffer[];
    //int bufferPosition;
    boolean stopThread;
    //ConnectedThread mConnectedThread;
    private double accuracySum;
    private double pointsSum;
    private int series1lastX = 0;
    private int series2lastX = 0;
    private long calibrateTime = 15000000000l; //nanoseconds
    private long startTime = 0;
    private boolean isCalibrating = false;
    private boolean hasCalibrated = false;
    double maxBreath = 0.0;
    double minBreath = 5.0;

    double amplitude = 0.0;
    double angularFrequency = 0.0;

    double abdBeginnerAmplitude = 2.5;
    double beginnerPeriod = 4.0; //In seconds
    double beginnerFrequency = 1/beginnerPeriod;
    double beginnerAngularFrequency = Math.PI * 2 * beginnerFrequency;

    double abdAdvancedAmplitude = 5.0;
    double advancedPeriod = 8.0; //In seconds
    double advancedFrequency = 1/advancedPeriod;
    double advancedAngularFrequency = Math.PI * 2 * advancedFrequency;

    double haraAmplitude = 0.0;

    double baseline = 5.0;
    double haraAdvancedBaseline = 8.0;

    double numbers [] = new double[10];
    double weights [] = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    double numbers2 [] = new double[10];
    double calibnumbers [] = new double [10];

    boolean isGame = true;

    GraphView graph;
    LineGraphSeries<DataPoint> userData;
    LineGraphSeries<DataPoint> aspirationalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        circleObject = findViewById(R.id.circle);
        circleObject.setEnabled(true);
        startButton = findViewById(R.id.buttonStart);
        stopButton = findViewById(R.id.buttonStop);
        textView = findViewById(R.id.btTextView);
        info = findViewById(R.id.info);
        settingsclickable = findViewById(R.id.settingsclickable);
        mode = findViewById(R.id.mode);
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        method = getIntent().getStringExtra("METHOD");
        email = getIntent().getStringExtra("GOOGLE_EMAIL");
        textView.setEnabled(true);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        info.setText(method + ", " + difficulty);
        info.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        accuracy = 27;
        streak = 13;
        settingsclickable.setVisibility(View.VISIBLE);
        mode.setText("Game");
        mode.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        // set mode
        if(method.equals(getString(R.string.haramode))) {
            amplitude = haraAmplitude;
            if(difficulty.equals(getString(R.string.difficultyAdvanced))) {
                baseline = haraAdvancedBaseline;
            }else{
                thresh = 1.0;
            }
        }

        if(method.equals(getString(R.string.abdominalmode))) {
            if(difficulty.equals(getString(R.string.difficultyBeginner))) {
                amplitude = abdBeginnerAmplitude;
                angularFrequency = beginnerAngularFrequency;
                thresh = 1.0;
               // textView.setText("why am i bad");
            } else {
                amplitude = abdAdvancedAmplitude;
                angularFrequency = advancedAngularFrequency;
            }
        }

        setUiEnabled(false);

        graph = findViewById(R.id.graph);
        GridLabelRenderer graphStyle = graph.getGridLabelRenderer();
        graphStyle.setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphStyle.setHorizontalLabelsVisible(false);
        graphStyle.setVerticalLabelsVisible(false);

        //customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setScrollable(true);

        // calibration image setup

        circleObject = findViewById(R.id.circle);
        circleObject.setVisibility(View.INVISIBLE);

    }

    // reference: https://stackoverflow.com/questions/28269837/android-layout-params-change-only-width-and-height
    private void setDimensions(View view, int width, int height){
        android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public void setUiEnabled(boolean bool)
    {
        startButton.setEnabled(!bool);
        //----------NOT JOSH (Uncomment for Bluetooth testing)----------
        stopButton.setEnabled(bool);

    }

    public void onClickCalibrate(View view){

        isCalibrating = true;
        startTime = System.nanoTime();
        doUpdate("Calibrating for 15 seconds.\nPlease expand and contract abdomen to your greatest range");
        circleObject.setVisibility(view.VISIBLE);
    }

    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesn't support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please pair the device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getName().equals(DEVICE_NAME))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }

        if(!found) {
            Toast.makeText(getApplicationContext(), "Device not found", Toast.LENGTH_SHORT).show();
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return connected;
    }

    public void createGraph() {
        userData = new LineGraphSeries<DataPoint>();
        userData.setColor(R.color.colorPrimary);
        aspirationalData = new LineGraphSeries<DataPoint>();
        aspirationalData.setColor(R.color.colorAccent);
        graph.addSeries(aspirationalData);
        graph.addSeries(userData);
        graph.setVisibility(View.VISIBLE);
    }

    public void onClickStart(View view) {
        //createGraph();
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;

                //add series to graph
                beginListenForData();
                textView.setText("Connection opened!");
            }
        }
    }

    void addEntry(double dataNum, double aspirationNum) {
        userData.appendData(new DataPoint(series1lastX++, dataNum), true, 100);
        aspirationalData.appendData(new DataPoint(series2lastX++, aspirationNum), true, 100);
    }


    void beginListenForData()
    {
        textView.setText("Listen for data");
        //final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 4)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            //handler.post(new Runnable() {
                            runOnUiThread(new Runnable() {
                                public void run()
                                {
                                    DecimalFormat format = new DecimalFormat("#.#");
                                    try {
                                        long timePassed = System.nanoTime() - startTime;
                                        if(!isCalibrating && !hasCalibrated) {
                                            doUpdate("Connection found. Please press calibrate!");
                                            //doUpdate(string);
                                            //isCalibrating = false;
                                        }

                                        final double number = format.parse(string).doubleValue();
                                        //final double userVal = Math.abs(number);
                                        if(isCalibrating && !hasCalibrated) {
                                            //doUpdate("Calibration will last for 15 seconds.");

                                            int n = (int) Math.abs(number);
                                            int setter = n * 25 + 250;
                                            double d = movingWindowWeightedAverage(weights, calibnumbers, setter);
                                            int c = (int)d;
                                            setDimensions(circleObject, c, c);

                                            if(number > maxBreath) {
                                                maxBreath = number;
                                            }
                                            if(number < minBreath) {
                                                minBreath = number;
                                            }
                                            timePassed = System.nanoTime() - startTime;
                                            if(timePassed > calibrateTime) {
                                                isCalibrating = false;
                                                hasCalibrated = true;
                                                circleObject.setVisibility(View.INVISIBLE);
                                                doUpdate("Follow aspirational curve");
                                                createGraph();
                                                recentFail = System.nanoTime();

                                            }
                                            //addEntry(number, 0);
                                        }

                                        if(hasCalibrated) {
                                            final double calibrated = adjust(maxBreath, minBreath, number);
                                            double currTime = System.nanoTime() - startTime;
                                            double aspiration = Math.floor((amplitude*(Math.sin(angularFrequency * currTime/1000000000)) + baseline) * 100) / 100;
                                            //addEntry(calibrated, aspiration);
                                            // doUpdate("Calibrated: " + calibrated + " Aspir: " + aspiration);
                                            double weightedAverageUser;
                                            weightedAverageUser = movingWindowWeightedAverage(weights, numbers, calibrated);
                                            double weightedAverageAspiration;
                                            weightedAverageAspiration = movingWindowWeightedAverage(weights, numbers2, aspiration);
                                            addEntry(weightedAverageUser, weightedAverageAspiration);
                                            pointsSum ++;
                                            if(weightedAverageUser < (weightedAverageAspiration + thresh) && weightedAverageUser > (weightedAverageAspiration - thresh) ){
                                                accuracySum ++;
                                                streakTemp = System.nanoTime() - recentFail;
                                                if(isGame) {
                                                    userData.setColor(0XFF81c784);
                                                }
                                                if (streakTemp > streakMax){
                                                    streakMax = streakTemp;
                                                }
                                            }else{
                                                recentFail = System.nanoTime();
                                                if(isGame) {
                                                    userData.setColor(0XFFe57373);
                                                }
                                            }

                                        }

                                        //doUpdate(string);

                                    }
                                    //catch(ParseException e) {
                                    catch(Exception e) {
                                        //doUpdate(e.getMessage());
                                    }
                                    //for(int i = 0; i < 100; i++) {
                                    // addEntry();
                                    //}
                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                        textView.setText("Stop thread");
                    }
                }
            }
        });

        thread.start();
    }


    public void onClickStop(View view) throws IOException {
        //----------NOT JOSH (Uncomment for Bluetooth testing)----------
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        hasCalibrated = false;
        deviceConnected=false;
        removeGraph();
        textView.setText("Connection closed!");
        double temp = (accuracySum/pointsSum * 100);
        accuracy = (int)temp;
        streakMax = streakMax/1000000000;
        streak = (int) streakMax;

        //---------------------------JOSH-------------------------------

        Intent stopIntent = new Intent(this, SplashActivity.class);
        stopIntent.putExtra("DIFFICULTY", difficulty);
        stopIntent.putExtra("METHOD", method);
        stopIntent.putExtra("GOOGLE_EMAIL", email);
        stopIntent.putExtra("ACCURACY", Integer.toString(accuracy));
        stopIntent.putExtra("STREAK", Integer.toString(streak));
        startActivity(stopIntent);
    }

    public void onClickSettings(View view) {
        if (mode.getText().equals("Game")) {
            mode.setText("Zen");
            //adjust/set view setting according to Zen spec
            isGame = false;
            userData.setColor(R.color.colorPrimary);
        }
        else if (mode.getText().equals("Zen")) {
            mode.setText("Game");
            //adjust/set view setting according to Game spec
            isGame = true;
        }
    }

    public void removeGraph() {
        graph.removeAllSeries();
        graph.setVisibility(View.GONE);
    }

    public static double adjust(double max, double min, double num){
        double adjusted = Math.floor(10 * ((num - max) / (min - max)) * 100) / 100;
        return adjusted;

    }

    public static double movingWindowWeightedAverage(double[] w, double[] values, double newNum){
        for(int i = values.length -1; i > 0; i --){
            values[i] = values[i-1];
        }
        values[0] = newNum;

        ///calculate weighted average USERDATA
        double weightedAverage = 0;
        for(int i = 0; i < values.length; i ++){
            weightedAverage = weightedAverage + values[i]* w[i];
        }

        weightedAverage = weightedAverage/55;
        return weightedAverage;

    }

    private void doUpdate(String string) {
        textView.setText(string);
    }

}
