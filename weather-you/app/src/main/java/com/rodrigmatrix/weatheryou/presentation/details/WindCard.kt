package com.rodrigmatrix.weatheryou.presentation.details

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.presentation.components.WeatherYouCard
import com.rodrigmatrix.weatheryou.presentation.extensions.speedString
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme

@Composable
fun WindCard(
    windSpeed: Double,
    windDirection: Double,
    modifier: Modifier = Modifier
) {
    WeatherYouCard(modifier.height(200.dp)) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_air),
                        contentDescription = stringResource(R.string.wind),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = stringResource(R.string.wind),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            Icon(
                painter = painterResource(R.drawable.ic_round_navigation),
                contentDescription = stringResource(R.string.wind_direction),
                modifier = Modifier
                    .size(70.dp)
                    .rotate(windDirection.toFloat())
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = windSpeed.speedString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WindCardPreview() {
    WeatherYouTheme {
        WindCard(
            windSpeed = 10.0,
            windDirection = 251.0
        )
    }
}