package org.like.a.fly.ui.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import org.like.a.fly.MainActivity

@Composable
fun ViewOrPreview(modifier: Modifier = Modifier,
                  previewImageID: Int,
                  content: @Composable () -> Unit
) {
    if (LocalContext.current.findActivity() is MainActivity) {
        content()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = previewImageID), "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize())
        }
    }
}