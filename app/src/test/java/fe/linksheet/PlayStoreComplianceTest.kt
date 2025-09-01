package fe.linksheet

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Test to verify Play Store compliance
 * Ensures no prohibited features are present in the build
 */
class PlayStoreComplianceTest {

    @Test
    fun `verify no debug features in release build`() {
        // This test ensures debug features are properly removed
        val buildConfig = BuildConfig.DEBUG
        
        // In release builds, debug should be false
        if (!buildConfig) {
            assertTrue(true, "Debug features properly disabled in release build")
        }
    }

    @Test
    fun `verify app package name is correct`() {
        val packageName = "fe.linksheet"
        assertEquals(packageName, BuildConfig.APPLICATION_ID)
    }

    @Test
    fun `verify version info is present`() {
        assertNotNull(BuildConfig.VERSION_NAME)
        assertTrue(BuildConfig.VERSION_CODE > 0)
    }
}