package com.example.coffeeapp.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeeapp.Adapter.ItemListCategoryAdapter
import com.example.coffeeapp.R
import com.example.coffeeapp.ViewModel.MainViewModel
import com.example.coffeeapp.databinding.ActivityItemsListBinding

class ItemsListActivity : AppCompatActivity() {

    lateinit var binding : ActivityItemsListBinding
    private val viewModel = MainViewModel()
    private var id : String = ""
    private var title : String = ""


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("EMBED", "ItemsListActivity created")
        enableEdgeToEdge()
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundles()
        initList()
    }

    private fun initList() {
        binding.apply {
            progressBar2.visibility = View.VISIBLE
            viewModel.loadItems(id).observe(this@ItemsListActivity, Observer{
                recyclerView.layoutManager = GridLayoutManager(this@ItemsListActivity, 2)
                recyclerView.adapter = ItemListCategoryAdapter(it)
                progressBar2.visibility = View.GONE
            })
            backBtn.setOnClickListener { finish() }
        }
    }

    private fun getBundles(){
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!

        binding.categoryTxt.text = title

    }
}