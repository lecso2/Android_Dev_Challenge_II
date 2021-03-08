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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class CountdownViewModel : ViewModel() {

    private val timerTime = TimerTime()

    val percentage: MutableLiveData<Float> = MutableLiveData(0F)
    val liveTime: MutableLiveData<Long> = MutableLiveData(0L)
    val state: MutableLiveData<TimerState> = MutableLiveData(TimerState.IDLE)

    @ObsoleteCoroutinesApi
    private fun startTimer() {
        val tickerChannel = ticker(delayMillis = 1_000, initialDelayMillis = 0)
        val fullTime = timerTime.minute * 60F + timerTime.secondTens * 10F + timerTime.second
        var counter = 0

        CoroutineScope(Dispatchers.Main).launch {
            state.postValue(TimerState.TICKING)
            for (event in tickerChannel) {
                timerTime.minusSecond()
                liveTime.postValue(timerTime.toLong())
                percentage.postValue((counter + 1) / fullTime)

                if (timerTime.isDone()) {
                    state.postValue(TimerState.IDLE)
                    break
                }
                counter++
            }
            tickerChannel.cancel()
        }
    }

    private fun stopTimer() {
    }

    @ObsoleteCoroutinesApi
    fun keyPressed(it: NumAction) {
        timerTime.let { t ->
            when (it) {
                NumAction.MINUTE_UP -> t.plusMinute()
                NumAction.MINUTE_DOWN -> t.minusMinute()
                NumAction.SECOND_TENS_UP -> t.plusTens()
                NumAction.SECOND_TENS_DOWN -> t.minusTens()
                NumAction.SECOND_UP -> t.plusSecond()
                NumAction.SECOND_DOWN -> t.minusSecond()
                NumAction.STOP -> stopTimer()
                NumAction.START -> startTimer()
            }
        }
        liveTime.postValue(timerTime.toLong())
    }
}
