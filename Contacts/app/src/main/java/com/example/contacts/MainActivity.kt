//package com.example.contacts
//
//import android.content.Context
//import android.net.Uri
//import android.os.Bundle
//import android.provider.BaseColumns
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.*
//import androidx.lifecycle.ViewModel
//import androidx.navigation.NavController
//import androidx.room.Room
//import com.example.contacts.ui.theme.ContactsTheme
//import java.io.File
//import java.io.FileOutputStream
//import java.io.OutputStream
//import androidx.compose.material3.Icon
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.draw.clip
//import com.example.contacts.ui.theme.GreenJC
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.compose.rememberAsyncImagePainter
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
//import androidx.compose.ui.modifier.modifierLocalConsumer
//import androidx.compose.ui.text.font.FontWeight
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState : Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        val database = Room.databaseBuilder(
//            applicationContext,
//            ContactDatabase :: class.java,
//            "contacts.db"
//        ).build()
//
//        val repository = ContactRepository(database.contactDao())
//
//        val viewModel : ContactViewModel by viewModels { ContactViewModelFactory(repository) }
//
//        setContent {
//            val navController = rememberNavController()
//            NavHost(navController = navController, startDestination = "contactList"){
//                composable("contactList") { ContactListScreen(viewModel, navController) }
//                composable("addContact") { AddContactScreen(viewModel, navController) }
//
//                composable("contactDetail/{contactId}") { backStackEntry ->
//                    val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
//                    val contact = viewModel.allContacts.observeAsState(initial = emptyList()).value.find { it.id.toInt() == contactId }
//                    contact?.let{ ContactDetailScreen(it, viewModel, navController)}
//                }
//
//                composable("editContact/{contactId}") { backStackEntry ->
//                    val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
//                    val contact = viewModel.allContacts.observeAsState(initial = emptyList()).value.find { it.id.toInt() == contactId }
//                    contact?.let{ EditContactScreen(it, viewModel, navController)}
//                }
//
//            }
//        }
//    }
//}
//
//@Composable
//fun ContactIcon(contact : Contact, onClick : () -> Unit){
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 4.dp)
//            .clickable(onClick = onClick),
//        colors = CardDefaults.cardColors(Color.White),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ){
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable(onClick = onClick)
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically)
//        {
//            Image(
//                painter = rememberAsyncImagePainter(contact.imageUri),
//                contentDescription = contact.name,
//                modifier = Modifier
//                    .size(50.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//            Text(contact.name)
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ContactListScreen(viewModel : ContactViewModel, navController : NavController){
//    val context = LocalContext.current.applicationContext
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                modifier = Modifier.height(48.dp),
//                title = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .wrapContentHeight(Alignment.CenterVertically)
//                    ){
//                        Text(text = "Contacts", fontSize = 18.sp)
//                    }
//                }, navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            Toast.makeText(context, "Contacts", Toast.LENGTH_SHORT).show()
//                        }
//                    ) {
//                        Icon(painter = painterResource(R.drawable.contacticon), contentDescription = null)
//                    }
//                }, colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = GreenJC,
//                    titleContentColor = Color.White,
//                    navigationIconContentColor = Color.White
//                )
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                containerColor = GreenJC, onClick = { navController.navigate("addContact") }
//            ) {
//                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contact")
//            }
//        }
//    ) { paddingValues ->
//        val contacts by viewModel.allContacts.observeAsState(initial = emptyList())
//        LazyColumn(modifier = Modifier.padding(paddingValues)){
//            items(contacts) { contact ->
//                ContactIcon(contact = contact){
//                    navController.navigate("contactDetail/${contact.id}")
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddContactScreen(viewModel : ContactViewModel,  navController : NavController) {
//    val context = LocalContext.current.applicationContext
//
//    var imageUri by remember {
//        mutableStateOf<Uri?>(null)
//    }
//
//    var name by remember {
//        mutableStateOf("")
//    }
//
//    var phoneNumber by remember {
//        mutableStateOf("")
//    }
//
//    var email by remember {
//        mutableStateOf("")
//    }
//
//    val launcher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri : Uri? ->
//            imageUri = uri
//        }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                modifier = Modifier.height(48.dp),
//                title = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .wrapContentHeight(Alignment.CenterVertically)
//                    ) {
//                        Text(text = "Add Contact", fontSize = 18.sp)
//                    }
//                }, navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            Toast.makeText(context, "Add Contact", Toast.LENGTH_SHORT).show()
//                        }
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.addcontact),
//                            contentDescription = null
//                        )
//                    }
//                }, colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = GreenJC,
//                    titleContentColor = Color.White,
//                    navigationIconContentColor = Color.White
//                )
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            imageUri?.let { uri ->
//                Image(
//                    painter = rememberAsyncImagePainter(uri),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(128.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//            }
//                Spacer(modifier = Modifier.height(12.dp))
//                Button(onClick = { launcher.launch("image/*") }, colors = ButtonDefaults.buttonColors(GreenJC)) {
//                    Text(text = "Choose Image")
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//
//                TextField(value = name, onValueChange = { name = it},
//                    label = {Text(text = "Name") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(8.dp)),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                TextField(value = phoneNumber, onValueChange = { phoneNumber = it},
//                    label = {Text(text = "Phone Number") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(8.dp)),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                TextField(value = email, onValueChange = { email = it},
//                    label = {Text(text = "Email") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(8.dp)),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        imageUri?.let {
//                            val internalPath = copyUriToInternalStorage(context, it, "$name.jpg")
//                            internalPath?.let{ path ->
//                                viewModel.addContact(path,name,phoneNumber,email)
//                                navController.navigate("contactList"){
//                                    popUpTo(0)
//                                }
//                            }
//                        }
//                    }, colors = ButtonDefaults.buttonColors(GreenJC)) {
//                    Text(text = "Add Contact")
//                }
//
//
//        }
//
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ContactDetailScreen(contact: Contact, viewModel : ContactViewModel, navController : NavController){
//
//    val context = LocalContext.current.applicationContext
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                modifier = Modifier.height(48.dp),
//                title = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .wrapContentHeight(Alignment.CenterVertically)
//                    ) {
//                        Text(text = "Contact Details", fontSize = 18.sp)
//                    }
//                }, navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            Toast.makeText(context, "Contact Details", Toast.LENGTH_SHORT).show()
//                        }
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.contactdetails),
//                            contentDescription = null
//                        )
//                    }
//                }, colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = GreenJC,
//                    titleContentColor = Color.White,
//                    navigationIconContentColor = Color.White
//                )
//            )
//        }, floatingActionButton = {
//            FloatingActionButton( containerColor = GreenJC, onClick = { navController.navigate("editContact/${contact.id}") }) {
//                Icon( imageVector = Icons.Default.Edit, contentDescription = null )
//            }
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(paddingValues)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                colors = CardDefaults.cardColors(Color.White),
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(8.dp)
//            ){
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    Image(painter = rememberAsyncImagePainter(contact.imageUri), contentDescription = contact.name,
//                        modifier = Modifier
//                            .size(128.dp)
//                            .clip(CircleShape),
//                        contentScale = ContentScale.Crop)
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Card(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                        colors = CardDefaults.cardColors(Color.White),
//                        shape = RoundedCornerShape(16.dp),
//                        elevation = CardDefaults.cardElevation(8.dp)
//                    ){
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ){
//                            Text("Name: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(contact.name, fontSize = 16.sp)
//
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Card(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                        colors = CardDefaults.cardColors(Color.White),
//                        shape = RoundedCornerShape(16.dp),
//                        elevation = CardDefaults.cardElevation(8.dp)
//                    ){
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ){
//                            Text("Phone: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                            Spacer(modifier = Modifier.width(8.dp))
//                            contact.phone?.let { Text(it, fontSize = 16.sp) }
//
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Card(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                        colors = CardDefaults.cardColors(Color.White),
//                        shape = RoundedCornerShape(16.dp),
//                        elevation = CardDefaults.cardElevation(8.dp)
//                    ){
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ){
//                            Text("Email: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                            Spacer(modifier = Modifier.width(8.dp))
//                            contact.email?.let { Text(it, fontSize = 16.sp) }
//
//                        }
//                    }
//                }
//
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Button(colors = ButtonDefaults.buttonColors(GreenJC), onClick = {
//                viewModel.delete(contact)
//                navController.navigate("contactList"){
//                    popUpTo(0)
//                }
//            }) {
//                Text("Delete Contact")
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditContactScreen(contact: Contact, viewModel : ContactViewModel, navController : NavController){
//    val context = LocalContext.current.applicationContext
//
//    var imageUri by remember {
//        mutableStateOf(contact.imageUri)
//    }
//    var name by remember {
//        mutableStateOf(contact.name)
//    }
//    var phoneNumber by remember {
//        mutableStateOf(contact.phone)
//    }
//    var email by remember {
//        mutableStateOf(contact.email)
//    }
//
//    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri : Uri? ->
//        uri?.let{ newUri ->
//            val internalPath = copyUriToInternalStorage(context, newUri, "$name.jpg")
//            internalPath?.let{ path -> imageUri = path }
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                modifier = Modifier.height(48.dp),
//                title = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .wrapContentHeight(Alignment.CenterVertically)
//                    ) {
//                        Text(text = "Edit Contact", fontSize = 18.sp)
//                    }
//                }, navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            Toast.makeText(context, "Edit Contact", Toast.LENGTH_SHORT).show()
//                        }
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.editcontact),
//                            contentDescription = null
//                        )
//                    }
//                }, colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = GreenJC,
//                    titleContentColor = Color.White,
//                    navigationIconContentColor = Color.White
//                )
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//
//                Image(
//                    painter = rememberAsyncImagePainter(imageUri),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(128.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//                Button(onClick = { launcher.launch("image/*") }, colors = ButtonDefaults.buttonColors(GreenJC)) {
//                    Text(text = "Choose Image")
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//
//                TextField(value = name, onValueChange = { name = it},
//                    label = {Text(text = "Name") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(8.dp)),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//            phoneNumber?.let { it1 ->
//                TextField(value = it1, onValueChange = { phoneNumber = it},
//                    label = {Text(text = "Phone Number") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(8.dp)),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black
//                    )
//                )
//            }
//
//            email?.let { it1 ->
//                TextField(value = it1, onValueChange = { email = it},
//                    label = {Text(text = "Email") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(8.dp)),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.White,
//                        unfocusedContainerColor = Color.White,
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black
//                    )
//                )
//            }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        val updateContact = contact.copy(name = name, phone = phoneNumber, email = email, imageUri = imageUri)
//                        viewModel.update(updateContact)
//                        navController.navigate("contactList"){
//                            popUpTo(0)
//                        }
//                    }, colors = ButtonDefaults.buttonColors(GreenJC)) {
//                    Text(text = "Update Contact")
//                }
//
//            }
//        }
//
//    }
//
//fun copyUriToInternalStorage (context : Context, uri : Uri, filename : String) : String? {
//    val file = File(context.filesDir, filename)
//    return try{
//        context.contentResolver.openInputStream(uri)?.use{ inputStream ->
//            FileOutputStream(file).use{ outputStream ->
//                inputStream.copyTo(outputStream)
//            }
//        }
//        file.absolutePath
//    }
//    catch(e : Exception){
//        e.printStackTrace()
//        null
//    }
//}


package com.example.contacts

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import androidx.room.Room
import java.io.File
import java.io.FileOutputStream

// If you have a theme color:
val GreenJC = Color(0xFF2E7D32) // replace with your theme color if you already have one

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // NOTE: For production, prefer a singleton for Room instead of building every time.
//        val database = Room.databaseBuilder(
//            applicationContext,
//            ContactDatabase::class.java,
//            "contacts.db"
//        ).build()
//
//        val repository = ContactRepository(database.contactDao())

        val database = ContactDatabase.build(applicationContext)
        val repository = ContactRepository(database.contactDao())

        val viewModel: ContactViewModel by viewModels { ContactViewModelFactory(repository) }

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "contactList") {

                composable("contactList") {
                    ContactListScreen(viewModel, navController)
                }

                composable("addContact") {
                    AddContactScreen(viewModel, navController)
                }

                composable(
                    route = "contactDetail/{contactId}",
                    arguments = listOf(navArgument("contactId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val contactId = backStackEntry.arguments?.getLong("contactId")
                    val contact = viewModel.allContacts
                        .observeAsState(initial = emptyList()).value
                        .find { it.id == contactId }
                    contact?.let { ContactDetailScreen(it, viewModel, navController) }
                }

                composable(
                    route = "editContact/{contactId}",
                    arguments = listOf(navArgument("contactId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val contactId = backStackEntry.arguments?.getLong("contactId")
                    val contact = viewModel.allContacts
                        .observeAsState(initial = emptyList()).value
                        .find { it.id == contactId }
                    contact?.let { EditContactScreen(it, viewModel, navController) }
                }
            }
        }
    }
}

/** ---- UI Pieces ---- **/

@Composable
fun ContactIcon(contact: Contact, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val avatarPainter =
                if (!contact.imageUri.isNullOrBlank())
                    rememberAsyncImagePainter(contact.imageUri)
                else
                    painterResource(R.drawable.contacticon)

            Image(
                painter = avatarPainter,
                contentDescription = contact.name,
                modifier = Modifier.size(50.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(contact.name)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(viewModel: ContactViewModel, navController: NavController) {
    val context = LocalContext.current.applicationContext

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) { Text(text = "Contacts", fontSize = 18.sp) }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Contacts", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(painter = painterResource(R.drawable.contacticon), contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(containerColor = GreenJC, onClick = { navController.navigate("addContact") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { paddingValues ->
        val contacts by viewModel.allContacts.observeAsState(initial = emptyList())
        Column(modifier = Modifier.padding(paddingValues)) {
            androidx.compose.foundation.lazy.LazyColumn {
                items(contacts.size) { index ->
                    val contact = contacts[index]
                    ContactIcon(contact = contact) {
                        navController.navigate("contactDetail/${contact.id}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(contact: Contact, viewModel: ContactViewModel, navController: NavController) {
    val context = LocalContext.current.applicationContext

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) { Text(text = "Contact Details", fontSize = 18.sp) }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Contact Details", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.contactdetails), contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(containerColor = GreenJC, onClick = { navController.navigate("editContact/${contact.id}") }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val avatarPainter =
                        if (!contact.imageUri.isNullOrBlank())
                            rememberAsyncImagePainter(contact.imageUri)
                        else
                            painterResource(R.drawable.contactdetails)

                    Image(
                        painter = avatarPainter,
                        contentDescription = contact.name,
                        modifier = Modifier.size(128.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoRow(label = "Name:", value = contact.name)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoRow(label = "Phone:", value = contact.phone ?: "")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoRow(label = "Email:", value = contact.email ?: "")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                colors = ButtonDefaults.buttonColors(GreenJC),
                onClick = {
                    viewModel.delete(contact)
                    navController.popBackStack()
                }
            ) { Text("Delete Contact") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactScreen(contact: Contact, viewModel: ContactViewModel, navController: NavController) {
    val context = LocalContext.current.applicationContext

    var imageUri by remember { mutableStateOf(contact.imageUri) }
    var name by remember { mutableStateOf(contact.name) }
    var phoneNumber by remember { mutableStateOf(contact.phone) }
    var email by remember { mutableStateOf(contact.email) }

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { newUri ->
            val internalPath = copyUriToInternalStorage(context, newUri, "${name.ifBlank { "image" }}.jpg")
            internalPath?.let { path -> imageUri = path }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) { Text(text = "Edit Contact", fontSize = 18.sp) }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Edit Contact", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(painter = painterResource(id = R.drawable.editcontact), contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = if (!imageUri.isNullOrBlank())
                    rememberAsyncImagePainter(imageUri)
                else
                    painterResource(R.drawable.editcontact),
                contentDescription = null,
                modifier = Modifier.size(128.dp).clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { launcher.launch("image/*") }, colors = ButtonDefaults.buttonColors(GreenJC)) {
                Text(text = "Choose Image")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Name") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = phoneNumber.orEmpty(),
                onValueChange = { phoneNumber = it },
                label = { Text(text = "Phone Number") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email.orEmpty(),
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val updateContact = contact.copy(
                        name = name.trim(),
                        phone = phoneNumber?.trim(),
                        email = email?.trim(),
                        imageUri = imageUri
                    )
                    viewModel.update(updateContact)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(GreenJC)
            ) {
                Text(text = "Update Contact")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(viewModel: ContactViewModel, navController: NavController) {
    val context = LocalContext.current.applicationContext

    var imageUri by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf<String?>(null) }

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { newUri ->
            val internalPath = copyUriToInternalStorage(context, newUri, "${name.ifBlank { "image" }}.jpg")
            internalPath?.let { path -> imageUri = path }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentHeight(Alignment.CenterVertically)
                    ) { Text(text = "Add Contact", fontSize = 18.sp) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = if (!imageUri.isNullOrBlank())
                    rememberAsyncImagePainter(imageUri)
                else
                    painterResource(R.drawable.addcontact),
                contentDescription = null,
                modifier = Modifier.size(128.dp).clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { launcher.launch("image/*") }, colors = ButtonDefaults.buttonColors(GreenJC)) {
                Text(text = "Choose Image")
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Name") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = phoneNumber.orEmpty(),
                onValueChange = { phoneNumber = it },
                label = { Text(text = "Phone Number") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email.orEmpty(),
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.addContact(imageUri, name, phoneNumber, email)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(GreenJC)
            ) { Text(text = "Save Contact") }
        }
    }
}

/** Small helper to show labeled rows **/
@Composable
private fun InfoRow(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(value, fontSize = 16.sp)
        }
    }
}

/** Utility: copy a picked content URI into internal storage and return its absolute path **/
fun copyUriToInternalStorage(context: Context, uri: Uri, filename: String): String? {
    val safeName = if (filename.isBlank()) "image_${System.currentTimeMillis()}.jpg" else filename
    val file = File(context.filesDir, safeName)
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
