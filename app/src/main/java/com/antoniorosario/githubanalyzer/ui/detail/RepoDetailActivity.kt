package com.antoniorosario.githubanalyzer.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.antoniorosario.githubanalyzer.R
import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.data.source.local.RepoDatabase
import com.antoniorosario.githubanalyzer.di.Injector
import kotlinx.android.synthetic.main.activity_repo_detail.*

class RepoDetailActivity : AppCompatActivity(), RepoDetailView {
    companion object {
        const val EXTRA_REPO_ID = "extra_repo_id"
        const val KEY_REPO_ID = "key_repo_id"

        fun newIntent(packageContext: Context, repoId: Int): Intent {
            val intent = Intent(packageContext, RepoDetailActivity::class.java)
            intent.putExtra(EXTRA_REPO_ID, repoId)
            return intent
        }
    }

    private lateinit var repoDetailPresenter: RepoDetailPresenter
    private var currentRepo: Repo? = null
    private var repoDataBase: RepoDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)
        repoDetailPresenter = Injector.provideRepoDetailPresenter()
        repoDetailPresenter.repoDetailView = this
        repoDataBase = Injector.provideRepoDatabaseInstance(this)

        val repoId = intent.getIntExtra(EXTRA_REPO_ID, -1)

        currentRepo = repoDataBase?.repoDao()?.loadRepoById(repoId)

        initRepoDetailView(currentRepo)

        repo_url_button.setOnClickListener { repoDetailPresenter.onRepoUrlButtonClicked() }

        savedInstanceState?.let {
            currentRepo = repoDataBase?.repoDao()?.loadRepoById(it.getInt(KEY_REPO_ID))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_REPO_ID, currentRepo?.id!!)
    }

    private fun initRepoDetailView(repo: Repo?) {

        if (repo == null) {
            repo_name.text = getString(R.string.error_loading_repo)
        } else {
            repo.let {
                repo_name.text = getString(R.string.repo_name, it.name)
                repo_organization.text = getString(R.string.repo_organization, it.organization.name)
                repo_stars.text = getString(R.string.star_count, it.starCount)
                repo_forks.text = getString(R.string.fork_count, it.forkCount)
                repo_description.text = getString(R.string.repo_description, it.description)
            }
        }
    }

    override fun goToRepoUrl() {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(currentRepo?.url)
        startActivity(browserIntent)
    }
}
