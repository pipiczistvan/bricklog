package hu.piware.bricklog.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.util.DevLevels
import kotlin.math.sqrt

fun Modifier.drawDevLevelRibbon() = composed(
    factory = {
        Modifier
            .let {
                when (BuildKonfig.DEV_LEVEL) {
                    DevLevels.DEVELOPMENT -> it.drawDiagonalLabel(
                        text = "DEV",
                        color = Color.Blue.copy(alpha = 0.5f),
                    )

                    DevLevels.MOCK -> it.drawDiagonalLabel(
                        text = "MOCK",
                        color = Color.Red.copy(alpha = 0.5f),
                    )

                    DevLevels.BENCHMARK -> it.drawDiagonalLabel(
                        text = "BENCHMARK",
                        color = Color.Green.copy(alpha = 0.5f),
                    )

                    else -> it
                }
            }
    },
)

// https://stackoverflow.com/a/78153809
fun Modifier.drawDiagonalLabel(
    text: String,
    color: Color,
    labelTextRatio: Float = 3f,
    style: TextStyle? = null,
) = composed(
    factory = {
        val textMeasurer = rememberTextMeasurer()
        val currentStyle: TextStyle = style ?: TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )

        val textLayoutResult: TextLayoutResult = remember {
            textMeasurer.measure(text = AnnotatedString(text), style = currentStyle)
        }

        Modifier
            .clipToBounds()
            .drawWithContent {
                val canvasWidth = size.width

                val textSize = textLayoutResult.size
                val textWidth = textSize.width
                val textHeight = textSize.height

                val rectWidth = textWidth * labelTextRatio
                val rectHeight = textHeight * 1.1f

                val rect = Rect(
                    offset = Offset(canvasWidth - rectWidth, 0f),
                    size = Size(rectWidth, rectHeight),
                )

                val sqrt = sqrt(rectWidth / 2f)
                val translatePos = sqrt * sqrt

                drawContent()
                withTransform(
                    {
                        rotate(
                            degrees = 45f,
                            pivot = Offset(
                                canvasWidth - rectWidth / 2,
                                translatePos,
                            ),
                        )
                    },
                ) {
                    drawRect(
                        color = color,
                        topLeft = rect.topLeft,
                        size = rect.size,
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = currentStyle,
                        topLeft = Offset(
                            rect.left + (rectWidth - textWidth) / 2f,
                            rect.top + (rect.bottom - textHeight) / 2f,
                        ),
                    )
                }
            }
    },
)
