package com.sandorln.champion.database.roomdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.sandorln.champion.model.ChampionData

@Dao
interface ChampionDao {
    @Query("SELECT * FROM ChampionData WHERE version == :version AND languageCode == :languageCode ORDER BY name")
    fun getAllChampion(version: String, languageCode: String): List<ChampionData>

    @Insert(onConflict = REPLACE)
    suspend fun insertChampionList(championList: List<ChampionData>)
}