package com.example.insectopedia.helpers

import java.text.SimpleDateFormat
import java.util.*

private const val PICTURE_PREFIX = "Insectopedia"
private const val PICTURE_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PICTURE_EXTENSION = ".jpg"

fun generatePictureFileName(): String {
    val date = SimpleDateFormat(PICTURE_DATE_FORMAT, Locale.US)
        .format(System.currentTimeMillis())
    return "${PICTURE_PREFIX}-${date}${PICTURE_EXTENSION}"
}