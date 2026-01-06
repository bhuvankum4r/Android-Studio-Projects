
// app/src/main/java/com/example/contactsviewer/ViewerViewModel.kt
package com.example.contactsviewer

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.contactsviewer.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class QueryDiagnostics(
    val usingUri: String,
    val literalCount: Int?,
    val literalColumns: List<String>?,
    val contractCount: Int?,
    val contractColumns: List<String>?,
    val projectedCount: Int?,
    val error: String?
)

class ViewerViewModel(application: Application) : AndroidViewModel(application) {
    private val _contacts = MutableLiveData<List<ContactUi>>()
    val contacts : LiveData<List<ContactUi>> = _contacts

    private val _diagnostics = MutableLiveData<QueryDiagnostics>()
    val diagnostics : LiveData<QueryDiagnostics> = _diagnostics

    fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val resolver = getApplication<Application>().contentResolver

            val literalUri = Uri.parse("content://com.example.contacts.provider/contacts")
            val contractUri = ContactsContract.CONTENT_URI
            val projection = arrayOf(
                ContactsContract.COL_ID,
                ContactsContract.COL_IMAGE_URI,
                ContactsContract.COL_NAME,
                ContactsContract.COL_PHONE,
                ContactsContract.COL_EMAIL
            )

            Log.d("ViewerVM", "Using Contract URI: $contractUri")

            var literalCount : Int? = null
            var contractCount : Int? = null
            var projectedCount : Int? = null
            var literalColumns : List<String>? = null
            var contractColumns : List<String>? = null
            var errorMsg : String? = null

            // 1) Literal URI test
            try {
                resolver.query(literalUri, null, null, null, null)?.use { c ->
                    literalCount = c.count
                    literalColumns = c.columnNames?.toList()
                    Log.d(
                        "ViewerVM",
                        "LiteralURI count=$literalCount, columns=${literalColumns?.joinToString()}"
                    )
                }
            } catch (t : Throwable) {
                errorMsg = "Literal URI query failed: ${t.message}"
                Log.e("ViewerVM", "Literal URI query error", t)
            }

            // 2) Contract URI test (null projection)
            try {
                resolver.query(contractUri, null, null, null, null)?.use { c ->
                    contractCount = c.count
                    contractColumns = c.columnNames?.toList()
                    Log.d(
                        "ViewerVM",
                        "ContractURI(null proj) count=$contractCount, columns=${contractColumns?.joinToString()}"
                    )
                }
            } catch (t : Throwable) {
                errorMsg = "Contract URI (null projection) query failed: ${t.message}"
                Log.e("ViewerVM", "Contract URI (null projection) error", t)
            }

            // 3) Contract URI with explicit projection and sort
            val list = mutableListOf<ContactUi>()
            try {
                resolver.query(
                    contractUri,
                    projection,
                    null, null,
                    "${ContactsContract.COL_NAME} ASC"
                )?.use { c ->
                    projectedCount = c.count
                    Log.d("ViewerVM", "ContractURI(projected) count=$projectedCount")

                    val idxId = c.getColumnIndexOrThrow(ContactsContract.COL_ID)
                    val idxImage = c.getColumnIndexOrThrow(ContactsContract.COL_IMAGE_URI)
                    val idxName = c.getColumnIndexOrThrow(ContactsContract.COL_NAME)
                    val idxPhone = c.getColumnIndexOrThrow(ContactsContract.COL_PHONE)
                    val idxEmail = c.getColumnIndexOrThrow(ContactsContract.COL_EMAIL)

                    while (c.moveToNext()) {
                        val item = ContactUi(
                            id = c.getLong(idxId),
                            imageUri = if (!c.isNull(idxImage)) c.getString(idxImage) else null,
                            name = c.getString(idxName),
                            phone = if (!c.isNull(idxPhone)) c.getString(idxPhone) else null,
                            email = if (!c.isNull(idxEmail)) c.getString(idxEmail) else null
                        )
                        Log.d("ViewerVM", "Row: $item")
                        list.add(item)
                    }
                }
            } catch (t : Throwable) {
                errorMsg = "Projected query failed: ${t.message}"
                Log.e("ViewerVM", "Projected query error", t)
            }

            Log.d("ViewerVM", "Posting list size=${list.size}")

            _diagnostics.postValue(
                QueryDiagnostics(
                    usingUri = contractUri.toString(),
                    literalCount = literalCount,
                    literalColumns = literalColumns,
                    contractCount = contractCount,
                    contractColumns = contractColumns,
                    projectedCount = projectedCount,
                    error = errorMsg
                )
            )
            _contacts.postValue(list)
        }
    }
}