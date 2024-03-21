package com.example.tiptime.ui

sealed class NumericKeypadAction {
    data class Number(val number: Int) : NumericKeypadAction()
    data object Clear : NumericKeypadAction()
    data object Delete : NumericKeypadAction()
    data object Done : NumericKeypadAction()
    data object Decimal : NumericKeypadAction()
}