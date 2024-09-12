package com.shadypines.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.animation.content.Content;
import com.shadypines.radio.R;
import com.shadypines.radio.api.ApiInterface;
import com.shadypines.radio.model.RecoModel;
import com.shadypines.radio.view.activity.Deshbord_UpdateActivity;
import com.shadypines.radio.view.activity.SUbscribersListActivity;
import com.shadypines.radio.view.authentication.signin.model.Djs_model;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import retrofit2.Retrofit;

public class DjAapter extends RecyclerView.Adapter<DjAapter.MyviewHolder> {

    private Context context;

    private List<Djs_model>djs_modelList;

    public DjAapter(Context context, List<Djs_model> djs_modelList) {
        this.context = context;
        this.djs_modelList = djs_modelList;
    }

    @Override
    public DjAapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view= layoutInflater.inflate(R.layout.dj_simple_layout,parent,false);

        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DjAapter.MyviewHolder myviewHolder, int position) {

        Djs_model item=djs_modelList.get(position);

        myviewHolder.textureView_djName.setText(""+item.getDjName());
        myviewHolder.textureView_showName.setText(""+item.getShowName());
        myviewHolder.text_subscribers.setText(""+item.getSubscribers()+" Subscribers");

        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Picasso.get().load(item.getImage()).into(myviewHolder.imageView);
        }

        myviewHolder.text_subscribers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, SUbscribersListActivity.class);
                intent.putExtra("id",item.getId());

                context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        myviewHolder.linearLayout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, Deshbord_UpdateActivity.class);
              //  intent.putExtra("DjName",item.getDjName());
                intent.putExtra("PushTitle",item.getPushTitle());
                intent.putExtra("PushMessage",item.getPushMessage());
                intent.putExtra("DefaultPushTitle",item.getDefaultPushTitle());
                intent.putExtra("DefaultPushMessage",item.getDefaultPushMessage());
                intent.putExtra("id",item.getId());
                intent.putExtra("image",item.getImage());

                context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });


        if (myviewHolder.toggleSong != null) {
            myviewHolder.toggleSong.setChecked(item.getIs_temporary_music_recognition());
            myviewHolder.toggleSong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Update the model when the switch state changes
                    item.setIs_temporary_music_recognition(isChecked);
                    // Call your API here
                    callYourAPI(item.getId(), isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return djs_modelList.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder{

         private TextView textureView_showName,textureView_djName,text_subscribers;

         private ImageView imageView;

         private LinearLayout linearLayout_edit;
         private Switch toggleSong;



        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.imageview_dj);

            linearLayout_edit=(LinearLayout)itemView.findViewById(R.id.linear_djUpdate);


            text_subscribers=(TextView)itemView.findViewById(R.id.text_subscribers);
            textureView_djName=(TextView)itemView.findViewById(R.id.text_DjName);
            textureView_showName=(TextView)itemView.findViewById(R.id.text_djShowName);
            toggleSong = itemView.findViewById(R.id.toggle_song);
        }
    }

    private void callYourAPI(String id, boolean isChecked) {
        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://shadypinesradio.herokuapp.com/") // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of your API interface
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        // Convert boolean to String ("true"/"false")
        String isActive = isChecked ? "true" : "false";

        // Make the API call
        Call<RecoModel> call = apiInterface.updateToggleState(id, isActive);

        // Enqueue the API call
        call.enqueue(new Callback<RecoModel>() {
            @Override
            public void onResponse(Call<RecoModel> call, Response<RecoModel> response) {
                // Handle API response
                Log.e("toggle", response.toString());
                if (response.isSuccessful()) {
                    RecoModel recoModel = response.body();
                    if (recoModel != null) {
                        // Handle successful response
                        // You can access recoModel.message here
                        Toast.makeText(context, recoModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<RecoModel> call, Throwable t) {
                // Handle API call failure
            }
        });
    }
}
