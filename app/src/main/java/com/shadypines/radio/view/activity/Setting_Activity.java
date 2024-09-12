package com.shadypines.radio.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shadypines.radio.R;
import com.shadypines.radio.SharedPrefUtils;
import com.shadypines.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Setting_Activity extends AppCompatActivity {

    private TextView textView_userName,textView_email,textView_password;

    private ImageView imageView_close;

    private AlertDialog alertDialog;

    private LayoutInflater layoutInflater;

    private String message="";

    private boolean isEmali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);

        textView_userName=(TextView)findViewById(R.id.text_userName);
        textView_email=(TextView)findViewById(R.id.text_userEmail);
        textView_password=(TextView)findViewById(R.id.text_userPass);

        isEmali =SharedPrefUtils.INSTANCE.readBoolean("isEmail");

        textView_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isEmali==true){

                    Intent intent=new Intent(Setting_Activity.this,Update_UserNameActivity.class);
                    startActivity(intent);

                }else {

                    AlertDialog.Builder aBuilder=new AlertDialog.Builder(Setting_Activity.this);
                    aBuilder.setMessage("You're not Able to  Update User Name")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    aBuilder.show();

                }

            }
        });


        textView_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isEmali==true){

                    Intent intent=new Intent(Setting_Activity.this,UpdateEmail_Activity.class);
                    startActivity(intent);

                }else {

                    AlertDialog.Builder aBuilder=new AlertDialog.Builder(Setting_Activity.this);
                    aBuilder.setMessage("You're not Able to  Update Email!!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    aBuilder.show();

                }


            }
        });

        textView_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmali==true){

                    Intent intent=new Intent(Setting_Activity.this,UpdatePassword_Activity.class);
                    startActivity(intent);

                }else {

                    AlertDialog.Builder aBuilder=new AlertDialog.Builder(Setting_Activity.this);
                    aBuilder.setMessage("You're not Able to  Update Password!!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    aBuilder.show();

                }

            }
        });


        imageView_close=(ImageView)findViewById(R.id.imageView_closeSetting);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void Delete_Account(View view) {

        AlertDialog.Builder aBuilder=new AlertDialog.Builder(Setting_Activity.this);
        aBuilder.setTitle("Are your Sure ??");
        aBuilder.setMessage("Do You want Delete Your Shady Pines Radio Account")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Delete_Account();
                    }
                });

        aBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        aBuilder.show();



       /* alertDialog=new AlertDialog.Builder(Setting_Activity.this).create();

        alertDialog.setCancelable(false);
        alertDialog.setTitle("Are your Sure ??");

        layoutInflater=getLayoutInflater();

        view=layoutInflater.inflate(R.layout.delete_account,null);
        alertDialog.setView(view);

        Button button_cencle=(Button)view.findViewById(R.id.btn_cancel);

        Button button_ok=(Button)view.findViewById(R.id.btn_okay);

        button_cencle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();*/
    }


    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(Setting_Activity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(Setting_Activity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(Setting_Activity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(Setting_Activity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(Setting_Activity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(Setting_Activity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }


    private void Delete_Account(){

        ProgressDialog progressDialog=new ProgressDialog(this);
        // progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wite...");
        progressDialog.show();

        String current_email= SharedPrefUtils.INSTANCE.readString("email");


        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://shadypinesradio.herokuapp.com/api/delete-account/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObMain=new JSONObject(response);

                    message=jsonObMain.getString("message");

                    if(message.contains("Account Delete Successful.")){

                      SharedPrefUtils.INSTANCE.delete("email");
                      SharedPrefUtils.INSTANCE.delete("username");

                      progressDialog.dismiss();

                        Toast.makeText(Setting_Activity.this, message, Toast.LENGTH_SHORT).show();
                    }else{

                        progressDialog.dismiss();
                        Toast.makeText(Setting_Activity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                /// Toast.makeText(Login_Activity.this, "error:"+error.getMessage(), Toast.LENGTH_LONG).show();
                netWorkError(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email",current_email);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Setting_Activity.this);
        requestQueue.add(stringRequest);

    }
}