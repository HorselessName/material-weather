package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.presentation.components.ExpandButton
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouDivider
import com.rodrigmatrix.weatheryou.presentation.extensions.*
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewFutureDaysForecast

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FutureDaysForecast(
    futureDaysList: List<WeatherDay>,
    isExpanded: Boolean,
    onExpandedButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.next_x_days_forecast, futureDaysList.size),
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 20.dp,
                            top = 10.dp
                        ),
                    style = MaterialTheme.typography.headlineSmall
                )
                ExpandButton(
                    isExpanded = isExpanded,
                    contentDescription = stringResource(R.string.show_all_days_forecast),
                    onExpandButtonClick = {
                        onExpandedButtonClick(it)
                    },
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }
            futureDaysList.forEachIndexed { index, day ->
                DayRow(day, index == 0)
            }
        }
    }
}

@Composable
fun DayRow(day: WeatherDay, isToday: Boolean) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(day.icon))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    WeatherYouDivider(Modifier.height(1.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            }
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = if (isToday) {
                    stringResource(R.string.today)
                } else {
                    day.dateTime.getDateWithMonth()
                },
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = day.weatherCondition,
                modifier = Modifier
                    .padding(bottom = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row {
            Column {
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier
                        .size(42.dp)
                )
                if (day.precipitationType.isNotEmpty()) {
                    Text(
                        text = day.precipitationProbability.percentageString(),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(Modifier.align(Alignment.Top)) {
                Text(
                    text = day.maxTemperature.temperatureString(),
                    modifier = Modifier
                        .align(Alignment.End),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = day.minTemperature.temperatureString(),
                    modifier = Modifier
                        .align(Alignment.End),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
    ExpandedCardContent(
        day = day,
        isExpanded = isExpanded
    )
}

@Composable
fun ExpandedCardContent(
    day: WeatherDay,
    isExpanded: Boolean
) {
    AnimatedVisibility(visible = isExpanded) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            LazyRow(Modifier.padding(start = 16.dp, end = 16.dp)) {
                items(day.hours) { item  ->
                    HourRow(item, false)
                }
            }
            if (day.precipitationType.isNotEmpty()) {
                Text(
                    text = stringResource(
                        R.string.chance_of_precipitation,
                        day.precipitationType,
                        day.precipitationProbability.percentageString()
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            Text(
                text = stringResource(
                    R.string.sunrise_sunset_x_y,
                    day.sunrise.getHourString(),
                    day.sunset.getHourString()
                ),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.wind_x, day.windSpeed.windSpeed()),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.humidity_x, day.humidity.percentageString()),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FutureDaysForecastPreview() {
    WeatherYouTheme {
        FutureDaysForecast(
            PreviewFutureDaysForecast,
            isExpanded = false,
            {}
        )
    }
}
