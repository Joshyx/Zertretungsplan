package de.joshi.application.api

import de.sematre.dsbmobile.DSBMobile
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

data class Timetable(
    val name: String,
    val pages: List<URL>,
    val date: Date
) {
    companion object {
        fun of(timeTables: List<DSBMobile.TimeTable>): List<Timetable> {
            val groupedTables = timeTables.groupBy { it.uuid }
            return groupedTables.map { entry ->
                val timeTable = entry.value
                Timetable(
                    name = timeTable.firstOrNull()?.groupName ?: "",
                    pages = timeTable.map { URL(it.detail) },
                    date = SimpleDateFormat("dd.MM.yyyy hh:mm").parse(timeTable.firstOrNull()?.date)
                )
            }
        }
    }
}