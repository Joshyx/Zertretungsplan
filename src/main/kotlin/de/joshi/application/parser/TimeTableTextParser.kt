package de.joshi.application.parser

import de.joshi.application.api.TimeTableEntry
import java.text.ParseException
import java.text.SimpleDateFormat

class TimeTableTextParser {

    fun parse(text: String): List<TimeTableEntry> {

        return toParseableForm(text).map {
            lineToEntry(it)
        }.toSet().toList().map {
            if(it.`class`.length <= 3) return@map listOf(it)

            val lastDigitIndex = it.`class`.indexOfLast { it.isDigit() }
            val classes = it.`class`.substring(lastDigitIndex + 1)

            classes.map { classId ->
                TimeTableEntry(
                    "${it.`class`.take(lastDigitIndex + 1)}$classId",
                    it.date,
                    it.hour,
                    it.substituteTeacher,
                    it.substituteSubject,
                    it.room,
                    it.originalTeacher,
                    it.originalSubject,
                    it.description
                )
            }
        }.flatten()
    }

    fun lineToEntry(line: String): TimeTableEntry {
        val tokens = line.split(" ").toMutableList()
        if(!tokens[1].endsWith(".") && tokens[1].split(".").size > 2) {
            val token = tokens[1]
            tokens.set(1, token.take(token.indexOfLast { it == '.' }))
            tokens.add(2, token.split(".")[2])
        }
        try {
            SimpleDateFormat("dd.MM").parse(tokens[1])
        } catch (e: ParseException) {
            tokens.add(0, "err")
        }

        val `class` = tokens[0]
        val date = SimpleDateFormat("dd.MM").parse(tokens[1])
        val hour = tokens[2]
        val substituteTeacher = tokens[3].uppercase()
        val substituteSubject = tokens[4].uppercase()
        val room = tokens[5].uppercase().replace("O", "0")
        val originalTeacher = tokens[6].uppercase()
        val originalSubject = tokens[7].uppercase()
        val description = if(tokens.size > 8) {
            tokens.subList(8, tokens.size).joinToString(" ")
        } else {
            ""
        }

        return TimeTableEntry(
            `class` = `class`,
            date = date,
            hour = hour,
            substituteTeacher = substituteTeacher,
            substituteSubject = substituteSubject,
            room = room,
            originalTeacher = originalTeacher,
            originalSubject = originalSubject,
            description = description
        )
    }

    private fun toParseableForm(text: String): List<String> {
        val splittedText = text
            .split("abw. entfall.")
            .filter { it.isNotBlank() }

        val body = splittedText[1].trim()
            .replace("|", " ")
            .replace("_", " ")
            .replace(" -", "-")
            .replace("(", " ")
            .replace(")", " ")
            .replace(":", " ")
            .replace("\\", " ")
            .replace("/", " ")
            .replace("\n-", "\n")
            .replace(".\n", "\n")
            .replace(",\n", "\n")
            .replace(".12-",". 2-")
            .replace(".13-",". 3-")
            .replace(".14-",". 4-")
            .replace(".15-",". 5-")
            .replace(".16-",". 6-")
            .replace(".17-",". 7-")
            .replace(".18-",". 8-")
            .replace(".19-",". 9-")
            .replace(" +".toRegex(), " ")

        return body.split("\n").filter {
            it.trim().contains(" ") && it.any { it.isDigit() }
        }.map { it.trim() }
    }
}