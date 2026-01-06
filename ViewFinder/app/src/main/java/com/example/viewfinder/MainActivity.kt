package com.example.viewfinder

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var blurButton: Button
    private var originalBitmap: Bitmap? = null
    private var detectedFaces: List<Face> = emptyList()

    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.uploadButton)
        blurButton = findViewById(R.id.blurButton)

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        blurButton.setOnClickListener {
            originalBitmap?.let {
                if (detectedFaces.isNotEmpty()) {
                    val blurred = blurFaces(it, detectedFaces)
                    imageView.setImageBitmap(blurred)
                } else {
                    Log.e("FaceBlur", "No faces detected yet.")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data ?: return
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            originalBitmap = bitmap
            imageView.setImageBitmap(bitmap)
            detectFaces(bitmap)
        }
    }

    private fun detectFaces(bitmap: Bitmap) {
        // Step 1: Normalize image orientation & contrast
        val matrix = Matrix().apply { postRotate(0f) }
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        val contrasted = increaseContrast(rotatedBitmap)

        // Step 2: Create InputImage (force upright)
        val image = InputImage.fromBitmap(contrasted, 0)

        // Step 3: Configure detector with all advanced options
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setMinFaceSize(0.01f)
            .enableTracking()
            .build()

        val detector = FaceDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { faces ->
                detectedFaces = faces
                if (faces.isNotEmpty()) {
                    Log.d("FaceDetect", "✅ Detected ${faces.size} faces")
                    val withBoxes = drawFaceBoxes(bitmap, faces)
                    imageView.setImageBitmap(withBoxes)
                } else {
                    Log.e("FaceDetect", "❌ No faces detected. Try with brightness fix.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FaceDetect", "⚠️ Detection failed: ${e.message}")
            }
    }

    private fun increaseContrast(bitmap: Bitmap): Bitmap {
        val cm = ColorMatrix().apply {
            set(floatArrayOf(
                1.5f, 0f, 0f, 0f, -50f,
                0f, 1.5f, 0f, 0f, -50f,
                0f, 0f, 1.5f, 0f, -50f,
                0f, 0f, 0f, 1f, 0f
            ))
        }

        val ret = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(ret)
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(cm) }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return ret
    }


    private fun drawFaceBoxes(bitmap: Bitmap, faces: List<Face>): Bitmap {
        val result = bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        for (face in faces) {
            val rect = face.boundingBox
            canvas.drawRect(rect, paint)
        }

        return result
    }

    private fun blurFaces(bitmap: Bitmap, faces: List<Face>): Bitmap {
        val result = bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint()

        for (face in faces) {
            val rect = face.boundingBox

            val left = rect.left.coerceAtLeast(0)
            val top = rect.top.coerceAtLeast(0)
            val right = rect.right.coerceAtMost(bitmap.width)
            val bottom = rect.bottom.coerceAtMost(bitmap.height)

            val width = right - left
            val height = bottom - top

            if (width <= 0 || height <= 0) continue

            val faceBitmap = Bitmap.createBitmap(bitmap, left, top, width, height)

            // Simple pixelation blur
            val blurredFace = Bitmap.createScaledBitmap(faceBitmap, 25, 25, false)
            val scaledBack = Bitmap.createScaledBitmap(blurredFace, width, height, false)

            canvas.drawBitmap(scaledBack, left.toFloat(), top.toFloat(), paint)
        }

        return result
    }
}
