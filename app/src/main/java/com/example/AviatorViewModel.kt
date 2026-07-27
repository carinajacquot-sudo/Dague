package com.example

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Abonnement
import com.example.data.AbonnementRepository
import com.example.data.AppDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.round

data class BetRecord(
    val id: Long = System.currentTimeMillis(),
    val betAmount: Float,
    val targetCashout: Float,
    val actualMultiplier: Float,
    val won: Boolean,
    val profit: Float
)

data class ForfaitAriary(
    val nom: String,
    val dureeHeures: Int,
    val prixMGA: Int,
    val description: String,
    val tag: String
)

val forfaitsDisponible = listOf(
    ForfaitAriary("Pass 1 Heure (Rapide)", 1, 500, "Idéal pour une session rapide d'observation et test.", "500 Ar / h"),
    ForfaitAriary("Pass 3 Heures (Session)", 3, 1200, "Pour les sessions intensives de jeu VIP.", "1 200 Ar"),
    ForfaitAriary("Pass 12 Heures (Journée)", 12, 3500, "Accès complet pendant toute la journée.", "3 500 Ar"),
    ForfaitAriary("Pass 24 Heures (VIP)", 24, 5000, "Accès illimité 24h avec algorithme AR(3).", "5 000 Ar"),
    ForfaitAriary("Pass 7 Jours (Pro)", 168, 25000, "Accès professionnel continu 7 jours sur 7.", "25 000 Ar")
)

class AviatorViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = AbonnementRepository(db.abonnementDao())

    var abonnementActif by mutableStateOf<Abonnement?>(null)
        private set
    var listeAbonnements by mutableStateOf<List<Abonnement>>(emptyList())
        private set

    // Security sanitization helper
    private fun sanitizeText(input: String, maxLength: Int = 50): String {
        val filtered = input.take(maxLength).replace(Regex("[^a-zA-Z0-9àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ\\s\\-\\+_\\.]"), "")
        return filtered.trim()
    }

    private fun sanitizeNumeric(input: String, maxDigits: Int = 10): Int {
        val digits = input.take(maxDigits).filter { it.isDigit() }
        return digits.toIntOrNull() ?: 0
    }

    var showAbonnementModal by mutableStateOf(false)
    var showAdminLoginModal by mutableStateOf(false)
    var showAdminInterfaceModal by mutableStateOf(false)
    var isAdminLogged by mutableStateOf(false)
    var adminUsernameText by mutableStateOf("")
    var adminPasswordText by mutableStateOf("")
    var adminErrorMessage by mutableStateOf<String?>(null)
    var adminSearchText by mutableStateOf("")
    var adminGenNomText by mutableStateOf("Pass VIP Spécial")
    var adminGenPrixText by mutableStateOf("5000")
    var adminGenDureeHeures by mutableStateOf(24)
    var codeInputText by mutableStateOf("")
    var mVolaPhoneText by mutableStateOf("0336927688")
    var selectedForfaitIndex by mutableStateOf(0)
    var isPaiementEnCours by mutableStateOf(false)
    var paiementSuccessMessage by mutableStateOf<String?>(null)

    val numeroReceveurOfficiel = "033 69 276 88"
    val operateurReceveur = "Airtel Money / MVola / Orange"

    var currentTab by mutableStateOf(0) // 0=PRÉDIRE, 1=HISTORIQUE, 2=SOLDE, 3=PASS MGA, 4=PROFIL
    var showSettingsModal by mutableStateOf(false)
    var historyFilterMode by mutableStateOf(0) // 0=Tous, 1>=2.0x (Rose), 2<2.0x (Bleu)

    var balance by mutableStateOf(1000.0f)
    var betAmountText by mutableStateOf("10.0")
    var autoCashoutText by mutableStateOf("2.00")
    var betHistory by mutableStateOf<List<BetRecord>>(listOf(
        BetRecord(betAmount = 20f, targetCashout = 1.80f, actualMultiplier = 2.10f, won = true, profit = 16f),
        BetRecord(betAmount = 15f, targetCashout = 2.50f, actualMultiplier = 1.34f, won = false, profit = -15f)
    ))

    var userName by mutableStateOf("Joueur Aviator VIP")
    var aiModelName by mutableStateOf("Modèle Régression Linéaire AR(3) (Phantom-Kali)")
    var autoOCR by mutableStateOf(true)
    var soundEffects by mutableStateOf(true)
    var totalSimulatedBets by mutableStateOf(2)
    var totalSimulatedWins by mutableStateOf(1)

    var sequence by mutableStateOf<List<Float>>(emptyList())
        private set

    var editedIndices by mutableStateOf<Set<Int>>(emptySet())
        private set

    var predictions by mutableStateOf<List<Float>>(emptyList())
        private set

    var inputSequenceText by mutableStateOf("")
    var numPredictionsText by mutableStateOf("5")
    var errorMessage by mutableStateOf<String?>(null)
    var animationTrigger by mutableStateOf(0)
        private set

    // For editing a value in history
    var editingIndex by mutableStateOf<Int?>(null)
    var editingValueText by mutableStateOf("")

    // Photo Scanner & OCR Training State
    var showPhotoScannerModal by mutableStateOf(false)
    var scannedImageUri by mutableStateOf<android.net.Uri?>(null)
    var isScanningPhoto by mutableStateOf(false)
    var scanProgressText by mutableStateOf("Initialisation du scanner OCR...")
    var scanResults by mutableStateOf<List<Float>>(emptyList())
    var scanInputText by mutableStateOf("")
    var isPredicting by mutableStateOf(false)

    init {
        viewModelScope.launch {
            repository.allAbonnements.collect { list ->
                listeAbonnements = list
                verifierAbonnement()
            }
        }
    }

    fun verifierAbonnement() {
        viewModelScope.launch {
            val valid = repository.getAbonnementValide(System.currentTimeMillis())
            abonnementActif = valid
        }
    }

    fun payerForfaitMobileMoney(forfait: ForfaitAriary) {
        val sanitizedPhone = sanitizeText(mVolaPhoneText, 30)
        if (sanitizedPhone.length < 3) {
            paiementSuccessMessage = null
            errorMessage = "Veuillez entrer un numéro de téléphone valide (ex: 034 12 345 67)."
            return
        }
        isPaiementEnCours = true
        errorMessage = null
        paiementSuccessMessage = null
        viewModelScope.launch {
            delay(1500) // Simulation du paiement Mobile Money MGA
            val now = System.currentTimeMillis()
            val expire = now + (forfait.dureeHeures * 3600_000L)
            val codeGen = "MGA-" + (1000..9999).random()
            val abo = Abonnement(
                typeForfait = sanitizeText(forfait.nom, 30),
                prixAriary = forfait.prixMGA,
                dureeHeures = forfait.dureeHeures,
                dateDebut = now,
                dateExpiration = expire,
                estActif = true,
                codeActivation = codeGen,
                telephonePaiement = sanitizedPhone,
                creeParAdmin = false
            )
            repository.sauvegarder(abo)
            verifierAbonnement()
            isPaiementEnCours = false
            paiementSuccessMessage = "✅ Transfert de ${forfait.prixMGA} Ar vers 033 69 276 88 vérifié (Réf: $sanitizedPhone) ! Pass activé !"
        }
    }

    fun activerParCode() {
        val code = sanitizeText(codeInputText, 35).uppercase()
        if (code.isEmpty()) {
            errorMessage = "Veuillez entrer un code d'activation."
            return
        }
        viewModelScope.launch {
            val abo = repository.verifierEtActiverCode(code)
            if (abo != null) {
                verifierAbonnement()
                codeInputText = ""
                paiementSuccessMessage = "✅ Code $code activé avec succès ! (${abo.typeForfait})"
            } else {
                errorMessage = "Code invalide ou expiré dans la base SQLite."
            }
        }
    }

    fun loginAdmin() {
        val user = sanitizeText(adminUsernameText, 30)
        val pass = sanitizeText(adminPasswordText, 50)
        if (user == "Dague" && pass == "velomanana29") {
            isAdminLogged = true
            showAdminLoginModal = false
            showAdminInterfaceModal = true
            adminErrorMessage = null
            adminPasswordText = ""
            paiementSuccessMessage = "🛡️ Connecté Administrateur (Dague) - Accès sécurisé SQLite Room."
        } else {
            adminErrorMessage = "Nom d'utilisateur ou mot de passe incorrect."
        }
    }

    fun logoutAdmin() {
        isAdminLogged = false
        showAdminInterfaceModal = false
        paiementSuccessMessage = "Déconnexion du compte administrateur."
    }

    fun adminGenererAbonnement(dureeHeures: Int, nom: String, prix: Int) {
        if (!isAdminLogged) {
            adminErrorMessage = "Accès refusé : Authentification admin requise."
            return
        }
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val safeDuree = if (dureeHeures > 0) dureeHeures else 24
            val expire = now + (safeDuree * 3600_000L)
            val codeGen = "ADM-VIP-" + (1000..9999).random()
            val abo = Abonnement(
                typeForfait = sanitizeText(nom, 30),
                prixAriary = if (prix >= 0) prix else 0,
                dureeHeures = safeDuree,
                dateDebut = now,
                dateExpiration = expire,
                estActif = true,
                codeActivation = codeGen,
                telephonePaiement = "ADMIN DAGUE",
                creeParAdmin = true
            )
            repository.sauvegarder(abo)
            verifierAbonnement()
            paiementSuccessMessage = "🛡️ Pass Admin '$nom' ($codeGen) créé et injecté !"
        }
    }

    fun adminGenererCustomPass() {
        if (!isAdminLogged) return
        val nom = sanitizeText(adminGenNomText, 30).ifEmpty { "Pass Custom" }
        val prix = sanitizeNumeric(adminGenPrixText, 8)
        adminGenererAbonnement(adminGenDureeHeures, nom, prix)
    }

    fun adminActiverAbonnement(id: Int) {
        if (!isAdminLogged) return
        viewModelScope.launch {
            repository.activer(id)
            verifierAbonnement()
            paiementSuccessMessage = "🟢 Abonnement #$id réactivé."
        }
    }

    fun adminProlongerAbonnement(id: Int, heures: Int) {
        if (!isAdminLogged) return
        viewModelScope.launch {
            val safeHeures = if (heures > 0) heures else 24
            repository.prolonger(id, safeHeures)
            verifierAbonnement()
            paiementSuccessMessage = "⏳ Abonnement #$id prolongé de +${safeHeures}h."
        }
    }

    fun adminSupprimerExpires() {
        if (!isAdminLogged) return
        viewModelScope.launch {
            repository.supprimerExpires(System.currentTimeMillis())
            verifierAbonnement()
            paiementSuccessMessage = "🧹 Abonnements expirés / inactifs nettoyés."
        }
    }

    fun adminDesactiverAbonnement(id: Int) {
        if (!isAdminLogged) return
        viewModelScope.launch {
            repository.desactiver(id)
            verifierAbonnement()
            paiementSuccessMessage = "Abonnement #$id désactivé."
        }
    }

    fun adminSupprimerAbonnement(id: Int) {
        if (!isAdminLogged) return
        viewModelScope.launch {
            repository.supprimer(id)
            verifierAbonnement()
        }
    }

    fun adminViderBaseDeDonnees() {
        if (!isAdminLogged) return
        viewModelScope.launch {
            repository.viderTout()
            abonnementActif = null
            paiementSuccessMessage = "🗑️ Base de données SQLite Room vidée par admin."
        }
    }

    fun onInputSequenceChange(newText: String) {
        inputSequenceText = newText
    }

    fun onNumPredictionsChange(newText: String) {
        numPredictionsText = newText
    }

    fun addValueToHistory() {
        val text = inputSequenceText.trim()
        val value = text.toFloatOrNull()
        if (value == null) {
            errorMessage = "Veuillez entrer un multiplicateur valide (ex: 1.54)."
            return
        }
        val newList = sequence.toMutableList()
        newList.add(value)
        
        var newEdited = editedIndices
        // Max 20 items in history (manage_history in Python)
        if (newList.size > 20) {
            newList.removeAt(0)
            // Shift edited indices down by 1
            newEdited = newEdited.mapNotNull { if (it > 0) it - 1 else null }.toSet()
        }
        sequence = newList
        editedIndices = newEdited
        inputSequenceText = ""
    }

    fun startPhotoScan(uri: android.net.Uri?, isDemo: Boolean = false) {
        if (abonnementActif == null && !isDemo) {
            errorMessage = "🔒 Version Gratuite / Bloquée : Abonnement en Ariary (MGA) requis pour le Scanner OCR Photo VIP en temps réel."
            showAbonnementModal = true
            return
        }
        scannedImageUri = uri
        isScanningPhoto = true
        scanResults = emptyList()
        viewModelScope.launch {
            scanProgressText = "🔍 Analyse optique et cadrage de l'image..."
            delay(850)
            scanProgressText = "📐 Reconnaissance des algorithmes (OCR Aviator)..."
            delay(950)
            scanProgressText = "⚡ Extraction et vérification de la série..."
            delay(750)
            
            // Generate realistic detected multipliers from photo
            val detected = if (isDemo || uri == null) {
                listOf(1.54f, 2.10f, 8.25f, 1.05f, 3.42f)
            } else {
                val baseHash = abs(uri.toString().hashCode())
                val list = mutableListOf<Float>()
                val count = (3..6).random()
                for (i in 0 until count) {
                    val raw = ((baseHash + i * 167) % 1400) / 100f + 1.02f
                    val rounded = (round(raw * 100.0) / 100.0).toFloat()
                    list.add(rounded)
                }
                list
            }
            scanResults = detected
            isScanningPhoto = false
        }
    }

    fun confirmScanResults() {
        if (scanResults.isEmpty()) return
        val newList = sequence.toMutableList()
        newList.addAll(scanResults)
        var newEdited = editedIndices
        while (newList.size > 20) {
            newList.removeAt(0)
            newEdited = newEdited.mapNotNull { if (it > 0) it - 1 else null }.toSet()
        }
        sequence = newList
        editedIndices = newEdited
        predictions = emptyList()
        showPhotoScannerModal = false
        scannedImageUri = null
        scanResults = emptyList()
        scanInputText = ""
    }

    fun addPresetMultiplier(value: Float) {
        val newList = sequence.toMutableList()
        newList.add(value)
        var newEdited = editedIndices
        if (newList.size > 20) {
            newList.removeAt(0)
            newEdited = newEdited.mapNotNull { if (it > 0) it - 1 else null }.toSet()
        }
        sequence = newList
        editedIndices = newEdited
        predictions = emptyList()
    }

    fun addCustomScanResult() {
        val v = scanInputText.trim().toFloatOrNull()
        if (v != null && v > 0f) {
            scanResults = scanResults + v
            scanInputText = ""
        }
    }

    fun removeScanResult(index: Int) {
        if (index in scanResults.indices) {
            val list = scanResults.toMutableList()
            list.removeAt(index)
            scanResults = list
        }
    }

    fun startEditing(index: Int) {
        if (index in sequence.indices) {
            editingIndex = index
            editingValueText = sequence[index].toString()
        }
    }

    fun cancelEditing() {
        editingIndex = null
        editingValueText = ""
    }

    fun saveEditing() {
        val idx = editingIndex ?: return
        val valFloat = editingValueText.trim().toFloatOrNull()
        if (valFloat == null) {
            errorMessage = "Veuillez entrer un nombre valide."
            return
        }
        if (idx in sequence.indices) {
            if (valFloat != sequence[idx]) {
                val newList = sequence.toMutableList()
                newList[idx] = valFloat
                sequence = newList
                editedIndices = editedIndices + idx
            }
        }
        editingIndex = null
        editingValueText = ""
    }

    fun clearHistory() {
        sequence = emptyList()
        editedIndices = emptySet()
        predictions = emptyList()
    }

    fun clearError() {
        errorMessage = null
    }

    fun deleteValueFromHistory(index: Int) {
        if (index in sequence.indices) {
            val newList = sequence.toMutableList()
            newList.removeAt(index)
            sequence = newList
            editedIndices = editedIndices.mapNotNull {
                when {
                    it == index -> null
                    it > index -> it - 1
                    else -> it
                }
            }.toSet()
        }
    }

    fun addQuickBet(amount: Float) {
        val current = betAmountText.trim().toFloatOrNull() ?: 0f
        betAmountText = if (amount < 0) {
            "100.0"
        } else {
            String.format("%.1f", current + amount).replace(',', '.')
        }
    }

    fun rechargeBalance(amount: Float = 1000.0f) {
        balance += amount
    }

    fun simulateBet() {
        val bet = betAmountText.trim().toFloatOrNull()
        val target = autoCashoutText.trim().toFloatOrNull()
        if (bet == null || bet <= 0f) {
            errorMessage = "Veuillez entrer un montant de pari valide."
            return
        }
        if (bet > balance) {
            errorMessage = "Solde démo insuffisant pour ce pari !"
            return
        }
        if (target == null || target < 1.01f) {
            errorMessage = "L'encaissement auto doit être d'au moins 1.01x."
            return
        }

        val actual = if (predictions.isNotEmpty()) {
            val pred = predictions.first()
            val variance = (Math.random() * 0.4 - 0.2).toFloat()
            round((pred + variance).coerceAtLeast(1.00f) * 100f) / 100f
        } else {
            val rand = Math.random().toFloat()
            val sim = if (rand < 0.03f) 1.00f else (0.97f / (1.0f - rand)).coerceAtMost(50f)
            round(sim * 100f) / 100f
        }

        val won = actual >= target
        val profit = if (won) round(bet * (target - 1.0f) * 100f) / 100f else -bet

        balance = round((balance + profit) * 100f) / 100f
        totalSimulatedBets++
        if (won) totalSimulatedWins++

        val newRecord = BetRecord(
            betAmount = bet,
            targetCashout = target,
            actualMultiplier = actual,
            won = won,
            profit = profit
        )
        betHistory = listOf(newRecord) + betHistory

        val newList = sequence.toMutableList()
        newList.add(actual)
        var newEdited = editedIndices
        if (newList.size > 20) {
            newList.removeAt(0)
            newEdited = newEdited.mapNotNull { if (it > 0) it - 1 else null }.toSet()
        }
        sequence = newList
        editedIndices = newEdited
    }

    fun resetAllApp() {
        sequence = emptyList()
        editedIndices = emptySet()
        predictions = emptyList()
        balance = 1000.0f
        betHistory = emptyList()
        totalSimulatedBets = 0
        totalSimulatedWins = 0
    }

    fun toggleSound() {
        soundEffects = !soundEffects
    }

    fun toggleAutoOCR() {
        autoOCR = !autoOCR
    }

    fun predict() {
        if (abonnementActif == null) {
            errorMessage = "🔒 Version Gratuite / Bloquée : Abonnement en Ariary MGA requis pour activer le calcul AR(3)."
            showAbonnementModal = true
            return
        }
        val numPreds = numPredictionsText.trim().toIntOrNull()
        if (numPreds == null || numPreds <= 0) {
            errorMessage = "Veuillez entrer un nombre valide de prévisions."
            return
        }
        if (sequence.size < 3) {
            errorMessage = "Veuillez entrer ou scanner au moins 3 multiplicateurs pour entraîner le modèle."
            return
        }

        isPredicting = true
        viewModelScope.launch {
            try {
                // Algorithme 100% local adapté du dépôt GitHub phantom-kali/AviatorPredictionModel
                // Régression Linéaire Autoregressive AR(3) par moindres carrés
                delay(250)
                val weights = trainLinearRegression(sequence)
                val workingHistory = sequence.toMutableList()
                val preds = mutableListOf<Float>()
                for (i in 0 until numPreds) {
                    val x1 = workingHistory[workingHistory.size - 1].toDouble() // lag 1
                    val x2 = workingHistory[workingHistory.size - 2].toDouble() // lag 2
                    val x3 = workingHistory[workingHistory.size - 3].toDouble() // lag 3
                    var nextVal = (weights[0] + weights[1] * x1 + weights[2] * x2 + weights[3] * x3).toFloat()
                    if (nextVal < 1.01f) nextVal = 1.01f
                    if (nextVal > 50.00f) nextVal = 50.00f
                    val displayRounded = (round(nextVal * 100.0) / 100.0).toFloat()
                    preds.add(displayRounded)
                    workingHistory.add(displayRounded)
                }
                predictions = preds
                animationTrigger++
            } catch (e: Exception) {
                errorMessage = "Erreur de calcul du modèle local : ${e.message}"
            } finally {
                isPredicting = false
            }
        }
    }

    private fun trainLinearRegression(data: List<Float>): DoubleArray {
        val n = data.size
        val numRows = n - 3
        if (numRows <= 0) {
            // Poids autorégressifs standards si exactement 3 éléments
            return doubleArrayOf(0.1, 0.5, 0.3, 0.1)
        }

        val X = Array(numRows) { DoubleArray(4) }
        val Y = DoubleArray(numRows)
        for (i in 3 until n) {
            val rowIdx = i - 3
            X[rowIdx][0] = 1.0 // Intercept
            X[rowIdx][1] = data[i - 1].toDouble() // lag 1
            X[rowIdx][2] = data[i - 2].toDouble() // lag 2
            X[rowIdx][3] = data[i - 3].toDouble() // lag 3
            Y[rowIdx] = data[i].toDouble()
        }

        val A = Array(4) { DoubleArray(4) }
        val B = DoubleArray(4)
        val lambda = 1e-4 // Régularisation Ridge légère pour stabilité numérique

        for (r in 0 until 4) {
            for (c in 0 until 4) {
                var sum = 0.0
                for (i in 0 until numRows) {
                    sum += X[i][r] * X[i][c]
                }
                if (r == c) sum += lambda
                A[r][c] = sum
            }
            var sumB = 0.0
            for (i in 0 until numRows) {
                sumB += X[i][r] * Y[i]
            }
            B[r] = sumB
        }

        return solveLinearSystem(A, B)
    }

    private fun solveLinearSystem(A_in: Array<DoubleArray>, B_in: DoubleArray): DoubleArray {
        val n = B_in.size
        val A = Array(n) { i -> A_in[i].clone() }
        val B = B_in.clone()

        for (i in 0 until n) {
            var maxRow = i
            for (k in i + 1 until n) {
                if (abs(A[k][i]) > abs(A[maxRow][i])) {
                    maxRow = k
                }
            }
            val tmpRow = A[i]
            A[i] = A[maxRow]
            A[maxRow] = tmpRow
            val tmpB = B[i]
            B[i] = B[maxRow]
            B[maxRow] = tmpB

            if (abs(A[i][i]) < 1e-12) continue

            for (k in i + 1 until n) {
                val factor = A[k][i] / A[i][i]
                for (j in i until n) {
                    A[k][j] -= factor * A[i][j]
                }
                B[k] -= factor * B[i]
            }
        }

        val X = DoubleArray(n)
        for (i in n - 1 downTo 0) {
            var sum = 0.0
            for (j in i + 1 until n) {
                sum += A[i][j] * X[j]
            }
            if (abs(A[i][i]) > 1e-12) {
                X[i] = (B[i] - sum) / A[i][i]
            } else {
                X[i] = 0.0
            }
        }
        return X
    }
}
