package com.sandorln.champion.repository

import com.sandorln.champion.database.roomdao.ChampionDao
import com.sandorln.champion.manager.VersionManager
import com.sandorln.champion.model.ChampionData
import com.sandorln.champion.model.result.ResultData
import com.sandorln.champion.network.ChampionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class ChampionRepository(
    private val championService: ChampionService,
    private val championDao: ChampionDao
) {
    /**
     * 모든 챔피언 정보 가져오기
     */
    val championList: MutableStateFlow<ResultData<List<ChampionData>>> = MutableStateFlow(ResultData.Loading)
    suspend fun refreshAllChampionList() {
        try {
            championList.emit(ResultData.Loading)
            val response = championService.getAllChampion(VersionManager.getVersion().category.champion)
            response.parsingData()
            championDao.insertChampionList(response.rChampionList)

            val allChampions = ResultData.Success(response.rChampionList.sortedBy { it.name })
            championList.emit(allChampions)
        } catch (e: Exception) {
            championList.emit(ResultData.Failed(e))
        }
    }

    /**
     * 특정 캐릭터 정보값 가져오기
     */
    private var isLoadingGetChampion = false
    suspend fun getChampionInfo(champID: String): ResultData<ChampionData> =
        if (!isLoadingGetChampion) {
            try {
                withContext(Dispatchers.IO) {
                    isLoadingGetChampion = true
                    val response = championService.getChampionDetailInfo(VersionManager.getVersion().category.champion, champID)
                    response.parsingData()
                    ResultData.Success(response.rChampionList.first())
                }
            } catch (e: Exception) {
                ResultData.Failed(e)
            } finally {
                isLoadingGetChampion = false
            }
        } else
            ResultData.Failed(Exception("이미 로딩중 입니다"))
}