package com.example.customcharts.cube

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

private val brush = Brush.linearGradient(listOf(Color.Black, Color.Black, Color.Black))

@Composable
fun RotatableWireframeCube(
    angleY: Float,
    sizeCanvas: Dp = 100.dp,
    sizeCube: Float = 200f,
) {
    Canvas(modifier = Modifier.size(sizeCanvas)) {
        // Размер куба
        val size = sizeCube
        val halfSize = size / 2

        // Определение исходных координат (X, Y, Z) для вершин куба
        val frontTopLeft = Triple(-halfSize, -halfSize, halfSize)
        val frontTopRight = Triple(halfSize, -halfSize, halfSize)
        val frontBottomLeft = Triple(-halfSize, halfSize, halfSize)
        val frontBottomRight = Triple(halfSize, halfSize, halfSize)

        val backTopLeft = Triple(-halfSize, -halfSize, -halfSize)
        val backTopRight = Triple(halfSize, -halfSize, -halfSize)
        val backBottomLeft = Triple(-halfSize, halfSize, -halfSize)
        val backBottomRight = Triple(halfSize, halfSize, -halfSize)

        // Применяем поворот вокруг оси Y
        val rotatedFrontTopLeft = rotateY(frontTopLeft, angleY)
        val rotatedFrontTopRight = rotateY(frontTopRight, angleY)
        val rotatedFrontBottomLeft = rotateY(frontBottomLeft, angleY)
        val rotatedFrontBottomRight = rotateY(frontBottomRight, angleY)

        val rotatedBackTopLeft = rotateY(backTopLeft, angleY)
        val rotatedBackTopRight = rotateY(backTopRight, angleY)
        val rotatedBackBottomLeft = rotateY(backBottomLeft, angleY)
        val rotatedBackBottomRight = rotateY(backBottomRight, angleY)

        // Сместим куб обратно в центр экрана и добавим перспективу
        val finalFrontTopLeft = project(rotatedFrontTopLeft)
        val finalFrontTopRight = project(rotatedFrontTopRight)
        val finalFrontBottomLeft = project(rotatedFrontBottomLeft)
        val finalFrontBottomRight = project(rotatedFrontBottomRight)

        val finalBackTopLeft = project(rotatedBackTopLeft)
        val finalBackTopRight = project(rotatedBackTopRight)
        val finalBackBottomLeft = project(rotatedBackBottomLeft)
        val finalBackBottomRight = project(rotatedBackBottomRight)

        // Рисуем грани куба
        drawEdge(
            finalFrontTopLeft,
            finalFrontTopRight,
            finalFrontBottomRight,
            finalFrontBottomLeft,
            Color.Blue
        )
        drawEdge(
            finalBackTopLeft,
            finalBackTopRight,
            finalBackBottomRight,
            finalBackBottomLeft,
            Color.Gray
        )

        // Рисуем линии, соединяющие переднюю и заднюю части куба
        drawLine(brush, finalFrontTopLeft, finalBackTopLeft, strokeWidth = 2f)
        drawLine(brush, finalFrontTopRight, finalBackTopRight, strokeWidth = 2f)
        drawLine(brush, finalFrontBottomLeft, finalBackBottomLeft, strokeWidth = 2f)
        drawLine(brush, finalFrontBottomRight, finalBackBottomRight, strokeWidth = 2f)
    }
}

// Функция для поворота точки вокруг оси Y (включая координату Z)
private fun rotateY(
    point: Triple<Float, Float, Float>,
    angleY: Float
): Triple<Float, Float, Float> {
    val radiansY = Math.toRadians(angleY.toDouble()).toFloat()

    val x = point.first * cos(radiansY) - point.third * sin(radiansY)
    val z = point.first * sin(radiansY) + point.third * cos(radiansY)
    return Triple(x, point.second, z)
}

// Проекция 3D координат на 2D с добавлением перспективы
private fun project(point: Triple<Float, Float, Float>): Offset {
    val perspective = 400f / (400f + point.third)  // регулировка перспективы
    val offsetX = 100f + point.first * perspective
    val offsetY = 100f + point.second * perspective
    return Offset(offsetX, offsetY)
}

// Функция для рисования одной грани куба
private fun DrawScope.drawEdge(
    topLeft: Offset,
    topRight: Offset,
    bottomRight: Offset,
    bottomLeft: Offset,
    color: Color
) {
    val path = Path().apply {
        moveTo(topLeft.x, topLeft.y)
        lineTo(topRight.x, topRight.y)
        lineTo(bottomRight.x, bottomRight.y)
        lineTo(bottomLeft.x, bottomLeft.y)
        close()
    }
    drawPath(path, brush = brush, style = Stroke(width = 2f))
}
