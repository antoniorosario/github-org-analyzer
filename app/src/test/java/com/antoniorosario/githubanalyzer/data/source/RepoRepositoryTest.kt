package com.antoniorosario.githubanalyzer.data.source

import com.antoniorosario.githubanalyzer.data.Organization
import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.data.source.local.RepoLocalDataSource
import com.antoniorosario.githubanalyzer.data.source.remote.RepoRemoteDataSource
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*


//TODO(1) Write unit test
class RepoRepositoryTest {

    private lateinit var repoRepository: RepoRepository
    private lateinit var repos: List<Repo>

    @Mock private lateinit var repoRemoteDataSource: RepoRemoteDataSource
    @Mock private lateinit var repoLocalDataSource: RepoLocalDataSource
    @Mock private lateinit var getReposCallback: RepoDataSource.LoadReposCallback
    @Captor private lateinit var reposCallbackCaptor: ArgumentCaptor<RepoDataSource.LoadReposCallback>

    @Before fun setUpRepoRepository() {
        MockitoAnnotations.initMocks(this)
        repoRepository = RepoRepository(repoLocalDataSource, repoRemoteDataSource)
        repos = Collections.singletonList(
                Repo(1, "Repo1", 50, 40, "Descrip", "url.com", Organization("GitHub"))
        )
    }

    @Test fun getRepos_shouldRequestsAllReposFromLocalDataSource() {


    }

}