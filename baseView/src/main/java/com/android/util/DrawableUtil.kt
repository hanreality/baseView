package com.android.util

/**
 * Author: han.chen
 * Time: 2022/1/26 23:40
 */
object DrawableUtil {

    @JvmStatic
    val STATE_PRESSED =
        intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)

    @JvmStatic
    val STATE_SELECTED =
        intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled)

    @JvmStatic
    val STATE_DISABLED =
        intArrayOf(-android.R.attr.state_enabled)

    @JvmStatic
    val STATE_EMPTY =
        intArrayOf()
}