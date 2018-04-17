package com.antoniorosario.githubanalyzer.ui.detail

import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RepoDetailPresenterTest {

    @Mock private lateinit var repoDetailView: RepoDetailView

    private lateinit var repoDetailPresenter: RepoDetailPresenter
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repoDetailPresenter = RepoDetailPresenter(repoDetailView)
    }

    @Test
    fun onRepoUrlButtonClicked() {
        repoDetailPresenter.onRepoUrlButtonClicked()
        Mockito.verify(repoDetailView).goToRepoUrl()
    }
}