package com.sandorln.champion.usecase

import com.sandorln.champion.model.SummonerSpell
import com.sandorln.champion.model.result.ResultData
import com.sandorln.champion.repository.SummonerSpellRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetSummonerSpellList(
    private val getVersion: GetVersion,
    private val summonerSpellRepository: SummonerSpellRepository
) {
    operator fun invoke(): Flow<ResultData<List<SummonerSpell>>> =
        getVersion()
            .flatMapLatest { summonerVersion ->
                if (summonerVersion.isEmpty())
                    flow { emit(ResultData.Failed(Exception("버전 정보를 알 수 없습니다"))) }
                else
                    summonerSpellRepository.getSummonerSpellList(summonerVersion)
            }
}