package com.example.kodetrainee.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kodetrainee.R
import com.example.kodetrainee.entity.User
import java.text.SimpleDateFormat

class UserRecyclerAdapter(private val context: Context, private var  listener: onItemListener ):
    RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>() {

    var users : List<User> = mutableListOf()

    class ViewHolder(itemView: View, private val listener: onItemListener):
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        var userImage : ImageView = itemView.findViewById(R.id.list_user_img)
        var userName : TextView = itemView.findViewById(R.id.list_user_name)
        var userTitle : TextView = itemView.findViewById(R.id.list_user_title)
        var userBirth : TextView = itemView.findViewById(R.id.list_user_birth)


        override fun onClick(v: View?) {
            userBirth.visibility = View.VISIBLE
            listener.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list_element, parent, false)
        return  ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = users[position].firstName
        holder.userTitle.text = users[position].position
        Glide.with(context)
            .load(users[position].avatarUrl)
            .into(holder.userImage)
        val pattern = "d MMM"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(users[position].birthday!!)
        holder.userBirth.text = date
    }

    override fun getItemCount(): Int {
        return  users.size
    }

    interface onItemListener {
        fun onItemClick(position: Int)
    }
}