package com.shadypines.radio.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deshbord_MainActivity extends AppCompatActivity {

    private List<Djs_model> djs_modelList = new ArrayList<>();

    private DjAapter dj_apter;

    private RecyclerView recyclerView_dj;

    LinearLayout linearLayout;
    private Toolbar toolbar;

    private String message;

    String id, showName,djName,showDescription,subscribers,
            pushTitle,pushMessage,defaultPushTitle,defaultPushMessage,image;
    Boolean is_temporary_music_recognition;

    private TextView textView_adminPanal;

    private ProgressDialog progressDialog;
    ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshbord_main);

        backIv = findViewById(R.id.backIv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressDialog=new ProgressDialog(this);
       // progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        recyclerView_dj=(RecyclerView)findViewById(R.id.recyclerview_djDeshbord);
        recyclerView_dj.setHasFixedSize(true);
        linearLayout = (LinearLayout)findViewById(R.id.admin_layout);
        recyclerView_dj.setLayoutManager(new LinearLayoutManager(this));
        String email= SharedPrefUtils.INSTANCE.readString("email");
        boolean admin= SharedPrefUtils.INSTANCE.readBoolean("isAdmin");
        if (admin) {
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }

       textView_adminPanal=(TextView)findViewById(R.id.text_admin_panal);

       textView_adminPanal.setOnClickListener(v -> {
           String url="https://shadypinesradio.herokuapp.com/admin/login/?next=/admin/";
           Intent intent=new Intent(Deshbord_MainActivity.this,Admin_panal_Activity.class);
           intent.putExtra("url",url);
           startActivity(intent);
       });

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://shadypinesradio.herokuapp.com/api/my-shows/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObMain=new JSONObject(response);

                    message=jsonObMain.getString("message");

                    if(message.contains("Date get Successful.")){

                        JSONArray jsonArray= jsonObMain.getJSONArray("data");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            id= jsonObject.getString("id");
                            showName= jsonObject.getString("showName");
                            djName= jsonObject.getString("djName");
                            //showDescription= jsonObject.getString("showDescription");
                            subscribers= jsonObject.getString("subscribers");
                            pushTitle= jsonObject.getString("pushTitle");
                            pushMessage= jsonObject.getString("pushMessage");
                            defaultPushTitle= jsonObject.getString("defaultPushTitle");
                            defaultPushMessage= jsonObject.getString("defaultPushMessage");
                            image= jsonObject.getString("image");
                            is_temporary_music_recognition= jsonObject.getBoolean("is_temporary_music_recognition");

     Djs_model  item=new Djs_model(id,showName,djName,subscribers,pushTitle,
             pushMessage,defaultPushTitle,defaultPushMessage,image, is_temporary_music_recognition);

                    djs_modelList.add(item);

                    }
               dj_apter=new DjAapter(getApplicationContext(), djs_modelList);

                recyclerView_dj.setAdapter(dj_apter);
                        progressDialog.dismiss();

                    }else{
                        Toast.makeText(Deshbord_MainActivity.this, "message:"+message, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
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
                hashMap.put("email",email);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Deshbord_MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void netWorkError(VolleyError error) {
        progressDialog.dismiss();

        if (error instanceof NetworkError) {
            Toast.makeText(Deshbord_MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(Deshbord_MainActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(Deshbord_MainActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(Deshbord_MainActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(Deshbord_MainActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(Deshbord_MainActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}