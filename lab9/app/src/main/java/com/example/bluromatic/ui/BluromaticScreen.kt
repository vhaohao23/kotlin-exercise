package com.example.bluromatic.ui

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bluromatic.R
import com.example.bluromatic.ui.theme.BluromaticTheme

@Composable
fun BluromaticScreen(blurViewModel: BlurViewModel = viewModel(factory = BlurViewModel.Factory)) {
    val uiState by blurViewModel.blurUiState.collectAsState()
    BluromaticScreenContent(
        blurUiState = uiState,
        applyBlur = blurViewModel::applyBlur,
        cancelWork = blurViewModel::cancelWork
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluromaticScreenContent(
    blurUiState: BlurUiState,
    applyBlur: (Int) -> Unit,
    cancelWork: () -> Unit
) {
    var blurLevel by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (blurUiState) {
                    is BlurUiState.Complete -> {
                        AsyncImage(
                            model = blurUiState.outputUri,
                            contentDescription = stringResource(R.string.description_image),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                    is BlurUiState.Loading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(stringResource(R.string.processing))
                        }
                    }
                    else -> {
                        // Draw cupcake entirely in Compose Canvas
                        ComposeCupcake(modifier = Modifier.size(280.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.blur_level),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    1 to stringResource(R.string.low_blur),
                    2 to stringResource(R.string.medium_blur),
                    3 to stringResource(R.string.high_blur)
                ).forEach { (level, label) ->
                    FilterChip(
                        selected = blurLevel == level,
                        onClick = { blurLevel = level },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (blurUiState is BlurUiState.Loading) {
                    OutlinedButton(
                        onClick = cancelWork,
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.cancel)) }
                } else {
                    Button(
                        onClick = { applyBlur(blurLevel) },
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.start)) }
                }
                if (blurUiState is BlurUiState.Complete) {
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) { Text(stringResource(R.string.see_file)) }
                }
            }
        }
    }
}

/** Cupcake drawn entirely with Compose Canvas — no XML drawable needed. */
@Composable
fun ComposeCupcake(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // --- wrapper / base ---
        val basePath = Path().apply {
            moveTo(w * 0.25f, h * 0.55f)
            lineTo(w * 0.75f, h * 0.55f)
            lineTo(w * 0.70f, h * 0.85f)
            lineTo(w * 0.30f, h * 0.85f)
            close()
        }
        drawPath(basePath, Color(0xFF795548))

        // wrapper vertical lines
        listOf(0.38f, 0.50f, 0.62f).forEach { xFrac ->
            drawLine(
                color = Color(0xFF5D4037),
                start = Offset(w * xFrac, h * 0.55f),
                end = Offset(w * (xFrac - 0.04f), h * 0.85f),
                strokeWidth = 3f
            )
        }

        // --- frosting ---
        val frostingPath = Path().apply {
            moveTo(w * 0.18f, h * 0.55f)
            quadraticBezierTo(w * 0.25f, h * 0.25f, w * 0.50f, h * 0.20f)
            quadraticBezierTo(w * 0.75f, h * 0.25f, w * 0.82f, h * 0.55f)
            quadraticBezierTo(w * 0.68f, h * 0.45f, w * 0.60f, h * 0.50f)
            quadraticBezierTo(w * 0.50f, h * 0.38f, w * 0.40f, h * 0.50f)
            quadraticBezierTo(w * 0.30f, h * 0.45f, w * 0.18f, h * 0.55f)
            close()
        }
        drawPath(frostingPath, Color(0xFFE91E63))

        // frosting highlight
        val highlightPath = Path().apply {
            moveTo(w * 0.32f, h * 0.38f)
            quadraticBezierTo(w * 0.38f, h * 0.28f, w * 0.50f, h * 0.27f)
            quadraticBezierTo(w * 0.62f, h * 0.28f, w * 0.68f, h * 0.38f)
            quadraticBezierTo(w * 0.55f, h * 0.33f, w * 0.50f, h * 0.34f)
            quadraticBezierTo(w * 0.45f, h * 0.33f, w * 0.32f, h * 0.38f)
            close()
        }
        drawPath(highlightPath, Color(0xFFF06292))

        // --- cherry ---
        drawCircle(color = Color(0xFFC62828), radius = w * 0.09f, center = Offset(w * 0.50f, h * 0.13f))
        drawCircle(color = Color(0xFFEF5350), radius = w * 0.035f, center = Offset(w * 0.465f, h * 0.10f))

        // --- sprinkles ---
        val sprinkles = listOf(
            Triple(0.38f, 0.46f, Color(0xFFFFC107)),
            Triple(0.55f, 0.40f, Color(0xFF4CAF50)),
            Triple(0.46f, 0.51f, Color(0xFF2196F3))
        )
        sprinkles.forEach { (x, y, color) ->
            drawLine(
                color = color,
                start = Offset(w * x, h * y),
                end = Offset(w * (x + 0.05f), h * (y + 0.05f)),
                strokeWidth = 5f
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BluromaticScreenPreview() {
    BluromaticTheme {
        BluromaticScreenContent(
            blurUiState = BlurUiState.Default,
            applyBlur = {},
            cancelWork = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CupcakePreview() {
    BluromaticTheme {
        ComposeCupcake(modifier = Modifier.size(280.dp))
    }
}
