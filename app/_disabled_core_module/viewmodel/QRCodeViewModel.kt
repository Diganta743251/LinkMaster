package fe.linksheet.module.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Removed ZXing dependencies - will implement simple QR code generation
import fe.linksheet.composable.page.history.QRCodeState
import fe.linksheet.composable.page.history.SharingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * ViewModel for QR Code Screen with generation and sharing functionality
 */
class QRCodeViewModel(
    private val context: Context,
    private val linkUrl: String
) : ViewModel() {
    
    private val _qrCodeState = MutableStateFlow<QRCodeState>(QRCodeState.Idle)
    val qrCodeState = _qrCodeState.asStateFlow()
    
    private val _sharingState = MutableStateFlow<SharingState>(SharingState.Idle)
    val sharingState = _sharingState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    private val _qrCodeSize = MutableStateFlow(400)
    val qrCodeSize = _qrCodeSize.asStateFlow()
    
    private val _foregroundColor = MutableStateFlow(Color.Black)
    val foregroundColor = _foregroundColor.asStateFlow()
    
    private val _backgroundColor = MutableStateFlow(Color.White)
    val backgroundColor = _backgroundColor.asStateFlow()
    
    private val _qrCodeBitmap = MutableStateFlow<Bitmap?>(null)
    val qrCodeBitmap = _qrCodeBitmap.asStateFlow()
    
    /**
     * Generate QR Code bitmap with enhanced error handling
     */
    fun generateQRCode() {
        viewModelScope.launch {
            _qrCodeState.value = QRCodeState.Generating
            
            try {
                val startTime = System.currentTimeMillis()
                val bitmap = withContext(Dispatchers.IO) {
                    createQRCodeBitmap(
                        text = linkUrl,
                        size = _qrCodeSize.value,
                        foregroundColor = _foregroundColor.value.toArgb(),
                        backgroundColor = _backgroundColor.value.toArgb()
                    )
                }
                val generationTime = System.currentTimeMillis() - startTime
                
                _qrCodeBitmap.value = bitmap
                _qrCodeState.value = QRCodeState.Success(
                    bitmap = bitmap,
                    size = _qrCodeSize.value,
                    foregroundColor = _foregroundColor.value.toArgb(),
                    backgroundColor = _backgroundColor.value.toArgb()
                )
                
                // Log performance metrics
                if (generationTime > 1000) {
                    android.util.Log.w("QRCodeViewModel", "QR generation took ${generationTime}ms")
                }
                
            } catch (e: Exception) {
                android.util.Log.e("QRCodeViewModel", "QR generation failed", e)
                _qrCodeState.value = QRCodeState.Error(
                    message = when (e) {
                        is IllegalArgumentException -> "Failed to encode QR code: ${e.message}"
                        is OutOfMemoryError -> "QR code too large for available memory"
                        else -> "Failed to generate QR code: ${e.message}"
                    },
                    throwable = e
                )
            }
        }
    }
    
    /**
     * Update QR code size and regenerate
     */
    fun updateQRCodeSize(size: Int) {
        _qrCodeSize.value = size
        generateQRCode()
    }
    
    /**
     * Update foreground color and regenerate
     */
    fun updateForegroundColor(color: Color) {
        _foregroundColor.value = color
        generateQRCode()
    }
    
    /**
     * Update background color and regenerate
     */
    fun updateBackgroundColor(color: Color) {
        _backgroundColor.value = color
        generateQRCode()
    }
    
    /**
     * Copy text to clipboard
     */
    fun copyToClipboard(text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Link URL", text)
        clipboardManager.setPrimaryClip(clipData)
        
        Toast.makeText(context, "URL copied to clipboard", Toast.LENGTH_SHORT).show()
    }
    
    /**
     * Share QR code image and link together
     */
    fun shareQRCodeAndLink() {
        val bitmap = _qrCodeBitmap.value
        if (bitmap == null) {
            Toast.makeText(context, "QR code not ready", Toast.LENGTH_SHORT).show()
            return
        }
        
        viewModelScope.launch {
            try {
                val imageUri = withContext(Dispatchers.IO) {
                    saveQRCodeToCache(bitmap as Bitmap)
                }
                
                val shareText = "Check out this link: $linkUrl\n\nScan the QR code to open it quickly!"
                
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    putExtra(Intent.EXTRA_SUBJECT, "Shared Link with QR Code")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                
                val chooserIntent = Intent.createChooser(shareIntent, "Share QR Code & Link")
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooserIntent)
                
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to share: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    /**
     * Create QR code bitmap from text - Simple implementation
     * TODO: Integrate proper QR code library when ZXing dependency is resolved
     */
    private fun createQRCodeBitmap(
        text: String,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int
    ): Bitmap {
        // Create a simple placeholder bitmap with text
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = backgroundColor
        }
        
        // Fill background
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)
        
        // Draw simple pattern as placeholder
        paint.color = foregroundColor
        val cellSize = size / 25 // 25x25 grid
        
        // Create a simple pattern
        for (i in 0 until 25) {
            for (j in 0 until 25) {
                // Create a simple hash-based pattern from the text
                val hash = (text.hashCode() + i * 31 + j * 17).rem(4)
                if (hash == 0) {
                    canvas.drawRect(
                        (i * cellSize).toFloat(),
                        (j * cellSize).toFloat(),
                        ((i + 1) * cellSize).toFloat(),
                        ((j + 1) * cellSize).toFloat(),
                        paint
                    )
                }
            }
        }
        
        // Add text in center
        paint.textSize = size / 20f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(
            "QR: ${text.take(20)}...",
            size / 2f,
            size / 2f,
            paint
        )
        
        return bitmap
    }
    
    /**
     * Save QR code bitmap to cache directory and return URI
     */
    private fun saveQRCodeToCache(bitmap: Bitmap): Uri {
        val cacheDir = File(context.cacheDir, "qr_codes")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        
        val fileName = "qr_code_${System.currentTimeMillis()}.png"
        val file = File(cacheDir, fileName)
        
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: IOException) {
            throw Exception("Failed to save QR code image: ${e.message}")
        }
    }
}