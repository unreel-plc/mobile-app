package com.unreel.unreel.ui.feature.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unreel.unreel.core.common.utils.Constants.BASE_URL_IMAGE
import com.unreel.unreel.core.navigation.NavScreenGraph
import com.unreel.unreel.networks.models.auth.DownloadItem
import com.unreel.unreel.ui.theme.unreelTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(
    navConroller: NavController = rememberNavController()
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.collectAsState()
    viewModel.collectSideEffect {
        when (it) {
            is Event.GoToDownloadDetail -> {
                navConroller.navigate(NavScreenGraph.DownloadDetailScreen.route + "/${it.id}")
            }
            else -> {}
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: State = State(),
    onAction: (Action) -> Unit = {}
) {
    // Dark theme colors
    val darkBackground = Color(0xFF000000)
    val darkSurface = Color(0xFF1A1A1A)
    val darkOnSurface = Color(0xFFFFFFFF)
    val darkOnBackground = Color(0xFFE0E0E0)

    val categories = listOf("Shorts", "Reels", "TikTok", "Comedy", "Entertainment")
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = darkBackground
    ) { paddingValues ->
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .pullToRefresh(
                        isRefreshing = state.isLoading,
                        state = pullToRefreshState,
                        enabled = true,
                        onRefresh = { onAction(Action.OnRefresh) },
                    )
        ) {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.padding(paddingValues).align(Alignment.TopCenter).zIndex(1f),
                isRefreshing = state.isLoading,
                state = pullToRefreshState,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                color = MaterialTheme.colorScheme.primary,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()

                    .background(darkBackground)
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        searchQuery = state.searchQuery,
                        onSearchQueryChanged = { onAction(Action.OnSearchQueryChanged(it)) },
                        onSearchClicked = { onAction(Action.OnSearchClicked) },
                        modifier = Modifier.weight(1f)
                    )

                    ThemeToggleButton(
                        isDarkTheme = state.isDarkTheme,
                        onToggle = { onAction(Action.OnThemeToggleClicked) }
                    )
                }

                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            text = category,
                            isSelected = category == state.selectedCategory,
                            onClick = { onAction(Action.OnCategorySelected(category)) }
                        )
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.downloadedItems) { content ->
                        ContentCard(
                            content = content,
                            onClick = {
                                if (content.id != null) onAction(
                                    Action.OnContentClicked(
                                        content.id
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .background(
                Color(0xFF2A2A2A),
                RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Search videos, tags...",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun ThemeToggleButton(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
            .size(48.dp)
            .background(
                Color(0xFF2A2A2A),
                RoundedCornerShape(24.dp)
            )
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = "Toggle Theme",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color.White else Color(0xFF2A2A2A)
    val textColor = if (isSelected) Color.Black else Color.White

    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun getPlatformIconUrl(channel: String): String {
    return when {
        channel.contains("youtube", ignoreCase = true) || 
        channel.contains("yt", ignoreCase = true) ||
        channel.contains("google", ignoreCase = true) -> "https://www.youtube.com/s/desktop/9b55e232/img/favicon_32x32.png"
        
        channel.contains("tiktok", ignoreCase = true) || 
        channel.contains("tt", ignoreCase = true) -> "https://www.tiktok.com/favicon.ico"
        
        channel.contains("instagram", ignoreCase = true) || 
        channel.contains("ig", ignoreCase = true) ||
        channel.contains("insta", ignoreCase = true) -> "https://static.cdninstagram.com/rsrc.php/v4/yR/r/lam-fZmwmvn.png"
        
        channel.contains("facebook", ignoreCase = true) || 
        channel.contains("fb", ignoreCase = true) -> "https://img.icons8.com/color/48/facebook-new.png"
        
        else -> "https://www.youtube.com/s/desktop/9b55e232/img/favicon_32x32.png" // Default to YouTube
    }
}

@Composable
fun ContentCard(
    content: DownloadItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f) // More vertical like TikTok/Instagram stories
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        // Background image
        AsyncImage(
            model = BASE_URL_IMAGE + (content.thumbnail?.ifEmpty { "https://i.ytimg.com/vi/-l8CmiKn8jc/maxresdefault.jpg" }),
            contentDescription = content.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        // Platform icon (bottom right)
        AsyncImage(
            model = getPlatformIconUrl(content.platform ?: ""),
            contentDescription = "Platform icon",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .padding(2.dp)
        )

        // Content info overlay at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(12.dp)
                .padding(end = 40.dp) // Add padding to avoid overlap with platform icon
        ) {
            // Title
            Text(
                text = content?.title ?: "",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Creator name
            Text(
                text = content?.channel ?: "",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            // View count with heart icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Views",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatViewCount(content.likeCount?.toString() ?: "0"),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

fun formatViewCount(viewCount: String): String {
    val count = viewCount.replace("K", "").replace("M", "").replace(",", "").toIntOrNull() ?: 0
    return when {
        count >= 1000000 -> "${(count / 1000000.0).toString().take(3)}M"
        count >= 1000 -> "${(count / 1000.0).toString().take(3)}K"
        else -> viewCount
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color(0xFF000000),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color(0xFF1DA1F2)
                )
            },
            label = {
                Text(
                    "Home",
                    color = Color(0xFF1DA1F2),
                    fontSize = 12.sp
                )
            },
            selected = true,
            onClick = { }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Archive,
                    contentDescription = "Archive",
                    tint = Color.Gray
                )
            },
            label = {
                Text(
                    "Archive",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.Gray
                )
            },
            label = {
                Text(
                    "Profile",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            },
            selected = false,
            onClick = { }
        )
    }
}

@Preview()
@Composable
fun HomeScreenPreview() {
    unreelTheme {
        HomeScreen(
            state = State(
                searchQuery = "",
                selectedCategory = "Shorts",
                downloadedItems = getSampleContent(),
                isDarkTheme = true
            ),
            onAction = {}
        )
    }
}

fun getSampleContent(): List<DownloadItem> {
    return listOf(
        DownloadItem(
            id = "1",
            title = "Dog Core Compilation",
            channel = "dogs.loveru",
            thumbnail = "https://i.ytimg.com/vi/-l8CmiKn8jc/maxresdefault.jpg",
            viewCount = 163200,
            duration = "0:45"
        ),
        DownloadItem(
            id = "2",
            title = "not gonna lie, it was very brilliant of leonard",
            channel = "MoviesXSeriesClips",
            thumbnail = "https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg",
            viewCount = 247700,
            duration = "1:23"
        ),
        DownloadItem(
            id = "3",
            title = "Day 60 of Women Being Women 😂",
            channel = "srt__marrion",
            thumbnail = "https://i.ytimg.com/vi/jNQXAC9IVRw/maxresdefault.jpg",
            viewCount = 89000,
            duration = "0:32"
        ),
        DownloadItem(
            id = "4",
            title = "Instagram Reel Content",
            channel = "InstagramCreator",
            thumbnail = "https://i.ytimg.com/vi/kJQP7kiw5Fk/maxresdefault.jpg",
            viewCount = 152000,
            duration = "0:28"
        )
    )
}