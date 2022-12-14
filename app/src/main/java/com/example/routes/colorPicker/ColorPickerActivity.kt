package com.example.routes.colorPicker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routes.R
import com.example.routes.colorPicker.pickerTheme.Shapes
import com.example.routes.colorPicker.pickerTheme.Typography
import com.github.skydoves.colorpicker.compose.*

class ColorPickerActivity : ComponentActivity() {
    companion object {
        const val RESULT_COLOR_NAME = "color_name"
        const val RESULT_COLOR_VALUE = "color_value"

        const val INPUT_COLOR_NAME_PLACEHOLDER = "color_name"
        const val INPUT_COLOR_VALUE = "color_value"

        fun newInstance(context: Context) = Intent(context, ColorPickerActivity::class.java)
    }

    private lateinit var colorName: String
    private lateinit var colorValue: String

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        colorName = intent.getStringExtra(INPUT_COLOR_NAME_PLACEHOLDER) ?: ""
        colorValue = intent.getStringExtra(INPUT_COLOR_VALUE) ?: "#ffffff"

        setContent {
            ColorPickerDemoTheme {
                colorPicker()
            }
        }
    }

    @Preview
    @Composable
    @ExperimentalComposeUiApi
    fun colorPicker(){
        val controller = rememberColorPickerController()
        var colorName by remember { mutableStateOf(colorName) }
        var colorValue = colorValue

        val keyboardController = LocalSoftwareKeyboardController.current
        val localFocusManager = LocalFocusManager.current
        val focusRequester = FocusRequester()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .focusable(enabled = true)
                    .background(MaterialTheme.colors.background)
            ) {
                // TODO: make constraint layout here
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 30.dp)
                        .focusable(enabled = true)
                        //.verticalScroll(rememberScrollState())
                ) {
                    CompositionLocalProvider(
                        LocalRippleTheme provides ClearRippleTheme
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = true) {
                                    localFocusManager.clearFocus()
                                }
                                .wrapContentHeight()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AlphaTile(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    controller = controller
                                )
                            }
                            BrightnessSlider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .height(35.dp),
                                controller = controller,
                            )
                            HsvColorPicker(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                                    .padding(10.dp),
                                controller = controller,
                                onColorChanged = {
                                    colorValue = "#" + it.hexCode
                                    localFocusManager.clearFocus()
                                }
                            )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .focusRequester(focusRequester),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = colorName,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colors.secondary,
                                    textColor = MaterialTheme.colors.primaryVariant,
                                    focusedLabelColor = MaterialTheme.colors.secondary,
                                    cursorColor = MaterialTheme.colors.secondary
                                ),
                                onValueChange = {
                                    var resultString = it
                                    //val regex = Regex("[^A-Za-z0-9_ -]")
                                    //resultString = regex.replace(resultString, "")
                                    colorName = resultString
                                },
                                label = { Text("Color name") },
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        if (!it.isFocused) keyboardController?.hide()
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                            onClick = {
                                returnResult(colorName, colorValue)
                            }) { Text(text = "add") }
                    }
                }
            }
    }

    object ClearRippleTheme : RippleTheme {
        @Composable
        override fun defaultColor(): Color = Color.Transparent

        @Composable
        override fun rippleAlpha() = RippleAlpha(
            draggedAlpha = 0.0f,
            focusedAlpha = 0.0f,
            hoveredAlpha = 0.0f,
            pressedAlpha = 0.0f,
        )
    }

    @Composable
    fun ColorPickerDemoTheme(content: @Composable () -> Unit) {
        var sharedPreferences: SharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("nightMode", false)
        val colors = if (nightMode) {
            darkColors(
                background = colorResource(R.color.bg_color_secondary_dark),

                primary = colorResource(R.color.bg_color_primary_dark),
                primaryVariant = colorResource(R.color.text_color_primary_dark),
                secondary = colorResource(R.color.color_accent_primary_dark)
            )
        } else {
            lightColors(
                background = colorResource(R.color.bg_color_secondary_light),

                primary = colorResource(R.color.bg_color_primary_light),
                primaryVariant = colorResource(R.color.text_color_primary_light),
                secondary = colorResource(R.color.color_accent_primary_light)


                /* Other default colors to override
                background = Color.White,
                surface = Color.White,
                onPrimary = Color.White,
                onSecondary = Color.Black,
                onBackground = Color.Black,
                onSurface = Color.Black,
                */
            )
        }

        MaterialTheme(
            colors = colors,
            content = content
        )
    }

    private fun returnResult(colorName: String, colorValue: String) {
        val data = Intent().apply {
            var resultColorName =
                if (colorName == "") colorValue
                else colorName
            putExtra(RESULT_COLOR_NAME, resultColorName)
            putExtra(RESULT_COLOR_VALUE, colorValue)
        }
        setResult(RESULT_OK, data)
        finish()
    }
}
