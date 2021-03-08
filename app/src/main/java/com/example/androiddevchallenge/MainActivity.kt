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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.ObsoleteCoroutinesApi

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CountdownViewModel>()

    @ObsoleteCoroutinesApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
    }
}

@ObsoleteCoroutinesApi
@ExperimentalComposeUiApi
@Composable
fun MyApp(viewModel: CountdownViewModel) {
    val timerMinute: Long by viewModel.liveTime.observeAsState(0L)
    val state: TimerState by viewModel.state.observeAsState(initial = TimerState.IDLE)
    val percentage: Float by viewModel.percentage.observeAsState(0F)

    CounterControls(timerMinute, percentage, state == TimerState.IDLE) { viewModel.keyPressed(it) }
}
