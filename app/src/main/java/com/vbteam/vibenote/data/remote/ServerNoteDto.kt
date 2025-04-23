package com.vbteam.vibenote.data.remote

data class ServerNoteDTO(
    val entryId: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val emotionTagId: String?,
    val emotionName: String?,
    val emotionValue: Int?
)