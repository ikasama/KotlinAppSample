package com.badlogic.masaki.passwordmanagementsample

import com.badlogic.masaki.passwordmanagementsample.api.PasswordManagementClient
import com.badlogic.masaki.passwordmanagementsample.api.service.GooglePlayStoreService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class GooglePlayStoreServiceTest {

    private val mService = mock(GooglePlayStoreService::class.java)

    @Before
    fun setUp() {
        whenever(mService.getLatestAppVersion(BuildConfig.APPLICATION_ID))
                .thenReturn(Single.just("1.0.2"))
    }

    @Test
    fun googlePlayStoreServiceTest() {
        PasswordManagementClient(mService).getLatestAppVersion(BuildConfig.APPLICATION_ID)
                .subscribeOn(Schedulers.io())
                .test()
                .await().run {
            assertNoErrors()
            assertResult("1.0.2")
        }
    }
}