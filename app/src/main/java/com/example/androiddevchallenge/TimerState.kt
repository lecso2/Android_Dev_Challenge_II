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

enum class TimerState {
    TICKING,
    IDLE
}

enum class NumAction {
    MINUTE_UP, MINUTE_DOWN, SECOND_TENS_UP, SECOND_TENS_DOWN, SECOND_UP, SECOND_DOWN, START, STOP
}

class TimerTime {

    fun minus(): TimerTime? {
        when {
            second > 0 -> second--
            secondTens > 0 -> {
                secondTens--
                second = 9
            }
            minute > 0 -> {
                minute--
                secondTens = 5
                second = 9
            }
            else -> return null
        }
        return this
    }

    fun isDone() = minute == 0 && secondTens == 0 && second == 0

    fun toLong(): Long {
        return (minute * 1000 + secondTens * 100 + second).toLong()
    }

    var minute = 0
    var secondTens = 0
    var second = 0
}

fun fromLong(time: Long): TimerTime {
    var time2 = time
    val t = TimerTime()
    t.minute = (time / 1000).toInt()
    time2 -= t.minute * 1000
    t.secondTens = (time2 / 100).toInt()
    time2 -= t.secondTens * 100
    t.second = time2.toInt()
    return t
}
