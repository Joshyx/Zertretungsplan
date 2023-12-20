package de.joshi.application.api

import java.text.SimpleDateFormat
import java.util.Date

data class TimeTableEntry (
    val `class`: String,
    val date: Date,
    val hour: String,
    val substituteTeacher: String,
    val substituteSubject: String,
    val room: String,
    val originalTeacher: String,
    val originalSubject: String,
    val description: String
) {
    override fun toString(): String {
        return "$`class` | ${SimpleDateFormat("dd.MM.").format(date)} | $hour | $substituteTeacher | $substituteSubject | $room | $originalTeacher | $originalSubject | $description"
    }
}