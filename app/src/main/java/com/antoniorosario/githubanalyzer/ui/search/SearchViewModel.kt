package com.antoniorosario.githubanalyzer.ui.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.antoniorosario.githubanalyzer.data.Repo
import retrofit2.Response

/* Responsibility: Hold UI data*/
class SearchViewModel : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var currentResponse: MutableLiveData<Response<List<Repo>>> = MutableLiveData()
    var organizationQueried: MutableLiveData<String> = MutableLiveData()


    // setter methods help us with testing
    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun setCurrentResponse(response: Response<List<Repo>>?) {
        currentResponse.value = response
    }

    fun setOrganizationQueried(organizationName: String) {
        organizationQueried.value = organizationName
    }
}