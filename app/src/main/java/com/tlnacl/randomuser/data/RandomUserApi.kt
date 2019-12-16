package com.tlnacl.randomuser.data

import retrofit2.http.GET

interface RandomUserApi {
    @GET("/api/?results=10")
    suspend fun getRandomUserData(): RandomUserData
}