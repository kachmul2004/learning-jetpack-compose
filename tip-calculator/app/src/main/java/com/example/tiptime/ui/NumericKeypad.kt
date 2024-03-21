package com.example.tiptime.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NumericKeypad(
    modifier: Modifier = Modifier,
    calculatorMode: Boolean = false,//true when used as a calculator
    value: String = "0",
    onKeypadAction: (NumericKeypadAction) -> Unit
) {
    val buttonSpacing = 5.dp
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(buttonSpacing),
    ) {

        if (calculatorMode) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
            ) {
                Text(
                    text = value,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    color = Color.Black,
                    maxLines = 1
                )
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing),
        ) {
            NumericKeypadButton(
                symbol = stringResource(R.string.clear_screen),
                color = Color.LightGray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Clear)
            }
            NumericKeypadButton(
                symbol = "Del",
                color = Color.Red,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Delete)
            }
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            NumericKeypadButton(
                symbol = "7",
                color = Color.Gray,
                modifier = Modifier

                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(7))
            }
            NumericKeypadButton(
                symbol = "8",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(8))
            }
            NumericKeypadButton(
                symbol = "9",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(9))
            }
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            NumericKeypadButton(
                symbol = "4",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(4))
            }
            NumericKeypadButton(
                symbol = "5",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(5))
            }
            NumericKeypadButton(
                symbol = "6",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(6))
            }
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            NumericKeypadButton(
                symbol = "1",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(1))
            }
            NumericKeypadButton(
                symbol = "2",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(2))
            }
            NumericKeypadButton(
                symbol = "3",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(3))
            }
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            NumericKeypadButton(
                symbol = ".",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Decimal)
            }
            NumericKeypadButton(
                symbol = "0",
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                onKeypadAction(NumericKeypadAction.Number(0))
            }
            if (calculatorMode) {
                NumericKeypadButton(
                    symbol = "=",
                    color = Color.Red,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    onKeypadAction(NumericKeypadAction.Done)
                }
            } else {
                NumericKeypadButton(
                    symbol = "00",
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    repeat(2) { onKeypadAction(NumericKeypadAction.Number(0)) }
                }
            }
        }
    }
}
