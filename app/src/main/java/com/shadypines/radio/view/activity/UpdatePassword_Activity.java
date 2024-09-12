package com.shadypines.radio.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePassword_Activity extends AppCompatActivity {

    private ImageView imageView_close;

    private EditText editText_newPassword;
    private  TextView editText_current_pass;

    private String message="";
    private TextView editText_current_email;
    String current_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password_);

        imageView_close=(ImageView)findViewById(R.id.imageviw_upPass);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             finish();
            }
        });

        editText_current_pass=(TextView)findViewById(R.id.edit_text_currentPassword);
        String current_pass = SharedPrefUtils.INSTANCE.readString("password");
        editText_current_pass.setEnabled(false);
        editText_current_pass.setText(current_pass);




        editText_newPassword=(EditText)findViewById(R.id.edit_new_password);

        editText_current_email = (TextView) findViewById(R.id.nt_email);
        current_email = SharedPrefUtils.INSTANCE.readString("email");
        editText_current_email.setText(current_email);
        editText_current_email.setEnabled(false);




    }


    public void Save_password(View view) {

        if (editText_current_pass.getText().toString().isEmpty()){

            editText_current_pass.setError("Please Your Current password");
            editText_current_pass.requestFocus();

        }else if(editText_newPassword.getText().toString().isEmpty()) {

            editText_newPassword.setError("Please Your New password");
            editText_newPassword.requestFocus();
        }else {

            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Please Wite...");
            progressDialog.show();

            String  new_passw =editText_newPassword.getText().toString();
            String  current_passw =editText_current_pass.getText().toString();



            StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://shadypinesradio.herokuapp.com/api/change-password/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObMain=new JSONObject(response);

                        message=jsonObMain.getString("message");

                        if(message.contains("Password changing successful.")){

                            editText_newPassword.setText("");



                            progressDialog.dismiss();

                   Toast.makeText(UpdatePassword_Activity.this, message, Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                     Toast.makeText(UpdatePassword_Activity.this, message, Toast.LENGTH_LONG).show();
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
                    hashMap.put("current_password",current_passw);
                    hashMap.put("new_password",new_passw);

                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(UpdatePassword_Activity.this);
            requestQueue.add(stringRequest);

        }
    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(UpdatePassword_Activity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(UpdatePassword_Activity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(UpdatePassword_Activity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(UpdatePassword_Activity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(UpdatePassword_Activity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(UpdatePassword_Activity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}