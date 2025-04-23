package com.vbteam.vibenote.ui.screens.notes

import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.data.model.Note
import java.time.LocalDateTime

// Функция для создания моковых записей с разными эмоциями
internal fun createMockNotes(): List<Note> {
    val now = LocalDateTime.now()
    return listOf(
        Note(
            id = "mock-happy-1",
            title = "Моковая запись - Радость",
            text = "Это тестовая запись с эмоцией Радость",
            createdAt = now.minusDays(1),
            updatedAt = now.minusDays(1),
            emotion = Emotion.HAPPY,
            emotionTagId = "happy-tag",
            emotionValue = 5
        ),
        Note(
            id = "mock-sad-1",
            title = "Моковая запись - Печаль",
            text = "Это тестовая запись с эмоцией Печаль",
            createdAt = now.minusDays(2),
            updatedAt = now.minusDays(2),
            emotion = Emotion.SAD,
            emotionTagId = "sad-tag",
            emotionValue = 3
        ),
        Note(
            id = "mock-angry-1",
            title = "Моковая запись - Злость",
            text = "Это тестовая запись с эмоцией Злость",
            createdAt = now.minusDays(3),
            updatedAt = now.minusDays(3),
            emotion = Emotion.ANGRY,
            emotionTagId = "angry-tag",
            emotionValue = 4
        ),
        Note(
            id = "mock-confused-1",
            title = "Моковая запись - Растерянность",
            text = "Это тестовая запись с эмоцией Растерянность",
            createdAt = now.minusDays(4),
            updatedAt = now.minusDays(4),
            emotion = Emotion.CONFUSED,
            emotionTagId = "confused-tag",
            emotionValue = 2
        ),
        Note(
            id = "mock-calm-1",
            title = "Моковая запись - Спокойствие",
            text = "Это тестовая запись с эмоцией Спокойствие",
            createdAt = now.minusDays(5),
            updatedAt = now.minusDays(5),
            emotion = Emotion.CALM,
            emotionTagId = "calm-tag",
            emotionValue = 4
        ),
        Note(
            id = "mock-worried-1",
            title = "Моковая запись - Тревога",
            text = "Это тестовая запись с эмоцией Тревога",
            createdAt = now.minusDays(6),
            updatedAt = now.minusDays(6),
            emotion = Emotion.WORRIED,
            emotionTagId = "worried-tag",
            emotionValue = 3
        ),
        Note(
            id = "mock-draft-1",
            title = "Моковая запись - Черновик",
            text = "Это тестовая запись с эмоцией Черновик",
            createdAt = now.minusDays(7),
            updatedAt = now.minusDays(7),
            emotion = Emotion.DRAFT,
            emotionTagId = "draft-tag",
            emotionValue = 1
        )
    )
}