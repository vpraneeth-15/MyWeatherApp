package com.tracking.myweatherapp.features.weather

import app.cash.turbine.test
import com.tracking.clients.common.ApiResult
import com.tracking.clients.common.Message
import com.tracking.myweatherapp.features.weather.model.WeatherEvent
import com.tracking.myweatherapp.features.weather.model.WeatherState
import com.tracking.myweatherapp.features.weather.model.WeatherUiState
import com.tracking.myweatherapp.features.weather.usecase.WeatherScreenRepository
import com.tracking.myweatherapp.services.location.model.LocationInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: WeatherScreenRepository = mockk()

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateSearchQuery updates searchQuery state`() = runTest {
        val newQuery = "London"
        viewModel.onEventUpdate(WeatherEvent.UpdateSearchQuery(newQuery))
        advanceUntilIdle()
        assertEquals(newQuery, viewModel.searchQuery.value)
    }

    @Test
    fun `handleSearchClickEvent fetches weather data and clears searchQuery`() = runTest {
        val searchQuery = "New York"
        val weatherState = WeatherState(0.0, 0.0, "", emptyList(), searchQuery)
        coEvery { repository.getWeather(city = searchQuery) } returns ApiResult.Success(weatherState)

        viewModel.onEventUpdate(WeatherEvent.UpdateSearchQuery(searchQuery))
        viewModel.onEventUpdate(WeatherEvent.SearchClicked)
        advanceUntilIdle()
        coVerify { repository.getWeather(city = searchQuery) }
        assertEquals("", viewModel.searchQuery.value)
        viewModel.stateFlow.test {
            assertEquals(WeatherUiState.State(weatherState), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleSearchClickEvent handles error response`() = runTest {
        val searchQuery = "Invalid City"
        val errorMessage = Message(200, "Something went wrong, please try again later")
        coEvery { repository.getWeather(city = searchQuery) } returns ApiResult.Error(errorMessage)

        viewModel.onEventUpdate(WeatherEvent.UpdateSearchQuery(searchQuery))
        viewModel.onEventUpdate(WeatherEvent.SearchClicked)
        advanceUntilIdle()
        coVerify { repository.getWeather(city = searchQuery) }
        assertEquals("", viewModel.searchQuery.value)
        viewModel.stateFlow.test {
            assertEquals(WeatherUiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateLocationInfo fetches weather data with location`() = runTest {
        val locationInfo = LocationInfo(10.0, 20.0)
        val weatherState = WeatherState(0.0, 0.0, "", emptyList(), "")
        coEvery { repository.getWeather(locationInfo) } returns ApiResult.Success(weatherState)

        viewModel.updateLocationInfo(locationInfo)
        advanceUntilIdle()
        coVerify { repository.getWeather(locationInfo) }
        viewModel.stateFlow.test {
            assertEquals(WeatherUiState.State(weatherState), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `locationServiceDisabled checks last saved location`() = runTest {
        val savedSearchQuery = "Paris"
        every { repository.readSearchQuery() } returns flowOf(savedSearchQuery)
        coEvery { repository.getWeather(city = savedSearchQuery) } returns ApiResult.Success(
            WeatherState(0.0, 0.0, "", emptyList(), savedSearchQuery)
        )

        viewModel.locationServiceDisabled()
        advanceUntilIdle()
        coVerify { repository.readSearchQuery() }
        coVerify { repository.getWeather(city = savedSearchQuery) }
    }

    @Test
    fun `checkLastSavedLocation fetches weather data if saved query exists`() = runTest {
        val savedSearchQuery = "Tokyo"
        every { repository.readSearchQuery() } returns flowOf(savedSearchQuery)
        coEvery { repository.getWeather(city = savedSearchQuery) } returns ApiResult.Success(
            WeatherState(0.0, 0.0, "", emptyList(), savedSearchQuery)
        )

        viewModel.checkLastSavedLocation()
        advanceUntilIdle()
        coVerify { repository.readSearchQuery() }
        coVerify { repository.getWeather(city = savedSearchQuery) }
    }

}
