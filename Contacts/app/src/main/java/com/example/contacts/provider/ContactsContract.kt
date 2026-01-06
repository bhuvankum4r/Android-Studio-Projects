
package com.example.contacts.provider

import android.net.Uri

object ContactsContract {
    // Must match AndroidManifest provider authorities: "${applicationId}.provider"
    const val AUTHORITY = "com.example.contacts.provider"
    const val PATH_CONTACTS = "contacts"

    // content://com.example.contacts.provider/contacts
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_CONTACTS")

    // Cursor columns (use `_id` for compatibility)
    const val COL_ID = "_id"
    const val COL_IMAGE_URI = "imageUri"
    const val COL_NAME = "name"
    const val COL_PHONE = "phone"
    const val COL_EMAIL = "email"
}
