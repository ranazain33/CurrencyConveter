package com.capitalwriting.currencyconveter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConvertActivity extends AppCompatActivity {

    private Spinner BaseCurrency;
    private EditText Value;
    private Button Convert;
    private TextView Result;
    private String To="",From="",conversionRate;
    private double ResultRate;

    private JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        Value = (EditText) findViewById(R.id.value);
        Convert = (Button) findViewById(R.id.convert);
        Result = (TextView) findViewById(R.id.result);
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                Toast.makeText(this, "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
                Intent home = new Intent(this,MainActivity.class);
                startActivity(home);
            }else{
                From  = extras.getString("From");
            }
        }

        getSupportActionBar().setTitle("Convert "+From+" to");


        BaseCurrency = (Spinner) findViewById(R.id.baseCurrency);
        final String[] items = new String[]{"--Click to Select---","USD","BTC","EUR","CNY","ETH","CAD","KTW","N","LTC","RUB","XMR","GOLD"
                ,"GBP","CHF","DASH","ZEC","CLP","UGX","KGS","UZS"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, items);
        BaseCurrency.setAdapter(adapter3);
        BaseCurrency.setVisibility(View.VISIBLE);
        BaseCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                To = items[arg2];
                Convert.setClickable(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Convert.setClickable(false);
            }
        });
        Result.setVisibility(View.GONE);
        Convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(To.equals("")||From.equals("")||To.equals("--Select---")){
                    Toast.makeText(ConvertActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    new LoadConversionRate().execute();
                }
            }
        });
    }

    class LoadConversionRate extends AsyncTask<String, String, String> {


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting All articles from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("fsyms", From);
            params.put("tsyms", To);
            // getting JSON string from URL
            JSONObject json = new JSONObject();
            try {
                json = jParser.makeHttpRequest("https://min-api.cryptocompare.com/data/pricemulti", "GET", params);
            } catch (Exception e) {
                //Toast.makeText(context, "A connection could not be established", Toast.LENGTH_LONG).show();
            }
            // Check your log cat for JSON reponse
            //Log.d("All tabs: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                JSONObject jsonObject = json.getJSONObject(From);
                conversionRate = jsonObject.getString(To);
            } catch (JSONException e) {
                Log.d("HELLIX", "printStackTraceD");
            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String result) {
            Log.d("HELLIX", conversionRate);

            ResultRate=Double.parseDouble(conversionRate)*Double.parseDouble(Value.getText().toString());
            Result.setVisibility(View.VISIBLE);

            Result.setText(Double.toString(ResultRate));
        }
    }
}
