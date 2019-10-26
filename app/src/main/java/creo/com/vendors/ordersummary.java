package creo.com.vendors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import creo.com.vendors.utils.ApiClient;
import creo.com.vendors.utils.SessionManager;

public class ordersummary extends AppCompatActivity {

    TextView makepayment, total;
    ScrollView scrollView;
    Activity activity = this;
    RecyclerView recyclerView;
    PopupWindow popupWindow;
    ImageView back;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    String token = null;
    ArrayList<String> name = new ArrayList<>();
    public List<CartPojo> pjo = new ArrayList<>();
    String payment_mode= "null";
    Context context = this;
    ArrayList<String>total_price = new ArrayList<>();
    public int i = 0;
    JSONArray arr = new JSONArray();
    JSONObject products = new JSONObject();
    public String orderid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersummary);
     //   Window window = activity.getWindow();
        progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        // clear FLAG_TRANSLUCENT_STATUS flag:
       /* window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.black));*/
        relativeLayout = findViewById(R.id.relative);
        scrollView = findViewById(R.id.scrollview);
        sessionManager = new SessionManager(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersummary.super.onBackPressed();
            }
        });
        total = findViewById(R.id.total);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        recyclerView = findViewById(R.id.recyclerview);
        Log.d("ajkd","bhjnkm"+ ApiClient.directid);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        token = (pref.getString("token", ""));

        try {
            HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
            for (int i = 0; i < ApiClient.productB2BPojo.size(); i++) {
                if (!(ApiClient.productB2BPojo.get(i).getQuantity().equals("0"))){
                    JSONObject json = new JSONObject();
                    json.put("id", ApiClient.productB2BPojo.get(i).getId());
                    json.put("price", ApiClient.productB2BPojo.get(i).getPrice());
                    total_price.add(ApiClient.productB2BPojo.get(i).getPrice());
                    Log.d("quantityhgg","qty"+ApiClient.productB2BPojo.get(i).getQuantity());
                    json.put("quantity", ApiClient.productB2BPojo.get(i).getQuantity());
                    json.put("refer_token", "");
                    map.put("json" + i, json);
                    arr.put(map.get("json" + i));
                }
            }

            products.put("product", arr);
            //   Log.d("Thejsonstringis " ,"dsfs"+products.length());

            Log.d("size","mm"+ApiClient.productB2BPojo.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int value = 0;

        for (int i = 0; i < ApiClient.productB2BPojo.size(); i++) {

            String price[] = ApiClient.productB2BPojo.get(i).getPrice().split("₹ ");



            value += Integer.parseInt(price[0])*Integer.parseInt(ApiClient.productB2BPojo.get(i).getQuantity());
     //       Log.d("kjbjhvhcv", "jvghcgfc" + ApiClient.productB2BPojo.get(0).getQuantity()+Integer.parseInt(price[1]));

        }
        total.setText("₹ "+String.valueOf(value));


       // Checkout.preload(getApplicationContext());
        makepayment = findViewById(R.id.gotopayment);

        for (int i = 0; i < ApiClient.productB2BPojo.size(); i++) {
            if (!(ApiClient.productB2BPojo.get(i).getQuantity().equals("0"))) {
                CartPojo productPojo = new CartPojo(ApiClient.productB2BPojo.get(i).getProduct_name(), ApiClient.productB2BPojo.get(i).getPrice(), ApiClient.productB2BPojo.get(i).getImage(), "Quantity : " + ApiClient.productB2BPojo.get(i).getQuantity());
                pjo.add(productPojo);
            }
        }

        SummaryAdapter cartAdapter = new SummaryAdapter(pjo, context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(cartAdapter);
        makepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater) ordersummary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.payment_mode, null);
                RadioButton cod = customView.findViewById(R.id.cod);
                RadioButton online = customView.findViewById(R.id.online);
                cod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payment_mode = "COD";
                    }
                });
//                online.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        payment_mode = "ONLINE";
//                    }
//                });
                Button closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                TextView apply = customView.findViewById(R.id.apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(payment_mode.equals("null")){
                            Toast.makeText(ordersummary.this,"Please select the mode of payment",Toast.LENGTH_SHORT).show();
                        }

                        if(payment_mode.equals("COD")) {

                            //checkavail();
                            showProgressDialogWithTitle("Confirming order..");
                          create_order(payment_mode);
                        }
//                        if(payment_mode.equals("ONLINE")){
//
//                            //checkavail_online();
////                            Intent intent = new Intent(OrderSummary.this,MakePayment.class);
////                            intent.putExtra("products",products.toString());
////                            intent.putExtra("address",ApiClient.address);
////                            intent.putExtra("payment_method",payment_mode);
////                            ApiClient.token = token;
////                            intent.putExtra("token",token);
////                            startActivity(intent);
//                        }
                    }
                });

                popupWindow = new PopupWindow(customView, ViewPager.LayoutParams.MATCH_PARENT, 1000);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));
                relativeLayout.setAlpha(0.2F);

                //display the popup window
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        relativeLayout.setAlpha(1.0F);
                    }
                });

            }


        });
    }


    private void showProgressDialogWithTitle(String substring) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }

    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }

    /*void checkavail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.BASE_URL+"order_details/check_pin_payment/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("availresss","ordr"+ApiClient.address);
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if(message.equals("Success")){
                                //  Toast.makeText(OrderSummary.this,"hhh"+jsonObject,Toast.LENGTH_SHORT).show();
                            //    create_order(payment_mode);
                            }
                            if(message.equals("Failed")){
                                Toast.makeText(ordersummary.this,"Delivery not available to this location",Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ordersummary.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("product",products.toString());
                params.put("address_id",ApiClient.address);

                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization","Token "+token);
                return params;

            }
        };

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/

    public  void create_order(final String payment_mode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiClient.BASE_URL+"manual_sel/order_manual_det/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Log.d("orderresponse","ordr"+response);
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("message").toString().equals("Success")){
                                popupWindow.dismiss();
                                relativeLayout.setAlpha(1.0F);
                                hideProgressDialogWithTitle();
                               // startActivity(new Intent(ordersummary.this,DirectSellOrderSuccess.class));
                                Toast.makeText(ordersummary.this,"Success",Toast.LENGTH_SHORT).show();
                            }
                            if (jsonObject.optString("message").toString().equals("Failed")){
                                popupWindow.dismiss();
                                relativeLayout.setAlpha(1.0F);
                                hideProgressDialogWithTitle();
                                Toast.makeText(ordersummary.this,"Failed",Toast.LENGTH_SHORT).show();}
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ordersummary.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("product",products.toString());
                params.put("id",sessionManager.getID());
                params.put("payment_status","complete");
                params.put("payment_method",payment_mode);

                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization","Token "+sessionManager.getTokens());
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



}
