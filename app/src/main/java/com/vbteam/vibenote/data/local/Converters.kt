package com.vbteam.vibenote.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vbteam.vibenote.data.model.Analysis
import com.vbteam.vibenote.data.model.Tag
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val gson = Gson()
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Конвертеры для LocalDateTime
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(dateFormatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, dateFormatter) }
    }

    // Конвертеры для Analysis
    @TypeConverter
    fun fromAnalysis(analysis: Analysis?): String? {
        return analysis?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAnalysis(json: String?): Analysis? {
        return json?.let {
            gson.fromJson(it, Analysis::class.java)
        }
    }

    // Конвертеры для List<Tag>
    @TypeConverter
    fun fromTagList(tags: List<Tag>): String {
        return gson.toJson(tags)
    }

    @TypeConverter
    fun toTagList(json: String): List<Tag> {
        val type = object : TypeToken<List<Tag>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}