package org.like.a.fly.ui.screens

import android.content.pm.ActivityInfo
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.NEXUS_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.like.a.fly.R
import org.like.a.fly.models.DroneState
import org.like.a.fly.ui.composables.*
import org.like.a.fly.ui.theme.LikeAFlyTheme
import org.like.a.fly.ui.tools.HideSystemUi
import org.like.a.fly.ui.tools.LockScreenOrientation
import org.like.a.fly.ui.tools.ViewOrPreview
import org.like.a.fly.ui.viewmodels.MainScreenUiState
import org.like.a.fly.ui.viewmodels.MainScreenViewModel


@Composable
fun VisibilityAndEnability(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit
) {

    Box(modifier = modifier
        .alpha(if (visible) 1f else 0.1f)
        .let {
            return@let if (!visible) {
                it.clickable {}
                it.size(0.dp)
            } else it
        },
    ) {

        content()
    }
}
@Preview(
    showBackground = true,
    device = NEXUS_9)
@Composable
fun MainScreenContent(
    @PreviewParameter(MainViewStateParamProvider::class) state: MainScreenUiState,
    onTBCrosshair: () -> Unit = {},
    onTBMapVisible: () -> Unit = {},
    onTBMapMode: () -> Unit = {},
    onTBController: () -> Unit = {},
    onTBLiftOff: () -> Unit = {},
    onTBLog: () -> Unit = {},
    onTBEnv: () -> Unit = {},
    onTBCameraMode: () -> Unit = {},
    onLeftJoyMoved: (Float, Float) -> Unit = { _, _ -> },
    onRightJoyMoved: (Float, Float) -> Unit = { _, _ -> }
) {


    Box {
        state.dummyBackgroundId?.let {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = it), "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize())
            }
        }


        if (state.dummyBackgroundId == null) {
            VideoView(videoUri = "rtmp://ratss.mooo.com/live/1234")
        }


        VisibilityAndEnability(visible = state.mapVisible) {
            VisibilityAndEnability(visible = state.mapIsScene) {
                ViewOrPreview(previewImageID = R.drawable.preview_map_scene) {
                    OverlaySceneView(
                        location = state.droneState.location(),
                        pitch = state.droneState.pitch,
                        roll = state.droneState.roll,
                        altitude = state.droneState.altitude,
                        heading = state.droneState.heading)
                }
            }
            VisibilityAndEnability(visible = !state.mapIsScene) {
                ViewOrPreview(previewImageID = R.drawable.preview_map) {
                    OverlayMapView(location = state.droneState.location())
                }
            }
        }



        Row (Modifier.height(40.dp)){
            Spacer(modifier = Modifier.width(2.dp))
            LocationGauge(location = state.droneState.location())
            Spacer(modifier = Modifier.width(10.dp))
            Spacer(Modifier.weight(1f))

            HorizontalProgressBarWithPercentLabel(value = state.droneState.battery,modifier = Modifier.height(40.dp))

        }


        Row (modifier = Modifier.align(Alignment.BottomStart)) {
            Column(modifier = Modifier.align(Alignment.Bottom)) {
                var liftOffClicked by remember { mutableStateOf(false) }
                ToolButton(image = painterResource(R.drawable.tb_map_mode), isChecked = state.mapIsScene, onClick = onTBMapMode)
                ToolButton(image= painterResource(R.drawable.tb_map), isChecked = state.mapVisible, onClick = onTBMapVisible)
                ToolButton(image= painterResource(R.drawable.tb_controller), isChecked = state.controllerVisible, onClick = onTBController)
                ToolButton(image= painterResource(R.drawable.tb_liftoff), isChecked = liftOffClicked, onClick = {
                    if (!liftOffClicked) {
                        liftOffClicked = true
                        onTBLiftOff()
                    }
                })
            }
            if (state.controllerVisible) {
                JoystickWidget(moved = onLeftJoyMoved, modifier = Modifier.align(Alignment.Bottom))
            }
        }


        Row (modifier = Modifier.align(Alignment.BottomEnd)) {

            if (state.controllerVisible) {
                JoystickWidget(
                    moved = onRightJoyMoved,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                )
            }
            Column (modifier = Modifier.align(Alignment.Bottom)) {
                ToolButton(image = painterResource(R.drawable.tb_log), onClick = onTBLog)
                ToolButton(image = painterResource(R.drawable.tb_env), onClick = onTBEnv)
                ToolButton(
                    image = painterResource(R.drawable.tb_camera_mode),
                    onClick = onTBCameraMode
                )
            }

        }
    }


}

class MainViewStateParamProvider : PreviewParameterProvider<MainScreenUiState> {
    override val values: Sequence<MainScreenUiState> = sequenceOf(
        MainScreenUiState(R.drawable.preview_aerial, DroneState(), true, true, false, true),
        MainScreenUiState(R.drawable.preview_aerial2, DroneState(), true, true, true, true),
        MainScreenUiState(R.drawable.preview_aerial1, DroneState(), true, true, true, true),
        MainScreenUiState(R.drawable.preview_aerial, DroneState(), true, false, true, true),
        MainScreenUiState(R.drawable.preview_aerial, DroneState(), true, true, true, false),

    )
}


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,

) {
    HideSystemUi()
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    LikeAFlyTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val viewState by viewModel.state.collectAsState()


            MainScreenContent(
                state = viewState,
                onTBCameraMode = viewModel::onTBCameraMode,
                onTBController = viewModel::onTBController,
                onTBCrosshair = viewModel::onTBCrossHair,
                onTBMapVisible = viewModel::onTBMapVisible,
                onTBMapMode = viewModel::onTBMapMode,

                onTBLiftOff = viewModel::onTBLiftOff,
                onTBEnv = viewModel::onTBEnv,
                onTBLog = viewModel::onTBLog,
                onLeftJoyMoved = viewModel::onLeftJoyMoved,
                onRightJoyMoved = viewModel::onRightJoyMoved
                )
        }
    }


}
