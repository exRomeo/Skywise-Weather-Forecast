package com.example.skywise.data.remotesource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skywise.data.WeatherData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class APITest {
    @get:Rule
    var instanceExecutorRule = InstantTaskExecutorRule()

    lateinit var retrofit: RetrofitClient

    @Before
    fun initializeRetrofit() {
        retrofit = RetrofitClient
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sendRequestToApi_queryForASetOfData_ResponseIsSuccessfulAndIsMatchingMyResponseModel() =
        runTest {
            //given an api interface

            //when used to request data
            val response = retrofit.api.oneCall(
                lat = 30.1,
                lon = 28.01,
                lang = "en",
                exclude = "",
                unit = "metric",
                apiKey = API_KEY
            )
            //response is successful && body is not null && matching my Response Model
            assertThat(response.isSuccessful, `is`(true))
            assertThat(response.body(), notNullValue())
            assertThat(response.body(), instanceOf(WeatherData::class.java))
        }
}