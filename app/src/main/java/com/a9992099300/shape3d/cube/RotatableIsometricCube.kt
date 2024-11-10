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
fun RotatableIsometricCube(
    angleY: Float,
    sizeCanvas: Dp = 100.dp,
    sizeCube: Float = 200f,
) {
    Canvas(modifier = Modifier.size(sizeCanvas)) {
        // Размер куба
        val size = sizeCube
        val halfSize = size / 2

        // Исходные координаты вершин (X, Y, Z) для куба
        val frontTopLeft = Triple(-halfSize, -halfSize, halfSize)
        val frontTopRight = Triple(halfSize, -halfSize, halfSize)
        val frontBottomLeft = Triple(-halfSize, halfSize, halfSize)
        val frontBottomRight = Triple(halfSize, halfSize, halfSize)

        val backTopLeft = Triple(-halfSize, -halfSize, -halfSize)
        val backTopRight = Triple(halfSize, -halfSize, -halfSize)
        val backBottomLeft = Triple(-halfSize, halfSize, -halfSize)
        val backBottomRight = Triple(halfSize, halfSize, -halfSize)

        // Применяем поворот вокруг оси Y к каждой вершине
        val rotatedFrontTopLeft = rotateY(frontTopLeft, angleY)
        val rotatedFrontTopRight = rotateY(frontTopRight, angleY)
        val rotatedFrontBottomLeft = rotateY(frontBottomLeft, angleY)
        val rotatedFrontBottomRight = rotateY(frontBottomRight, angleY)

        val rotatedBackTopLeft = rotateY(backTopLeft, angleY)
        val rotatedBackTopRight = rotateY(backTopRight, angleY)
        val rotatedBackBottomLeft = rotateY(backBottomLeft, angleY)
        val rotatedBackBottomRight = rotateY(backBottomRight, angleY)

        // Применяем изометрическую проекцию к каждой повернутой вершине
        val projectedFrontTopLeft = projectIsometric(rotatedFrontTopLeft)
        val projectedFrontTopRight = projectIsometric(rotatedFrontTopRight)
        val projectedFrontBottomLeft = projectIsometric(rotatedFrontBottomLeft)
        val projectedFrontBottomRight = projectIsometric(rotatedFrontBottomRight)

        val projectedBackTopLeft = projectIsometric(rotatedBackTopLeft)
        val projectedBackTopRight = projectIsometric(rotatedBackTopRight)
        val projectedBackBottomLeft = projectIsometric(rotatedBackBottomLeft)
        val projectedBackBottomRight = projectIsometric(rotatedBackBottomRight)

        // Смещаем куб в центр экрана
        val translation = Offset(100f, 100f)
        val finalFrontTopLeft = projectedFrontTopLeft + translation
        val finalFrontTopRight = projectedFrontTopRight + translation
        val finalFrontBottomLeft = projectedFrontBottomLeft + translation
        val finalFrontBottomRight = projectedFrontBottomRight + translation

        val finalBackTopLeft = projectedBackTopLeft + translation
        val finalBackTopRight = projectedBackTopRight + translation
        val finalBackBottomLeft = projectedBackBottomLeft + translation
        val finalBackBottomRight = projectedBackBottomRight + translation

        val faces = listOf(
            Face(
                finalFrontTopLeft,
                finalFrontTopRight,
                finalFrontBottomRight,
                finalFrontBottomLeft,
                listOf(Color.Red, Color.Blue),
                rotatedFrontTopLeft.third
            ),
            Face(
                finalBackTopLeft,
                finalBackTopRight,
                finalBackBottomRight,
                finalBackBottomLeft,
                listOf(Color.Red, Color.Blue),
                rotatedBackTopLeft.third
            ),
            Face(
                finalFrontTopLeft,
                finalBackTopLeft,
                finalBackBottomLeft,
                finalFrontBottomLeft,
                listOf(Color.Red, Color.Blue),
                (rotatedFrontTopLeft.third + rotatedBackTopLeft.third) / 2
            ),
            Face(
                finalFrontTopRight,
                finalBackTopRight,
                finalBackBottomRight,
                finalFrontBottomRight,
                listOf(Color.Red, Color.Blue),
                (rotatedFrontTopRight.third + rotatedBackTopRight.third) / 2
            ),
            Face(
                finalFrontTopLeft,
                finalFrontTopRight,
                finalBackTopRight,
                finalBackTopLeft,
                listOf(Color.Red, Color.Magenta),
                (rotatedFrontTopLeft.third + rotatedBackTopLeft.third) / 2
            ),
            //  Face(finalFrontBottomLeft, finalFrontBottomRight, finalBackBottomRight, finalBackBottomLeft, Color.Cyan, (rotatedFrontBottomLeft.third + rotatedBackBottomLeft.third) / 2)
        )

        // Сортируем грани по глубине и рисуем от самой дальней до самой ближней
        faces.sortedBy { it.depth }.forEach { face ->
            drawEdge(face.topLeft, face.topRight, face.bottomRight, face.bottomLeft, face.color)
        }
    }
}

// Структура для хранения информации о грани
data class Face(
    val topLeft: Offset,
    val topRight: Offset,
    val bottomRight: Offset,
    val bottomLeft: Offset,
    val color: List<Color>,
    val depth: Float
)

//
// Функция для поворота точки вокруг оси Y (включая координату Z)
private fun rotateY(
    point: Triple<Float, Float, Float>,
    angleY: Float
): Triple<Float, Float, Float> {
    val radiansY = Math.toRadians(angleY.toDouble()).toFloat()

    val x = point.first * cos(radiansY) + point.third * sin(radiansY)
    val z = -point.first * sin(radiansY) + point.third * cos(radiansY)
    return Triple(x, point.second, z)
}

//Функция для изометрической проекции
fun projectIsometric(point: Triple<Float, Float, Float>): Offset {
    val angleX = Math.toRadians(-30.0).toFloat() // 30 градусов для оси X
    val angleY = Math.toRadians(0.0).toFloat() // 0 градусов для оси Y

    val x = point.first * cos(angleY) - point.third * sin(angleY)
    val y = point.second * cos(angleX) - point.third * sin(angleX)
    return Offset(x, y)
}

fun projectIsometricPoint(point: Triple<Float, Float, Float>): Offset {
    val angleX = Math.toRadians(-30.0).toFloat() // 30 градусов для оси X
    val angleY = Math.toRadians(0.0).toFloat() // 0 градусов для оси Y

    val x = point.first * cos(angleY) - point.third * sin(angleY)
    val y = point.second * cos(angleX) - point.third * sin(angleX)
    return Offset(x, y)
}

// Функция для рисования одной грани куба
private fun DrawScope.drawEdge(
    topLeft: Offset,
    topRight: Offset,
    bottomRight: Offset,
    bottomLeft: Offset,
    color: List<Color>
) {
    val path = Path().apply {
        moveTo(topLeft.x, topLeft.y)
        lineTo(topRight.x, topRight.y)
        lineTo(bottomRight.x, bottomRight.y)
        lineTo(bottomLeft.x, bottomLeft.y)
        close()
    }
    drawPath(
        path, brush = Brush.linearGradient(color),
        style = Fill
        //  style = Stroke(width = 2.0f)
    ) // Изменяем на заливку грани
}


// Функция для рисования одной грани куба
private fun DrawScope.drawPoint(
    topLeft: Offset,
    topRight: Offset,
    bottomRight: Offset,
    bottomLeft: Offset,
    color: List<Color>
) {
    val path = Path().apply {
        moveTo(topLeft.x, topLeft.y)
        lineTo(topRight.x, topRight.y)
        lineTo(bottomRight.x, bottomRight.y)
        lineTo(bottomLeft.x, bottomLeft.y)
        close()
    }
    drawPath(
        path, brush = Brush.linearGradient(color),
        style = Fill
        //  style = Stroke(width = 2.0f)
    ) // Изменяем на заливку грани
}
