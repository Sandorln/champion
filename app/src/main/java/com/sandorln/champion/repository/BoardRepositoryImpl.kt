package com.sandorln.champion.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sandorln.model.ChampionBoard
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor() : BoardRepository {
    override fun getChampionBoardPaging(championId: String): Pager<QuerySnapshot, com.sandorln.model.ChampionBoard> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        initialKey = null,
        pagingSourceFactory = { ChampionBoardPagingSource(championId) }
    )

    class ChampionBoardPagingSource(private val championId: String) : PagingSource<QuerySnapshot, com.sandorln.model.ChampionBoard>() {
        private val db = FirebaseFirestore.getInstance()

        override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, com.sandorln.model.ChampionBoard> = try {
            val pageSize = params.loadSize.toLong()

            db.collection(championId).limit(pageSize).get().addOnCompleteListener {

            }
            val currentPage = mutableListOf<com.sandorln.model.ChampionBoard>()
            val nextPage = null

            LoadResult.Page(
                data = currentPage,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

        override fun getRefreshKey(state: PagingState<QuerySnapshot, com.sandorln.model.ChampionBoard>): QuerySnapshot? = null
    }
}