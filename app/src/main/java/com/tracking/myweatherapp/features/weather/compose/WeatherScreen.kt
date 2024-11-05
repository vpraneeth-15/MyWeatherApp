package com.tracking.myweatherapp.features.weather.compose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tracking.myweatherapp.features.weather.model.WeatherEvent
import com.tracking.myweatherapp.features.weather.model.WeatherUiState
import com.tracking.myweatherapp.ui.components.SearchBar
import com.tracking.myweatherapp.ui.dimes.Dimens.ImageSizeMedium
import com.tracking.myweatherapp.ui.dimes.Dimens.MarginStandard
import com.tracking.myweatherapp.ui.dimes.Dimens.MediumPadding1
import com.tracking.myweatherapp.ui.theme.MyWeatherAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    state: WeatherUiState,
    query: String,
    onEvent: (WeatherEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = MarginStandard)
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(MediumPadding1))
        SearchBar(
            text = query,
            onValueChange = {
                onEvent(WeatherEvent.UpdateSearchQuery(it))
            },
            onSearch = {
                onEvent(WeatherEvent.SearchClicked)
            }
        )
        Spacer(modifier = Modifier.height(MediumPadding1))
        if (state is WeatherUiState.State) {
            val context = LocalContext.current
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(ImageSizeMedium)
                    .clip(MaterialTheme.shapes.medium),
                model = ImageRequest.Builder(context).data(state.state.weather.first().iconUrl)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                text = state.state.location,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(MediumPadding1))
            Text(
                text = state.state.temperature.toString(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
            )
        } else {
            val text = when (state) {
                WeatherUiState.Empty -> "Enter location to see weather"
                WeatherUiState.Loading -> "Loading..."
                WeatherUiState.Error -> "Something went wrong, try again later"
                else -> ""
            }
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WeatherScreenPreview() {
    MyWeatherAppTheme {
        WeatherScreen(state = WeatherUiState.Empty, query = "", onEvent = {})
    }
}