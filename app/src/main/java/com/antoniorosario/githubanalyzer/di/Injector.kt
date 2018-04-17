package com.antoniorosario.githubanalyzer.di

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import com.antoniorosario.githubanalyzer.API_KEY
import com.antoniorosario.githubanalyzer.BASE_URL
import com.antoniorosario.githubanalyzer.data.source.RepoRepository
import com.antoniorosario.githubanalyzer.data.source.local.RepoDao
import com.antoniorosario.githubanalyzer.data.source.local.RepoDatabase
import com.antoniorosario.githubanalyzer.data.source.local.RepoLocalDataSource
import com.antoniorosario.githubanalyzer.data.source.remote.RepoRemoteDataSource
import com.antoniorosario.githubanalyzer.network.ApiKeyInterceptor
import com.antoniorosario.githubanalyzer.network.GitHubApi
import com.antoniorosario.githubanalyzer.ui.detail.RepoDetailPresenter
import com.antoniorosario.githubanalyzer.ui.search.RepoAdapter
import com.antoniorosario.githubanalyzer.ui.search.SearchPresenter
import com.antoniorosario.githubanalyzer.ui.search.SearchViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//TODO(2) this is getting messy. Use Dagger2 for dependency injection.
object Injector {

    fun provideSearchPresenter(uiController: FragmentActivity) =
            SearchPresenter(provideRepoRepository(uiController), provideSearchViewModel(uiController))

    private fun provideSearchViewModel(uiController: FragmentActivity) =
            ViewModelProviders.of(uiController).get(SearchViewModel::class.java)

    private fun provideRepoRepository(context: Context): RepoRepository {
        val repoDatabase = provideRepoDatabaseInstance(context)

        return RepoRepository(
                provideRepoLocalDataSource(repoDatabase!!.repoDao()),
                provideRepoRemoteDataSource()
        )
    }

    fun provideRepoDatabaseInstance(context: Context) = RepoDatabase.getInstance(context)

    private fun provideRepoLocalDataSource(repoDao: RepoDao) = RepoLocalDataSource(repoDao)

    private fun provideRepoRemoteDataSource() = RepoRemoteDataSource(provideGitHubApi())

    private fun provideGitHubApi() = provideRetrofit(BASE_URL).create(GitHubApi::class.java)

    fun provideRetrofit(baseUrl: String): Retrofit =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient()).build()

    private fun provideOkHttpClient() =
            OkHttpClient.Builder()
                    .addInterceptor(provideAccessTokenInterceptor(API_KEY))
                    .build()

    private fun provideAccessTokenInterceptor(apiKey: String) = ApiKeyInterceptor(apiKey)

    fun provideSearchAdapter() = RepoAdapter()

    fun provideLayoutManager(context: Context) = LinearLayoutManager(context)

    fun provideRepoDetailPresenter() = RepoDetailPresenter()
}

