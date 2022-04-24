package com.sandorln.champion.usecase

import com.sandorln.champion.model.SummonerSpell
import com.sandorln.champion.model.result.ResultData
import com.sandorln.champion.repository.SummonerSpellRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetSummonerSpellList(
    private val getVersion: GetVersion,
    private val summonerSpellRepository: SummonerSpellRepository
) {
    operator fun invoke(): Flow<ResultData<List<SummonerSpell>>> =
        getVersion()
            .flatMapLatest { totalVersion ->
                flow {
                    emit(ResultData.Loading)
                    val summonerSpellList = summonerSpellRepository.getSummonerSpellList(totalVersion)
                    emit(ResultData.Success(summonerSpellList))
                }.catch {
                    emit(ResultData.Failed(Exception(it)))
                }
            }
}