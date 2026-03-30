package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
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

data class WebsiteLink(
    val name: String,
    val url: String,
    val emoji: String,
    val description: String,
)

data class CreativePrompt(val title: String, val prompt: String)

private val homeLinks = listOf(
    WebsiteLink(
        "Google Drive",
        "https://drive.google.com/",
        "☁️",
        "Quick access to files, folders, and team docs"
    ),
    WebsiteLink(
        "YouTube",
        "https://www.youtube.com/",
        "▶️",
        "Watch tutorials, learning playlists, and inspiration"
    ),
    WebsiteLink(
        "Gmail",
        "https://mail.google.com/",
        "✉️",
        "Jump to your inbox and keep communication moving"
    ),
    WebsiteLink(
        "Google Calendar",
        "https://calendar.google.com/",
        "🗓️",
        "Plan your day with focus blocks and reminders"
    ),
    WebsiteLink(
        "Google Docs",
        "https://docs.google.com/",
        "📝",
        "Write ideas, notes, and project briefs quickly"
    ),
    WebsiteLink(
        "KSTVET Portal",
        "https://kttc.mycampuscura.com/Campuscura/?TenantID=kttc&Apply=1",
        "🎓",
        "Student portal for coursework and campus resources"
    ),
    WebsiteLink(
        "Nexinvent Solutions",
        "https://www.nexinvent.co.ke/",
        "🌍",
        "Explore company resources and updates"
    ),
)

private val creativePrompts = listOf(
    CreativePrompt("Daily Spark", "Sketch one app idea in 5 bullets."),
    CreativePrompt("Learning Quest", "Watch one tutorial and note 3 takeaways."),
    CreativePrompt("Build Habit", "Ship one tiny improvement before sunset."),
)

@Composable
fun HomeLinksScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "Creative Launchpad", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Open your favorite tools and start building something bold today.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Text(
                text = "Creative challenges",
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(creativePrompts) { prompt ->
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .aspectRatio(1f)
                    ) {
                        Text(text = "✨", fontSize = 20.sp)
                    }
                    Column {
                        Text(text = prompt.title, style = MaterialTheme.typography.titleSmall)
                        Text(text = prompt.prompt, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        item {
            Text(
                text = "Common links",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(homeLinks) { link ->
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "${link.emoji}  ${link.name}")
                    Text(text = link.description, style = MaterialTheme.typography.bodySmall)
                }
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
