package site.ikosmov.screenshottest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.ikosmov.screenshottest.ui.theme.ScreenshotTestTheme

class ExamplePreviewsScreenshots {

    @Preview(showBackground = true, device = Devices.NEXUS_6)
    @Composable
    fun GreetingPreview() {
        ProvideScreenshotTest {
            ScreenshotTestTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(
                                tag = "TAG",
                                sign = TestTagScreenshotSign(
                                    signTextName = "11",
                                    borderWidth = 4.dp,
                                    signTextAlignment = Alignment.CenterStart
                                )
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(5) {
                            Greeting(
                                name = "Android!",
                                modifier = Modifier.testTag(
                                    tag = "TAG",
                                    sign = TestTagScreenshotSign(
                                        signTextName = it.toString()
                                    )
                                )
                            )
                        }
                    }


                }
            }
        }
    }
}