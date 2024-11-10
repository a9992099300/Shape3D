import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OrthographicColoredCube(
    angleY: Float,
    sizeCanvas: Dp = 100.dp,
    sizeCube: Float = 200f,
) {
    Canvas(modifier = Modifier.size(sizeCanvas)) {
        // Размер куба
        val size = sizeCube
        val halfSize = size / 2

        // Определение исходных координат (X, Y, Z) для куба
        val frontTopLeft = Triple(-halfSize, -halfSize, halfSize)
        val frontTopRight = Triple(halfSize, -halfSize, halfSize)
        val frontBottomLeft = Triple(-halfSize, halfSize, halfSize)
        val frontBottomRight = Triple(halfSize, halfSize, halfSize)

        val backTopLeft = Triple(-halfSize, -halfSize, -halfSize)
        val backTopRight = Triple(halfSize, -halfSize, -halfSize)
        val backBottomLeft = Triple(-halfSize, halfSize, -halfSize)
        val backBottomRight = Triple(halfSize, halfSize, -halfSize)

        // Применяем поворот к каждой вершине вокруг оси Y
        val rotatedFrontTopLeft = rotateY2(frontTopLeft, angleY)
        val rotatedFrontTopRight = rotateY2(frontTopRight, angleY)
        val rotatedFrontBottomLeft = rotateY2(frontBottomLeft, angleY)
        val rotatedFrontBottomRight = rotateY2(frontBottomRight, angleY)

        val rotatedBackTopLeft = rotateY2(backTopLeft, angleY)
        val rotatedBackTopRight = rotateY2(backTopRight, angleY)
        val rotatedBackBottomLeft = rotateY2(backBottomLeft, angleY)
        val rotatedBackBottomRight = rotateY2(backBottomRight, angleY)

        // Сместим куб в центр экрана без перспективного эффекта
        val translation = Offset(100f, 100f)
        val finalFrontTopLeft = projectOrthographic(rotatedFrontTopLeft) + translation
        val finalFrontTopRight = projectOrthographic(rotatedFrontTopRight) + translation
        val finalFrontBottomLeft = projectOrthographic(rotatedFrontBottomLeft) + translation
        val finalFrontBottomRight = projectOrthographic(rotatedFrontBottomRight) + translation

        val finalBackTopLeft = projectOrthographic(rotatedBackTopLeft) + translation
        val finalBackTopRight = projectOrthographic(rotatedBackTopRight) + translation
        val finalBackBottomLeft = projectOrthographic(rotatedBackBottomLeft) + translation
        val finalBackBottomRight = projectOrthographic(rotatedBackBottomRight) + translation

        // Рисуем грани куба с разными цветами
        drawEdge(
            finalFrontTopLeft,
            finalFrontTopRight,
            finalFrontBottomRight,
            finalFrontBottomLeft,
            Color.Blue
        ) // Передняя грань
        drawEdge(
            finalBackTopLeft,
            finalBackTopRight,
            finalBackBottomRight,
            finalBackBottomLeft,
            Color.Gray
        ) // Задняя грань
        drawEdge(
            finalFrontTopLeft,
            finalBackTopLeft,
            finalBackBottomLeft,
            finalFrontBottomLeft,
            Color.Red
        ) // Левая грань
        drawEdge(
            finalFrontTopRight,
            finalBackTopRight,
            finalBackBottomRight,
            finalFrontBottomRight,
            Color.Green
        ) // Правая грань
        drawEdge(
            finalFrontTopLeft,
            finalFrontTopRight,
            finalBackTopRight,
            finalBackTopLeft,
            Color.Yellow
        ) // Верхняя грань
        drawEdge(
            finalFrontBottomLeft,
            finalFrontBottomRight,
            finalBackBottomRight,
            finalBackBottomLeft,
            Color.Cyan
        ) // Нижняя грань
    }
}

// Функция для поворота точки вокруг оси Y (включая координату Z)
private fun rotateY2(
    point: Triple<Float, Float, Float>,
    angleY: Float
): Triple<Float, Float, Float> {
    val radiansY = Math.toRadians(angleY.toDouble()).toFloat()

    val x = point.first * cos(radiansY) - point.third * sin(radiansY)
    val z = point.first * sin(radiansY) + point.third * cos(radiansY)
    return Triple(x, point.second, z)
}

// Прямая (ортографическая) проекция 3D координат на 2D
private fun projectOrthographic(point: Triple<Float, Float, Float>): Offset {
    return Offset(point.first, point.second)
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
    drawPath(
        path,
        brush = Brush.linearGradient(listOf(color, color, color)),
        style = Fill
    ) // Заливка грани
}
