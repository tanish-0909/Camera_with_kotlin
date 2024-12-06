//package com.example.srcnnv0
//
//import android.content.Intent
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import android.widget.Button
//import android.widget.ImageView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.srcnnv0.ml.ModelSrcnn935
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.schema.ResizeBilinearOptions
//import org.tensorflow.lite.support.image.ImageProcessor
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.image.ops.ResizeOp
//import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//
//class MainActivity : AppCompatActivity() {
//
//    lateinit var captureBtn : Button
//    lateinit var improveBtn : Button
//    lateinit var myImagePreview : ImageView
//    lateinit var myOutputImage : ImageView
//    lateinit var bitmap : Bitmap
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//
//        captureBtn = findViewById(R.id.captureBtn)
//        improveBtn = findViewById(R.id.improveBtn)
//        myImagePreview = findViewById(R.id.myImagePreview)
//        myOutputImage = findViewById(R.id.myOutputImage)
//
//        //image processor
//
//        var imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
//            .build()
//
//        captureBtn.setOnClickListener {
//            var intent: Intent = Intent()
//            intent.setAction(Intent.ACTION_GET_CONTENT)
//            intent.setType("image/*")
//            startActivityForResult(intent, 100)
//        }
//
//        improveBtn.setOnClickListener {
//            var tensorImage: TensorImage = TensorImage(DataType.UINT8)
//            if (::bitmap.isInitialized) {
//                tensorImage.load(bitmap)
//
//                tensorImage = imageProcessor.process(tensorImage)
//
//                val model = ModelSrcnn935.newInstance(this)
//
//                // Creates inputs for reference.
//                val inputFeature0 =
//                    TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
//                inputFeature0.loadBuffer(tensorImage.buffer)
//
//                // Runs model inference and gets result.
//                try {
//                    val outputFeature0 = model.process(inputFeature0).outputFeature0AsTensorBuffer
//                    val outputBitmap = tensorBufferToBitmapRGB(outputFeature0)
//                    myOutputImage.setImageBitmap(outputBitmap)
//                    Log.d("Image", "Bitmap size: ${bitmap.width} x ${bitmap.height}")
//                    Log.d("Tensor", "Input tensor size: ${inputFeature0.shape.contentToString()}")
//                    Log.d("Model", "Output tensor size: ${outputFeature0.shape.contentToString()}")
//                } catch (e: Exception) {
//                    Log.e("ModelInference", "Error during model inference", e)
//                }
//
//
//                // Releases model resources if no longer used.
//                model.close()
//            } else {
//                Log.e("ImproveButton", "Bitmap is not initialized")
//            }
//
//            val maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024
//            Log.d("Memory", "Max memory: $maxMemory MB")
//
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 100) {
//            var uri =
//                data?.data        //Uniform Resource Indicator: read the image from this location
//            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, 112, 112)
//            myImagePreview.setImageBitmap(bitmap)
//        }
//    }
//
//    fun tensorBufferToBitmapRGB(tensorBuffer: TensorBuffer): Bitmap {
//        val shape = tensorBuffer.shape // e.g., [height, width, 3]
//        val height = shape[0]
//        val width = shape[1]
//
//        val floatArray = tensorBuffer.floatArray // Get the pixel values
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//
//        for (y in 0 until height) {
//            for (x in 0 until width) {
//                val offset = (y * width + x) * 3
//                val r = (floatArray[offset] * 255).toInt()
//                val g = (floatArray[offset + 1] * 255).toInt()
//                val b = (floatArray[offset + 2] * 255).toInt()
//
//                val color = 0xFF shl 24 or (r shl 16) or (g shl 8) or b // Combine RGB
//                bitmap.setPixel(x, y, color)
//            }
//        }
//
//        return bitmap
//    }
//
//
//    }
package com.example.srcnnv0

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.srcnnv0.ml.ModelSrcnn935
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {

    lateinit var captureBtn: Button
    lateinit var improveBtn: Button
    lateinit var myImagePreview: ImageView
    lateinit var myOutputImage: ImageView
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        captureBtn = findViewById(R.id.captureBtn)
        improveBtn = findViewById(R.id.improveBtn)
        myImagePreview = findViewById(R.id.myImagePreview)
        myOutputImage = findViewById(R.id.myOutputImage)

        val inputShape = intArrayOf(1, 112, 112, 3) // Adjust input shape if needed
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputShape[1], inputShape[2], ResizeOp.ResizeMethod.BILINEAR))
            .build()

        captureBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        improveBtn.setOnClickListener {
            if (::bitmap.isInitialized) {
                val tensorImage = TensorImage(DataType.UINT8)
                tensorImage.load(bitmap)
                val processedImage = imageProcessor.process(tensorImage)

                val model = ModelSrcnn935.newInstance(this)

                // Create input tensor buffer
                val inputFeature0 =
                    TensorBuffer.createFixedSize(inputShape, DataType.UINT8)
                inputFeature0.loadBuffer(processedImage.buffer)

                // Run model inference
                try {
                    val outputFeature0 = model.process(inputFeature0).outputFeature0AsTensorBuffer
                    val outputBitmap = tensorBufferToBitmapRGB(outputFeature0)
                    myOutputImage.setImageBitmap(outputBitmap)
                    Log.d("Image", "Bitmap size: ${bitmap.width} x ${bitmap.height}")
                    Log.d("Tensor", "Input tensor size: ${inputFeature0.shape.contentToString()}")
                    Log.d("Model", "Output tensor size: ${outputFeature0.shape.contentToString()}")
                } catch (e: Exception) {
                    Log.e("ModelInference", "Error during model inference", e)
                } finally {
                    model.close()
                }
            } else {
                Log.e("ImproveButton", "Bitmap is not initialized")
            }

            // Log memory usage
            val maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024
            Log.d("Memory", "Max memory: $maxMemory MB")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                // Resize bitmap for preview if needed
                bitmap = Bitmap.createScaledBitmap(bitmap, 112, 112, true)
                myImagePreview.setImageBitmap(bitmap)
            }
        }
    }

    private fun tensorBufferToBitmapRGB(tensorBuffer: TensorBuffer): Bitmap {
        val shape = tensorBuffer.shape // [1, height, width, 3]
        val height = shape[1]
        val width = shape[2]

        val floatArray = tensorBuffer.floatArray
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val offset = (y * width + x) * 3
                val r = (floatArray[offset] * 255).toInt().coerceIn(0, 255)
                val g = (floatArray[offset + 1] * 255).toInt().coerceIn(0, 255)
                val b = (floatArray[offset + 2] * 255).toInt().coerceIn(0, 255)

                val color = (0xFF shl 24) or (r shl 16) or (g shl 8) or b
                bitmap.setPixel(x, y, color)
            }
        }

        return bitmap
    }
}
