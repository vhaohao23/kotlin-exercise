package com.example.bluromatic.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.data.KEY_BLUR_LEVEL
import com.example.bluromatic.data.KEY_IMAGE_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    private fun createDefaultBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Background
        paint.color = Color.WHITE
        canvas.drawRect(0f, 0f, 400f, 400f, paint)

        // Cupcake wrapper (brown trapezoid)
        paint.color = Color.parseColor("#795548")
        val wrapperPath = Path().apply {
            moveTo(100f, 220f); lineTo(300f, 220f)
            lineTo(280f, 340f); lineTo(120f, 340f)
            close()
        }
        canvas.drawPath(wrapperPath, paint)

        // Frosting (pink)
        paint.color = Color.parseColor("#E91E63")
        val frostingPath = Path().apply {
            moveTo(72f, 220f)
            quadTo(100f, 100f, 200f, 80f)
            quadTo(300f, 100f, 328f, 220f)
            quadTo(272f, 180f, 240f, 200f)
            quadTo(200f, 152f, 160f, 200f)
            quadTo(120f, 180f, 72f, 220f)
            close()
        }
        canvas.drawPath(frostingPath, paint)

        // Cherry (red circle)
        paint.color = Color.parseColor("#C62828")
        canvas.drawCircle(200f, 52f, 36f, paint)

        // Cherry highlight
        paint.color = Color.parseColor("#EF5350")
        canvas.drawCircle(186f, 40f, 14f, paint)

        return bitmap
    }

    override suspend fun doWork(): Result {
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val resolver = applicationContext.contentResolver
                val picture = if (resourceUri != null) {
                    val inputStream = resolver.openInputStream(Uri.parse(resourceUri))
                    BitmapFactory.decodeStream(inputStream)
                } else {
                    createDefaultBitmap()
                }

                val output = blurBitmap(applicationContext, picture, blurLevel)
                val outputUri = writeBitmapToFile(applicationContext, output)
                Result.success(workDataOf(KEY_IMAGE_URI to outputUri.toString()))
            } catch (throwable: Throwable) {
                Log.e(TAG, "Error applying blur", throwable)
                Result.failure()
            }
        }
    }
}
