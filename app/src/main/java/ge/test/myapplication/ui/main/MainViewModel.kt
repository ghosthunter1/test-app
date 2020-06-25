package ge.test.myapplication.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ge.test.myapplication.network.api.Api
import ge.test.myapplication.network.api.ServiceBuilder
import ge.test.myapplication.network.interactors.GetGroupMembersInteractor
import ge.test.myapplication.network.interactors.GetInfoInteractor
import ge.test.myapplication.network.models.InfoResponse
import ge.test.myapplication.network.models.Member
import ge.test.myapplication.network.models.MembersResponse
import ge.test.myapplication.network.models.Resource
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.single.SingleObserveOn
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.BiFunction

class MainViewModel : ViewModel() {

    var info = MutableLiveData<Resource<InfoResponse>>()
    var memberlist = MutableLiveData<Resource<List<Member>>>()
    var hasmore = MutableLiveData<Boolean>()
    var currentPage = MutableLiveData<Int>()
    var compositeDisposable = CompositeDisposable()
    val api = ServiceBuilder.buildService(Api::class.java)
    var infoInteractor = GetInfoInteractor(api)
    var membersInteractor = GetGroupMembersInteractor(api)


    init {
        hasmore.value = true
        currentPage.value = 1
    }

    fun getInfo() {
        infoInteractor.execute().subscribe(object : SingleObserver<InfoResponse> {
            override fun onSuccess(t: InfoResponse) {
                membersInteractor.execute(currentPage.value!!).subscribe(object : SingleObserver<MembersResponse> {
                    override fun onSuccess(r: MembersResponse) {
                        hasmore.value = r.hasMore
                        memberlist.value = Resource(Resource.Status.SUCCESS, r.members, null)
                        info.value = Resource(Resource.Status.SUCCESS, t, null)
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        memberlist.value = Resource(Resource.Status.ERROR, null, "Connection Error")
                    }
                })
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable) {
                memberlist.value = Resource(Resource.Status.ERROR, null, "Connection Error")
            }

        })
    }


    fun getMembers() {
        membersInteractor.execute(currentPage.value!!)
            .subscribe(object : SingleObserver<MembersResponse> {
                override fun onSuccess(t: MembersResponse) {
                    hasmore.value = t.hasMore
                    memberlist.value = Resource(Resource.Status.SUCCESS, t.members, null)
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    memberlist.value = Resource(Resource.Status.ERROR, null, "Connection Error")

                }
            })
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }

}