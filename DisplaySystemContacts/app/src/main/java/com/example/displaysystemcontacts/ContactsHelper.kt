package com.example.displaysystemcontacts

import android.content.Context
import android.provider.ContactsContract

fun getPhoneContacts(context : Context) : List<Contact> {

    val contactsList = mutableListOf<Contact>()

    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection,
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use{
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while(it.moveToNext()){
            val name = it.getString(nameIndex) ?: "unknown"
            val phone = it.getString(phoneIndex) ?: "N/A"

            contactsList.add(Contact(name,phone))
        }
    }
    return contactsList
}