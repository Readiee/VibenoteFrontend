package com.vbteam.vibenote.data

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vbteam.vibenote.data.local.AppDatabase
import com.vbteam.vibenote.data.local.LocalNoteDao
import com.vbteam.vibenote.data.local.MIGRATION_1_2
import com.vbteam.vibenote.data.local.MIGRATION_2_3
import com.vbteam.vibenote.data.local.MIGRATION_3_4
import com.vbteam.vibenote.data.remote.CloudService
import com.vbteam.vibenote.data.remote.api.AuthApi
import com.vbteam.vibenote.data.remote.api.EntryApi
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
        )
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
        .build()
    }

    @Provides
    fun provideNoteDao(db: AppDatabase): LocalNoteDao = db.noteDao()

    @Provides
    fun provideNoteRepository(localNoteDao: LocalNoteDao, cloudService: CloudService): NotesRepository =
        NotesRepository(localNoteDao, cloudService)

    @Provides
    @Singleton
    fun provideCloudService(authApi: AuthApi, entryApi: EntryApi): CloudService {
        return CloudService(authApi, entryApi)
    }
}