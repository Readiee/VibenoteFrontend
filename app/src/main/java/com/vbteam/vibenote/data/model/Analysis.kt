package com.vbteam.vibenote.data.model

data class Analysis(
    val id: String,
    val result: String,
    val tags: List<AnalysisTag>
)

data class AnalysisTag(
    val tag: TagInfo,
    val value: Int,
    val triggerWords: List<TriggerWord>
)

data class TagInfo(
    val id: String,
    val name: String
)

data class TriggerWord(
    val id: String,
    val value: String
)
