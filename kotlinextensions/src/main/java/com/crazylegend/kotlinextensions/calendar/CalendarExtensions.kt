package com.crazylegend.kotlinextensions.calendar

import android.Manifest.permission.READ_CALENDAR
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.annotation.RequiresPermission
import java.util.*


/**
 * Created by crazy on 11/11/19 to long live and prosper !
 */

val insertCalendarIntent: Intent
    get() {
        return Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
    }


fun Context.viewEventOnSpecificDate(CALENDAR_EVENT_TIME: Long, segment: String = "time") {
    val builder = CalendarContract.CONTENT_URI.buildUpon().appendPath(segment)
    ContentUris.appendId(builder, CALENDAR_EVENT_TIME)
    val uri = builder.build()
    val intent = Intent(Intent.ACTION_VIEW)
            .setData(uri)
    startActivity(intent)
}

@RequiresPermission(READ_CALENDAR)
fun Context.getEventIds(): ArrayList<Long> {
    val eventIdList = ArrayList<Long>()

    val EVENT_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Events._ID, // 0
            CalendarContract.Events.TITLE  // 1
    )
    val PROJECTION_EVENT_ID_INDEX = 0

    contentResolver.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, "", arrayOf(), null)?.use {
        while (it.moveToNext()) {
            // Get the field values
            val eventId = it.getLong(PROJECTION_EVENT_ID_INDEX)
            eventIdList.add(eventId)
        }
    }
    return eventIdList
}

@RequiresPermission(READ_CALENDAR)
fun Context.getEventTitles(): ArrayList<String> {
    val eventIdList = ArrayList<String>()

    val EVENT_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Events._ID, // 0
            CalendarContract.Events.TITLE  // 1
    )
    val PROJECTION_TITLE_INDEX = 1

    contentResolver.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, "", arrayOf(), null)?.use {
        while (it.moveToNext()) {
            // Get the field values
            val title = it.getString(PROJECTION_TITLE_INDEX)
            eventIdList.add(title)
        }
    }
    return eventIdList
}


fun Context.openCalendarIntentByID(eventId: Long) {
    val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
    val intent = Intent(Intent.ACTION_VIEW)
            .setData(uri)
    startActivity(intent)
}

fun Context.updateEventByID(eventId: Long, extras: Intent.() -> Unit = {}) {
    val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
    val intent = Intent(Intent.ACTION_EDIT)
            .setData(uri)
    intent.extras()
    startActivity(intent)
}

