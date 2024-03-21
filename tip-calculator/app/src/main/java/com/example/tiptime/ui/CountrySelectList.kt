package com.example.tiptime.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tiptime.R
import com.example.tiptime.data.Country


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelectBottomSheet(
    modifier: Modifier = Modifier,
    countries: List<Country>,
    onSelectedCountryChanged: (String) -> Unit,
    showBottomSheet: Boolean,
    onShowBottomSheetChanged: (Boolean) -> Unit,
) {
    var countrySearchText by remember {
        mutableStateOf("")
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var cannotSearch by remember { mutableStateOf(true) }
    val focusRequester = FocusRequester()

    if (showBottomSheet) {
        val filteringOptions = countries.filter {
            it.name.contains(
                countrySearchText, ignoreCase = true
            )
        }

        Row(modifier = modifier.fillMaxHeight()) {
            ModalBottomSheet(
                shape = BottomSheetDefaults.HiddenShape,
                onDismissRequest = {
                    countrySearchText = ""
                    onShowBottomSheetChanged(false)
                },
                sheetState = sheetState,
                dragHandle = null,
                modifier = Modifier.fillMaxHeight(),
            ) {
                // Sheet content
                Column(modifier = modifier) {
                    OutlinedTextField(
                        readOnly = cannotSearch,
                        value = countrySearchText,
                        onValueChange = { countrySearchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFF2F9B5A))
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(0.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                                contentDescription = stringResource(R.string.back_to_home_screen),
                                modifier = Modifier.clickable {
                                    onShowBottomSheetChanged(false)
                                    countrySearchText = ""
                                    cannotSearch = true
                                },
                                tint = Color.White
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.TwoTone.Search,
                                contentDescription = stringResource(
                                    R.string.search_label
                                ),
                                modifier = Modifier.noRippleClickable {
                                    cannotSearch = false
                                    focusRequester.requestFocus()
                                },
                                tint = Color.White
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true,
                    )
                }
                if (filteringOptions.isNotEmpty()) {
                    LazyColumn {
                        filteringOptions.forEach { (name) ->
                            item(key = name) {
                                Text(text = name, modifier = modifier
                                    .padding(16.dp)
                                    .clickable {
                                        onSelectedCountryChanged(name)
                                        onShowBottomSheetChanged(false)
                                        countrySearchText = ""
                                    })
                            }
                        }
                    }
                } else {
                    Column {
                        Text(
                            text = stringResource(
                                R.string.no_countries_matching, countrySearchText
                            ), modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

    }
}
