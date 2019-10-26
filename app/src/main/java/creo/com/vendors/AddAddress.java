package creo.com.vendors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import creo.com.vendors.utils.ApiClient;
import creo.com.vendors.utils.Global;
import creo.com.vendors.utils.SessionManager;

public class AddAddress extends AppCompatActivity {
    TextInputEditText name,address,phone_no,landmark,city,state,pincode,area;
    TextView save;
    String device_id = null;
    ImageView imageView;
    Context context= this;
    Spinner spinner;
    private String URLline = Global.BASE_URL+"manual_sel/address_manual_det/";
    private ProgressDialog dialog ;
    Activity activity = this;
    SessionManager sessionManager;
    ArrayList<String> areas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        areas.add("Choose Area");
        dialog=new ProgressDialog(AddAddress.this,R.style.MyAlertDialogStyle);

        spinner = findViewById(R.id.spinner);
        sessionManager = new SessionManager(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(AddAddress.this,"selected"+areas.get(position),Toast.LENGTH_SHORT).show();
                if (areas.get(position).equals("Choose Area")) {
                    area.setText("");
                }
                if (!(areas.get(position).equals("Choose Area"))) {
                    area.setText(areas.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        name=findViewById(R.id.name);
        name.setText(ApiClient.username);
        address=findViewById(R.id.address);
        phone_no=findViewById(R.id.phone);
        landmark=findViewById(R.id.landmark);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        pincode=findViewById(R.id.pincode);
        save=findViewById(R.id.save);
        area = findViewById(R.id.area);
        imageView=findViewById(R.id.imageView3);

        device_id =  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
       // addaddressPresenter=new AddaddressPresenterImpl(this);

        pincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                areas.clear();
                getLocationDetails();
            }
        });
        pincode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                areas.clear();

                return false;
            }
        });

        save.setText("Save & Continue");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().length() < 3){
                    name.setError("Enter a valid name");
                }
                if(phone_no.getText().toString().length() < 10){
                    phone_no.setError("Enter a valid number");
                }
                if(pincode.getText().toString().length() <6 ){
                    pincode.setError("Pincode must be of six digits");
                }
                if(name.getText().toString().length() == 0){
                    name.setError("Name cannot be empty");
                }
                if(address.getText().toString().length() == 0 ){
                    address.setError("Address cannot be empty");
                }
                if (city.getText().toString().length() == 0){
                    city.setError("City cannot be empty");
                }
                if(state.getText().toString().length() == 0){
                    state.setError("State name cannot be empty");
                }
                if(name.getText().toString().length()>=3 && phone_no.getText().toString().length() == 10 && pincode.getText().toString().length() == 6
                        && address.getText().toString().length()!=0 && city.getText().toString().length() !=0 && state.getText().toString().length() !=0)
                {
                    addaddress();
                }
            }
        });
    }

    private void addaddress(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Toast.makeText(AddAddress.this,response,Toast.LENGTH_LONG).show();
                        //parseData(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ot = jsonObject.optString("message");
                            String status=jsonObject.optString("code");
                            Log.d("otp","mm"+ot);
                            if(status.equals("200")){
                                Toast.makeText(AddAddress.this, "Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AddAddress.this, ordersummary.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(AddAddress.this, "Failed."+ot, Toast.LENGTH_LONG).show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response","hhh"+response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddAddress.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",city.getText().toString());
                params.put("landmark",landmark.getText().toString());
                params.put("pincode",pincode.getText().toString());
                params.put("state",state.getText().toString());
                params.put("area",area.getText().toString());
                params.put("house_no",address.getText().toString());
                params.put("id",sessionManager.getID());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token "+sessionManager.getTokens());
                Log.d("token","mm"+sessionManager.getTokens());
                return params;
            }



        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);



    }


    public void getLocationDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://form-api.com/api/geo/country/zip?key=snN7x4rWja5Jm3whmFwQ&country=IN&zipcode="+pincode.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            areas.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject results = jsonObject.getJSONObject("result");
                            String state1 = results.optString("state");
                            String city1 = results.optString("city");
                            String level4area = results.optString("level4area");
                            state.setText(state1);
                            city.setText(city1);
//                  area.setText(level4area);
                            String[] seperated = level4area.split(",");

//                  ArrayList<String> places = new ArrayList<>();
//                  for (int i=0;i<seperated.length;i++){
//                    places.add(seperated[i].replace("[",""));
//                  }
//                  String split = seperated[0].replace("[", "");

                            List<String> list = new ArrayList<String>(Arrays.asList(seperated));
                            ArrayList<String>places = new ArrayList<String>();
                            areas.add("Choose Area");
                            for (int i =0 ;i<list.size();i++){
                                areas.add(list.get(i).replace("[","").replace("\"", "").replace("]",""));
                            }


//                  Log.d("results","result222"+places.size()+places.get(0));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(AddAddress.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                //params.put("search_key",searchView.getQuery().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
}
