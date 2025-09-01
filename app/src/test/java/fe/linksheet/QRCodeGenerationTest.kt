package fe.linksheet

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import org.junit.Test
import org.junit.Assert.*

/**
 * Test QR Code generation functionality
 */
class QRCodeGenerationTest {

    @Test
    fun testQRCodeGeneration() {
        val testUrl = "https://github.com/LinkSheet/LinkSheet"
        val size = 400
        
        try {
            val qrCodeBitmap = createQRCodeBitmap(
                text = testUrl,
                size = size,
                foregroundColor = Color.Black.toArgb(),
                backgroundColor = Color.White.toArgb()
            )
            
            assertNotNull("QR code bitmap should not be null", qrCodeBitmap)
            assertEquals("QR code width should match requested size", size, qrCodeBitmap.width)
            assertEquals("QR code height should match requested size", size, qrCodeBitmap.height)
            
        } catch (e: Exception) {
            fail("QR code generation should not throw exception: ${e.message}")
        }
    }
    
    @Test
    fun testQRCodeWithDifferentSizes() {
        val testUrl = "https://example.com"
        val sizes = listOf(200, 400, 600, 800)
        
        sizes.forEach { size ->
            try {
                val qrCodeBitmap = createQRCodeBitmap(
                    text = testUrl,
                    size = size,
                    foregroundColor = Color.Black.toArgb(),
                    backgroundColor = Color.White.toArgb()
                )
                
                assertNotNull("QR code bitmap should not be null for size $size", qrCodeBitmap)
                assertEquals("QR code width should be $size", size, qrCodeBitmap.width)
                assertEquals("QR code height should be $size", size, qrCodeBitmap.height)
                
            } catch (e: Exception) {
                fail("QR code generation should not fail for size $size: ${e.message}")
            }
        }
    }
    
    @Test
    fun testQRCodeWithDifferentColors() {
        val testUrl = "https://linksheet.app"
        val size = 400
        
        val colorCombinations = listOf(
            Color.Black.toArgb() to Color.White.toArgb(),
            Color.Blue.toArgb() to Color.White.toArgb(),
            Color.Red.toArgb() to Color.Yellow.toArgb()
        )
        
        colorCombinations.forEach { (foreground, background) ->
            try {
                val qrCodeBitmap = createQRCodeBitmap(
                    text = testUrl,
                    size = size,
                    foregroundColor = foreground,
                    backgroundColor = background
                )
                
                assertNotNull("QR code bitmap should not be null", qrCodeBitmap)
                
                // Check that the bitmap contains both colors
                val pixels = IntArray(size * size)
                qrCodeBitmap.getPixels(pixels, 0, size, 0, 0, size, size)
                
                val containsForeground = pixels.any { it == foreground }
                val containsBackground = pixels.any { it == background }
                
                assertTrue("QR code should contain foreground color", containsForeground)
                assertTrue("QR code should contain background color", containsBackground)
                
            } catch (e: Exception) {
                fail("QR code generation should not fail with colors: ${e.message}")
            }
        }
    }
    
    @Test
    fun testQRCodeWithLongUrl() {
        val longUrl = "https://example.com/very/long/path/with/many/segments/and/parameters?param1=value1&param2=value2&param3=value3&param4=value4&param5=value5"
        val size = 400
        
        try {
            val qrCodeBitmap = createQRCodeBitmap(
                text = longUrl,
                size = size,
                foregroundColor = Color.Black.toArgb(),
                backgroundColor = Color.White.toArgb()
            )
            
            assertNotNull("QR code bitmap should not be null for long URL", qrCodeBitmap)
            assertEquals("QR code width should match requested size", size, qrCodeBitmap.width)
            assertEquals("QR code height should match requested size", size, qrCodeBitmap.height)
            
        } catch (e: Exception) {
            fail("QR code generation should handle long URLs: ${e.message}")
        }
    }
    
    @Test
    fun testQRCodeWithSpecialCharacters() {
        val specialUrl = "https://example.com/path?query=hello%20world&emoji=🔗&special=@#$%"
        val size = 400
        
        try {
            val qrCodeBitmap = createQRCodeBitmap(
                text = specialUrl,
                size = size,
                foregroundColor = Color.Black.toArgb(),
                backgroundColor = Color.White.toArgb()
            )
            
            assertNotNull("QR code bitmap should not be null for URL with special characters", qrCodeBitmap)
            
        } catch (e: Exception) {
            fail("QR code generation should handle special characters: ${e.message}")
        }
    }
    
    /**
     * Helper function to create QR code bitmap (same as in QRCodeViewModel)
     */
    private fun createQRCodeBitmap(
        text: String,
        size: Int,
        foregroundColor: Int,
        backgroundColor: Int
    ): Bitmap {
        val writer = QRCodeWriter()
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M)
            put(EncodeHintType.MARGIN, 1)
        }
        
        try {
            val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x, y,
                        if (bitMatrix[x, y]) foregroundColor else backgroundColor
                    )
                }
            }
            
            return bitmap
        } catch (e: WriterException) {
            throw Exception("Failed to encode QR code: ${e.message}")
        }
    }
}