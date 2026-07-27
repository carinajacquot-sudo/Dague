package com.example.data

import kotlinx.coroutines.flow.Flow

class AbonnementRepository(private val dao: AbonnementDao) {
    val allAbonnements: Flow<List<Abonnement>> = dao.getAllAbonnements()

    fun getAbonnementValideFlow(tempsActuel: Long): Flow<Abonnement?> = dao.getAbonnementValideFlow(tempsActuel)

    suspend fun getAbonnementValide(tempsActuel: Long): Abonnement? = dao.getAbonnementValide(tempsActuel)

    suspend fun verifierEtActiverCode(code: String): Abonnement? {
        val abo = dao.getByCode(code.trim().uppercase())
        if (abo != null && abo.estActif && abo.dateExpiration > System.currentTimeMillis()) {
            return abo
        }
        return null
    }

    suspend fun sauvegarder(abonnement: Abonnement): Long = dao.sauvegarderAbonnement(abonnement)

    suspend fun desactiver(id: Int) = dao.desactiverAbonnement(id)

    suspend fun activer(id: Int) = dao.activerAbonnement(id)

    suspend fun prolonger(id: Int, heures: Int) = dao.prolongerAbonnement(id, heures)

    suspend fun supprimer(id: Int) = dao.supprimerAbonnement(id)

    suspend fun supprimerExpires(tempsActuel: Long) = dao.supprimerExpires(tempsActuel)

    suspend fun viderTout() = dao.viderTout()
}
