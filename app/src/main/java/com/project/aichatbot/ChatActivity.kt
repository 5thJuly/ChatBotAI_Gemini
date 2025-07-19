package com.project.aichatbot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.aichatbot.ui.theme.ColorModelMessage
import com.project.aichatbot.ui.theme.ColorUserMessage


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFE9ECEF)
                    )
                )
            )
    ) {
        AppHeader()
        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )
        MessageInput(
            onMessageSend = {
                viewModel.sendMessage(it)
            }
        )
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<ModelActivity>) {
    if (messageList.isEmpty()) {
        EmptyState(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 8.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messageList.reversed()) { message ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(
                        initialOffsetX = { if (message.role == "model") -it else it },
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300))
                ) {
                    MessageRow(messageModel = message)
                }
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated AI icon
        Card(
            modifier = Modifier
                .size(120.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape
                ),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF667eea)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "ChatBot Logo",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Hello! I'm ChatBot AI",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "How can I help you today?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF6C757D),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Suggestion chips
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Try asking me about:",
                fontSize = 14.sp,
                color = Color(0xFF6C757D),
                fontWeight = FontWeight.Medium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                SuggestionChip("Creative writing")
                SuggestionChip("Problem solving")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                SuggestionChip("Learning topics")
                SuggestionChip("General questions")
            }
        }
    }
}

@Composable
fun SuggestionChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF667eea).copy(alpha = 0.1f),
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = Color(0xFF667eea),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MessageRow(messageModel: ModelActivity) {
    val isModel = messageModel.role == "model"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End
    ) {
        if (isModel) {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Top),
                shape = CircleShape,
                color = Color(0xFF667eea)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
        }

        // Message bubble
        Surface(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isModel) 4.dp else 20.dp,
                        bottomEnd = if (isModel) 20.dp else 4.dp
                    )
                ),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isModel) 4.dp else 20.dp,
                bottomEnd = if (isModel) 20.dp else 4.dp
            ),
            color = if (isModel) Color.White else Color(0xFF667eea)
        ) {
            SelectionContainer {
                Text(
                    text = messageModel.message,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = if (isModel) Color(0xFF2C3E50) else Color.White
                )
            }
        }

        if (!isModel) {
            Spacer(modifier = Modifier.width(12.dp))

            // User Avatar
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Top),
                shape = CircleShape,
                color = Color(0xFF28a745)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                value = message,
                onValueChange = { message = it },
                placeholder = {
                    Text(
                        text = "Type your message...",
                        color = Color(0xFF6C757D)
                    )
                },
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    unfocusedBorderColor = Color(0xFFDEE2E6),
                    cursorColor = Color(0xFF667eea)
                ),
                maxLines = 4
            )

            // Send button
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(elevation = 4.dp, shape = CircleShape),
                shape = CircleShape,
                color = if (message.isNotEmpty()) Color(0xFF667eea) else Color(0xFFDEE2E6)
            ) {
                IconButton(
                    onClick = {
                        if (message.isNotEmpty()) {
                            onMessageSend(message)
                            message = ""
                        }
                    },
                    enabled = message.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (message.isNotEmpty()) Color.White else Color(0xFF6C757D),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AppHeader() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF667eea),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "ChatBot",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "ChatBot AI",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Powered by Gemini Pro",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}