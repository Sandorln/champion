package com.sandorln.champion.repository

import androidx.paging.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sandorln.champion.model.ChampionBoard
import kotlinx.coroutines.tasks.await

class BoardRepository {
    fun getChampionBoardPagingFlow(championId: String): Pager<QuerySnapshot, ChampionBoard> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        initialKey = null,
        pagingSourceFactory = { ChampionBoardPagingSource(championId) }
    )

    class ChampionBoardPagingSource(private val championId: String) : PagingSource<QuerySnapshot, ChampionBoard>() {
        private val db = FirebaseFirestore.getInstance()

        override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ChampionBoard> = try {
            val pageSize = params.loadSize.toLong()

            val currentPage = params.key ?: db.collection(championId)
                .limit(pageSize)
                .get()
                .await()

            val lastOrNull = currentPage.documents.lastOrNull()

            val nextPage =
                if (lastOrNull == null)
                    null
                else {
                    db.collection(championId)
                        .limit(pageSize)
                        .startAfter(lastOrNull)
                        .get()
                        .await()
                }

            LoadResult.Page(
                data = currentPage.toObjects(ChampionBoard::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

        override fun getRefreshKey(state: PagingState<QuerySnapshot, ChampionBoard>): QuerySnapshot? = null
    }
}