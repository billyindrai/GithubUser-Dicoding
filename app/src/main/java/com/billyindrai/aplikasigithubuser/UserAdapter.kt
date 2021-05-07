package com.billyindrai.aplikasigithubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.billyindrai.aplikasigithubuser.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.CardViewViewHolder>() {

    private var listUsers = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(dataUser: ArrayList<User>){
        listUsers.clear()
        listUsers.addAll(dataUser)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewViewHolder(binding)
    }
    override fun getItemCount(): Int = listUsers.size
    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }
    inner class CardViewViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(350, 350))
                    .into(imgItemPhoto)
                tvUsername.text = user.username
                tvName.text = user.name

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}