package com.example.routes.colorPicker.pickerTheme

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import com.example.routes.R

@Composable
fun ColorPickerDemoThemeOther(
    darkTheme: Boolean = isSystemInDarkTheme(),
    //darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = colorResource(R.color.dark_primary),
            primaryVariant = colorResource(R.color.dark_primary_variant),
            secondary = colorResource(R.color.dark_secondary)
        )
    } else {
        lightColors(
            primary = colorResource(R.color.light_primary),
            primaryVariant = colorResource(R.color.light_primary_variant),
            secondary = colorResource(R.color.light_secondary),


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
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}