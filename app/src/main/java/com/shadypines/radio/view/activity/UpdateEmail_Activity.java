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

public class UpdateEmail_Activity extends AppCompatActivity {

    private ImageView imageView_close;

    private String message = "";
    private TextView editText_current_email;

    private EditText editText_newEmail;
    String current_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email_);

        imageView_close = (ImageView) findViewById(R.id.imageview_emailClose);

        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        editText_current_email = (TextView) findViewById(R.id.edit_current_email);
        current_email = SharedPrefUtils.INSTANCE.readString("email");

        editText_current_email.setText(current_email);
        editText_current_email.setEnabled(false);

        editText_newEmail = (EditText) findViewById(R.id.edit_newEmail);
    }


    public void Save_email(View view) {

        if (editText_current_email.getText().toString().isEmpty()) {

            editText_current_email.setError("Please Your Current Email");
            editText_current_email.requestFocus();

        } else if (editText_newEmail.getText().toString().isEmpty()) {

            editText_newEmail.setError("Please Your New Email");
            editText_newEmail.requestFocus();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            // progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wite...");
            progressDialog.show();


            ;

            String new_Email = editText_newEmail.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://shadypinesradio.herokuapp.com/api/update-email/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObMain = new JSONObject(response);

                        message = jsonObMain.getString("message");

                        if (message.contains("Update Successful.")) {

                            String new_email = editText_newEmail.getText().toString().trim();


                            SharedPrefUtils.INSTANCE.write("email", new_email);

                            editText_current_email.setText("" + new_email);

                            editText_newEmail.setText("");
                            progressDialog.dismiss();
                            Toast.makeText(UpdateEmail_Activity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();

                            Toast.makeText(UpdateEmail_Activity.this, message, Toast.LENGTH_LONG).show();
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
            }) {
                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("current_email", current_email);
                    hashMap.put("new_email", new_Email);

                    return hashMap;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(UpdateEmail_Activity.this);
            requestQueue.add(stringRequest);

        }

    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(UpdateEmail_Activity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(UpdateEmail_Activity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(UpdateEmail_Activity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(UpdateEmail_Activity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(UpdateEmail_Activity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(UpdateEmail_Activity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}