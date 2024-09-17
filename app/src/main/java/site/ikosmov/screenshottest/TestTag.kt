package site.ikosmov.screenshottest

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LocalIsScreenshotTest = staticCompositionLocalOf { false }

@Composable
fun ProvideScreenshotTest(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalIsScreenshotTest provides true, content)
}

/**
 * Add test tag based on string resource
 *
 * @param id - the resource identifier
 */
@Stable
fun Modifier.testTag(
    sign: TestTagScreenshotSign? = null,
    @StringRes id: Int
) = composed {
    val tag = stringResource(id = id)
    semantics(
        properties = {
            testTag = tag
        }
    ).drawTestTagSign(sign)
}

/**
 * Add test tag based on string resource
 *
 * @param id - the resource identifier
 * @param formatArgs - the format arguments
 */
@Stable
fun Modifier.testTag(
    sign: TestTagScreenshotSign? = null,
    @StringRes id: Int,
    vararg formatArgs: Any
) = composed {
    val tag = stringResource(
        id = id,
        formatArgs
    )
    semantics(
        properties = {
            testTag = tag
        }
    ).drawTestTagSign(sign)
}

/**
 * Add test tag based on string
 *
 * @param tag - the tag
 */
@Stable
fun Modifier.testTag(
    tag: String?,
    sign: TestTagScreenshotSign? = null
) = composed {
    semantics(properties = { tag?.let { testTag = it } }).drawTestTagSign(sign)
}


@Stable
private fun Modifier.drawTestTagSign(
    sign: TestTagScreenshotSign?
) = composed {
    val isScreenshotTest = LocalIsScreenshotTest.current
    if (sign == null || isScreenshotTest.not()) return@composed this
    val textMeasurer = rememberTextMeasurer()
    val textToDraw = sign.signTextName
    val style = TextStyle(
        fontSize = 25.sp,
        color = Color.Magenta
    )
    val textLayoutResult = remember(textToDraw) {
        textMeasurer.measure(textToDraw, style)
    }
    Modifier.drawWithContent {
        val borderWidth = sign.borderWidth.toPx()
        val totalBorderWidth = size.width + borderWidth * 2
        val totalBorderHeight = size.height + borderWidth * 2
        val borderOffset = Offset(-borderWidth, -borderWidth)
        val totalBorderSize = Size(totalBorderWidth, totalBorderHeight)


        val signWidth = textLayoutResult.size.width
        val signHeight = textLayoutResult.size.height
        val signTextTopLeft = when (sign.signTextAlignment) {
            Alignment.TopStart -> Offset(0f, 0f)
            Alignment.Top,
            Alignment.TopCenter -> Offset(center.x - signWidth / 2, 0f)

            Alignment.TopEnd -> Offset(size.width - signWidth, 0f)
            Alignment.Start,
            Alignment.CenterStart -> Offset(0f, center.y - signHeight / 2)

            Alignment.CenterVertically,
            Alignment.CenterHorizontally,
            Alignment.Center -> Offset(center.x - signWidth / 2, center.y - signHeight / 2)

            Alignment.End,
            Alignment.CenterEnd -> Offset(size.width - signWidth, center.y - signHeight / 2)

            Alignment.BottomStart -> Offset(0f, size.height - signHeight)
            Alignment.Bottom,
            Alignment.BottomCenter -> Offset(center.x - signWidth / 2, size.height - signHeight)

            Alignment.BottomEnd -> Offset(size.width - signWidth, size.height - signHeight)
            else -> Offset(0f, 0f)
        }
        drawContent()
        drawRect(
            color = sign.color,
            topLeft = borderOffset,
            size = totalBorderSize,
            style = Stroke(width = borderWidth)
        )
        drawText(
            textLayoutResult = textLayoutResult,
            color = sign.color,
            topLeft = signTextTopLeft
        )
    }
}

@Stable
data class TestTagScreenshotSign(
    val signTextName: String,
    val borderWidth: Dp = 4.dp,
    val signTextAlignment: Alignment = Alignment.Center,
    val color: Color = Color.Magenta
)