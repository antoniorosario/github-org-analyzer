package com.antoniorosario.githubanalyzer.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
import org.parceler.ParcelConstructor


@Entity
@Parcel
data class Repo @ParcelConstructor constructor(
        @SerializedName("id")
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")
        val name: String,
        @SerializedName("stargazers_count")
        @ColumnInfo(name = "star_count")
        val starCount: Int,
        @SerializedName("forks_count")
        @ColumnInfo(name = "fork_count")
        val forkCount: Int,
        @ColumnInfo(name = "description")
        val description: String?,
        @SerializedName("html_url")
        @ColumnInfo(name = "url")
        val url: String,
        @SerializedName("owner")
        @Embedded(prefix = "organization_")
        val organization: Organization
)

@Parcel
data class Organization @ParcelConstructor constructor(
        @SerializedName("login")
        val name: String
)