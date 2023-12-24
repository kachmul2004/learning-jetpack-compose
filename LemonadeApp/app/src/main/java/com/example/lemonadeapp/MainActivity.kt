/**
 * App Overview:
 * 1. When the user first launches the app, they see a lemon tree. There's a label that prompts them to tap the lemon tree image to "select" a lemon from the tree.
 * 2. After they tap the lemon tree, the user sees a lemon. They are prompted to tap the lemon to "squeeze" it to make lemonade. They need to tap the lemon several times to squeeze it. The number of taps required to squeeze the lemon is different each time and is a randomly generated number between 2 to 4 (inclusive).
 * 3. After they've tapped the lemon the required number of times, they see a refreshing glass of lemonade! They are asked to tap the glass to "drink" the lemonade.
 * 4. After they tap the lemonade glass, they see an empty glass. They are asked to tap the empty glass to start again.
 * 5. After they tap the empty glass, they see the lemon tree and can begin the process again. More lemonade please!
 */

package com.example.lemonadeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonadeapp.ui.theme.LemonadeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeAppTheme {
                LemonadeApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LemonadeApp() {
    LemonsAndGlass(
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun LemonsAndGlass(modifier: Modifier = Modifier) {
    var currentStep by remember { mutableIntStateOf(1) } //
    var squeezes by remember { mutableIntStateOf(2) }

    // select an image based on the current step
    val imageResource = when (currentStep) {
        1 -> R.drawable.lemon_tree
        2 -> R.drawable.lemon_squeeze
        3 -> R.drawable.lemon_drink
        else -> R.drawable.lemon_restart
    }

    // select an action text based on the current step
    val actionText = when (currentStep) {
        1 -> R.string.select_lemon
        2 -> R.string.squeeze_lemon
        3 -> R.string.drink_lemonade
        else -> R.string.restart
    }

    // select an image description based on the current step
    val imageDescription = when (currentStep) {
        1 -> R.string.lemon_tree_content_description
        2 -> R.string.lemon_content_description
        3 -> R.string.lemonade_content_description
        else -> R.string.empty_glass_content_description
    }

    fun onImageClick() {
        when (currentStep) {
            1 -> currentStep = 2
            2 -> {
                squeezes -= 1
                if (squeezes == 0) {
                    currentStep = 3
                    squeezes = (2..4).random()
                }
            }

            3 -> currentStep = 4
            else -> {
                currentStep = 1
                squeezes = (2..4).random()
            }
        }
    }

    Box(modifier = modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFF9E44C)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp),
                onClick = { onImageClick() },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC3ECD2)
                ),
                content = {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = stringResource(imageDescription),
                    )
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = stringResource(id = actionText), fontSize = 18.sp)
        }
    }
}