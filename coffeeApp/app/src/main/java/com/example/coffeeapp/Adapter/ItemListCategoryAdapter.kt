package com.example.coffeeapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.UiContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeapp.Activity.DetailActivity
import com.example.coffeeapp.Domain.ItemsModel
import com.example.coffeeapp.databinding.ViewholderItemListBinding
import com.example.coffeeapp.databinding.ViewholderPopularBinding

class ItemListCategoryAdapter(val items : MutableList<ItemsModel>) : RecyclerView.Adapter<ItemListCategoryAdapter.ViewHolder>() {

    lateinit var context : Context
    class ViewHolder( val binding : ViewholderItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent : ViewGroup,
        viewType : Int
    ) : ItemListCategoryAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ItemListCategoryAdapter.ViewHolder, position : Int) {
        holder.binding.titleTxt.text = items[position].title
        holder.binding.priceTxt.text = "$" + items[position].price.toString()
        holder.binding.subtitleTxt.text = items[position].extra.toString()

        Glide.with(context).load(items[position].picUrl[0]).into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity :: class.java)
            intent.putExtra("object", items[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount() : Int = items.size


}