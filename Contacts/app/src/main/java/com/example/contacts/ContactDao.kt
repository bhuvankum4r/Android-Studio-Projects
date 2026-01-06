
package com.example.contacts

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    /** App-internal operations (suspend + Flow) **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<Contact>>

    /** Provider-facing operations (Cursor + non-suspend) **/

    // List cursor: IMPORTANT -> id AS _id for Cursor adapters
    @Query("""
        SELECT id AS _id, imageUri, name, phone, email
        FROM contacts
        ORDER BY name ASC
    """)
    fun getContactsCursor(): Cursor

    // Single item cursor for URIs like content://AUTH/contacts/#
    @Query("""
        SELECT id AS _id, imageUri, name, phone, email
        FROM contacts
        WHERE id = :id
        LIMIT 1
    """)
    fun getContactCursorById(id: Long): Cursor

    // Insert for provider (non-suspend so provider can call synchronously)
    @Insert
    fun insertProvider(contact: Contact): Long

    // Update for provider
    @Update
    fun updateProvider(contact: Contact): Int

    // Helper to fetch by id for provider's update/delete
    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getById(id: Long): Contact?

    // Delete for provider using entity (provider resolves id -> entity)
    @Delete
    fun deleteProvider(contact: Contact): Int
}
