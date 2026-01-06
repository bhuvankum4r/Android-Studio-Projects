package com.example.todo_list

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private lateinit var todoAdapter : TodoAdapter
    private lateinit var rvTodoItems : RecyclerView
    private lateinit var btnAddTodo : Button
    private lateinit var etToDoTitle : EditText
    private lateinit var btnDeleteDoneTodos : Button


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        rvTodoItems = findViewById(R.id.rvToDoItems)
        todoAdapter = TodoAdapter(mutableListOf())
        btnAddTodo = findViewById(R.id.btnAddToDo)
        etToDoTitle = findViewById(R.id.etToDo)
        btnDeleteDoneTodos = findViewById(R.id.btnDel)


        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        btnAddTodo.setOnClickListener {
            val todoTitle = etToDoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etToDoTitle.text.clear()
            }
        }

        btnDeleteDoneTodos.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
    }
}