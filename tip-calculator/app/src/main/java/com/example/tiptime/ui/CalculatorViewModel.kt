package com.example.tiptime.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel :
    ViewModel() {
    private val _uiState = MutableStateFlow(NumericKeypadState())

    val uiState: StateFlow<NumericKeypadState> = _uiState.asStateFlow()

    fun onAction(action: NumericKeypadAction) {
        when (action) {
            is NumericKeypadAction.Number -> enterNumber(action.number)
            is NumericKeypadAction.Delete -> delete()
            is NumericKeypadAction.Clear -> clear()
            is NumericKeypadAction.Decimal -> enterDecimal()
            is NumericKeypadAction.Done -> save()
        }
    }

    private fun save() {
        val number = _uiState.value.number.toDoubleOrNull()

        if (number != null) {
            _uiState.update {
                it.copy(
                    number = number.toString()
                )
            }
        }
    }

    private fun delete() {
        if (_uiState.value.number.isNotBlank()) {
            _uiState.update {
                it.copy(
                    number = it.number.dropLast(1)
                )
            }
        }
    }

    private fun enterDecimal() {
        if (!_uiState.value.number.contains(".") && _uiState.value.number.isNotBlank()) {
            _uiState.update {
                it.copy(
                    number = it.number + "."
                )
            }
        }
    }

    private fun enterNumber(number: Int) {
        if (uiState.value.number.length >= MAX_NUM_LENGTH) {
            return
        }
        _uiState.update {
            it.copy(
                number = it.number + number
            )
        }
    }

    private fun clear() {
        _uiState.update {
            it.copy(
                number = "0"
            )
        }
    }

    companion object {

        private const val MAX_NUM_LENGTH = 8

    }
}
