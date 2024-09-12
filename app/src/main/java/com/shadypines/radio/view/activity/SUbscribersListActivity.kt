package com.shadypines.radio.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.shadypines.Adapter.SubscribersAdapter
import com.shadypines.radio.databinding.ActivitySubscribersListBinding
import com.shadypines.radio.model.SubscribersListModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class SUbscribersListActivity : AppCompatActivity() {
    lateinit var binding: ActivitySubscribersListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscribersListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        fetchSubscribers()
        binding.backIv.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchSubscribers() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://shadypinesradio.herokuapp.com/api/subscribers-list/"

        val bundle = intent.extras
        val id = bundle?.getString("id") ?: "0"
        val jsonRequest = JSONObject()
        jsonRequest.put("id", id)

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                updateUI(response)
            },
            Response.ErrorListener { error ->
                // Handle Error
                Log.e("Volley Error", error.toString())
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonRequest.toString().toByteArray(Charsets.UTF_8)
            }
        }

        queue.add(stringRequest)
    }

    private fun updateUI(response: String) {
        try {
            val apiResponse = Gson().fromJson(response, SubscribersListModel::class.java)
            val subscribersList = apiResponse.data ?: emptyList()
            Log.d("myres", subscribersList.toString())
            binding.recyclerview.adapter = SubscribersAdapter(subscribersList) {
                // Handle your onClick logic here
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error, maybe show a message to the user or log the error
        }
    }

}