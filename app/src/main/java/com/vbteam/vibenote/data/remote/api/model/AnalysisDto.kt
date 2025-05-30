package com.vbteam.vibenote.data.remote.api.model

data class AnalysisDto(
    val id: String,
    val entryText: String,
    val result: String,
    val tags: List<AnalysisTagDto>
)

data class AnalysisTagDto(
    val tag: TagInfoDto,
    val value: Int,
    val triggerWords: List<TriggerWordDto>
)

data class TagInfoDto(
    val id: String,
    val name: String
)

data class TriggerWordDto(
    val id: String,
    val value: String
)

data class EntryDetailsDto(
    val id: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val analysis: AnalysisDto?
)

data class AnalysisResponse(
    val id: String,
    val entryText: String,
    val result: String,
    val tags: List<AnalysisTagDto>
) 