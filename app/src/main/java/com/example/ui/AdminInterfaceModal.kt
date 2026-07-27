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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.AviatorViewModel
import com.example.data.Abonnement
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
fun AdminInterfaceModalDialog(viewModel: AviatorViewModel) {
    val scrollState = rememberScrollState()
    val now = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    val abonnementsFiltres = viewModel.listeAbonnements.filter {
        if (viewModel.adminSearchText.isBlank()) true
        else {
            val query = viewModel.adminSearchText.trim().lowercase()
            it.codeActivation.lowercase().contains(query) ||
                    it.telephonePaiement.lowercase().contains(query) ||
                    it.typeForfait.lowercase().contains(query)
        }
    }

    val totalRevenu = viewModel.listeAbonnements.sumOf { it.prixAriary }
    val totalActifs = viewModel.listeAbonnements.count { it.estActif && it.dateExpiration > now }

    AlertDialog(
        onDismissRequest = { viewModel.showAdminInterfaceModal = false },
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .heightIn(max = 700.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ImmersiveBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = ImmersiveBlue, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(text = "TABLEAU DE BORD ADMIN ROOM", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Superviseur : Dague | SQLite ORM", color = ImmersiveBlue, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                IconButton(onClick = { viewModel.showAdminInterfaceModal = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer", tint = ImmersiveTextMuted)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. STATISTIQUES GLOBAL & NUMÉRO REÇU
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                    border = BorderStroke(1.dp, ImmersiveBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = null, tint = ImmersiveGreen, modifier = Modifier.size(18.dp))
                            Text(text = "COMPTE DE RÉCEPTION MOBILE MONEY :", color = ImmersiveTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = viewModel.numeroReceveurOfficiel,
                            color = ImmersiveGreen,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        )

                        Divider(color = ImmersiveBorder.copy(alpha = 0.5f))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "TOTAL REVENU BDD", color = ImmersiveTextMuted, fontSize = 10.sp)
                                Text(text = "$totalRevenu Ar", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "ACTIFS / TOTAL", color = ImmersiveTextMuted, fontSize = 10.sp)
                                Text(text = "$totalActifs / ${viewModel.listeAbonnements.size}", color = ImmersiveBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }

                // 2. GÉNÉRATEUR AVANCÉ SUR MESURE
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "⚡ CRÉATION DE PASS PERSONNALISÉ (ROOM DB)", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                        OutlinedTextField(
                            value = viewModel.adminGenNomText,
                            onValueChange = { viewModel.adminGenNomText = it },
                            label = { Text("Nom du Forfait (ex: Pass VIP Spécial)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = ImmersiveTextWhite,
                                unfocusedTextColor = ImmersiveTextWhite,
                                focusedBorderColor = ImmersiveBlue,
                                unfocusedBorderColor = ImmersiveBorder
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = viewModel.adminGenPrixText,
                                onValueChange = { viewModel.adminGenPrixText = it },
                                label = { Text("Prix MGA") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = ImmersiveTextWhite,
                                    unfocusedTextColor = ImmersiveTextWhite,
                                    focusedBorderColor = ImmersiveBlue,
                                    unfocusedBorderColor = ImmersiveBorder
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Durée: ${viewModel.adminGenDureeHeures}h", color = ImmersiveTextMuted, fontSize = 11.sp)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    val presets = listOf(1 to "1h", 24 to "24h", 168 to "7J", 720 to "30J")
                                    presets.forEach { (h, lbl) ->
                                        val sel = viewModel.adminGenDureeHeures == h
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (sel) ImmersiveBlue else ImmersiveSurface)
                                                .border(1.dp, if (sel) ImmersiveBlue else ImmersiveBorder, RoundedCornerShape(6.dp))
                                                .clickable { viewModel.adminGenDureeHeures = h }
                                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                        ) {
                                            Text(text = lbl, color = if (sel) Color.White else ImmersiveTextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.adminGenererCustomPass() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ImmersiveGreen, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Générer & Injecter dans SQLite Room", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                // 3. RECHERCHE ET GESTION DES ABONNEMENTS
                Text(text = "📋 GESTION CRUD ABONNEMENTS (${abonnementsFiltres.size})", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                OutlinedTextField(
                    value = viewModel.adminSearchText,
                    onValueChange = { viewModel.adminSearchText = it },
                    label = { Text("Filtrer par code, téléphone, forfait...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = ImmersiveTextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ImmersiveTextWhite,
                        unfocusedTextColor = ImmersiveTextWhite,
                        focusedBorderColor = ImmersiveBlue,
                        unfocusedBorderColor = ImmersiveBorder
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                if (abonnementsFiltres.isEmpty()) {
                    Text(text = "Aucun abonnement trouvé.", color = ImmersiveTextMuted, fontSize = 12.sp, modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        abonnementsFiltres.forEach { abo ->
                            val isValide = abo.estActif && abo.dateExpiration > now
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                                border = BorderStroke(1.dp, if (isValide) ImmersiveGreen else ImmersiveBorder),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "#${abo.id} - ${abo.typeForfait}", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isValide) ImmersiveGreenBg else ImmersiveRedBg)
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = if (isValide) "🟢 ACTIF" else "🔴 INACTIF / EXPIRÉ",
                                                color = if (isValide) ImmersiveGreen else ImmersiveRedLight,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                    Text(text = "Code: ${abo.codeActivation} | Tel: ${abo.telephonePaiement}", color = ImmersiveTextWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = "Prix: ${abo.prixAriary} Ar | Expire le: ${sdf.format(Date(abo.dateExpiration))}", color = ImmersiveTextDark, fontSize = 11.sp)

                                    Divider(color = ImmersiveBorder.copy(alpha = 0.4f), modifier = Modifier.padding(top = 2.dp))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Button(
                                            onClick = { viewModel.adminProlongerAbonnement(abo.id, 24) },
                                            colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlue, contentColor = Color.White),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("+24h Prolonger", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            if (abo.estActif) {
                                                OutlinedButton(
                                                    onClick = { viewModel.adminDesactiverAbonnement(abo.id) },
                                                    border = BorderStroke(1.dp, ImmersiveRedLight),
                                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveRedLight),
                                                    shape = RoundedCornerShape(6.dp),
                                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text("Désactiver", fontSize = 11.sp)
                                                }
                                            } else {
                                                OutlinedButton(
                                                    onClick = { viewModel.adminActiverAbonnement(abo.id) },
                                                    border = BorderStroke(1.dp, ImmersiveGreen),
                                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveGreen),
                                                    shape = RoundedCornerShape(6.dp),
                                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text("Réactiver", fontSize = 11.sp)
                                                }
                                            }

                                            TextButton(
                                                onClick = { viewModel.adminSupprimerAbonnement(abo.id) },
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.DeleteForever, contentDescription = "Supprimer", tint = ImmersiveRedLight, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. MAINTENANCE DE LA BASE DE DONNÉES
                Text(text = "⚙️ MAINTENANCE SQLITE ROOM", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.adminSupprimerExpires() },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, ImmersiveBlue),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("🧹 Nettoyer Expirés", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.adminViderBaseDeDonnees() },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, ImmersiveRedLight),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveRedLight),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("🗑️ Vider Tout", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.showAdminInterfaceModal = false },
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlue, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Fermer l'Interface", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = ImmersiveCard,
        shape = RoundedCornerShape(16.dp)
    )
}
