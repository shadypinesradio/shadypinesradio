package com.shadypines.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shadypines.radio.databinding.SubscribersListItemBinding
import com.shadypines.radio.model.Subscriber
import com.shadypines.radio.model.SubscribersListModel

class SubscribersAdapter(private val suggestList: List<Subscriber>, private val onClick: (String) -> Unit) : RecyclerView.Adapter<SubscribersAdapter.AutomateAdapterViewHolder>() {

    class AutomateAdapterViewHolder(val binding: SubscribersListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutomateAdapterViewHolder {
        val binding = SubscribersListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AutomateAdapterViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AutomateAdapterViewHolder, position: Int) {
        val suggestApp = suggestList[position]
        holder.binding.name.text = suggestApp.user.name
        if (suggestApp.user.username == "") {
            holder.binding.username.text = "---------"
        } else {
            holder.binding.username.text = suggestApp.user.username
        }

        holder.binding.joinDate.text = "Join date: " + suggestApp.user.create_at.split("T")[0]
        holder.binding.apply {
            // Bind data to your views

            root.setOnClickListener {
                onClick("")
            }
        }
    }

    override fun getItemCount() = suggestList.size

}