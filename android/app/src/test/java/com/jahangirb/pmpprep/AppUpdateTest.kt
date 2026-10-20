package com.jahangirb.pmpprep

import com.jahangirb.pmpprep.data.AppUpdate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Check for updates: GitHub's latest release against this build (build.gradle.kts, android-apk.yml). */
class AppUpdateTest {
    private val json = """{"tag_name":"apk-101","published_at":"2026-10-20T12:00:00Z","assets":[
        {"name":"notes.txt","browser_download_url":"https://github.com/x/notes.txt","size":10},
        {"name":"PMP-Prep.apk","browser_download_url":"https://github.com/JahangirB/PMP/releases/download/apk-101/PMP-Prep.apk","size":10485760}]}"""

    @Test fun readsTheReleaseAndItsApk() {
        val r = AppUpdate.parse(json)!!
        assertEquals(101, r.build)
        assertTrue(r.apkUrl.endsWith("/apk-101/PMP-Prep.apk"))
        assertEquals(10485760L, r.bytes)
        assertNull(AppUpdate.parse("""{"tag_name":"v1","assets":[]}"""))
        assertNull(AppUpdate.parse("""{"tag_name":"apk-5","assets":[{"name":"PMP-Prep.apk","browser_download_url":"http://insecure/x.apk"}]}"""))
        assertNull(AppUpdate.parse("not json"))
    }

    @Test fun newerOnlyWhenTheBuildIsAhead() {
        val r = AppUpdate.parse(json)!!
        assertTrue(AppUpdate.isNewer(r, 200))
        assertFalse(AppUpdate.isNewer(r, 201))
        assertFalse(AppUpdate.isNewer(r, 250))
        assertEquals(98L, AppUpdate.buildOf(198))
    }
}
