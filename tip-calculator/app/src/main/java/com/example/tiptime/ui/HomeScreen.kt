package com.example.tiptime.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptime.R
import com.example.tiptime.data.Country
import com.example.tiptime.data.countries
import com.example.tiptime.ui.theme.TipTimeTheme

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val homeUiState by homeViewModel.uiState.collectAsState()

    SmartTipCalculatorLayout(
        billAmount = homeUiState.billAmount,
        totalBillAmount = homeUiState.totalBillAmount,
        tipPercent = homeUiState.tipPercent,
        numOfPeople = homeUiState.numOfPeople,
        salesTax = homeUiState.salesTax,
        totalTip = homeUiState.totalTip,
        tipPerPerson = homeUiState.tipPerPerson,
        roundUpTip = homeUiState.roundUpTip,
        countries = homeUiState.countries,
        selectedCountry = homeUiState.selectedCountry,
        onBillAmountChanged = { homeViewModel.onKeypadAction(it) },
        onNumOfPeopleChanged = { homeViewModel.updateNumOfPeople(it) },
        onSalesTaxChanged = { homeViewModel.updateSalesTax(it) },
        onTipPercentChanged = { homeViewModel.updateTipPercentage(it) },
        onSelectedCountryChanged = { homeViewModel.updateSelectedCountry(it) }
    )
}

@Composable
fun SmartTipCalculatorLayout(
    billAmount: String,
    totalBillAmount: String,
    tipPercent: String,
    numOfPeople: String,
    salesTax: String,
    totalTip: String,
    tipPerPerson: String,
    roundUpTip: Boolean,
    countries: List<Country>,
    selectedCountry: String,
    onBillAmountChanged: (NumericKeypadAction) -> Unit,
    onNumOfPeopleChanged: (String) -> Unit,
    onSalesTaxChanged: (String) -> Unit,
    onTipPercentChanged: (String) -> Unit,
    onSelectedCountryChanged: (String) -> Unit,
) {
    var showCountriesBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            AmountDisplayCard(
                label = R.string.bill_amount_label,
                value = billAmount,
                modifier = Modifier.weight(1f),
                labelFontSize = 12.sp,
                valueFontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                horizontalAlignment = Alignment.Start,
                textAlignment = Alignment.Start
            )
            Spacer(modifier = Modifier.width(10.dp))
            AmountDisplayCard(
                label = R.string.tip_percentage_label,
                value = "$tipPercent%",
                modifier = Modifier.weight(1f),
                labelFontSize = 12.sp,
                valueFontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                horizontalAlignment = Alignment.End,
                textAlignment = Alignment.End
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            AmountDisplayCard(
                label = R.string.total_amount_label,
                value = totalBillAmount,
                modifier = Modifier.weight(1f),
                labelFontSize = 12.sp,
                valueFontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                horizontalAlignment = Alignment.Start,
                textAlignment = Alignment.Start
            )
            Spacer(modifier = Modifier.width(10.dp))
            AmountDisplayCard(
                label = R.string.tip_amount_label,
                value = totalTip,
                modifier = Modifier.weight(1f),
                labelFontSize = 12.sp,
                valueFontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                horizontalAlignment = Alignment.End,
                textAlignment = Alignment.End
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            AmountDisplayCard(
                label = R.string.total_per_person_label,
                value = totalBillAmount,
                modifier = Modifier.weight(1f),
                labelFontSize = 12.sp,
                valueFontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                horizontalAlignment = Alignment.Start,
                textAlignment = Alignment.Start
            )
            Spacer(modifier = Modifier.width(10.dp))
            AmountDisplayCard(
                label = R.string.tip_amount_per_person_label,
                value = tipPerPerson,
                modifier = Modifier.weight(1f),
                labelFontSize = 12.sp,
                valueFontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                horizontalAlignment = Alignment.End,
                textAlignment = Alignment.End
            )
        }

        EditNumberField(
            label = R.string.number_of_people,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            value = numOfPeople,
            onValueChanged = onNumOfPeopleChanged,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.sales_tax,
            leadingIcon = R.drawable.money,
            value = salesTax,
            onValueChanged = onSalesTaxChanged,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        )
        CountryDisplayField(
            modifier = Modifier.fillMaxWidth(),
            selectedCountry = selectedCountry,
            showBottomSheet = showCountriesBottomSheet
        ) { showCountriesBottomSheet = it }
        NumericKeypad(onKeypadAction = onBillAmountChanged)
        CountrySelectBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            onSelectedCountryChanged = onSelectedCountryChanged,
            showBottomSheet = showCountriesBottomSheet,
            onShowBottomSheetChanged = { showCountriesBottomSheet = it },
            countries = countries
        )
        Spacer(modifier = Modifier.height(50.dp))

    }
}

enum class CursorSelectionBehaviour {
    START, END, SELECT_ALL
}

@Composable
fun EditNumberField(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
) {
    TextField(
        value = value,
        modifier = modifier,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
    )
}

@Composable
fun RoundTheTipRow(
    modifier: Modifier = Modifier,
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SmartTipCalculatorPreview() {
    TipTimeTheme {
        SmartTipCalculatorLayout(
            billAmount = "100",
            totalBillAmount = "115",
            tipPercent = "15",
            numOfPeople = "2",
            salesTax = "5",
            totalTip = "15",
            tipPerPerson = "7.5",
            roundUpTip = false,
            selectedCountry = "United States",
            onBillAmountChanged = { },
            onNumOfPeopleChanged = { },
            onSalesTaxChanged = { },
            onTipPercentChanged = { },
            onSelectedCountryChanged = { },
            countries = countries(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDisplayField(
    modifier: Modifier = Modifier,
    selectedCountry: String,
    showBottomSheet: Boolean,
    onShowBottomSheetChanged: (Boolean) -> Unit,
) {

    Row(
        modifier = modifier, horizontalArrangement = Arrangement.Center
    ) {
        val defaultColorScheme = MaterialTheme.colorScheme.onSurface

        OutlinedTextField(
            value = selectedCountry,
            onValueChange = {},
            label = { Text(stringResource(R.string.country_label)) },
            modifier = modifier.noRippleClickable {
                onShowBottomSheetChanged(!showBottomSheet)
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = defaultColorScheme,
                disabledTextColor = defaultColorScheme,
                disabledLabelColor = defaultColorScheme
            ),
            readOnly = true,
            singleLine = true,
            enabled = false
        )

    } // end row
}

@Composable
fun AmountDisplayCard(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    value: String,
    labelFontSize: TextUnit,
    valueFontSize: TextUnit,
    fontWeight: FontWeight,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    textAlignment: Alignment.Horizontal = Alignment.Start,
    arrangement: String = "vertical"
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = modifier
    ) {
        when (arrangement.lowercase()) {
            "vertical" -> Column(
                modifier = Modifier
                    .padding(10.dp)
                    .align(horizontalAlignment)
            ) {
                Text(
                    text = stringResource(label),
                    fontSize = labelFontSize,
                    fontWeight = fontWeight,
                    modifier = Modifier
                        .align(textAlignment)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = value,
                    fontSize = valueFontSize,
                    fontWeight = fontWeight,
                    modifier = Modifier
                        .align(textAlignment)
                )
            }

            "horizontal" -> Row(
                modifier = Modifier
                    .padding(16.dp)
                    .align(horizontalAlignment)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(label),
                    fontSize = labelFontSize,
                    fontWeight = fontWeight,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = value,
                    fontSize = valueFontSize,
                    fontWeight = fontWeight,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

//Util
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = then(composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
})