package com.example.skywise.favoritelocations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.skywise.MainDispatcherRule
import com.example.skywise.data.FavoriteLocation
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherData
import com.example.skywise.data.remotesource.API_KEY
import com.example.skywise.settingsscreen.SkywiseSettings
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(manifest=Config.NONE)
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FavoriteLocationsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private lateinit var repository: Repository


    private lateinit var viewModel: FavoriteLocationsViewModel

    @Before
    fun createMockRepoAndViewModel() {

        repository = mockk()
        viewModel = FavoriteLocationsViewModel(repository)
    }

    @Test
    fun getFavoriteLocation_changesValueOfFavoriteLocationStateFlow() = runTest {
        // Given
        val favoriteLocations = listOf(FavoriteLocation(0.0, 0.0))
        coEvery { repository.getFavoriteLocations() } returns flowOf(favoriteLocations)

        // When
        viewModel.getFavoriteLocations()
        val result = viewModel.favoriteLocation.first()
        // Then
        assertThat(favoriteLocations, `is`(result))
    }

    @Test
    fun getLocationData_setsChangesTheValueOfShownData() = runTest {
        // Given
        val favoriteLocation = FavoriteLocation(0.0, 0.0)
        val weatherData = WeatherData()
        coEvery {
            repository.getLocationData(
                favoriteLocation.lat,
                favoriteLocation.lon,
                SkywiseSettings.lang,
                SkywiseSettings.units,
                "${SkywiseSettings.MINUTELY},${SkywiseSettings.HOURLY}",
                API_KEY
            )
        } returns flowOf(weatherData)

        // When
        viewModel.getLocationData(favoriteLocation)
        val result = viewModel.shownLocation.first()
        // Then
        assertThat(weatherData, `is`(result))
    }

    @Test
    fun removeLocation_isCalledOnRepository() {
        // Given
        val location = FavoriteLocation(0.0, 0.0)
        coEvery { repository.removeLocation(location) } just runs

        // When
        viewModel.removeLocation(location)

        // Then
        coVerify { repository.removeLocation(location) }
    }
}
