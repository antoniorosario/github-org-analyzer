package com.antoniorosario.githubanalyzer.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.antoniorosario.githubanalyzer.data.Repo

@Dao
interface RepoDao {
    @Query("SELECT * FROM Repo WHERE organization_name = :organization")
    fun loadReposByOrganization(organization: String): List<Repo>

    @Query("SELECT * FROM REPO WHERE id = :id")
    fun loadRepoById(id: Int): Repo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repositories: List<Repo>)
}