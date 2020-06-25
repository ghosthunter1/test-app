package ge.test.myapplication.network.api

import ge.test.myapplication.network.models.InfoResponse
import ge.test.myapplication.network.models.Member
import ge.test.myapplication.network.models.MembersResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("info.json")
    fun getInfo(): Single<InfoResponse>

    @GET("members/{page_number}.json")
    fun getMembers(@Path(value = "page_number", encoded = true) pageNumber: String): Single<MembersResponse>

}