package app.linksheet.testing.fake

import android.content.pm.PackageInfo

data class PackageInfoFake(val pkgName: String) {
    val packageInfo: PackageInfo = PackageInfo().apply { this.packageName = this@PackageInfoFake.pkgName }
}

object PackageInfoFakes {
    val MiBrowser = PackageInfoFake("com.mi.browser")
    val Youtube = PackageInfoFake("com.google.android.youtube")
    val NewPipe = PackageInfoFake("org.schabi.newpipe")
    val NewPipeEnhanced = PackageInfoFake("org.schabi.newpipe.enhanced")
    val Dummy = PackageInfoFake("com.example.dummy")
}
