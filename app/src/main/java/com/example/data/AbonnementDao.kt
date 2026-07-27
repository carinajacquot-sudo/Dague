package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AbonnementDao {
    @Query("SELECT * FROM abonnements ORDER BY dateDebut DESC")
    fun getAllAbonnements(): Flow<List<Abonnement>>

    @Query("SELECT * FROM abonnements WHERE dateExpiration > :tempsActuel AND estActif = 1 ORDER BY dateExpiration DESC LIMIT 1")
    fun getAbonnementValideFlow(tempsActuel: Long): Flow<Abonnement?>

    @Query("SELECT * FROM abonnements WHERE dateExpiration > :tempsActuel AND estActif = 1 ORDER BY dateExpiration DESC LIMIT 1")
    suspend fun getAbonnementValide(tempsActuel: Long): Abonnement?

    @Query("SELECT * FROM abonnements WHERE codeActivation = :code AND estActif = 1 LIMIT 1")
    suspend fun getByCode(code: String): Abonnement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun sauvegarderAbonnement(abonnement: Abonnement): Long

    @Query("UPDATE abonnements SET estActif = 0 WHERE id = :id")
    suspend fun desactiverAbonnement(id: Int)

    @Query("UPDATE abonnements SET estActif = 1 WHERE id = :id")
    suspend fun activerAbonnement(id: Int)

    @Query("UPDATE abonnements SET dateExpiration = dateExpiration + (:heures * 3600000), estActif = 1 WHERE id = :id")
    suspend fun prolongerAbonnement(id: Int, heures: Int)

    @Query("DELETE FROM abonnements WHERE id = :id")
    suspend fun supprimerAbonnement(id: Int)

    @Query("DELETE FROM abonnements WHERE dateExpiration <= :tempsActuel OR estActif = 0")
    suspend fun supprimerExpires(tempsActuel: Long)

    @Query("DELETE FROM abonnements")
    suspend fun viderTout()
}
