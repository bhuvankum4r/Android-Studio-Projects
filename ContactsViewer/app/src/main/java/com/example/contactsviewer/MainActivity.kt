
// app/src/main/java/com/example/contactsviewer/MainActivity.kt
package com.example.contactsviewer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val vm by viewModels<ViewerViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contacts by vm.contacts.observeAsState(emptyList())
            val diag by vm.diagnostics.observeAsState()

            LaunchedEffect(Unit) { vm.loadContacts() }

            LaunchedEffect(contacts.size) {
                Log.d("ViewerUI", "Observed contacts size=${contacts.size}")
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Contacts Viewer") },
                        actions = {
                            TextButton(onClick = { vm.loadContacts() }) {
                                Text("Reload")
                            }
                        }
                    )
                }
            ) { padding ->
                Column(Modifier.padding(padding).fillMaxSize()) {
                    DiagnosticsPanel(diag)

                    Divider()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(contacts, key = { it.id }) { contact ->
                            ContactCard(contact)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagnosticsPanel(diag: QueryDiagnostics?) {
    Column(Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Diagnostics", style = MaterialTheme.typography.titleMedium)
        Text("Using URI: ${diag?.usingUri ?: "(unknown)"}")
        Text("Literal count: ${diag?.literalCount ?: "?"}")
        Text("Literal columns: ${diag?.literalColumns?.joinToString() ?: "(none)"}")
        Text("Contract count (null projection): ${diag?.contractCount ?: "?"}")
        Text("Contract columns: ${diag?.contractColumns?.joinToString() ?: "(none)"}")
        Text("Projected count: ${diag?.projectedCount ?: "?"}")
        if (!diag?.error.isNullOrBlank()) {
            Text("Error: ${diag?.error}", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun ContactCard(contact: ContactUi) {
    // Start with text-only to eliminate image issues first.
    // Once counts > 0 and text shows, you can add Coil to load imageUri (must be content://)
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(contact.name, fontWeight = FontWeight.Bold)
            contact.phone?.let { Text("Phone: $it") }
            contact.email?.let { Text("Email: $it") }
        }
    }
}
