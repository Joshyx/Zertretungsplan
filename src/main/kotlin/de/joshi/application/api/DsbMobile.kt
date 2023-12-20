package de.joshi.application.api

import de.sematre.dsbmobile.DSBMobile

class DsbMobile (
    user: String,
    password: String
) {
    private val dsbMobile = DSBMobile(user, password)
    fun getTimetables(): List<Timetable> {
        return Timetable.of(dsbMobile.timeTables)
    }
}