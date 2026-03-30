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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.test.espresso.base.Default
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
    FAVORITES("Favorites", R.drawable.ic_favorite),
    HOME("Home", R.drawable.ic_home),
    PROFILE("Profile", R.drawable.ic_account_box),
}

data class WebsiteLink(
    val name: String,
    val url: String,
    val emoji: String,
    val description: String,
)

private val homeLinks = listOf(
    WebsiteLink(
        "Nexinvent Solutions",
        "https://www.nexinvent.co.ke/",
        "🌍",
        "Explore company resources and updates"
    ),
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
        "Android Developers",
        "https://developer.android.com/",
        "🤖",
        "Documentation for Jetpack Compose and Material 3"
    ),
    WebsiteLink(
        "Firebase Console",
        "https://console.firebase.google.com/",
        "🔥",
        "Manage your app databases, auth, and analytics"
    ),
    WebsiteLink(
        "Canva",
        "https://www.canva.com/",
        "🎨",
        "Quickly design assets for Nexinvent or Dwellio"
    ),
    WebsiteLink(
        "NPM Registry",
        "https://www.npmjs.com/",
        "📦",
        "Search for Node.js packages and libraries"
    ),
    WebsiteLink(
        "KSTVET Portal",
        "https://kttc.mycampuscura.com/Campuscura/?TenantID=kttc&Apply=1",
        "🎓",
        "Student portal for coursework and campus resources"
    ),
    WebsiteLink(
        "GitHub",
        "https://github.com/",
        "🐙",
        "Manage your repositories and collaborate on code"
    ),
    WebsiteLink(
        "KUCCPS Portal",
        "https://students.kuccps.net/",
        "🏫",
        "Placement processing and institutional career updates"
    ),
    WebsiteLink(
        "ChatGPT",
        "https://chatgpt.com/",
        "🤖",
        "Brainstorming, debugging, and drafting content"
    ),
    WebsiteLink(
        "KRA iTax",
        "https://itax.kra.go.ke/",
        "🇰🇪",
        "Quick access for personal or business tax compliance"
    ),
    WebsiteLink(
        "Stack Overflow",
        "https://stackoverflow.com/",
        "🥞",
        "Find answers to tricky programming challenges"
    ),
    WebsiteLink(
        "Vercel Dashboard",
        "https://vercel.com/dashboard",
        "📐",
        "Monitor your web deployments and site performance"
    ),
)

@Composable
fun HomeLinksScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Filtered list: recalculated only when searchQuery or homeLinks changes
    val filteredLinks by remember(searchQuery) {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                homeLinks
            } else {
                homeLinks.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.description.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
//        Header Card
        item {
            Card(
                modifier = Modifier.padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ){
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Creative Launchpad",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "Creative Challenges",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                }
            }
        }

        // 2. Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search links...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
        }

        // 3. List of filtered links
        items(filteredLinks) { link ->
            Card(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) {
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text(
                        text = "${link.emoji}  ${link.name}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = link.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(homeLinks) { link ->
            Card(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
                    try { context.startActivity(intent) } catch (_: Exception) {}
                },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(), // Or CardDefaults.outlinedCardColors()
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp) // More generous padding
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${link.emoji}  ${link.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = link.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
