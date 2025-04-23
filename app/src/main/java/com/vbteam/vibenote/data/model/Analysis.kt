package com.vbteam.vibenote.data.model

import java.time.LocalDateTime

data class Analysis(
    val id: String,
    val entryId: String,
    val result: String, // text
    val createdAt: LocalDateTime
)
