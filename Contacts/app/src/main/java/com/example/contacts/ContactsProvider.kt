
package com.example.contacts

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.example.contacts.provider.ContactsContract

class ContactsProvider : ContentProvider() {

    companion object {
        private const val CONTACTS = 1       // list: /contacts
        private const val CONTACT_ID = 2     // item: /contacts/#
    }

    private lateinit var db: ContactDatabase
    private lateinit var dao: ContactDao

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(ContactsContract.AUTHORITY, ContactsContract.PATH_CONTACTS, CONTACTS)
        addURI(ContactsContract.AUTHORITY, "${ContactsContract.PATH_CONTACTS}/#", CONTACT_ID)
    }


    override fun onCreate(): Boolean {
        val ctx = requireNotNull(context)
        db = ContactDatabase.build(ctx)   // <-- use the builder with migrations
        dao = db.contactDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = when (uriMatcher.match(uri)) {
            CONTACTS -> dao.getContactsCursor()
            CONTACT_ID -> {
                val id = ContentUris.parseId(uri)
                dao.getContactCursorById(id)
            }
            else -> null
        }
        cursor?.setNotificationUri(requireNotNull(context).contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        CONTACTS -> "vnd.android.cursor.dir/vnd.${ContactsContract.AUTHORITY}.${ContactsContract.PATH_CONTACTS}"
        CONTACT_ID -> "vnd.android.cursor.item/vnd.${ContactsContract.AUTHORITY}.${ContactsContract.PATH_CONTACTS}"
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != CONTACTS || values == null) return null
        val entity = Contact(
            id = 0L,
            imageUri = values.getAsString(ContactsContract.COL_IMAGE_URI),
            name = values.getAsString(ContactsContract.COL_NAME) ?: "",
            phone = values.getAsString(ContactsContract.COL_PHONE),
            email = values.getAsString(ContactsContract.COL_EMAIL)
        )
        val id = dao.insertProvider(entity)
        val newUri = ContentUris.withAppendedId(ContactsContract.CONTENT_URI, id)
        context?.contentResolver?.notifyChange(ContactsContract.CONTENT_URI, null)
        return newUri
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        if (uriMatcher.match(uri) != CONTACT_ID || values == null) return 0
        val id = ContentUris.parseId(uri)
        val existing = dao.getById(id) ?: return 0

        val updated = existing.copy(
            imageUri = values.getAsString(ContactsContract.COL_IMAGE_URI) ?: existing.imageUri,
            name     = values.getAsString(ContactsContract.COL_NAME) ?: existing.name,
            phone    = values.getAsString(ContactsContract.COL_PHONE) ?: existing.phone,
            email    = values.getAsString(ContactsContract.COL_EMAIL) ?: existing.email
        )
        val count = dao.updateProvider(updated)
        if (count > 0) context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        if (uriMatcher.match(uri) != CONTACT_ID) return 0
        val id = ContentUris.parseId(uri)
        val existing = dao.getById(id) ?: return 0
        val count = dao.deleteProvider(existing)
        if (count > 0) context?.contentResolver?.notifyChange(ContactsContract.CONTENT_URI, null)
        return count
    }
}
