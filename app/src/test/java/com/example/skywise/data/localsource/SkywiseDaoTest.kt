package com.example.skywise.data.localsource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skywise.alertscreen.WeatherAlert
import com.example.skywise.data.FavoriteLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SkywiseDaoTest {
    @get:Rule
    var instanceExecutorRule = InstantTaskExecutorRule()

    lateinit var database: RoomClient
    lateinit var favoriteList: List<FavoriteLocation>

    @Before
    fun createDataBase() {
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(), RoomClient::class.java)
            .allowMainThreadQueries().build()

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
    fun closeDatabase() {
        database.close()
    }

    //
    @Test
    fun getOfflineDataByDate_insertOfflineDataIntoDatabase_insertedAndReturnedDataAreTheSame() =
        runTest {

            //given
            val offlineData =
                OfflineDataModel(0.325, 7.025, area = "Unit Testing", description = "Clear Weather")
            database.getDao().addOfflineData(offlineData)
            val currentDate = LocalDate.now().toString()
            //when
            val resultData = database.getDao().getOfflineData(currentDate)

            //then
            assertThat(resultData as OfflineDataModel, notNullValue())
            assertThat(resultData.lat, `is`(offlineData.lat))
            assertThat(resultData.description, `is`(offlineData.description))
            assertThat("message", resultData.lon, `is`(offlineData.lon))
            assertThat(resultData.area, `is`(offlineData.area))
        }

    @Test
    fun getFavoritesList_insertListOfLocationsToFavorites_insertedAndRetrievedListsAreTheSame() =
        runTest {
            //given a pre created list inserted into the db
            favoriteList.forEach { database.getDao().addLocation(it) }
            //when retrieved
            val resultList = database.getDao().getFavoriteLocations().first()

            //then given List should equal result List
            assertThat(resultList.size, `is`(favoriteList.size))
            //making sure list is retrieved in same order it was inserted in
            for (i in favoriteList.indices)
                assertThat(resultList[i], `is`(favoriteList[i]))
        }

    @Test
    fun getAlerts_insertAnAlert_insertedAlertIsRetrievedSuccessfully() = runTest{
        //given an alert that was inserted into db
        val weatherAlert = WeatherAlert(id= 500,startDate = 6246524864L, endDate = 6246624864L)
        database.getDao().addWeatherAlert(weatherAlert)
        //when retrieved
        val result = database.getDao().getWeatherAlert(weatherAlert.id)
        //then its the same as it was when inserted
        assertThat(result,`is`(weatherAlert))
    }


}