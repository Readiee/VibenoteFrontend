package com.vbteam.vibenote.data.remote.api

import com.vbteam.vibenote.data.remote.api.model.*
import retrofit2.http.*

interface EntryApi {
    @GET(ApiConfig.Endpoints.ENTRIES)
    suspend fun getEntries(): List<EntryDto>

    @POST(ApiConfig.Endpoints.ENTRIES)
    suspend fun createEntry(@Body request: CreateEntryRequest): EntryResponse

    @GET(ApiConfig.Endpoints.ENTRY)
    suspend fun getEntry(@Path("id") id: String): EntryResponse

    @PUT(ApiConfig.Endpoints.ENTRY)
    suspend fun updateEntry(@Path("id") id: String, @Body request: UpdateEntryRequest): EntryResponse

    @POST(ApiConfig.Endpoints.ANALYSIS)
    suspend fun analyzeEntry(@Path("entryId") entryId: String): AnalysisResponse
} 