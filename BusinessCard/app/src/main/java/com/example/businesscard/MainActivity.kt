package com.example.businesscard

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscard.ui.theme.BusinessCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardTheme {
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
    Box(
        modifier = Modifier
            .background(Color(0xFFD2E8D4))
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.blue_tecno_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.35F,
            modifier = Modifier
                .fillMaxSize()
        )
        ProfessionalInfoCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 180.dp)
        )
        ContactInfoCard(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun ProfessionalInfoCard(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.discovery_clean_logo),
            contentDescription = stringResource(R.string.logo_description),
            modifier = Modifier
                .size(250.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.name),
            fontWeight = FontWeight.Medium,
            fontSize = 35.sp,
        )
        Text(
            text = stringResource(R.string.job_title),
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            color = Color(0xFF006D3B),
        )

    }
}


@Composable
fun ContactInfoCard(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 20.dp)) {
            ContactInfo(
                icon = Icons.Filled.Phone,
                value = stringResource(R.string.phone_number),
                contentDescription = stringResource(R.string.phone_number_content_description)
            )
            Spacer(Modifier.height(10.dp))
            ContactInfo(
                icon = Icons.Filled.Share,
                value = stringResource(R.string.share_handle),
                contentDescription = stringResource(R.string.share_handle_content_description)
            )
            Spacer(Modifier.height(10.dp))
            ContactInfo(
                icon = Icons.Filled.Email,
                value = stringResource(R.string.email_address),
                contentDescription = stringResource(R.string.email_address_content_description)
            )
        }
    }
}

@Composable
fun ContactInfo(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color = Color(0xFF006D3B),
    value: String,
    contentDescription: String? = null
) {
    Row() {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .align(Alignment.CenterVertically),
            tint = tint
        )
        Spacer(Modifier.width(20.dp))
        Text(
            text = value
        )

    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    BusinessCardTheme {
        App()
    }
}