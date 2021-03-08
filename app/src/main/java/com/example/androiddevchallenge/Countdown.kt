/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@ExperimentalComposeUiApi
@Composable
fun CounterControls(
    time: Long,
    percentage: Float,
    enabled: Boolean,
    action: (a: NumAction) -> Unit
) {
    val innerTime = fromLong(time)
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black)
    ) {
        val (circular, digits, buttons) = createRefs()
        CircularProgress(
            percentage,
            Modifier.constrainAs(circular) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
        )
        DigitRow(
            min = innerTime.minute,
            secDigitOne = innerTime.secondTens,
            secDigitSecond = innerTime.second,
            enabled = enabled,
            action = action,
            Modifier
                .constrainAs(digits) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(circular.bottom)
                    bottom.linkTo(buttons.bottom)
                }
                .padding(bottom = 15.dp)
        )
        Row(
            Modifier
                .constrainAs(buttons) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 15.dp)
        ) {
            Button(Icons.Filled.PlayArrow) { action(NumAction.START) }
            // Button(Icons.Filled.Stop) { action(NumAction.STOP) }
        }
    }
}

@Composable
fun DigitRow(
    min: Int,
    secDigitOne: Int,
    secDigitSecond: Int,
    enabled: Boolean,
    action: (a: NumAction) -> Unit,
    modifier: Modifier
) {
    Row(modifier) {
        DigitSetter(
            min,
            enabled,
            { action(NumAction.MINUTE_UP) },
            { action(NumAction.MINUTE_DOWN) }
        )
        Text(
            text = ":",
            modifier = Modifier.padding(top = 45.dp),
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        DigitSetter(
            secDigitOne,
            enabled,
            { action(NumAction.SECOND_TENS_UP) },
            { action(NumAction.SECOND_TENS_DOWN) }
        )
        DigitSetter(
            secDigitSecond,
            enabled,
            { action(NumAction.SECOND_UP) },
            { action(NumAction.SECOND_DOWN) }
        )
    }
}

@Composable
fun DigitSetter(digit: Int, enabled: Boolean, onUp: () -> Unit, onDown: () -> Unit) {
    Column {
        ArrowIcon(onClick = onUp, true, enabled)
        Number(digit)
        ArrowIcon(onClick = onDown, false, enabled)
    }
}

@Composable
fun Number(number: Int) {
    Text(
        text = number.toString(),
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier
            .size(40.dp)
            .padding(start = 19.dp, top = 10.dp)
    )
}

@Composable
fun ArrowIcon(onClick: () -> Unit, isUp: Boolean, enabled: Boolean) {
    IconButton(
        onClick = { onClick() },
        enabled = enabled,
    ) {
        Icon(
            imageVector = if (isUp) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "subtract or add a minute",
            tint = Color.White
        )
    }
}

@Composable
fun Button(imageVector: ImageVector, action: () -> Unit) {
    Button(
        onClick = action,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        elevation = ButtonDefaults.elevation(defaultElevation = 8.dp),
        enabled = true,
        modifier = Modifier.size(45.dp),
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = imageVector,
            contentDescription = null
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun CircularProgress(percentage: Float, modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.6f)
            .size(200.dp)
    ) {
        val (progressBar1, progressBar2) = createRefs()
        CircularProgressIndicator(
            modifier = Modifier
                .size(200.dp)
                .constrainAs(progressBar1) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            progress = 1f,
            strokeWidth = 3.dp,
            color = Color(android.graphics.Color.parseColor("#FF6200EE"))
        )
        CircularProgressIndicator(
            modifier = Modifier
                .size(180.dp)
                .constrainAs(progressBar2) {
                    top.linkTo(progressBar1.top)
                    bottom.linkTo(progressBar1.bottom)
                    start.linkTo(progressBar1.start)
                    end.linkTo(progressBar1.end)
                }
                .animateContentSize(),
            progress = percentage,
            strokeWidth = 6.dp,
            color = Color(android.graphics.Color.parseColor("#FF6200EE"))
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun CountdownPreview() {
    // DigitRow(min = 0, secDigitOne = 1, secDigitSecond = 5)
    // ArrowIcon(onClick = { })
    // DigitSetter(digit = 1, onUp = { }, onDown = { })
    // DigitRow(min = 1, secDigitOne = 5, secDigitSecond = 6, {})
    // CounterControls(TimerTime(), {})
    CounterControls(1000, 80F, true, {})
}
