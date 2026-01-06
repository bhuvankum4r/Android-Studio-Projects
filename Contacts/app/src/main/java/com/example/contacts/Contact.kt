
package com.example.contacts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imageUri: String?,   // store Uri.toString() or file path
    val name: String,
    val phone: String?,
    val email: String?
)
