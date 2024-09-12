package com.shadypines.radio.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.shadypines.Adapter.DjAapter;
import com.shadypines.radio.R;
import com.shadypines.radio.SharedPrefUtils;
import com.shadypines.radio.view.authentication.signin.model.Djs_model;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Deshbord_UpdateActivity extends AppCompatActivity {

    private EditText
            textView_pushNotification_title,textView_pushNotificationMessage,
            textView_defaultPushTitle,text_defaultPushMessage;

    private TextView textView_updateNotification,
            textView_weekNotification,textView_defaultNotification;

    private AlertDialog alertDialog;

    private LayoutInflater layoutInflater;
    private View view;

    private ImageView imageView;

    private String message;
    private String id;
    ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshbord__update);

        backIv = findViewById(R.id.backIv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView_pushNotification_title=(EditText) findViewById(R.id.text_pushNotification_title);
        textView_pushNotificationMessage=(EditText)findViewById(R.id.text_pushNotification_message);
        textView_defaultPushTitle=(EditText)findViewById(R.id.text_Submission);
        text_defaultPushMessage=(EditText)findViewById(R.id.text_Shopping);

        imageView=(ImageView) findViewById(R.id.imageview_deshbord);

        textView_updateNotification=(TextView)findViewById(R.id.text_update_notification);

        textView_weekNotification=(TextView)findViewById(R.id.text_weeky_notification);

        textView_defaultNotification=(TextView)findViewById(R.id.text_default_push);


        textView_updateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder aBuilder=new AlertDialog.Builder(Deshbord_UpdateActivity.this);
                aBuilder.setMessage("Every week when your show starts, your subscribers get a push notification reminding them to tune in.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                aBuilder.show();
            }
        });


        textView_defaultNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder aBuilder=new AlertDialog.Builder(Deshbord_UpdateActivity.this);
                aBuilder.setMessage("This message will go out if you don't have a Weekly Push Notification by show time. It should be a more general message about your show that would work on any week.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                aBuilder.show();
            }
        });



        textView_weekNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder aBuilder=new AlertDialog.Builder(Deshbord_UpdateActivity.this);
                aBuilder.setMessage("Say something about this week's show! This message expires after show time.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                aBuilder.show();
            }
        });

        ShwoData();
    }

    private void ShwoData() {

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){

           // bundle.get("DjName");
            textView_pushNotification_title.setText(""+bundle.getString("PushTitle"));
            textView_pushNotificationMessage.setText(""+bundle.getString("PushMessage"));
            textView_defaultPushTitle.setText(""+bundle.getString("DefaultPushTitle"));
            text_defaultPushMessage.setText(""+bundle.getString("DefaultPushMessage"));
              id= bundle.getString("id");

           String image=  bundle.getString("image");

            Picasso.get().load(image).into(imageView);
        }
    }


    public void update_btn(View view) {


//        if(textView_pushNotification_title.getText().toString().isEmpty()){
//
//            textView_pushNotification_title.setError("Push Notication Empty..");
//
//            Toast.makeText(this, "Push Notication Empty..", Toast.LENGTH_SHORT).show();
//
//        }else  if(textView_pushNotificationMessage.getText().toString().isEmpty()){
//
//            textView_pushNotificationMessage.setError("Push Notication Message Empty..");
//
//            Toast.makeText(this, "Push Notication Message Empty..", Toast.LENGTH_SHORT).show();
//
//        }else
            if(textView_defaultPushTitle.getText().toString().isEmpty()){

            textView_defaultPushTitle.setError("Default Push Title Empty..");

            Toast.makeText(this, "Default Push Title  Empty..", Toast.LENGTH_SHORT).show();
        }else  if(text_defaultPushMessage.getText().toString().isEmpty()){

            text_defaultPushMessage.setError("Default Push Title Empty..");

            Toast.makeText(this, "Default Push Message  Empty..", Toast.LENGTH_SHORT).show();
        }else {

           ProgressDialog progressDialog=new ProgressDialog(this);
            // progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wite...");
            progressDialog.show();

            String email= SharedPrefUtils.INSTANCE.readString("email");

           String pushNotification_tilte=  textView_pushNotification_title.getText().toString();
            String pushNotification_messag=  textView_pushNotificationMessage.getText().toString();
             String Default_pushMessage= text_defaultPushMessage.getText().toString();

             String Default_pushTitle= textView_defaultPushTitle.getText().toString();


            StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://shadypinesradio.herokuapp.com/api/push-update/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObMain=new JSONObject(response);

                        message=jsonObMain.getString("message");

                        if(message.contains("Data updated successful.")){

                   /*         textView_pushNotification_title.setText("");
                            textView_pushNotificationMessage.setText("");
                            textView_defaultPushTitle.setText("");
                            text_defaultPushMessage.setText("");*/

              Toast.makeText(Deshbord_UpdateActivity.this, message, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Deshbord_UpdateActivity.this, "message:"+message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
           /// Toast.makeText(Login_Activity.this, "error:"+error.getMessage(), Toast.LENGTH_LONG).show();
                    netWorkError(error);
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> hashMap = new HashMap<String, String>();
                  //  hashMap.put("email",email);
                    hashMap.put("id",id);
                    hashMap.put("pushTitle",pushNotification_tilte);
                    hashMap.put("pushMessage",pushNotification_messag);
                    hashMap.put("defaultPushTitle",Default_pushTitle);
                    hashMap.put("defaultPushMessage",Default_pushMessage);
                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(Deshbord_UpdateActivity.this);
            requestQueue.add(stringRequest);

        }
    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(Deshbord_UpdateActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(Deshbord_UpdateActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(Deshbord_UpdateActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(Deshbord_UpdateActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(Deshbord_UpdateActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(Deshbord_UpdateActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}