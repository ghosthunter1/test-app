package ge.test.myapplication.network.interactors

import ge.test.myapplication.network.api.Api
import ge.test.myapplication.network.models.InfoResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetInfoInteractor(val api: Api) {
    fun execute(): Single<InfoResponse> {
        return api.getInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}