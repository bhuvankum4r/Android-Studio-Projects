package com.example.coffeeapp.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.coffeeapp.Domain.CategoryModel
import com.example.coffeeapp.databinding.ViewholderCategoryBinding
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import com.example.coffeeapp.Activity.ItemsListActivity
import com.example.coffeeapp.R
import com.google.android.material.color.MaterialColors


class CategoryAdapter( val items : MutableList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context : Context
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class ViewHolder(val binding : ViewholderCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent : ViewGroup,
        viewType : Int
    ) : CategoryAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : CategoryAdapter.ViewHolder, position : Int) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, ItemsListActivity::class.java).apply {
                    putExtra("id", item.id.toString())
                    putExtra("title", item.title)
                }
                ContextCompat.startActivity(context, intent, null)
            }, 500)
        }

        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundColor(
                MaterialColors.getColor(
                    holder.binding.root,
                    com.google.android.material.R.attr.colorOnPrimary
                )
            )
            holder.binding.titleCat.setTextColor(
                MaterialColors.getColor(
                    holder.binding.root,
                    com.google.android.material.R.attr.colorOnPrimary
                )
            )
        } else {
            holder.binding.titleCat.setBackgroundColor(
                MaterialColors.getColor(
                    holder.binding.root,
                    com.google.android.material.R.attr.colorSurface
                )
            )
            holder.binding.titleCat.setTextColor(
                MaterialColors.getColor(
                    holder.binding.root,
                    com.google.android.material.R.attr.colorOnSurface
                )
            )
        }

    }

    override fun getItemCount() : Int = items.size
}