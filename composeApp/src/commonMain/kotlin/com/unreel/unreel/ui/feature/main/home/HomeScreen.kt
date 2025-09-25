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
import com.unreel.unreel.ui.theme.unreelTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.collectAsState()
    viewModel.collectSideEffect {
        when (it) {
            else -> {}
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

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
    val sampleContent = getSampleContent()

    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = darkBackground
    ) { paddingValues ->
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
                items(sampleContent) { content ->
                    ContentCard(
                        content = content,
                        onClick = { onAction(Action.OnContentClicked(content.id)) }
                    )
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
                        text = "Search videos, tags, creators...",
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

@Composable
fun ContentCard(
    content: ContentItem,
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
            model = if (content.thumbnailUrl.isNotEmpty()) content.thumbnailUrl else "https://i.ytimg.com/vi/-l8CmiKn8jc/maxresdefault.jpg",
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

        // Duration overlay (top right)
        content.duration?.let { duration ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = duration,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Content info overlay at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Title
            Text(
                text = content.title,
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
                text = content.creator,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like icon and count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Likes",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = content.viewCount,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // View button
                Button(
                    onClick = { onClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(
                        text = "View",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

fun getSampleContent(): List<ContentItem> {
    return listOf(
        ContentItem(
            id = "1",
            title = "Tyrion Confronts Tywin ...",
            creator = "Ewan Edris",
            thumbnailUrl = "https://i.ytimg.com/vi/-l8CmiKn8jc/maxresdefault.jpg",
            viewCount = "2.1M",
            duration = null,
            isVerified = true
        ),
        ContentItem(
            id = "2",
            title = "Hikaru Nakamura. The p...",
            creator = "matershell",
            thumbnailUrl = "https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg",
            viewCount = "256.5K",
            duration = null,
            isVerified = false
        ),
        ContentItem(
            id = "3",
            title = "Video by not.racist.meme...",
            creator = "not.racist.memes",
            thumbnailUrl = "https://i.ytimg.com/vi/jNQXAC9IVRw/maxresdefault.jpg",
            viewCount = "1.2M",
            duration = null,
            isVerified = false
        ),
        ContentItem(
            id = "4",
            title = "New York is a Weird Plac...",
            creator = "NewYork",
            thumbnailUrl = "https://i.ytimg.com/vi/kJQP7kiw5Fk/maxresdefault.jpg",
            viewCount = "892K",
            duration = null,
            isVerified = true
        ),
        ContentItem(
            id = "5",
            title = "Daily dose of comedy",
            creator = "CompiKing",
            thumbnailUrl = "https://i.ytimg.com/vi/9bZkp7q19f0/maxresdefault.jpg",
            viewCount = "445K",
            duration = null,
            isVerified = true
        ),
        ContentItem(
            id = "6",
            title = "Internet wisdom compilation",
            creator = "WiseGuy",
            thumbnailUrl = "https://i.ytimg.com/vi/ScMzIvxBSi4/maxresdefault.jpg",
            viewCount = "673K",
            duration = null,
            isVerified = false
        )
    )
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
                contentItems = getSampleContent(),
                isDarkTheme = true
            ),
            onAction = {}
        )
    }
}