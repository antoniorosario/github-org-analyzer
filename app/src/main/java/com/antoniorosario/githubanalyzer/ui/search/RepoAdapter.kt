package com.antoniorosario.githubanalyzer.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.antoniorosario.githubanalyzer.R
import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.ui.detail.RepoDetailActivity
import kotlinx.android.synthetic.main.list_item_repo_list.view.*

class RepoAdapter : RecyclerView.Adapter<RepoAdapter.RepoHolder>() {

    var repos = mutableListOf<Repo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_repo_list, parent, false)

        return RepoHolder(view)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        val repo = getItem(position)
        holder.bindRepo(repo)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    private fun getItem(position: Int): Repo {
        return repos[position]
    }

    fun addRepos(reposToAdd: List<Repo>) {
        repos.addAll(reposToAdd)
        notifyDataSetChanged()
    }

    fun removeRepos() {
        repos = mutableListOf()
        notifyDataSetChanged()
    }

    class RepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        private lateinit var repo: Repo
        private val context = itemView.context
        private val repoNameTextView = itemView.repo_name_text_view
        private val starCountTextView = itemView.star_count_text_view
        private val forkCountTextView = itemView.fork_count_text_view


        fun bindRepo(repo: Repo) {
            this.repo = repo
            with(repo) {
                repoNameTextView.text = name
                starCountTextView.text = context.getString(R.string.star_count, starCount)
                forkCountTextView.text = context.getString(R.string.fork_count, forkCount)
            }
        }

        override fun onClick(v: View?) {

            val intent = RepoDetailActivity.newIntent(context, repo.id)
            context.startActivity(intent)
        }
    }
}


