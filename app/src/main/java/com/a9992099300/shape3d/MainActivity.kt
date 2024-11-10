package com.a9992099300.shape3d

import RotatableIsometricCube
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a9992099300.shape3d.ui.theme.Shape3DTheme
import com.example.customcharts.cube.RotatableColoredCube
import com.example.customcharts.cube.RotatableWireframeCube

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Shape3DTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var degrees = remember { mutableStateOf(0) }
                    val infiniteTransition = rememberInfiniteTransition(label = "infinite rotate")
                    val rotate by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart),
                        label = "rotate"
                    )
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top

                    ) {
                        Spacer(Modifier.weight(1f))
                        Text("RotatableWireframeCube")
                        Spacer(Modifier.padding(16.dp))
                        RotatableWireframeCube(angleY = rotate)
                        Spacer(Modifier.padding(24.dp))
                        Text("RotatableIsometricCube")
                        Spacer(Modifier.padding(16.dp))
                        RotatableIsometricCube(angleY = rotate)
                        Spacer(Modifier.padding(24.dp))
                        Text("RotatableColoredCube")
                        Spacer(Modifier.padding(16.dp))
                        RotatableColoredCube(angleY = rotate)
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}