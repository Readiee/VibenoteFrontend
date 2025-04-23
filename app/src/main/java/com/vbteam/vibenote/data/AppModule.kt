package com.vbteam.vibenote.data

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vbteam.vibenote.data.local.AppDatabase
import com.vbteam.vibenote.data.local.LocalNoteDao
import com.vbteam.vibenote.data.remote.CloudService
import com.vbteam.vibenote.data.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    @Provides
    fun provideNoteDao(db: AppDatabase): LocalNoteDao = db.noteDao()

    @Provides
    fun provideNoteRepository(localNoteDao: LocalNoteDao, cloudService: CloudService): NotesRepository =
        NotesRepository(localNoteDao, cloudService)

    @Provides
    @Singleton
    fun provideCloudService(): CloudService {
        return CloudService()
    }
}