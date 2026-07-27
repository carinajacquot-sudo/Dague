package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.AviatorViewModel
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryTabScreen(viewModel: AviatorViewModel) {
    val sequence = viewModel.sequence
    val totalRounds = sequence.size
    val maxMultiplier = if (sequence.isNotEmpty()) sequence.maxOrNull() ?: 0f else 0f
    val avgMultiplier = if (sequence.isNotEmpty()) sequence.average().toFloat() else 0f
    val pinkRounds = sequence.count { it >= 2.0f }
    val winRate = if (totalRounds > 0) (pinkRounds * 100f / totalRounds) else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stats Overview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ANALYSE STATISTIQUE GLOBALE",
                        color = ImmersiveTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    if (sequence.isNotEmpty()) {
                        TextButton(
                            onClick = { viewModel.clearHistory() },
                            modifier = Modifier.testTag("clear_all_history_btn")
                        ) {
                            Text("Tout effacer", color = ImmersiveRedLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Stat 1: Max
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ImmersiveBg)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("SOMMET MAX", color = ImmersiveTextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (maxMultiplier > 0f) String.format("%.2fx", maxMultiplier) else "--",
                                color = ImmersiveRedLight,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                    // Stat 2: Avg
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ImmersiveBg)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("MOYENNE", color = ImmersiveTextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (avgMultiplier > 0f) String.format("%.2fx", avgMultiplier) else "--",
                                color = ImmersiveGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                    // Stat 3: Cotes Roses %
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ImmersiveBg)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("COTES ROSES", color = ImmersiveTextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format("%.1f%%", winRate),
                                color = ImmersiveBlue,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filtrer", tint = ImmersiveTextMuted, modifier = Modifier.size(20.dp))
            val filters = listOf("Tous (${sequence.size})", "Cotes Roses >=2.0x", "Cotes Bleues <2.0x")
            filters.forEachIndexed { idx, label ->
                val isSelected = viewModel.historyFilterMode == idx
                val bg = if (isSelected) ImmersiveRed else ImmersiveSurface
                val borderCol = if (isSelected) ImmersiveRedLight else ImmersiveBorder
                val textCol = if (isSelected) Color.White else ImmersiveTextMuted
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bg)
                        .border(BorderStroke(1.dp, borderCol), RoundedCornerShape(20.dp))
                        .clickable { viewModel.historyFilterMode = idx }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = label, color = textCol, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // History List
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "HISTORIQUE DÉTAILLÉ DES TOURS",
                    color = ImmersiveTextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                val filteredSequence = sequence.mapIndexed { i, valFloat -> i to valFloat }.filter { (_, valFloat) ->
                    when (viewModel.historyFilterMode) {
                        1 -> valFloat >= 2.0f
                        2 -> valFloat < 2.0f
                        else -> true
                    }
                }

                if (filteredSequence.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Analytics, contentDescription = null, tint = ImmersiveTextDark, modifier = Modifier.size(48.dp))
                        Text(
                            text = "Aucune donnée dans cet historique.\nScannez une photo ou ajoutez des multiplicateurs.",
                            color = ImmersiveTextMuted,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { viewModel.showPhotoScannerModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlueBg, contentColor = ImmersiveBlue),
                            border = BorderStroke(1.dp, ImmersiveBlue.copy(alpha = 0.5f))
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Scanner une Photo OCR", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                } else {
                    filteredSequence.reversed().forEach { (originalIdx, valFloat) ->
                        val isEdited = originalIdx in viewModel.editedIndices
                        val isRose = valFloat >= 2.0f
                        val (bg, badgeCol, textCol) = when {
                            isEdited -> Triple(ImmersiveBlueBg, ImmersiveBlue.copy(alpha = 0.5f), ImmersiveBlue)
                            isRose -> Triple(ImmersiveRedBg, ImmersiveRed.copy(alpha = 0.5f), ImmersiveRedLight)
                            else -> Triple(ImmersiveBg, ImmersiveBorder, ImmersiveTextWhite)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg)
                                .border(BorderStroke(1.dp, badgeCol), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(badgeCol.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${originalIdx + 1}",
                                        color = ImmersiveTextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column {
                                    Text(
                                        text = String.format("%.2fx", valFloat),
                                        color = textCol,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    if (isEdited) {
                                        Text(text = "Modifié manuellement", color = ImmersiveBlue, fontSize = 10.sp)
                                    } else if (isRose) {
                                        Text(text = "Cote Rose (Gain Élevé)", color = ImmersiveRedLight, fontSize = 10.sp)
                                    } else {
                                        Text(text = "Cote Bleue (Crash Rapide)", color = ImmersiveTextDark, fontSize = 10.sp)
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { viewModel.startEditing(originalIdx) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Modifier", tint = ImmersiveTextMuted, modifier = Modifier.size(18.dp))
                                }
                                IconButton(
                                    onClick = { viewModel.deleteValueFromHistory(originalIdx) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Supprimer", tint = ImmersiveRedLight, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceTabScreen(viewModel: AviatorViewModel) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Wallet Balance Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SOLDE DE SIMULATION DÉMO",
                        color = ImmersiveTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ImmersiveGreenBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("MODE SANS RISQUE", color = ImmersiveGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%.2f €", viewModel.balance),
                        color = ImmersiveTextWhite,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                    Button(
                        onClick = { viewModel.rechargeBalance(1000.0f) },
                        colors = ButtonDefaults.buttonColors(containerColor = ImmersiveGreenBg, contentColor = ImmersiveGreen),
                        border = BorderStroke(1.dp, ImmersiveGreen.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+1 000 €", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Divider(color = ImmersiveBorder, thickness = 1.dp)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PARIS SIMULÉS", color = ImmersiveTextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${viewModel.totalSimulatedBets}", color = ImmersiveTextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("VICTOIRES", color = ImmersiveTextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${viewModel.totalSimulatedWins}", color = ImmersiveGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val rate = if (viewModel.totalSimulatedBets > 0) (viewModel.totalSimulatedWins * 100f / viewModel.totalSimulatedBets) else 0f
                        Text("RÉUSSITE", color = ImmersiveTextDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = String.format("%.0f%%", rate), color = ImmersiveBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Betting Simulation Console
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "LANCEUR DE STRATÉGIE (TESTEUR IA)",
                    color = ImmersiveTextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // Bet Amount Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mise de pari ( € ) :", color = ImmersiveTextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = viewModel.betAmountText,
                        onValueChange = { viewModel.betAmountText = it },
                        modifier = Modifier.width(120.dp).testTag("sim_bet_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImmersiveRedLight,
                            unfocusedBorderColor = ImmersiveBorder,
                            focusedTextColor = ImmersiveTextWhite,
                            unfocusedTextColor = ImmersiveTextWhite,
                            focusedContainerColor = ImmersiveBg,
                            unfocusedContainerColor = ImmersiveBg
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Quick Bet Chips
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val chips = listOf("+10" to 10f, "+50" to 50f, "+100" to 100f, "Max" to -1f)
                    chips.forEach { (label, amount) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ImmersiveBg)
                                .border(BorderStroke(1.dp, ImmersiveBorder), RoundedCornerShape(8.dp))
                                .clickable { viewModel.addQuickBet(amount) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = label, color = ImmersiveTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Target Cashout Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Encaissement Auto (x) :", color = ImmersiveTextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = viewModel.autoCashoutText,
                        onValueChange = { viewModel.autoCashoutText = it },
                        modifier = Modifier.width(120.dp).testTag("sim_cashout_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            viewModel.simulateBet()
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImmersiveGreen,
                            unfocusedBorderColor = ImmersiveBorder,
                            focusedTextColor = ImmersiveTextWhite,
                            unfocusedTextColor = ImmersiveTextWhite,
                            focusedContainerColor = ImmersiveBg,
                            unfocusedContainerColor = ImmersiveBg
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Launch Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.simulateBet()
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp).testTag("sim_launch_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = ImmersiveGreen, contentColor = Color.White),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("LANCER LE PARI SIMULÉ", fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 0.5.sp)
                    }
                }
            }
        }

        // Bet History List
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "HISTORIQUE DES PARIS SIMULÉS",
                    color = ImmersiveTextWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                if (viewModel.betHistory.isEmpty()) {
                    Text(
                        text = "Aucun pari simulé pour le moment.\nTestez vos stratégies avec l'argent démo ci-dessus !",
                        color = ImmersiveTextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                    )
                } else {
                    viewModel.betHistory.forEach { record ->
                        val (bg, borderCol, textCol, icon) = if (record.won) {
                            Quadruple(ImmersiveGreenBg, ImmersiveGreen.copy(alpha = 0.5f), ImmersiveGreen, Icons.Default.TrendingUp)
                        } else {
                            Quadruple(ImmersiveRedBg, ImmersiveRed.copy(alpha = 0.4f), ImmersiveRedLight, Icons.Default.TrendingDown)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg)
                                .border(BorderStroke(1.dp, borderCol), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(imageVector = icon, contentDescription = null, tint = textCol, modifier = Modifier.size(24.dp))
                                Column {
                                    Text(
                                        text = if (record.won) "GAGNÉ (+${String.format("%.2f €", record.profit)})" else "PERDU (${String.format("%.2f €", record.profit)})",
                                        color = textCol,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Mise: ${record.betAmount} € | Cible: ${record.targetCashout}x",
                                        color = ImmersiveTextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Crash à",
                                    color = ImmersiveTextDark,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = String.format("%.2fx", record.actualMultiplier),
                                    color = ImmersiveTextWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper tuple for styling
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun ProfileTabScreen(viewModel: AviatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // VIP User Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(ImmersiveRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = viewModel.userName,
                            color = ImmersiveTextWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Icon(imageVector = Icons.Default.Star, contentDescription = "VIP", tint = ImmersiveRedLight, modifier = Modifier.size(18.dp))
                    }
                    val abo = viewModel.abonnementActif
                    Text(
                        text = if (abo != null) "Statut : Pass '${abo.typeForfait}' Actif" else "Statut : Version Gratuite (Bloquée)",
                        color = if (abo != null) ImmersiveGreen else ImmersiveRedLight,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Accès Algorithme Phantom-Kali (GitHub) en local",
                        color = ImmersiveTextDark,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (viewModel.isAdminLogged) {
            Button(
                onClick = { viewModel.showAdminInterfaceModal = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlue, contentColor = Color.White),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("🚀 ACCÉDER AU TABLEAU DE BORD ADMIN", fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
        } else {
            OutlinedButton(
                onClick = { viewModel.showAdminLoginModal = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, ImmersiveBlue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveBlue),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("🛡️ Connexion Administrateur (Dague)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // AI Engine & API Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "ALGORITHME DE PRÉDICTION OPEN-SOURCE",
                    color = ImmersiveTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = ImmersiveBlue, modifier = Modifier.size(22.dp))
                        Column {
                            Text("Régression Linéaire AR(3)", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(viewModel.aiModelName, color = ImmersiveBlue, fontSize = 12.sp)
                        }
                    }
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(ImmersiveGreenBg).padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("LOCAL", color = ImmersiveGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Divider(color = ImmersiveBorder, thickness = 1.dp)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = ImmersiveGreen, modifier = Modifier.size(22.dp))
                        Column {
                            Text("Repo GitHub : phantom-kali/AviatorPredictionModel", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Calcul 100% Local (Sans API / Zéro Latence)", color = ImmersiveTextMuted, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Preferences & Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "PRÉFÉRENCES DE L'APPLICATION",
                    color = ImmersiveTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                // Sound Toggle
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(
                            imageVector = if (viewModel.soundEffects) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = null,
                            tint = ImmersiveTextWhite,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text("Effets Sonores & Vibrations", color = ImmersiveTextWhite, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text("Retour haptique lors des prédictions", color = ImmersiveTextDark, fontSize = 11.sp)
                        }
                    }
                    Switch(
                        checked = viewModel.soundEffects,
                        onCheckedChange = { viewModel.toggleSound() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ImmersiveRed)
                    )
                }

                // OCR Auto Toggle
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = ImmersiveTextWhite, modifier = Modifier.size(22.dp))
                        Column {
                            Text("Correction Auto OCR", color = ImmersiveTextWhite, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text("Améliorer la netteté des captures de jeu", color = ImmersiveTextDark, fontSize = 11.sp)
                        }
                    }
                    Switch(
                        checked = viewModel.autoOCR,
                        onCheckedChange = { viewModel.toggleAutoOCR() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ImmersiveBlue)
                    )
                }
            }
        }

        // Danger Zone Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ImmersiveRed.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "ZONE DE RÉINITIALISATION",
                    color = ImmersiveRedLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Effacer tout l'historique de simulation, restaurer le solde démo à 1 000 € et vider le cache des prévisions de l'IA.",
                    color = ImmersiveTextMuted,
                    fontSize = 13.sp
                )
                OutlinedButton(
                    onClick = { viewModel.resetAllApp() },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("reset_app_btn"),
                    border = BorderStroke(1.dp, ImmersiveRedLight),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveRedLight)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("RÉINITIALISER L'APPLICATION", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsModalDialog(viewModel: AviatorViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.showSettingsModal = false },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Paramètres Aviator Pro", color = ImmersiveTextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { viewModel.showSettingsModal = false }, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fermer", tint = ImmersiveTextMuted)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Réglez les options système en temps réel :", color = ImmersiveTextMuted, fontSize = 13.sp)
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Sons & Vibrations", color = ImmersiveTextWhite, fontSize = 14.sp)
                    Switch(
                        checked = viewModel.soundEffects,
                        onCheckedChange = { viewModel.toggleSound() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ImmersiveRed)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Correction OCR", color = ImmersiveTextWhite, fontSize = 14.sp)
                    Switch(
                        checked = viewModel.autoOCR,
                        onCheckedChange = { viewModel.toggleAutoOCR() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ImmersiveBlue)
                    )
                }

                Divider(color = ImmersiveBorder)

                OutlinedButton(
                    onClick = {
                        viewModel.resetAllApp()
                        viewModel.showSettingsModal = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, ImmersiveRedLight),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveRedLight)
                ) {
                    Text("Réinitialiser l'application", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.showSettingsModal = false },
                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveRed, contentColor = Color.White)
            ) {
                Text("Fermer", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = ImmersiveSurface,
        shape = RoundedCornerShape(20.dp)
    )
}
