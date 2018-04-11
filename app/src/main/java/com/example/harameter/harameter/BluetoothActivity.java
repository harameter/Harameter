package com.example.harameter.harameter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static java.lang.Math.abs;


public class BluetoothActivity extends Activity {
    //    private final String DEVICE_NAME="MyBTBee";
    //private final String DEVICE_ADDRESS="20:13:10:15:33:66";
    private final String DEVICE_NAME = "HC-05";

    private static final Random RANDOM = new Random();
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, stopButton;
    TextView textView;
    EditText editText;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    // ConnectedThread mConnectedThread;

    private int series1lastX = 0;
    private int series2lastX = 0;
    private long calibrateTime = 15000000000l; //nanoseconds
    private long startTime = 0;
    private boolean isCalibrating = false;
    private boolean hasCallibrated = false;
    double maxBreath = 0.0;
    double minBreath = 5.0;
    double amplitude = 5.0;
    double period = 6.0; //In seconds
    double frequency = 1/period;
    double angularFrequency = Math.PI * 2 * frequency;
    double baseline = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);
        textView = (TextView) findViewById(R.id.btTextView);
        textView.setEnabled(true);
        setUiEnabled(false);



//        graph = (GraphView) findViewById(R.id.graph);
//
//        // customize a little bit viewport
//        Viewport viewport = graph.getViewport();
//        viewport.setYAxisBoundsManual(true);
//        viewport.setMinY(0);
//        viewport.setMaxY(10);
//        viewport.setScrollable(true);
//        series1 = new LineGraphSeries<DataPoint>();
//        series2 = new LineGraphSeries<DataPoint>();
//        graph.addSeries(series1);
//        graph.addSeries(series2);
    }

    public void setUiEnabled(boolean bool)
    {
        startButton.setEnabled(!bool);
        stopButton.setEnabled(bool);

    }

    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
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

    public void onClickStart(View view) {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                beginListenForData();
                textView.setText("\nConnection Opened!\n");
            }
        }
    }

    public void onClickCalibrate(View view){
        isCalibrating = true;
        startTime = System.nanoTime();
        doUpdate("Calibrating");
    }

    private void addEntry(double num, double aspiration) {
//        series1.appendData(new DataPoint(series1lastX++, num), true, 100);
//
//        series2.appendData(new DataPoint(series2lastX++, aspiration), true, 100);
    }


    void beginListenForData()
    {
        textView.setText("Listen for data\n");
        final Handler handler = new Handler();
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
                                        final double numberNeg = format.parse(string).doubleValue();
                                        final double number = abs(numberNeg);
                 /////////////BEFORE CALIBRATION/////////////////
                                        if (!isCalibrating&& !hasCallibrated) {
                                            doUpdate("Connected! Ready to Calibrate");
                                            //doUpdate(string);
                                        }
                //////////////////////////CALIBRATION//////////////////
                                        if(isCalibrating && !hasCallibrated) {
                                            doUpdate("Calibration will last for 15 seconds.");
                                            if(number > maxBreath) {
                                                maxBreath = number;
                                            }
                                            if(number < minBreath) {
                                                minBreath = number;
                                            }
                                            long timePassed = System.nanoTime() - startTime;
                                            if(timePassed > calibrateTime) {
                                                isCalibrating = false;
                                                hasCallibrated = true;
                                            }
                                         //   addEntry(number, 0);
                                        }
                //////////////////////////POST CALIBRATION///////////////////////////
                                        if(hasCallibrated){
                                            // double currTime = System.nanoTime() - startTime;
                                            final double calibrated = 10 * ((number - maxBreath) / (minBreath - maxBreath));
                                            double currTime = System.nanoTime() - startTime;
                                            double aspiration = amplitude*(Math.sin(angularFrequency * currTime/1000000000)) + baseline;
                                         //   addEntry(calibrated, aspiration);
                                            doUpdate("Calibrated");

                                        }



                                    } //catch(ParseException e) {
                                    catch(Exception e) {
                                        doUpdate(e.getMessage());
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
                        textView.setText("Stop thread\n");
                    }
                }
            }
        });

        thread.start();
    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.setText("\nSent Data:"+string+"\n");

    }

    public void onClickStop(View view) throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected=false;
//        graph.removeAllSeries();
        textView.setText("\nConnection Closed!\n");
    }

    public void onClickClear(View view) {
        textView.setText("");
    }



    private void doUpdate(String string) {
        textView.setText(string);
    }

}
