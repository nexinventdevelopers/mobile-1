package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MyApplicationApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MyApplicationApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> HomeLinksScreen(modifier = Modifier.padding(innerPadding))
                AppDestinations.FAVORITES -> PlaceholderScreen(
                    title = "Favorites",
                    modifier = Modifier.padding(innerPadding)
                )

                AppDestinations.PROFILE -> PlaceholderScreen(
                    title = "Profile",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.ic_home),
    FAVORITES("Favorites", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box),
}

data class WebsiteLink(val name: String, val url: String)

private val homeLinks = listOf(
    WebsiteLink("Nexinvent Solutions", "https://www.nexinvent.co.ke/"),
    WebsiteLink(
        "KSTVET Portal",
        "https://kttc.mycampuscura.com/Campuscura/?TenantID=kttc&Apply=1"
    ),
    WebsiteLink("Youtube", "https://www.youtube.com/"),
    WebsiteLink("Gmail", "https://mail.google.com/")
)

@Composable
fun HomeLinksScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Useful links")

        homeLinks.forEach { link ->
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
                    try {
                        context.startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                        // No app can handle this link.
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = link.name)
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "$title page")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeLinksPreview() {
    MyApplicationTheme {
        HomeLinksScreen()
    }
}
