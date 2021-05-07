package com.billyindrai.aplikasigithubuser

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Services {

    @GET("/search/users")
    @Headers("Authorization: token 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")
    fun searchUser(@Query("q") key: String): Call<Result>

    @GET("/users/{username}")
    @Headers("Authorization: token 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")
    fun getUser(@Path("username") username: String?): Call<User>

    @GET("/users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String?): Call<List<User>>

    @GET("/users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("username") username: String?): Call<List<User>>

}