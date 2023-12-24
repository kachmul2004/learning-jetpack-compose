package com.example.digitalartspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitalartspace.ui.theme.DigitalArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigitalArtSpaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var currentImageResource by remember { mutableIntStateOf(1) }
    val artworkResource = when (currentImageResource) {
        1 -> R.drawable.giraffe
        2 -> R.drawable.meerkat
        3 -> R.drawable.peacock
        else -> R.drawable.puppy
    }
    val artworkDetails = when (currentImageResource) {
        1 -> mapOf("title" to "Giraffe", "artist" to "By Kachinga (2020)")
        2 -> mapOf("title" to "Meerkat", "artist" to "By Mule (2021)")
        3 -> mapOf("title" to "Peacock", "artist" to "By Kachman (2022)")
        else -> mapOf("title" to "Puppy", "artist" to "By KGM (2023)")
    }
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Bottom
    ) {
        ArtWork(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            image = artworkResource,
        )
        Spacer(modifier = Modifier.height(60.dp))
        ImageDescription(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            artworkDetails = artworkDetails
        )
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier

                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { currentImageResource-- },
                modifier = Modifier.width(150.dp),
                enabled = currentImageResource > 1
            ) {
                Text(text = "Previous")
            }
            Button(
                onClick = { currentImageResource++ },
                modifier = Modifier.width(150.dp),
                enabled = currentImageResource < 4
            ) {
                Text(text = "Next")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ArtWork(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,

    ) {
    Column(
        modifier = modifier
    ) {
        Surface(
            shadowElevation = 10.dp,
            border = BorderStroke(1.dp, Color.LightGray),

            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .padding(30.dp),
                painter = painterResource(id = image),
                contentDescription = "Artwork"
            )
        }
    }

}

@Composable
fun ImageDescription(modifier: Modifier = Modifier, artworkDetails: Map<String, String>) {
    val title = artworkDetails["title"]!!
    val artist = artworkDetails["artist"]!!
    Column(
        modifier = modifier.background(Color(0xFFECEBF4)).width(300.dp).padding(20.dp),
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraLight
        )

        Text(
            text = AnnotatedString(
                text = artist,
                spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(fontWeight = FontWeight.Bold),
                        0,
                        artist.length - 6
                    )
                ),
            ),
            fontSize = 20.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DigitalArtSpaceTheme {
        App()
    }
}