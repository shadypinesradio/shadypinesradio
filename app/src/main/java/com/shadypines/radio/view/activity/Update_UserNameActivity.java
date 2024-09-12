package com.shadypines.radio.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.shadypines.radio.R;
import com.shadypines.radio.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Update_UserNameActivity extends AppCompatActivity {

    private ImageView imageView_close;

    private EditText editText_newName;
    private  TextView editText_current_userName;
    String current_userName;

      private String message ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__user_name);

        imageView_close=(ImageView)findViewById(R.id.imageClose_update_name);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        editText_current_userName=(TextView)findViewById(R.id.edit_userOldeName);
        editText_newName=(EditText)findViewById(R.id.edit_newUserName);
        current_userName= SharedPrefUtils.INSTANCE.readString("username");
        editText_current_userName.setText(current_userName);
        editText_current_userName.setEnabled(false);


    }

    public void Save_useName(View view) {

        if (editText_current_userName.getText().toString().isEmpty()){

            editText_current_userName.setError("Please Your Current UserName");
            editText_current_userName.requestFocus();

        }else if(editText_newName.getText().toString().isEmpty()) {

            editText_newName.setError("Please Your New UserName");
            editText_newName.requestFocus();
        }else {

            ProgressDialog progressDialog=new ProgressDialog(this);

            progressDialog.setMessage("Please Wite...");
            progressDialog.show();




            String  new_userName =editText_newName.getText().toString();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://shadypinesradio.herokuapp.com/api/update-username/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObMain=new JSONObject(response);

                        message=jsonObMain.getString("message");

                        if(message.contains("Update Successful.")){

                          String newUserName= editText_newName.getText().toString().trim();

                         SharedPrefUtils.INSTANCE.write("username",newUserName);

                            editText_current_userName.setText(""+newUserName);
                            editText_newName.setText("");

                            progressDialog.dismiss();

                            Toast.makeText(Update_UserNameActivity.this, message, Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Update_UserNameActivity.this, message, Toast.LENGTH_LONG).show();
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
                    hashMap.put("username",current_userName);
                    hashMap.put("new_username",new_userName);

                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(Update_UserNameActivity.this);
            requestQueue.add(stringRequest);

        }
    }



    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(Update_UserNameActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(Update_UserNameActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(Update_UserNameActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(Update_UserNameActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(Update_UserNameActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(Update_UserNameActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}