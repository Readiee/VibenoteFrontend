package com.vbteam.vibenote.data.remote.api.model

data class EntryDto(
    val id: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val tags: List<TagDto>
)

data class TagDto(
    val tagName: String,
    val value: Int
)

data class CreateEntryRequest(
    val content: String
)

data class EntryResponse(
    val id: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val analysis: AnalysisDto?
)

data class EntriesResponse(
    val entries: List<EntryDto>
) 