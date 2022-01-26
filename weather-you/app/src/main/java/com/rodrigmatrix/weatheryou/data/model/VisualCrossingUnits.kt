package com.rodrigmatrix.weatheryou.data.model

sealed class VisualCrossingUnits(val unit: String) {

    object Metric: VisualCrossingUnits("metric")

    object US: VisualCrossingUnits("us")

    object UK: VisualCrossingUnits("uk")
}