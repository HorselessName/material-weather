package com.rodrigmatrix.weatheryou.presentation.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavigatorState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.details.WeatherDetailsScreen
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherList
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewWeatherLocation
import com.rodrigmatrix.weatheryou.presentation.utils.WeatherYouAppState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bottomAppState: MutableState<Boolean>,
    onAddLocation: () -> Unit,
    onCloseClick: () -> Unit,
    expandedScreen: Boolean,
    viewModel: HomeViewModel = getViewModel(),
    locationPermissionState: PermissionState = rememberPermissionState(ACCESS_COARSE_LOCATION)
) {
    val viewState by viewModel.viewState.collectAsState()
    bottomAppState.value = viewState.isLocationSelected().not()
    BackHandler {
        if (viewState.isLocationSelected().not()) {
            onCloseClick()
        } else {
            viewModel.selectLocation(null)
        }
    }
    when {
        viewState.showLocationPermissionRequest(locationPermissionState) -> {
            HomeFabContent(
                expandedScreen = expandedScreen,
                onAddLocation = onAddLocation
            ) {
                RequestLocationPermission(
                    locationPermissionState,
                    onLocationPermissionChanged = {

                    }
                )
            }
        }
        expandedScreen -> {
            HomeScreenWithLocation(
                viewState = viewState,
                onItemClick = { weatherLocation ->
                    viewModel.selectLocation(weatherLocation)
                },
                onSwipeRefresh = viewModel::loadLocations,
                onCloseClick = viewModel::onCloseClicked,
                onDeleteLocation = { location ->
                    viewModel.deleteLocation(location)
                }
            )
        }
        else -> {
            AnimatedVisibility(
                visible = viewState.isLocationSelected(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                WeatherDetailsScreen(
                    weatherLocation = viewState.selectedWeatherLocation,
                    onCloseClick = {
                        viewModel.selectLocation(null)
                    },
                    expandedScreen = expandedScreen
                )
            }
            AnimatedVisibility(
                visible = viewState.isLocationSelected().not(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                HomeFabContent(
                    expandedScreen = expandedScreen,
                    onAddLocation = onAddLocation
                ) {
                    HomeScreen(
                        viewState = viewState,
                        onItemClick = { weatherLocation ->
                            viewModel.selectLocation(weatherLocation)
                        },
                        onSwipeRefresh = viewModel::loadLocations,
                        onDeleteLocation = { location ->
                            viewModel.deleteLocation(location)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFabContent(
    expandedScreen: Boolean,
    onAddLocation: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            if (expandedScreen.not()) {
                LargeFloatingActionButton(
                    onClick = onAddLocation,
                    modifier = Modifier.padding(bottom = 80.dp),
                    shape = RoundedCornerShape(100)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        modifier = Modifier.size(24.dp),
                        contentDescription = stringResource(R.string.add_location)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewState: HomeViewState,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit
) {
    HomeScreenContent(
        viewState,
        onItemClick,
        onSwipeRefresh,
        onDeleteLocation
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreenContent(
    viewState: HomeViewState,
    onItemClick: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        when {
            viewState.isLoading.not() && viewState.locationsList.isEmpty() -> {
                WeatherLocationsEmptyState()
            }
            else -> {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(viewState.isLoading),
                    onRefresh = onSwipeRefresh,
                    swipeEnabled = viewState.locationsList.isNotEmpty()
                ) {
                    WeatherLocationList(
                        viewState.locationsList,
                        onItemClick = onItemClick,
                        onLongPress = onDeleteLocation,
                        contentPaddingValues = PaddingValues(bottom = 200.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreenWithLocation(
    viewState: HomeViewState,
    onItemClick: (WeatherLocation) -> Unit,
    onDeleteLocation: (WeatherLocation) -> Unit,
    onSwipeRefresh: () -> Unit,
    onCloseClick: () -> Unit
) {
    val detailsWeight: Float by animateFloatAsState(
        targetValue = if (viewState.selectedWeatherLocation != null) 1F else 0.1F,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    )
    Row {
        Column(Modifier.weight(1f)) {
            HomeScreenContent(
                viewState,
                onItemClick,
                onSwipeRefresh,
                onDeleteLocation
            )
        }
        if (viewState.selectedWeatherLocation != null) {
            Column(Modifier.weight(detailsWeight)) {
                WeatherDetailsScreen(
                    weatherLocation = viewState.selectedWeatherLocation,
                    onCloseClick = onCloseClick,
                    expandedScreen = true
                )
            }
        }
    }
}

@Composable
fun WeatherLocationsEmptyState() {
    Column(Modifier.fillMaxSize()) {
        Text(text = stringResource(R.string.empty_locations))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    locationPermissionState: PermissionState = rememberPermissionState(ACCESS_COARSE_LOCATION),
    onLocationPermissionChanged: () -> Unit
) {
    PermissionRequired(
        permissionState = locationPermissionState,
        permissionNotGrantedContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 200.dp)
            ) {
                Image(
                    imageVector = Icons.Filled.Place,
                    contentDescription = stringResource(R.string.location_image),
                    modifier = Modifier
                        .size(120.dp)
                        .padding(10.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = stringResource(R.string.enable_location),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = stringResource(R.string.enable_location_description),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text(stringResource(R.string.grant_location_permission))
                }
            }
        },
        permissionNotAvailableContent = {
            onLocationPermissionChanged()
        }
    ) {
        onLocationPermissionChanged()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    WeatherYouTheme {
        HomeScreen(
            viewState = HomeViewState(locationsList = PreviewWeatherList),
            { },
            { },
            { }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(device = Devices.PIXEL_C)
@Preview(device = Devices.PIXEL_C, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenWithLocationPreview() {
    WeatherYouTheme {
        HomeScreenWithLocation(
            viewState = HomeViewState(
                locationsList = PreviewWeatherList,
                selectedWeatherLocation = PreviewWeatherLocation,
            ),
            { },
            { },
            { },
            { }
        )
    }
}