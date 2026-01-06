package com.example.displaysystemcontacts

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.displaysystemcontacts.ui.theme.DisplaySystemContactsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactAppUI()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactAppUI(){

    val permissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ){
            when{
                permissionState.status.isGranted -> {
                    ContactsListUI()
                }

                permissionState.status.shouldShowRationale -> {
                    Column(Modifier.padding(16.dp)){
                        Text("This app needs permission to show your contacts")
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { permissionState.launchPermissionRequest() }) {
                            Text("Allow Permission")
                        }
                    }
                }

                else -> {
                    Column(Modifier.padding(16.dp)){
                        Text("This app needs permission to show your contacts")
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { permissionState.launchPermissionRequest() }) {
                            Text("Grant")
                        }
                    }
                }
            }
    }
}

@Composable
fun ContactsListUI(){
    val context = LocalContext.current
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }

    LaunchedEffect(true) {
        contacts = getPhoneContacts(context)
    }

    if(contacts.isEmpty()){
        Text("No Contacts found",
        modifier = Modifier.padding(20.dp)
        )
    }
    else{
        LazyColumn(Modifier.fillMaxSize()) {
            items(contacts){ contact ->
                ContactRow(contact)
            }
        }
    }
}

@Composable
fun ContactRow(contact : Contact){
    Column(Modifier.padding(16.dp)) {
        Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
        Text(text = contact.phone, style = MaterialTheme.typography.bodyMedium)
        Divider(Modifier.padding(vertical = 8.dp))
    }
}