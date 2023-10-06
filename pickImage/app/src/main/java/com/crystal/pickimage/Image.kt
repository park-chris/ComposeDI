package com.crystal.pickimage

data class ImageResponse (
    val id: String,
    val urls: UrlResponse,
    val color: String,
)

data class UrlResponse (
    val row: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
)