
// app/src/main/java/com/example/contactsviewer/provider/ContactsContract.kt
package com.example.contactsviewer.provider

import android.net.Uri

object ContactsContract {
    // MUST match the provider app’s <provider android:authorities="...">
    const val AUTHORITY = "com.example.contacts.provider"

    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/contacts")

    const val COL_ID = "_id"
    const val COL_IMAGE_URI = "imageUri"
    const val COL_NAME = "name"
    const val COL_PHONE = "phone"
    const val COL_EMAIL = "email"
}
