package de.joshi.application

import de.joshi.application.api.DsbMobile
import de.joshi.application.parser.TimeTableTextParser
import java.nio.file.Files
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val timeTables = DsbMobile("236661", "GOmobile").getTimetables()

    val url = timeTables.first().pages.first()
    url.openStream().use {
        Files.copy(it, Path("src/main/resources/timetable-${Instant.now()}.jpg"))
    }

    val result = TimeTableTextParser().parse(Path("src/main/resources/output.txt").readText())

    println(result.joinToString("\n"))
}
