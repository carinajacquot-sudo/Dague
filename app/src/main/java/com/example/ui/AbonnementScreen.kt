package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.AviatorViewModel
import com.example.ForfaitAriary
import com.example.forfaitsDisponible
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBlue
import com.example.ui.theme.ImmersiveBlueBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveCard
import com.example.ui.theme.ImmersiveGreen
import com.example.ui.theme.ImmersiveGreenBg
import com.example.ui.theme.ImmersiveRed
import com.example.ui.theme.ImmersiveRedBg
import com.example.ui.theme.ImmersiveRedLight
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveTextDark
import com.example.ui.theme.ImmersiveTextMuted
import com.example.ui.theme.ImmersiveTextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AbonnementTabScreen(viewModel: AviatorViewModel) {
    val scrollState = rememberScrollState()
    val now = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd/MM/yyyy 'à' HH:mm", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // En-tête du Tab
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            border = BorderStroke(1.dp, ImmersiveBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ImmersiveRed.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = ImmersiveRedLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "PASS VIP & ABONNEMENT (MGA)",
                                color = ImmersiveTextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Persistance locale sécurisée via SQLite Room",
                                color = ImmersiveTextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Divider(color = ImmersiveBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))

                // Statut de l'abonnement actuel
                val abo = viewModel.abonnementActif
                if (abo != null) {
                    val restantMillis = abo.dateExpiration - now
                    val heuresRestantes = (restantMillis / 3600_000L).coerceAtLeast(0)
                    val minutesRestantes = ((restantMillis % 3600_000L) / 60_000L).coerceAtLeast(0)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ImmersiveGreenBg)
                            .border(BorderStroke(1.dp, ImmersiveGreen), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = ImmersiveGreen, modifier = Modifier.size(18.dp))
                                Text(text = "ABONNEMENT ACTIF", color = ImmersiveGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Text(text = "Forfait : ${abo.typeForfait} (${abo.prixAriary} Ar)", color = ImmersiveTextWhite, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                            Text(text = "Expire le ${sdf.format(Date(abo.dateExpiration))}", color = ImmersiveTextDark, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "${heuresRestantes}h ${minutesRestantes}m", color = ImmersiveGreen, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Text(text = "restantes", color = ImmersiveTextMuted, fontSize = 10.sp)
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ImmersiveRedBg)
                            .border(BorderStroke(1.dp, ImmersiveRedLight), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = ImmersiveRedLight, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "VERSION GRATUITE / BLOQUÉE", color = ImmersiveRedLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "Aucun abonnement valide dans la base SQLite. Souscrivez à un forfait par heure pour débloquer le modèle AR(3).", color = ImmersiveTextDark, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Section 1 : Grille des forfaits disponibles en Ariary (MGA)
        Text(
            text = "🔥 1. CHOISIR UN FORFAIT HORAIRE (ARIARY MGA)",
            color = ImmersiveTextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            forfaitsDisponible.forEachIndexed { index, forfait ->
                val isSelected = viewModel.selectedForfaitIndex == index
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectedForfaitIndex = index },
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) ImmersiveRedBg else ImmersiveCard),
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) ImmersiveRedLight else ImmersiveBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(text = forfait.nom, color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) ImmersiveRed else ImmersiveBorder)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = forfait.tag, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(text = forfait.description, color = ImmersiveTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                        }
                        Text(
                            text = "${forfait.prixMGA} Ar",
                            color = if (isSelected) ImmersiveRedLight else ImmersiveGreen,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // Section 2 : Paiement Mobile Money (MVola, Orange Money, Airtel Money)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            border = BorderStroke(1.dp, ImmersiveBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = null, tint = ImmersiveBlue, modifier = Modifier.size(20.dp))
                    Text(text = "2. PAIEMENT MOBILE MONEY (MGA)", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveGreenBg),
                    border = BorderStroke(1.dp, ImmersiveGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📲 COMPTE RÉCEPTEUR OFFICIEL MOBILE MONEY :",
                            color = ImmersiveGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = viewModel.numeroReceveurOfficiel,
                            color = ImmersiveTextWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Opérateurs compatibles : ${viewModel.operateurReceveur}",
                            color = ImmersiveTextDark,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Text(
                    text = "Veuillez d'abord envoyer le montant au numéro officiel ci-dessus, puis validez ci-dessous :",
                    color = ImmersiveTextMuted,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    value = viewModel.mVolaPhoneText,
                    onValueChange = { viewModel.mVolaPhoneText = it },
                    label = { Text("Numéro Mobile Money expéditeur ou référence") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ImmersiveTextWhite,
                        unfocusedTextColor = ImmersiveTextWhite,
                        focusedBorderColor = ImmersiveBlue,
                        unfocusedBorderColor = ImmersiveBorder
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                val forfaitSelect = forfaitsDisponible.getOrElse(viewModel.selectedForfaitIndex) { forfaitsDisponible[0] }
                Button(
                    onClick = { viewModel.payerForfaitMobileMoney(forfaitSelect) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ImmersiveGreen, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !viewModel.isPaiementEnCours
                ) {
                    if (viewModel.isPaiementEnCours) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Vérification Mobile Money en cours...", fontWeight = FontWeight.Bold)
                    } else {
                        Text("💸 Valider le transfert ${forfaitSelect.prixMGA} Ar vers 033 69 276 88", fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                }
            }
        }

        // Section 3 : Activer par Code
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            border = BorderStroke(1.dp, ImmersiveBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = ImmersiveRedLight, modifier = Modifier.size(20.dp))
                    Text(text = "3. ACTIVER UN CODE D'ABONNEMENT", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Text(text = "Vous avez un code promo ou généré par l'Administrateur ? Activez-le ici :", color = ImmersiveTextMuted, fontSize = 12.sp)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = viewModel.codeInputText,
                        onValueChange = { viewModel.codeInputText = it },
                        label = { Text("Code (ex: MGA-4812)") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ImmersiveTextWhite,
                            unfocusedTextColor = ImmersiveTextWhite,
                            focusedBorderColor = ImmersiveRedLight,
                            unfocusedBorderColor = ImmersiveBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Button(
                        onClick = { viewModel.activerParCode() },
                        modifier = Modifier.height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ImmersiveRed, contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Activer", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section 4 : Sécurité et Accès Admin
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (viewModel.isAdminLogged) ImmersiveBlueBg else ImmersiveCard),
            border = BorderStroke(1.dp, if (viewModel.isAdminLogged) ImmersiveBlue else ImmersiveBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = if (viewModel.isAdminLogged) ImmersiveBlue else ImmersiveTextDark, modifier = Modifier.size(22.dp))
                        Column {
                            Text(
                                text = if (viewModel.isAdminLogged) "🛡️ SESSION ADMIN ACTIVE (DAGUE)" else "4. GESTION BASE ROOM (ADMINISTRATEUR)",
                                color = if (viewModel.isAdminLogged) ImmersiveBlue else ImmersiveTextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(text = "OR/M Room SQLite & Contrôle d'accès", color = ImmersiveTextMuted, fontSize = 11.sp)
                        }
                    }
                    if (!viewModel.isAdminLogged) {
                        OutlinedButton(
                            onClick = { viewModel.showAdminLoginModal = true },
                            border = BorderStroke(1.dp, ImmersiveBlue),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Connexion Admin", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.logoutAdmin() },
                            colors = ButtonDefaults.buttonColors(containerColor = ImmersiveRed, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Déconnexion", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                if (viewModel.isAdminLogged) {
                    Button(
                        onClick = { viewModel.showAdminInterfaceModal = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlue, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("🚀 OUVRIR LE TABLEAU DE BORD ADMIN", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }

                    AdminPanelContent(viewModel = viewModel)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AdminPanelContent(viewModel: AviatorViewModel) {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val now = System.currentTimeMillis()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Divider(color = ImmersiveBlue.copy(alpha = 0.5f))
        
        Text(
            text = "⚡ ACTIONS RAPIDES ADMINISTRATEUR (INJECTION SQLITE)",
            color = ImmersiveTextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { viewModel.adminGenererAbonnement(1, "Pass 1h Admin Rapide", 0) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveGreen, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("+ 1 Heure", fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            Button(
                onClick = { viewModel.adminGenererAbonnement(24, "Pass 24h Admin VIP", 0) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlue, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("+ 24 Heures", fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            Button(
                onClick = { viewModel.adminGenererAbonnement(168, "Pass 7J Admin Pro", 0) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveRed, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("+ 7 Jours", fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        Text(
            text = "📋 ENREGISTREMENTS TABLE ROOM 'abonnements' (${viewModel.listeAbonnements.size})",
            color = ImmersiveTextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (viewModel.listeAbonnements.isEmpty()) {
            Text(
                text = "Aucun enregistrement dans la base SQLite.",
                color = ImmersiveTextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                viewModel.listeAbonnements.forEach { abo ->
                    val isValide = abo.estActif && abo.dateExpiration > now
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                        border = BorderStroke(1.dp, if (isValide) ImmersiveGreen else ImmersiveBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "#${abo.id} - ${abo.typeForfait}", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(
                                    text = if (isValide) "🟢 ACTIF" else "🔴 EXPIRÉ/DÉSACTIVÉ",
                                    color = if (isValide) ImmersiveGreen else ImmersiveRedLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            Text(text = "Code: ${abo.codeActivation} | Tel: ${abo.telephonePaiement} | Prix: ${abo.prixAriary} Ar", color = ImmersiveTextDark, fontSize = 11.sp)
                            Text(text = "Expire: ${sdf.format(Date(abo.dateExpiration))}", color = ImmersiveTextMuted, fontSize = 11.sp)

                            if (abo.estActif) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    TextButton(onClick = { viewModel.adminDesactiverAbonnement(abo.id) }) {
                                        Text("Désactiver", color = ImmersiveRedLight, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = { viewModel.adminViderBaseDeDonnees() },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, ImmersiveRedLight),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveRedLight),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Vider toute la table 'abonnements' (SQLite Room)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AbonnementModalDialog(viewModel: AviatorViewModel) {
    val now = System.currentTimeMillis()
    AlertDialog(
        onDismissRequest = { viewModel.showAbonnementModal = false },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = ImmersiveRedLight)
                Text(text = "Abonnement Ariary MGA Requis", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "L'accès au moteur de régression linéaire AR(3) en temps réel nécessite un abonnement valide dans la base de données Room SQLite.",
                    color = ImmersiveTextDark,
                    fontSize = 13.sp
                )
                Text(
                    text = "Souscrivez à un forfait par heure à petit prix (en Ariary Malagasy) via le menu 'PASS MGA' ou payez par MVola/Mobile Money.",
                    color = ImmersiveTextWhite,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                    border = BorderStroke(1.dp, ImmersiveGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "💡 Forfaits Horaires Disponibles :", color = ImmersiveGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = "• 1 Heure : 500 Ar\n• 3 Heures : 1 200 Ar\n• 12 Heures : 3 500 Ar\n• 24 Heures : 5 000 Ar\n• 7 Jours Pro : 25 000 Ar", color = ImmersiveTextWhite, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.showAbonnementModal = false
                    viewModel.currentTab = 3 // Basculer vers le tab PASS MGA
                },
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveRed, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("S'abonner en Ariary (MGA)", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.showAbonnementModal = false }) {
                Text("Fermer", color = ImmersiveTextMuted)
            }
        },
        containerColor = ImmersiveCard,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun AdminLoginModalDialog(viewModel: AviatorViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.showAdminLoginModal = false },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = ImmersiveBlue)
                Text("Connexion Administrateur", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Entrez vos identifiants administrateur pour déverrouiller l'accès complet à la base de données Room SQLite et gérer les abonnements.",
                    color = ImmersiveTextDark,
                    fontSize = 12.sp
                )

                if (viewModel.adminErrorMessage != null) {
                    Text(text = viewModel.adminErrorMessage!!, color = ImmersiveRedLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = viewModel.adminUsernameText,
                    onValueChange = { viewModel.adminUsernameText = it },
                    label = { Text("Nom d'utilisateur (ex: Dague)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ImmersiveTextWhite,
                        unfocusedTextColor = ImmersiveTextWhite,
                        focusedBorderColor = ImmersiveBlue,
                        unfocusedBorderColor = ImmersiveBorder
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = viewModel.adminPasswordText,
                    onValueChange = { viewModel.adminPasswordText = it },
                    label = { Text("Mot de passe") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ImmersiveTextWhite,
                        unfocusedTextColor = ImmersiveTextWhite,
                        focusedBorderColor = ImmersiveBlue,
                        unfocusedBorderColor = ImmersiveBorder
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.loginAdmin() },
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlue, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Se Connecter", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.showAdminLoginModal = false }) {
                Text("Annuler", color = ImmersiveTextMuted)
            }
        },
        containerColor = ImmersiveCard,
        shape = RoundedCornerShape(16.dp)
    )
}
