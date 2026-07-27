package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abonnements")
data class Abonnement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val typeForfait: String,        // ex: "Pass 1h (Rapide)", "Pass 3h (Session)", "Pass 12h (Journée)", "Pass 24h (VIP)", "Pass 7J (Pro)"
    val prixAriary: Int,            // ex: 500, 1200, 3500, 5000, 25000
    val dureeHeures: Int,           // ex: 1, 3, 12, 24, 168
    val dateDebut: Long,            // Timestamp en millisecondes
    val dateExpiration: Long,       // Timestamp en millisecondes
    val estActif: Boolean = true,   // 1 = actif, 0 = désactivé/expiré
    val codeActivation: String = "", // Code unique d'activation (ex: "VIP-MGA-9821")
    val telephonePaiement: String = "", // ex: "034 12 345 67 (MVola)"
    val creeParAdmin: Boolean = false
)
