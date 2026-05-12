package com.example.bluromatic.workers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

const val OUTPUT_PATH = "blur_filter_outputs"

@Suppress("DEPRECATION")
fun blurBitmap(context: Context, bitmap: Bitmap, blurLevel: Int): Bitmap {
    var input = bitmap
    repeat(blurLevel) {
        val rs = RenderScript.create(context)
        val inputAlloc = Allocation.createFromBitmap(rs, input)
        val outputAlloc = Allocation.createTyped(rs, inputAlloc.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(25f)
        script.setInput(inputAlloc)
        script.forEach(outputAlloc)
        val output = Bitmap.createBitmap(input.width, input.height, input.config!!)
        outputAlloc.copyTo(output)
        input = output
        rs.destroy()
    }
    return input
}

fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val name = "blur-filter-output-${UUID.randomUUID()}.png"
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }
    val outputFile = File(outputDir, name)
    val out = FileOutputStream(outputFile)
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)
    out.close()
    return Uri.fromFile(outputFile)
}
