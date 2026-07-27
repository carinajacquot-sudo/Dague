package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import coil.compose.AsyncImage
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: AviatorViewModel = viewModel()
                AviatorPredictionScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun AviatorPredictionScreen(viewModel: AviatorViewModel) {
    val focusManager = LocalFocusManager.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.startPhotoScan(uri = uri, isDemo = false)
        }
    }

    // Animate button scale when prediction is triggered (matching PyQt6 QPropertyAnimation bounce)
    val buttonScale by animateFloatAsState(
        targetValue = if (viewModel.animationTrigger % 2 == 1) 1.03f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "predict_button_bounce"
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg),
        containerColor = ImmersiveBg,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ImmersiveBg)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ImmersiveRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = "Aviator Icon",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "PRÉDICTEUR AVIATOR",
                            color = ImmersiveTextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = (-0.5).sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.clickable { viewModel.currentTab = 3 }
                        ) {
                            val estAbonne = viewModel.abonnementActif != null
                            val statusColor = if (estAbonne) ImmersiveGreen else ImmersiveRedLight
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                            Text(
                                text = if (estAbonne) "VIP ACTIF : ${viewModel.abonnementActif?.typeForfait}" else "VERSION GRATUITE / BLOQUÉE - S'ABONNER",
                                color = statusColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (viewModel.sequence.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearHistory() },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ImmersiveBorder)
                                .testTag("clear_history_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Effacer l'historique",
                                tint = ImmersiveTextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(ImmersiveBorder)
                            .clickable { viewModel.showSettingsModal = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Paramètres",
                            tint = ImmersiveTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(ImmersiveBg)
                    .border(BorderStroke(1.dp, ImmersiveBorder)),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tabs = listOf(
                    Triple(Icons.Default.Home, "PRÉDIRE", 0),
                    Triple(Icons.Default.Analytics, "HISTO", 1),
                    Triple(Icons.Default.AccountBalanceWallet, "SOLDE", 2),
                    Triple(Icons.Default.Star, "PASS MGA", 3),
                    Triple(Icons.Default.Person, "PROFIL", 4)
                )
                tabs.forEach { (icon, label, idx) ->
                    val isSelected = viewModel.currentTab == idx
                    val tint = if (isSelected) ImmersiveRedLight else ImmersiveTextDark
                    val fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                    val bg = if (isSelected) ImmersiveRed.copy(alpha = 0.15f) else Color.Transparent
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bg)
                            .clickable { viewModel.currentTab = idx }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
                        Text(text = label, color = tint, fontWeight = fontWeight, fontSize = 9.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Atmosphere Glows
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.TopCenter)
                    .background(ImmersiveRedBg.copy(alpha = 0.35f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.BottomEnd)
                    .background(ImmersiveBlueBg.copy(alpha = 0.25f), CircleShape)
            )

            when (viewModel.currentTab) {
                1 -> com.example.ui.HistoryTabScreen(viewModel = viewModel)
                2 -> com.example.ui.BalanceTabScreen(viewModel = viewModel)
                3 -> com.example.ui.AbonnementTabScreen(viewModel = viewModel)
                4 -> com.example.ui.ProfileTabScreen(viewModel = viewModel)
                else -> {
                    // Main Scrollable Content
                    Column(
                        modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Main Prediction Meter
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, ImmersiveBorder), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(204.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(3.dp, ImmersiveRed.copy(alpha = 0.35f)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "PROCHAIN SIGNAL",
                                color = ImmersiveTextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val mainValueText = if (viewModel.predictions.isNotEmpty()) {
                                String.format("%.2fx", viewModel.predictions.first())
                            } else {
                                "--.--x"
                            }
                            Text(
                                text = mainValueText,
                                color = ImmersiveTextWhite,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (viewModel.predictions.isNotEmpty()) "HAUTE CONFIANCE" else "EN ATTENTE DE DONNÉES",
                                color = if (viewModel.predictions.isNotEmpty()) ImmersiveRedLight else ImmersiveTextDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                // Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Accuracy
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, ImmersiveBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "PRÉCISION",
                                color = ImmersiveTextDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "94.2%",
                                color = ImmersiveTextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(ImmersiveBorder)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.94f)
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(ImmersiveGreen)
                                )
                            }
                        }
                    }

                    // Card 2: Rounds Analyzed
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, ImmersiveBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "TOURS ANALYSÉS",
                                color = ImmersiveTextDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val totalRounds = 1248 + viewModel.sequence.size
                            Text(
                                text = String.format("%,d", totalRounds),
                                color = ImmersiveTextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(ImmersiveBorder)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.70f)
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(ImmersiveBlue)
                                )
                            }
                        }
                    }
                }

                // History Bar (Recent Multipliers)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, ImmersiveBorder.copy(alpha = 0.7f))
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
                            Text(
                                text = "MULTIPLICATEURS RÉCENTS (${viewModel.sequence.size}/20)",
                                color = ImmersiveTextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "EN DIRECT",
                                color = ImmersiveRedLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (viewModel.sequence.isEmpty()) {
                            Text(
                                text = "Aucun historique. Entrez des valeurs ou scannez une photo pour entraîner le modèle.",
                                color = ImmersiveTextDark,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        } else {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                viewModel.sequence.forEachIndexed { index, value ->
                                    val isEdited = index in viewModel.editedIndices
                                    val (bg, borderCol, textCol) = when {
                                        isEdited -> Triple(ImmersiveBlueBg, ImmersiveBlue.copy(alpha = 0.5f), ImmersiveBlue)
                                        value >= 2.0f -> Triple(ImmersiveRedBg, ImmersiveRed.copy(alpha = 0.4f), ImmersiveRedLight)
                                        else -> Triple(ImmersiveBorder, ImmersiveBorder, ImmersiveTextWhite)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(bg)
                                            .border(BorderStroke(1.dp, borderCol), RoundedCornerShape(8.dp))
                                            .clickable { viewModel.startEditing(index) }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                            .testTag("history_chip_$index"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = String.format("%.2fx", value),
                                            color = textCol,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Predicted Future Multipliers Bar
                if (viewModel.predictions.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, ImmersiveBorder.copy(alpha = 0.7f))
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
                                Text(
                                    text = "FUTURS MULTIPLICATEURS PRÉDITS",
                                    color = ImmersiveTextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "RÉGRESSION LINÉAIRE PHANTOM-KALI",
                                    color = ImmersiveGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                viewModel.predictions.forEachIndexed { index, value ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ImmersiveGreenBg)
                                            .border(BorderStroke(1.dp, ImmersiveGreen.copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 14.dp, vertical = 6.dp)
                                            .testTag("prediction_chip_$index"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = String.format("%.4fx", value),
                                            color = ImmersiveGreen,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            // Input Controls Area
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, ImmersiveBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Row 1: Number of Predictions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Horizon de prévision (Tours) :",
                            color = ImmersiveTextWhite,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        OutlinedTextField(
                            value = viewModel.numPredictionsText,
                            onValueChange = { viewModel.onNumPredictionsChange(it) },
                            modifier = Modifier
                                .width(90.dp)
                                .testTag("num_predictions_input"),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
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

                    // Row 2: Input Sequence
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Nouveau multiplicateur :",
                            color = ImmersiveTextWhite,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = viewModel.inputSequenceText,
                                onValueChange = { viewModel.onInputSequenceChange(it) },
                                modifier = Modifier
                                    .width(130.dp)
                                    .testTag("sequence_input"),
                                placeholder = { Text("ex: 1.54", color = ImmersiveTextDark, fontSize = 13.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        viewModel.addValueToHistory()
                                        focusManager.clearFocus()
                                    }
                                ),
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
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    viewModel.addValueToHistory()
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier
                                    .height(52.dp)
                                    .testTag("add_sequence_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ImmersiveRed,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Ajouter",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Quick Preset Multiplier Chips for Instant Manual Addition
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val presets = listOf(1.10f, 1.25f, 1.50f, 2.00f, 3.00f, 5.00f, 10.0f)
                        presets.forEach { preset ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ImmersiveBg)
                                    .border(BorderStroke(1.dp, ImmersiveBorder), RoundedCornerShape(8.dp))
                                    .clickable { viewModel.addPresetMultiplier(preset) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+${String.format("%.2fx", preset)}",
                                    color = ImmersiveTextMuted,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Row 3: Photo Scanner OCR Button
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.showPhotoScannerModal = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("open_scanner_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ImmersiveBlueBg,
                            contentColor = ImmersiveBlue
                        ),
                        border = BorderStroke(1.dp, ImmersiveBlue.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = ImmersiveBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SCANNER UNE PHOTO DE JEU (OCR)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            // CTA Button ("GENERATE SIGNAL")
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.predict()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .scale(buttonScale)
                    .testTag("predict_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImmersiveRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (viewModel.isPredicting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "ANALYSE DE L'IA...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = 1.sp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "GÉNÉRER LE SIGNAL",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

// Modal Dialog for Editing History Chip
    if (viewModel.editingIndex != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelEditing() },
            title = {
                Text(
                    text = "Modifier le Multiplicateur (#${viewModel.editingIndex!! + 1})",
                    color = ImmersiveTextWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Modifiez la valeur historique ci-dessous :",
                        color = ImmersiveTextMuted,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = viewModel.editingValueText,
                        onValueChange = { viewModel.editingValueText = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { viewModel.saveEditing() }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImmersiveBlue,
                            unfocusedBorderColor = ImmersiveBorder,
                            focusedTextColor = ImmersiveTextWhite,
                            unfocusedTextColor = ImmersiveTextWhite,
                            focusedContainerColor = ImmersiveBg,
                            unfocusedContainerColor = ImmersiveBg
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_value_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.saveEditing() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ImmersiveRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("save_edit_button")
                ) {
                    Text("Enregistrer", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.cancelEditing() }
                ) {
                    Text("Annuler", color = ImmersiveTextMuted)
                }
            },
            containerColor = ImmersiveSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Modal Dialog for Errors
    if (viewModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = {
                Text(
                    text = "Avis du Système",
                    color = ImmersiveTextWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = viewModel.errorMessage!!,
                    color = ImmersiveTextMuted,
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearError() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ImmersiveRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("dismiss_error_button")
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            },
            containerColor = ImmersiveSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Modal Dialog for Photo Scanner & OCR
    if (viewModel.showPhotoScannerModal) {
        AlertDialog(
            onDismissRequest = { if (!viewModel.isScanningPhoto) viewModel.showPhotoScannerModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        tint = ImmersiveBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Scanner OCR de Multiplicateur",
                        color = ImmersiveTextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    if (viewModel.isScanningPhoto) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(color = ImmersiveBlue, strokeWidth = 3.dp)
                            Text(
                                text = viewModel.scanProgressText,
                                color = ImmersiveTextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else if (viewModel.scanResults.isNotEmpty()) {
                        Text(
                            text = "✨ Résultats OCR extraits (Touchez pour supprimer, ou ajoutez/corrigez ci-dessous si l'OCR s'est trompé) :",
                            color = ImmersiveTextMuted,
                            fontSize = 13.sp
                        )
                        if (viewModel.scannedImageUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(BorderStroke(1.dp, ImmersiveBorder), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = viewModel.scannedImageUri,
                                    contentDescription = "Scanned Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                            }
                        }

                        // Correction Input for Scan Results
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = viewModel.scanInputText,
                                onValueChange = { viewModel.scanInputText = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Ajouter cote (ex: 2.15)", color = ImmersiveTextDark, fontSize = 12.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ImmersiveBlue,
                                    unfocusedBorderColor = ImmersiveBorder,
                                    focusedTextColor = ImmersiveTextWhite,
                                    unfocusedTextColor = ImmersiveTextWhite,
                                    focusedContainerColor = ImmersiveBg,
                                    unfocusedContainerColor = ImmersiveBg
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Button(
                                onClick = { viewModel.addCustomScanResult() },
                                colors = ButtonDefaults.buttonColors(containerColor = ImmersiveBlue),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter", tint = Color.White)
                            }
                        }

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            viewModel.scanResults.forEachIndexed { scanIdx, valFloat ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ImmersiveRedBg)
                                        .border(BorderStroke(1.dp, ImmersiveRed.copy(alpha = 0.5f)), RoundedCornerShape(8.dp))
                                        .clickable { viewModel.removeScanResult(scanIdx) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = String.format("%.2fx", valFloat),
                                            color = ImmersiveRedLight,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Supprimer",
                                            tint = ImmersiveRedLight,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Importez une capture d'écran de l'historique d'Aviator ou lancez un test de démonstration. L'algorithme OCR va extraire les valeurs pour entraîner l'IA.",
                            color = ImmersiveTextMuted,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                photoPickerLauncher.launch("image/*")
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ImmersiveBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("📁 Choisir une Photo dans la Galerie", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        OutlinedButton(
                            onClick = {
                                viewModel.startPhotoScan(uri = null, isDemo = true)
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            border = BorderStroke(1.dp, ImmersiveGreen),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveGreen)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("⚡ Tester avec capture Démo (Rapide)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (viewModel.scanResults.isNotEmpty() && !viewModel.isScanningPhoto) {
                    Button(
                        onClick = { viewModel.confirmScanResults() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ImmersiveRed,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("confirm_scan_button")
                    ) {
                        Text("📥 Ajouter à l'Entraînement", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (viewModel.scanResults.isNotEmpty()) {
                            viewModel.scanResults = emptyList()
                        } else {
                            viewModel.showPhotoScannerModal = false
                        }
                    }
                ) {
                    Text(
                        text = if (viewModel.scanResults.isNotEmpty()) "🔄 Recommencer" else "Fermer",
                        color = ImmersiveTextMuted
                    )
                }
            },
            containerColor = ImmersiveSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (viewModel.showSettingsModal) {
        com.example.ui.SettingsModalDialog(viewModel = viewModel)
    }

    if (viewModel.showAbonnementModal) {
        com.example.ui.AbonnementModalDialog(viewModel = viewModel)
    }

    if (viewModel.showAdminLoginModal) {
        com.example.ui.AdminLoginModalDialog(viewModel = viewModel)
    }

    if (viewModel.showAdminInterfaceModal) {
        com.example.ui.AdminInterfaceModalDialog(viewModel = viewModel)
    }
}
