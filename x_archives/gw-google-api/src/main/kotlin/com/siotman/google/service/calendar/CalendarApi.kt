package com.siotman.google.service.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_PATH: String = "/calendar/v3"

interface CalendarApi {

    @GET("$BASE_PATH/users/me/calendarList")
    fun calendars(
        @Query("maxResults") maxResults: Int,
        @Query("minAccessRole") minAccessRole: String? = null,
        @Query("pageToken") pageToken: String? = null,
        @Query("showDeleted") showDeleted: String? = null,
        @Query("showHidden") showHidden: String? = null,
        @Query("syncToken") syncToken: String? = null
    ): Call<CalendarResponse>
}

data class CalendarResponse(
    val kind: String,
    val etag: List<String>,
    val nextPageToken: String,
    val nextSyncToken: String,
    val items: List<CalendarListItem>
)

data class CalendarListItem(
    val kind: String,
    val etag: List<String>,
    val id: String,
    val summary: String,
    val description: String,
    val location: String,
    val timeZone: String,
    val summaryOverride: String,
    val colorId: String,
    val backgroundColor: String,
    val foregroundColor: String,
    val hidden: Boolean,
    val selected: Boolean,
    val accessRole: String,
    val defaultReminders: List<Reminder> = emptyList(),
    val notificationSettings: NotificationSettings,
    val primary: Boolean,
    val deleted: Boolean,
    val conferenceProperties: ConferenceProperties
) {
    data class Reminder(val method: String, val minutes: Int)

    data class Notification(val type: String, val method: String)
    data class NotificationSettings(val notifications: List<Notification> = emptyList())

    data class ConferenceProperties(val allowedConferenceSolutionTypes: List<String> = emptyList())
}