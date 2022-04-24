package com.sandorln.champion.database.shareddao

import com.sandorln.champion.model.VersionLol.VersionCategory

interface VersionDao {
    fun insertVersionCategory(versionCategory: VersionCategory)
    fun getVersionCategory(): VersionCategory

    fun insertTotalVersionList(versionList: List<String>)
    fun getTotalVersionList(): List<String>

    fun getTotalVersion(): String
    fun insertTotalVersion(version: String)

    fun getChampionVersion(totalVersion: String): String
    fun insertChampionVersion(totalVersion: String, championVersion: String)
}