package com.example.link

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import androidx.activity.compose.rememberLauncherForActivityResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QRCodeManager {
    
    companion object {
        private const val QR_CODE_SIZE = 512
        
        suspend fun generateQRCode(
            text: String,
            size: Int = QR_CODE_SIZE,
            backgroundColor: Int = Color.WHITE,
            foregroundColor: Int = Color.BLACK
        ): Bitmap? = withContext(Dispatchers.IO) {
            try {
                val writer = QRCodeWriter()
                val hints = hashMapOf<EncodeHintType, Any>().apply {
                    put(EncodeHintType.CHARACTER_SET, "UTF-8")
                    put(EncodeHintType.MARGIN, 1)
                }
                
                val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(x, y, if (bitMatrix[x, y]) foregroundColor else backgroundColor)
                    }
                }
                
                bitmap
            } catch (e: WriterException) {
                null
            }
        }
        
        fun createScanOptions(): ScanOptions {
            return ScanOptions().apply {
                setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                setPrompt("Scan a QR code to add a link")
                setCameraId(0) // Use back camera
                setBeepEnabled(true)
                setBarcodeImageEnabled(true)
                setOrientationLocked(false)
            }
        }
    }
}

@Composable
fun rememberQRCodeScanner(
    onResult: (String?) -> Unit
): () -> Unit {
    val context = LocalContext.current
    
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            onResult(result.contents)
        } else {
            onResult(null)
        }
    }
    
    return remember {
        {
            val options = QRCodeManager.createScanOptions()
            scanLauncher.launch(options)
        }
    }
}

// Extension function to generate QR code for a link
suspend fun Link.generateQRCode(size: Int = 512): Bitmap? {
    return QRCodeManager.generateQRCode(this.url, size)
}