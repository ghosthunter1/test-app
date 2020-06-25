package ge.test.myapplication.network.interactors

import ge.test.myapplication.network.api.Api
import ge.test.myapplication.network.models.InfoResponse
import ge.test.myapplication.network.models.MembersResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetGroupMembersInteractor(val api: Api) {
    fun execute(page: Int): Single<MembersResponse> {
        return api.getMembers(page.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}