package fe.linksheet.feature.qr

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.util.EnumMap

/**
 * QR Code Generator for LinkMaster
 * Generates QR codes for URLs and text content
 */
object QRCodeGenerator {

    /**
     * Generate QR code bitmap from text/URL
     */
    fun generateQRCode(
        content: String,
        width: Int = 512,
        height: Int = 512,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.MARGIN, 1)
            }
            
            val bitMatrix: BitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) foregroundColor else backgroundColor)
                }
            }
            
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Generate QR code with LinkMaster branding colors
     */
    fun generateBrandedQRCode(
        content: String,
        width: Int = 512,
        height: Int = 512
    ): Bitmap? {
        return generateQRCode(
            content = content,
            width = width,
            height = height,
            foregroundColor = Color.parseColor("#6200EE"), // LinkMaster primary color
            backgroundColor = Color.WHITE
        )
    }

    /**
     * Generate neon-themed QR code
     */
    fun generateNeonQRCode(
        content: String,
        width: Int = 512,
        height: Int = 512
    ): Bitmap? {
        return generateQRCode(
            content = content,
            width = width,
            height = height,
            foregroundColor = Color.parseColor("#BB86FC"), // Neon purple
            backgroundColor = Color.parseColor("#0A0A0A") // Dark background
        )
    }
}