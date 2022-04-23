package com.sandorln.champion.database.shareddao

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sandorln.champion.model.VersionLol.VersionCategory

class VersionDaoImpl(
    private val pref: SharedPreferences,
    private val gson: Gson
) : VersionDao {
    companion object {
        private const val KEY_VERSION_CATEGORY = "key_version_category"
        private const val KEY_VERSION_LIST = "key_version_list"
        private const val KEY_VERSION = "key_version"
    }

    override fun insertVersionCategory(versionCategory: VersionCategory) {
        pref.edit(commit = true) {
            putString(KEY_VERSION_CATEGORY, gson.toJson(versionCategory))
        }
    }

    override fun getVersionCategory(): VersionCategory =
        try {
            gson.fromJson(pref.getString(KEY_VERSION_CATEGORY, ""), VersionCategory::class.java)
        } catch (e: Exception) {
            VersionCategory()
        }

    override fun insertVersionList(versionList: List<String>) {
        pref.edit(commit = true) {
            putString(KEY_VERSION_LIST, gson.toJson(versionList))
        }
    }

    override fun getVersionList(): List<String> =
        try {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(pref.getString(KEY_VERSION_LIST, ""), type)
        } catch (e: Exception) {
            mutableListOf()
        }

    override fun insertVersion(version: String) {
        pref.edit(commit = true) { putString(KEY_VERSION, version) }
    }

    override fun getVersion(): String = try {
        pref.getString(KEY_VERSION, "") ?: ""
    } catch (e: Exception) {
        ""
    }
}