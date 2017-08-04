package com.example.ivana.rpimonitor;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {
    TextView cpu1;

    TextView mem_percent1, mem_total1, mem_free1, mem_used1;

    TextView hdd_percent1, hdd_used1, hdd_total1, hdd_free1;

    TextView network_rx1, network_tx1;

    Button refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new ConnectedToServer().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Pogreška!");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Aplikacija nije povezana s poslužiteljem.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Poništi",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearTextView();
                    }
                });
        alertDialog.show();
    }

    private void clearTextView() {
        cpu1 = (TextView) findViewById(R.id.cpu);
        mem_free1 = (TextView) findViewById(R.id.mem_free);
        mem_percent1 = (TextView) findViewById(R.id.mem_percent);
        mem_total1 = (TextView) findViewById(R.id.mem_total);
        mem_used1 = (TextView) findViewById(R.id.mem_used);
        hdd_free1 = (TextView) findViewById(R.id.hdd_free);
        hdd_percent1 = (TextView) findViewById(R.id.hdd_percent);
        hdd_total1 = (TextView) findViewById(R.id.hdd_total);
        hdd_used1 = (TextView) findViewById(R.id.hdd_used);
        network_rx1 = (TextView) findViewById(R.id.network_rx);
        network_tx1 = (TextView) findViewById(R.id.network_tx);

        cpu1.setText(null);
        mem_total1.setText(null);
        mem_free1.setText(null);
        mem_percent1.setText(null);
        mem_used1.setText(null);
        hdd_used1.setText(null);
        hdd_total1.setText(null);
        hdd_percent1.setText(null);
        hdd_free1.setText(null);
        network_tx1.setText(null);
        network_rx1.setText(null);
    }

    private void getData() {
        try {
            new JSONParse().execute();
            new ConnectedToServer().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ConnectedToServer extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL myUrl = new URL("http://10.0.0.1/");
                URLConnection connection = myUrl.openConnection();
                connection.setConnectTimeout(500);
                connection.connect();
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer check) {
            if (check == 1) {
                getData();
            } else {
                alert();
            }
        }
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            cpu1 = (TextView) findViewById(R.id.cpu);
            mem_free1 = (TextView) findViewById(R.id.mem_free);
            mem_percent1 = (TextView) findViewById(R.id.mem_percent);
            mem_total1 = (TextView) findViewById(R.id.mem_total);
            mem_used1 = (TextView) findViewById(R.id.mem_used);
            hdd_free1 = (TextView) findViewById(R.id.hdd_free);
            hdd_percent1 = (TextView) findViewById(R.id.hdd_percent);
            hdd_total1 = (TextView) findViewById(R.id.hdd_total);
            hdd_used1 = (TextView) findViewById(R.id.hdd_used);
            network_rx1 = (TextView) findViewById(R.id.network_rx);
            network_tx1 = (TextView) findViewById(R.id.network_tx);
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            return jParser.getJSONFromUrl("http://10.0.0.1/test.php");

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                // Getting JSON Array Values
                String cpu = json.getJSONObject("Params").getString("cpu") + " %";
                String mem_free = json.getJSONObject("Params").getString("mem_free") + " GB";
                String mem_used = json.getJSONObject("Params").getString("mem_used") + " GB";
                String mem_total = json.getJSONObject("Params").getString("mem_total") + " GB";
                String mem_percent = json.getJSONObject("Params").getString("mem_percent") + " %";
                String hdd_free = json.getJSONObject("Params").getString("hdd_free") + " GB";
                String hdd_used = json.getJSONObject("Params").getString("hdd_used") + " GB";
                String hdd_total = json.getJSONObject("Params").getString("hdd_total") + " GB";
                String hdd_percent = json.getJSONObject("Params").getString("hdd_percent") + " %";
                String network_rx = json.getJSONObject("Params").getString("network_rx") + " MB";
                String network_tx = json.getJSONObject("Params").getString("network_tx") + " MB";

                //Set JSON Data in TextView
                cpu1.setText(cpu);
                mem_total1.setText(mem_total);
                mem_free1.setText(mem_free);
                mem_percent1.setText(mem_percent);
                mem_used1.setText(mem_used);
                hdd_used1.setText(hdd_used);
                hdd_total1.setText(hdd_total);
                hdd_percent1.setText(hdd_percent);
                hdd_free1.setText(hdd_free);
                network_tx1.setText(network_tx);
                network_rx1.setText(network_rx);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}