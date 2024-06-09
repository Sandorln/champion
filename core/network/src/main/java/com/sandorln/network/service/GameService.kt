package com.sandorln.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.sandorln.network.model.FireStoreGame
import com.sandorln.network.util.getGameDocument
import com.sandorln.network.util.getUserId
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameService @Inject constructor(
    private val fireDB: FirebaseFirestore
) {
    suspend fun getCurrentGameScore(fireStoreGame: FireStoreGame): Long {
        val id = FirebaseInstallations.getInstance().getUserId()

        return runCatching {
            fireDB
                .getGameDocument(fireStoreGame)
                .document(id)
                .get()
                .await()
                .data
                ?.get("score") as Long
        }.getOrNull() ?: 0L
    }

    suspend fun updateGameScore(fireStoreGame: FireStoreGame, score: Long) {
        val id = FirebaseInstallations.getInstance().getUserId()
        val data = mapOf("score" to score)

        fireDB
            .getGameDocument(fireStoreGame)
            .document(id)
            .set(data)
            .await()
    }
}