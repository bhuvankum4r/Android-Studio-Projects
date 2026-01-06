package com.example.todo_list

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todo : MutableList<Todo>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent : ViewGroup,
        viewType : Int
    ) : TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    private fun toggleStrikeThrough(tvToDoTitle : TextView, isChecked : Boolean) {
        if(isChecked){
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
        else{
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addTodo(todos : Todo){
        todo.add(todos)
        notifyItemInserted(todo.size - 1)
    }

    fun deleteDoneTodos(){
        todo.removeAll{ todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todo[position]
        val tvToDoTitle = holder.itemView.findViewById<TextView>(R.id.tvToDoTitle)
        val cbDone = holder.itemView.findViewById<CheckBox>(R.id.cbDone)

        tvToDoTitle.text = curTodo.title
        cbDone.isChecked = curTodo.isChecked

        toggleStrikeThrough(tvToDoTitle, curTodo.isChecked)
        cbDone.setOnCheckedChangeListener { _, isChecked ->
            toggleStrikeThrough(tvToDoTitle,curTodo.isChecked)
            curTodo.isChecked = !curTodo.isChecked
        }
    }

    override fun getItemCount() : Int {
        return todo.size
    }
}