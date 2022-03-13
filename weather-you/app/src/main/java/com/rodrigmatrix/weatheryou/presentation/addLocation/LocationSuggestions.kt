package com.rodrigmatrix.weatheryou.presentation.addLocation

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rodrigmatrix.weatheryou.R
import com.rodrigmatrix.weatheryou.data.mapper.FamousCitiesMapper
import com.rodrigmatrix.weatheryou.presentation.theme.WeatherYouTheme
import com.rodrigmatrix.weatheryou.presentation.utils.PreviewFamousCities
import kotlin.math.max

private val MinImageSize = 134.dp
private val CategoryShape = RoundedCornerShape(10.dp)
private const val CategoryTextProportion = 0.55f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationSuggestions(
    suggestions: List<com.rodrigmatrix.weatheryou.domain.model.City>,
    onLocationClick: (com.rodrigmatrix.weatheryou.domain.model.City) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.famous_cities),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(horizontal = 24.dp, vertical = 4.dp)
                .wrapContentHeight()
        )
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            Modifier.padding(horizontal = 16.dp)
        ) {
            items(suggestions) {
                LocationRow(
                    location = it,
                    gradient = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer),
                    onLocationClick,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun LocationRow(
    location: com.rodrigmatrix.weatheryou.domain.model.City,
    gradient: List<Color>,
    onLocationClick: (com.rodrigmatrix.weatheryou.domain.model.City) -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier
            .aspectRatio(1.45f)
            .shadow(elevation = 4.dp, shape = CategoryShape)
            .clip(CategoryShape)
            .background(Brush.horizontalGradient(gradient))
            .clickable { onLocationClick(location) },
        content = {
            Text(
                text = stringResource(location.name),
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .padding(4.dp)
                    .padding(start = 8.dp)
            )
            AsyncImage(
                model = location.image,
                contentScale = ContentScale.FillBounds,
                contentDescription = stringResource(R.string.image_of_city, location.name),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 100.dp,
                            topEnd = 0.dp,
                            bottomStart = 100.dp,
                            bottomEnd = 0.dp
                        )
                    )
            )
        }
    ) { measurables, constraints ->
        val textWidth = (constraints.maxWidth * CategoryTextProportion).toInt()
        val textPlaceable = measurables[0].measure(Constraints.fixedWidth(textWidth))
        val imageSize = max(MinImageSize.roundToPx(), constraints.maxHeight)
        val imagePlaceable = measurables[1].measure(Constraints.fixed(imageSize, imageSize))
        layout(
            width = constraints.maxWidth,
            height = constraints.minHeight
        ) {
            textPlaceable.placeRelative(
                x = 0,
                y = (constraints.maxHeight - textPlaceable.height) / 2
            )
            imagePlaceable.placeRelative(
                x = textWidth,
                y = (constraints.maxHeight - imagePlaceable.height) / 2
            )
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun LocationSuggestionsPreview() {
    WeatherYouTheme {
        LocationSuggestions(
            PreviewFamousCities,
            { }
        )
    }
}