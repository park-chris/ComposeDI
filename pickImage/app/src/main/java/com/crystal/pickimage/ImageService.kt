package com.crystal.pickimage

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ImageService {

    @Headers("Authorization: Client-ID RQgCT96yHgq20X6PdspdJbrWChgP2fh2b5oaI52rbi8")
    @GET("photos/random")
    fun getRandomImage() : Call<ImageResponse>




}