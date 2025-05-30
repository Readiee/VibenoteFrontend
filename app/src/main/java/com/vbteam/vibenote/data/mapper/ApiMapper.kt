package com.vbteam.vibenote.data.mapper

import com.vbteam.vibenote.data.model.*
import com.vbteam.vibenote.data.remote.api.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import android.util.Log
import java.util.UUID

private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

// Utility functions for date conversion
object DateConverter {
    private val formatters = listOf(
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"),
        DateTimeFormatter.ISO_DATE_TIME
    )

    fun parseDateTime(dateString: String): LocalDateTime {
        for (formatter in formatters) {
            try {
                return LocalDateTime.parse(dateString, formatter)
            } catch (e: DateTimeParseException) {
                continue
            }
        }
        Log.w("DateConverter", "Failed to parse date: $dateString with all formatters, using current time")
        return LocalDateTime.now()
    }

    fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTime.format(formatters[0])
    }
}

fun EntryDto.toDomain(): Note {
    return Note(
        id = UUID.randomUUID().toString(),
        cloudId = id,
        content = content,
        createdAt = DateConverter.parseDateTime(createdAt),
        updatedAt = DateConverter.parseDateTime(updatedAt),
        tags = tags.map { it.toDomain() },
        isSyncedWithCloud = true
    )
}

fun TagDto.toDomain(): Tag {
    return Tag(
        name = tagName,
        value = value
    )
}

fun Note.toCreateRequest(): CreateEntryRequest {
    return CreateEntryRequest(
        content = content
    )
}

fun AnalysisDto.toDomain(): Analysis {
    return Analysis(
        id = id,
        entryText = entryText,
        result = result,
        tags = tags.map { it.toDomain() }
    )
}

fun AnalysisTagDto.toDomain(): AnalysisTag {
    return AnalysisTag(
        tag = tag.toDomain(),
        value = value,
        triggerWords = triggerWords.map { it.toDomain() }
    )
}

fun TagInfoDto.toDomain(): TagInfo {
    return TagInfo(
        id = id,
        name = this@toDomain.name
    )
}

fun TriggerWordDto.toDomain(): TriggerWord {
    return TriggerWord(
        id = id,
        value = value
    )
}

fun Note.toUpdateRequest(): UpdateEntryRequest {
    return UpdateEntryRequest(
        content = this.content
    )
}

fun EntryDetailsDto.toDomain(): Note {
    return Note(
        id = UUID.randomUUID().toString(),
        cloudId = id,
        content = content,
        createdAt = DateConverter.parseDateTime(createdAt),
        updatedAt = DateConverter.parseDateTime(updatedAt),
        analysis = analysis?.toDomain(),
        isSyncedWithCloud = true
    )
}

fun AnalysisResponse.toDomain(): Analysis {
    return Analysis(
        id = id,
        entryText = entryText,
        result = result,
        tags = tags.map { it.toDomain() }
    )
}