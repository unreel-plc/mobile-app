package com.unreel.unreel.ui.feature.main.home.download_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.unreel.unreel.core.common.utils.Constants.BASE_URL_IMAGE
import com.unreel.unreel.ui.feature.main.home.getPlatformIconUrl
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun DownloadDetailScreen(
    navController: NavController,
    viewModel: DownloadDetailViewModel
) {
    val state by viewModel.collectAsState()
    val uriHandler = LocalUriHandler.current
    
    viewModel.collectSideEffect { event ->
        when (event) {
            is Event.NavigateBack -> {
                navController.popBackStack()
            }
            is Event.OpenExternalLink -> {
                uriHandler.openUri(event.url)
            }
        }
    }
    
    DownloadDetailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun DownloadDetailScreen(
    state: State,
    onAction: (Action) -> Unit = {}
) {
    val darkBackground = Color(0xFF000000)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onAction(Action.OnBackClicked) }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to results",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Back to results",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (state.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${state.error}",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onAction(Action.OnRefresh) }
                    ) {
                        Text("Retry")
                    }
                }
            }
        } else if (state.downloadItem != null) {
            val item = state.downloadItem

            // Video thumbnail with overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = BASE_URL_IMAGE + (item.thumbnail ?: ""),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Platform icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .padding(8.dp)
                ) {
                    AsyncImage(
                        model = getPlatformIconUrl(item.platform ?: ""),
                        contentDescription = "Platform icon",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title and info
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = item.title ?: "No title",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Creator info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.channel ?: "Unknown Creator",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    item.channelFollowerCount?.let { followers ->
                        Text(
                            text = " • ${formatNumber(followers.toString())} followers",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        icon = Icons.Default.Favorite,
                        count = item.likeCount ?: 0,
                        label = "Likes",
                        color = Color(0xFFE91E63)
                    )
                    StatItem(
                        icon = Icons.Default.Visibility,
                        count = item.viewCount ?: 0,
                        label = "Views",
                        color = Color(0xFF2196F3)
                    )
                    StatItem(
                        icon = Icons.Default.Comment,
                        count = item.commentCount ?: 0,
                        label = "Comments",
                        color = Color(0xFF4CAF50)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date
                item.uploadDate?.let { uploadDate ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF9C27B0),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatDate(uploadDate),
                            color = Color(0xFF9C27B0),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Watch on Platform button
            item.platform?.let { platform ->
                Button(
                    onClick = { onAction(Action.OnWatchOnPlatformClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C5CE7),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Watch on ${platform.replaceFirstChar { it.titlecase() }}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Categories section
            if (item.categories.isNotEmpty()) {
                SectionCard(
                    title = "Categories",
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(item.categories) { category ->
                            CategoryChip(
                                text = category,
                                onClick = { onAction(Action.OnCategoryClicked(category)) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tags section
            if (item.tags.isNotEmpty()) {
                SectionCard(
                    title = "Tags",
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(item.tags) { tag ->
                            TagChip(
                                text = "#$tag",
                                onClick = { onAction(Action.OnTagClicked(tag)) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Key Points section
            item.flashcardSummary.takeIf { it.isNotBlank() }?.let { summary ->
                SectionCard(
                    title = "Key Points",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    icon = Icons.Default.Person
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        val points = summary.split("\n").filter { it.isNotBlank() }
                        points.forEach { point ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "- ",
                                    color = Color(0xFFFFC107),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = point.removePrefix("- "),
                                    color = Color(0xFFFFC107),
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    label: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = formatNumber(count.toString()),
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
//        Text(
//            text = label,
//            color = color,
//            fontSize = 12.sp
//        )
    }
}

@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        content()
    }
}

@Composable
fun CategoryChip(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(Color(0xFF2A2A2A), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TagChip(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(Color(0xFF2A2A2A), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

fun formatNumber(number: String): String {
    val count = number.replace("K", "").replace("M", "").replace(",", "").toIntOrNull() ?: 0
    return when {
        count >= 1000000 -> "${(count / 1000000.0).toString().take(3)}M"
        count >= 1000 -> "${(count / 1000.0).toString().take(3)}K"
        else -> number
    }
}

@OptIn(ExperimentalTime::class)
fun formatDate(dateString: String): String {
    return try {
        // Parse ISO-8601 string like "2025-09-29T18:16:00.000Z"
        val instant = Instant.parse(dateString)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Format manually (kotlinx-datetime doesn't provide a formatter yet)
        "${localDateTime.monthNumber}/${localDateTime.dayOfMonth}/${localDateTime.year}"
    } catch (e: Exception) {
        dateString // fallback if parsing fails
    }
}