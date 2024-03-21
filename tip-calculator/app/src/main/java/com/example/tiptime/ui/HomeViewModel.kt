package com.example.tiptime.ui

import android.icu.util.TimeZone
import androidx.annotation.VisibleForTesting
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiptime.OkHttpRequester
import com.example.tiptime.data.Country
import com.example.tiptime.data.TimeZoneJsonAdapter
import com.example.tiptime.data.countries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

class HomeViewModel : ViewModel() {
    // Home Screen UI state
    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private fun getUnformattedValue(value: String): String {
        return value.filter { it.isDigit() || it == '.' }
    }

    fun onKeypadAction(action: NumericKeypadAction) {

        when (action) {
            is NumericKeypadAction.Number -> updateBillAmount(action.number.toString())
            is NumericKeypadAction.Delete -> delete()
            is NumericKeypadAction.Clear -> clear()
            is NumericKeypadAction.Decimal -> enterDecimal()
            is NumericKeypadAction.Done -> updateNumOfPeople(_uiState.value.numOfPeople)
        }
    }

    private fun clear() {
        _uiState.update {
            it.copy(
                billAmount = NumberFormat.getCurrencyInstance().format(0),
                totalTip = NumberFormat.getCurrencyInstance().format(0),
                tipPerPerson = NumberFormat.getCurrencyInstance().format(0),
                totalPerPerson = NumberFormat.getCurrencyInstance().format(0),
                totalBillAmount = NumberFormat.getCurrencyInstance().format(0),
                numOfPeople = "1"
            )
        }
    }

    private fun delete() {
        val unformattedBillAmount = getUnformattedValue(_uiState.value.billAmount)

        if (unformattedBillAmount != "0.00") {
            val newBillAmount = (unformattedBillAmount.toDouble() / 10).toString()

            val tip = calculateTip(
                billAmount = newBillAmount
            )

            var tipPerPerson = _uiState.value.tipPerPerson

            if (tipPerPerson.isNotEmpty() && tipPerPerson != "1") {

                tipPerPerson = calculateTipPerPerson(
                    totalTip = tip, numOfPeople = _uiState.value.numOfPeople
                )

            }
            _uiState.update {
                it.copy(
                    billAmount = NumberFormat.getCurrencyInstance()
                        .format(newBillAmount.toDouble()),
                    totalTip = tip,
                    tipPerPerson = tipPerPerson
                )
            }
        }
    }

    private fun enterDecimal() {
        if (!_uiState.value.billAmount.contains(".") && _uiState.value.billAmount.isNotBlank()) {
            _uiState.update {
                it.copy(
                    billAmount = it.billAmount + "."
                )
            }
        }
    }

    private fun updateBillAmount(amountString: String) {
        if (getUnformattedValue(_uiState.value.billAmount).length >= MAX_NUM_LENGTH) {
            return
        }

        val amount = when (getUnformattedValue(_uiState.value.billAmount)) {
            "0.00" -> (amountString.toDouble() / 100).toString()
            else -> ((getUnformattedValue(_uiState.value.billAmount) + amountString).toDouble() * 10).toString()
        }

        if (amount.filter { it == '.' }.length == 1 || amount.isDigitsOnly()) {

            val tip = calculateTip(
                billAmount = when {
                    amount.isEmpty() -> "0"
                    amount.startsWith(".") -> "0$amount"
                    else -> amount
                }
            )

            var tipPerPerson = _uiState.value.tipPerPerson

            if (tipPerPerson.isNotEmpty() && tipPerPerson != "1") {

                tipPerPerson = calculateTipPerPerson(
                    totalTip = tip, numOfPeople = _uiState.value.numOfPeople
                )

            }

            val formattedAmount = NumberFormat.getCurrencyInstance().format(amount.toDouble())

            _uiState.update {
                it.copy(billAmount = formattedAmount, totalTip = tip, tipPerPerson = tipPerPerson)
            }
        }
    }

    private fun calculateTip(
        billAmount: String = _uiState.value.billAmount,
        tipPercentage: String = _uiState.value.tipPercent,
        roundUpBill: Boolean = _uiState.value.roundUpBill,
        roundDownBill: Boolean = _uiState.value.roundDownBill,
        roundUpTip: Boolean = _uiState.value.roundUpTip,
        roundDownTip: Boolean = _uiState.value.roundDownTip,
        taxOnTip: Boolean = _uiState.value.taxOnTip,
        salesTax: String = _uiState.value.salesTax

    ): String {
        val billAmountDouble = when (billAmount.isEmpty()) {
            true -> "0"
            else -> getUnformattedValue(billAmount)
        }.toDouble()
        val tipPercentageDouble = when (tipPercentage.isEmpty()) {
            true -> "0"
            else -> tipPercentage
        }.toDouble()
        val salesTaxDouble = when (salesTax.isEmpty()) {
            true -> "0"
            else -> salesTax
        }.toDouble()

        val taxedTip = if (taxOnTip) {
            billAmountDouble * (1 + salesTaxDouble / 100) * tipPercentageDouble / 100
        } else {
            billAmountDouble * tipPercentageDouble / 100
        }

        val finalTip = when {
            roundUpBill -> kotlin.math.ceil(taxedTip)
            roundDownBill -> kotlin.math.floor(taxedTip)
            else -> taxedTip
        }

        return NumberFormat.getCurrencyInstance().format(finalTip)
    }

    private fun calculateTipPerPerson(
        totalTip: String = _uiState.value.totalTip,
        numOfPeople: String = _uiState.value.numOfPeople,
        roundUp: Boolean = _uiState.value.roundUpTip,
        roundDown: Boolean = _uiState.value.roundDownTip
    ): String {

        val tip = getUnformattedValue(totalTip).toDouble()

        val numOfPeopleInt = numOfPeople.toInt()

        val tipPerPerson = when {
            roundUp -> kotlin.math.ceil(tip / numOfPeopleInt)
            roundDown -> kotlin.math.floor(tip / numOfPeopleInt)
            else -> tip / numOfPeopleInt
        }
        return NumberFormat.getCurrencyInstance().format(tipPerPerson)
    }

    private fun calculateTotalBillAmount(
        billAmount: String = _uiState.value.billAmount,
        totalTip: String = _uiState.value.totalTip,
        salesTax: String = _uiState.value.salesTax,
        taxOnTip: Boolean = _uiState.value.taxOnTip
    ): String {
        val billAmountDouble = when (billAmount.isEmpty()) {
            true -> "0"
            else -> getUnformattedValue(billAmount)
        }.toDouble()
        val totalTipDouble = getUnformattedValue(totalTip).toDouble()
        val salesTaxDouble = when (salesTax.isEmpty()) {
            true -> "0"
            else -> salesTax
        }.toDouble()

        val taxedBill = if (taxOnTip) {
            billAmountDouble * (1 + salesTaxDouble / 100)
        } else {
            billAmountDouble
        }

        val finalBill = taxedBill + totalTipDouble

        return NumberFormat.getCurrencyInstance().format(finalBill)
    }

    fun updateTipPercentage(tipPercentage: String) {
        if (tipPercentage.isDigitsOnly() || tipPercentage.contains('.')) {
            val tip = calculateTip(
                tipPercentage = when (tipPercentage.isEmpty()) {
                    true -> "0"
                    else -> tipPercentage
                }
            )
            val tipPerPerson = calculateTipPerPerson(
                totalTip = tip, numOfPeople = _uiState.value.numOfPeople
            )
            _uiState.update {
                it.copy(tipPercent = tipPercentage, totalTip = tip, tipPerPerson = tipPerPerson)
            }
        }
    }

    fun updateRoundBill(roundUp: Boolean, roundDown: Boolean) {
        when {
            roundUp -> {
                val tip = calculateTip(
                    roundUpBill = true
                )
                _uiState.update {
                    it.copy(roundUpBill = true, roundDownBill = false, totalTip = tip)
                }
            }

            roundDown -> {
                val tip = calculateTip(
                    roundDownBill = true
                )
                _uiState.update {
                    it.copy(roundUpBill = false, roundDownBill = true, totalTip = tip)
                }
            }

            else -> {
                val tip = calculateTip(
                    roundUpBill = false, roundDownBill = false
                )
                _uiState.update {
                    it.copy(roundUpBill = false, roundDownBill = false, totalTip = tip)
                }
            }
        }
    }

    fun updateRoundTip(roundUp: Boolean, roundDown: Boolean) {
        when {
            roundUp -> {
                val tip = calculateTip(
                    roundUpTip = true
                )
                _uiState.update {
                    it.copy(roundUpTip = true, roundDownTip = false, totalTip = tip)
                }
            }

            roundDown -> {
                val tip = calculateTip(
                    roundDownTip = true,
                )
                _uiState.update {
                    it.copy(roundUpTip = false, roundDownTip = true, totalTip = tip)
                }
            }

            else -> {
                val tip = calculateTip(roundUpTip = false, roundDownTip = false)
                _uiState.update {
                    it.copy(roundUpTip = false, roundDownTip = false, totalTip = tip)
                }
            }
        }
    }


    fun updateTipOnTaxOption(taxOnTip: Boolean) {
        val tip = calculateTip(
            taxOnTip = taxOnTip
        )
        _uiState.update {
            it.copy(taxOnTip = taxOnTip, totalTip = tip)
        }
    }

    fun updateSalesTax(salesTax: String) {
        if (salesTax.isDigitsOnly() || salesTax.contains('.')) {
            val tip = calculateTip(
                salesTax = when (salesTax.isEmpty()) {
                    true -> "0"
                    else -> salesTax
                }
            )
            _uiState.update {
                it.copy(salesTax = salesTax, totalTip = tip)
            }
        }
    }

    fun updateNumOfPeople(numOfPeople: String) {
        if (!numOfPeople.isDigitsOnly()) return

        val tipPerPerson = calculateTipPerPerson(
            numOfPeople = when {
                numOfPeople.isEmpty() -> "1"
                numOfPeople.toInt() < 1 -> "1"
                else -> numOfPeople
            }
        )

        _uiState.update {
            it.copy(
                numOfPeople = when {
                    numOfPeople == "0" -> "1"
                    else -> numOfPeople
                }, tipPerPerson = tipPerPerson
            )
        }
    }

    fun updateSelectedCountry(selectedCountryName: String) {

        val country = countries().find { it.name == selectedCountryName }

        val tip = calculateTip(
            tipPercentage = country?.tipPercentage.toString()
        )
        val tipPerPerson = calculateTipPerPerson(
            totalTip = tip, numOfPeople = _uiState.value.numOfPeople
        )
        _uiState.update {
            it.copy(
                selectedCountry = country?.name ?: "",
                tipPercent = country?.tipPercentage.toString(),
                totalTip = tip,
                tipPerPerson = tipPerPerson
            )
        }
    }


    private fun startApp() {
        _uiState.value = HomeUiState()
    }

    companion object {

        private const val MAX_NUM_LENGTH = 10

    }

    init {
        startApp()
        viewModelScope.launch {
            // TODO: Load timezone from API and auto-select country
        }
    }
}

data class HomeUiState(
    val roundUpBill: Boolean = false,
    val roundDownBill: Boolean = false,
    val roundUpTip: Boolean = false,
    val roundDownTip: Boolean = false,
    val taxOnTip: Boolean = false,
    val billAmount: String = NumberFormat.getCurrencyInstance().format(0), //before tip
    val tipPercent: String = "15",
    val salesTax: String = "0",
    val totalTip: String = NumberFormat.getCurrencyInstance().format(0),
    val tipPerPerson: String = NumberFormat.getCurrencyInstance().format(0),
    val totalPerPerson: String = NumberFormat.getCurrencyInstance().format(0),
    val numOfPeople: String = "1",
    val totalBillAmount: String = NumberFormat.getCurrencyInstance()
        .format(0),  //after tip and tax
    val countries: List<Country> = countries(),
    val selectedCountry: String = countries().firstOrNull { it.timeZone == TimeZone.getDefault().id }?.name
        ?: "",
)


@VisibleForTesting
internal suspend fun detectCountry(): String {
    val url = "https://worldtimeapi.org/api/ip"
    val client = OkHttpRequester()
    var timeZoneString = ""

    return withContext(Dispatchers.IO) {
        val response = client.run(url)

        if (response?.isSuccessful == true) {
            val timezoneResponse = TimeZoneJsonAdapter.fromJson(response.body!!.string())
            if (timezoneResponse != null) {
                timeZoneString = timezoneResponse.timezone.toString()
            }
        }
//        else{
//            println("Errorsssssss: ${response?.code}")
//        }
        // Return the result obtained from the network call
        countries().firstOrNull { it.timeZone == timeZoneString }?.name
            ?: countries().firstOrNull { it.timeZone == TimeZone.getDefault().id }?.name ?: ""
    }

}
