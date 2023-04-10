package com.example.skywise.data

import androidx.test.core.app.ApplicationProvider
import com.example.skywise.MainDispatcherRule
import com.example.skywise.alertscreen.WeatherAlert
import com.example.skywise.data.localsource.OfflineDataModel
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.API_KEY
import com.example.skywise.data.remotesource.RetrofitClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RepositoryTest {

    @get:Rule
    val mainCoroutineDispatcherRule = MainDispatcherRule()

    lateinit var repository: Repository
    lateinit var favoriteList: List<FavoriteLocation>


    @Before
    fun createRepo() {
        repository = Repository(
            RetrofitClient,
            RoomClient.getInstance(ApplicationProvider.getApplicationContext())
        )

        favoriteList = listOf(
            FavoriteLocation(lat = 31.0, lon = 28.0, area = "Alexandria", currentLocation = true),
            FavoriteLocation(lat = 27.0, lon = 10.0, area = "California", currentLocation = false),
            FavoriteLocation(lat = -20.0, lon = 3.0, area = "Djibouti", currentLocation = false),
            FavoriteLocation(lat = 55.0, lon = 21.0, area = "London", currentLocation = false),
            FavoriteLocation(lat = 77.854, lon = 33.0, area = "New York", currentLocation = false),
            FavoriteLocation(lat = 66.24, lon = 14.15, area = "Washington", currentLocation = false)
        )
    }

    @After
    fun clearTables() = runTest {
        repository.clearDatabase()
    }


    @Test
    fun getOfflineDataByDate_insertOfflineDataIntoDatabase_insertedAndReturnedDataAreTheSame() =
        runTest {

            //given
            val offlineData =
                OfflineDataModel(0.325, 7.025, area = "Unit Testing", description = "Clear Weather")
            repository.addOfflineData(offlineData)
            //when
            val resultData = repository.getOfflineData()

            //then
            MatcherAssert.assertThat(resultData, notNullValue())
            MatcherAssert.assertThat(resultData.lat, `is`(offlineData.lat))
            MatcherAssert.assertThat(
                resultData.description,
                `is`(offlineData.description)
            )
            MatcherAssert.assertThat("message", resultData.lon, `is`(offlineData.lon))
            MatcherAssert.assertThat(resultData.area, `is`(offlineData.area))
        }


    @Test
    fun getFavoritesList_insertListOfLocationsToFavorites_insertedAndRetrievedListsAreTheSame() =
        runTest {
            //given a pre created list inserted into the db
            favoriteList.forEach { repository.addLocation(it) }
            //when retrieved
            val resultList = repository.getFavoriteLocations().first()

            //then given List should equal result List
            MatcherAssert.assertThat(resultList.size, `is`(favoriteList.size))
            //making sure list is retrieved in same order it was inserted in
            for (i in favoriteList.indices)
                MatcherAssert.assertThat(resultList[i], `is`(favoriteList[i]))
        }

    @Test
    fun getAlerts_insertAnAlert_insertedAlertIsRetrievedSuccessfully() = runTest {
        //given an alert that was inserted into db
        val weatherAlert = WeatherAlert(id = 500, startDate = 6246524864L, endDate = 6246624864L)
        repository.addWeatherAlert(weatherAlert)
        //when retrieved
        val result = repository.getAlertByID(weatherAlert.id)
        //then its the same as it was when inserted
        MatcherAssert.assertThat(result, `is`(weatherAlert))
    }

    @Test
    fun getLocationData_givenQueryDetails_returnDataIsAHealthyObjOfWeatherData() = runTest {
        //given location data with no exclusion
        //when requested weather data
        val response = repository.getLocationData(
            lat = 51.1,
            lon = -1.0,
            language = "en",
            units = "metric",
            exclude = "",
            apiKey = API_KEY
        ).first()
        //then all lists of daily hourly and minutely are retrieved
        assertThat(response, notNullValue())

        assertThat(response, instanceOf(WeatherData::class.java))

        assertThat(response.daily, notNullValue())
        assertThat(response.hourly, notNullValue())
        assertThat(response.minutely, notNullValue())

    }

    @Test
    fun getPeriodic_givenPresetQueryDetailsToGetBareMinimum_returnDataDoesNotHaveHourlyOrMinutely() =
        runTest {
            //when data requested using the bare minimum getter
            val response = repository.getPeriodic()

            //then only most important data is retrieved
            assertThat(response, notNullValue())

            assertThat(response, instanceOf(WeatherData::class.java))

            assertThat(response.daily, notNullValue())
            assertThat(response.hourly, nullValue())
            assertThat(response.minutely, nullValue())
        }

}