package com.soda.powersense.alerts.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soda.powersense.alerts.domain.model.Alert

@Composable
fun AlertItem(
    alert: Alert,
    onDelete: (String) -> Unit = {}
) {
    val (backgroundColor, contentColor, icon) = when (alert.severity.uppercase()) {
        "CRITICAL", "ERROR" -> Triple(Color(0xFFFFEBEE), Color(0xFFD32F2F), Icons.Default.ErrorOutline)
        "WARNING" -> Triple(Color(0xFFFFF8E1), Color(0xFFF57C00), Icons.Default.WarningAmber)
        "INFO" -> Triple(Color(0xFFE3F2FD), Color(0xFF1976D2), Icons.Default.NotificationsNone)
        "SUCCESS", "COMPLETED" -> Triple(Color(0xFFE8F5E9), Color(0xFF388E3C), Icons.Default.CheckCircleOutline)
        else -> Triple(Color(0xFFF5F5F5), Color(0xFF616161), Icons.Default.Info)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = alert.type.replace("_", " ").lowercase().capitalize(),
                        color = contentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    
                    if (!alert.acknowledged) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFF66BB6A),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Nuevo",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = alert.message,
                    color = contentColor.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Hace poco", // In a real app, calculate time from alert.createdAt
                    color = contentColor.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
            
            IconButton(
                onClick = { onDelete(alert.id) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    tint = contentColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Extension function to capitalize
private fun String.capitalize() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
