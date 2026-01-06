package com.example.smartexpense

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.text.SimpleDateFormat
import java.util.*

data class Expense(val category: String, val amount: String, val date: String)

class MainActivity : AppCompatActivity() {

    private lateinit var selectImageButton: Button
    private lateinit var resultText: TextView
    private lateinit var receiptImageView: ImageView
    private lateinit var expenseListLayout: LinearLayout

    private val imagePickerCode = 1001
    private val expenses = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectImageButton = findViewById(R.id.btnSelectImage)
        resultText = findViewById(R.id.txtResult)
        receiptImageView = findViewById(R.id.receiptImageView)
        expenseListLayout = findViewById(R.id.expenseListLayout)

        selectImageButton.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, imagePickerCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == imagePickerCode && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                receiptImageView.setImageURI(imageUri)
                processImage(imageUri)
            }
        }
    }

    private fun processImage(imageUri: Uri) {
        val imageStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val fullText = visionText.text
                val category = detectCategory(fullText)
                val amount = detectAmount(fullText)
                val timestamp = getCurrentTimestamp()

                val result = """
                    🏷 Category: $category
                    💰 Amount: ₹$amount
                    🕒 Date: $timestamp
                """.trimIndent()

                resultText.text = result

                // Add to expense list
                val expense = Expense(category, amount, timestamp)
                expenses.add(expense)
                updateExpenseList()
            }
            .addOnFailureListener {
                resultText.text = "❌ Failed to read text."
            }
    }

    private fun detectCategory(text: String): String {
        val lowerText = text.lowercase()

        return when {
            listOf("restaurant", "food", "meal", "dine", "pizza", "burger", "cafe").any { it in lowerText } -> "Food"
            listOf("uber", "ola", "bus", "train", "taxi", "flight", "cab").any { it in lowerText } -> "Travel"
            listOf("store", "mall", "shopping", "fashion", "clothes", "lifestyle").any { it in lowerText } -> "Shopping"
            listOf("medicine", "pharmacy", "hospital", "clinic").any { it in lowerText } -> "Health"
            listOf("fuel", "petrol", "gas", "diesel").any { it in lowerText } -> "Fuel"
            else -> "Other"
        }
    }

    private fun detectAmount(text: String): String {
        val regex = Regex("""\b\d{1,3}(?:[.,]\d{3})*(?:[.,]\d{2})?\b""")
        val allAmounts = regex.findAll(text).map { it.value.replace(",", "").replace(" ", "") }.toList()

        return if (allAmounts.isNotEmpty()) {
            val maxAmount = allAmounts.maxOfOrNull { it.toDoubleOrNull() ?: 0.0 } ?: 0.0
            "%.2f".format(maxAmount)
        } else {
            "Not found"
        }
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun updateExpenseList() {
        expenseListLayout.removeAllViews()

        for (expense in expenses.reversed()) {
            val row = TextView(this)
            row.text = "• ${expense.category} — ₹${expense.amount} — ${expense.date}"
            row.textSize = 14f
            row.setPadding(0, 6, 0, 6)
            expenseListLayout.addView(row)
        }
    }
}
