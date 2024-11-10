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
import androidx.compose.ui.unit.dp

@Composable
fun WireframeCube() {
    Canvas(modifier = Modifier.size(200.dp)) {
        // Определяем координаты вершин куба в "изометрической проекции"
        val size = 200f
        val offsetX = size / 2
        val offsetY = size / 2


        val brush = Brush.linearGradient(listOf(Color.Black, Color.Black, Color.Black))
        // Координаты вершин
        val frontTopLeft = Offset(offsetX, offsetY)
        val frontTopRight = Offset(offsetX + size, offsetY)
        val frontBottomLeft = Offset(offsetX, offsetY + size)
        val frontBottomRight = Offset(offsetX + size, offsetY + size)

        val backTopLeft = Offset(offsetX + size / 2, offsetY - size / 2)
        val backTopRight = Offset(offsetX + size * 1.5f, offsetY - size / 2)
        val backBottomLeft = Offset(offsetX + size / 2, offsetY + size / 2)
        val backBottomRight = Offset(offsetX + size * 1.5f, offsetY + size / 2)

        // Рисуем грани куба
        drawEdge(frontTopLeft, frontTopRight, frontBottomRight, frontBottomLeft, Color.Blue)
        drawEdge(backTopLeft, backTopRight, backBottomRight, backBottomLeft, Color.Gray)

        // Рисуем линии, соединяющие переднюю и заднюю части куба
        drawLine(
            brush = brush,
            start = frontTopLeft,
            end = backTopLeft,
            strokeWidth = 2f
        )
        drawLine(
            brush = brush,
            start = frontTopRight,
            end = backTopRight,
            strokeWidth = 2f
        )
        drawLine(
            brush = brush,
            start = frontBottomLeft,
            end = backBottomLeft,
            strokeWidth = 2f
        )
        drawLine(
            brush = brush,
            start = frontBottomRight,
            end = backBottomRight,
            strokeWidth = 2f
        )
    }
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
        brush = Brush.linearGradient(listOf(Color.Black, Color.Black, Color.Black)),
        style = Stroke(width = 2F)
    )
}
