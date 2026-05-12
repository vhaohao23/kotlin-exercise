package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab3.ui.theme.Lab3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab3Theme {
                DiceRollerApp()
            }
        }
    }
}

@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DiceFace(
            result = result,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(24.dp))
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { result = (1..6).random() }) {
            Text(
                text = stringResource(R.string.roll),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun DiceFace(result: Int, modifier: Modifier = Modifier) {
    // (xFraction, yFraction) positions of each dot within the dice face
    val dotPositions: List<Pair<Float, Float>> = when (result) {
        1 -> listOf(Pair(0.5f, 0.5f))
        2 -> listOf(Pair(0.3f, 0.3f), Pair(0.7f, 0.7f))
        3 -> listOf(Pair(0.3f, 0.3f), Pair(0.5f, 0.5f), Pair(0.7f, 0.7f))
        4 -> listOf(Pair(0.3f, 0.3f), Pair(0.7f, 0.3f), Pair(0.3f, 0.7f), Pair(0.7f, 0.7f))
        5 -> listOf(Pair(0.3f, 0.3f), Pair(0.7f, 0.3f), Pair(0.5f, 0.5f), Pair(0.3f, 0.7f), Pair(0.7f, 0.7f))
        else -> listOf(Pair(0.3f, 0.25f), Pair(0.7f, 0.25f), Pair(0.3f, 0.5f), Pair(0.7f, 0.5f), Pair(0.3f, 0.75f), Pair(0.7f, 0.75f))
    }

    Canvas(modifier = modifier) {
        // White background
        drawRoundRect(
            color = Color.White,
            size = size,
            cornerRadius = CornerRadius(size.width * 0.12f)
        )
        // Border
        drawRoundRect(
            color = Color(0xFF333333),
            size = size,
            cornerRadius = CornerRadius(size.width * 0.12f),
            style = Stroke(width = 6f)
        )
        // Dots
        val dotRadius = size.width * 0.09f
        dotPositions.forEach { (xFrac, yFrac) ->
            drawCircle(
                color = Color(0xFF222222),
                radius = dotRadius,
                center = Offset(size.width * xFrac, size.height * yFrac)
            )
        }
    }
}

@Preview
@Composable
fun DiceRollerAppPreview() {
    Lab3Theme {
        DiceRollerApp()
    }
}
