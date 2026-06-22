package com.soda.powersense.auth.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun ProfileView(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is ProfileState.Unauthenticated) {
            onLogout()
        }
    }

    val defaultAvatar = "https://i.pinimg.com/564x/57/00/c0/5700c04197ee9a4372a35ef16eb78f4e.jpg"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        when (val currentState = state) {
            is ProfileState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ProfileState.Success -> {
                val user = currentState.session.user
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    // Profile Picture
                    Box(contentAlignment = Alignment.BottomEnd) {
                        AsyncImage(
                            model = user.avatarUrl ?: defaultAvatar,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            color = Color(0xFF81C784),
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp),
                            shadowElevation = 4.dp
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = user.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212B36)
                    )
                    Text(
                        text = user.email,
                        fontSize = 14.sp,
                        color = Color(0xFF919EAB)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Personal Info Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Información Personal",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF212B36)
                                )
                                IconButton(onClick = { showEditDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = "Editar",
                                        tint = Color(0xFF81C784)
                                    )
                                }
                            }
                            
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF4F6F8))
                            
                            ProfileDetailItem(label = "Nombre", value = user.name, icon = Icons.Default.Person)
                            ProfileDetailItem(label = "Correo", value = user.email, icon = Icons.Default.Email)
                            ProfileDetailItem(label = "Estado", value = if(user.isActive) "Activo" else "Inactivo", icon = Icons.Default.Badge)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Logout Button
                    Button(
                        onClick = { viewModel.logout() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFEBEE),
                            contentColor = Color(0xFFD32F2F)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Cerrar Sesión", fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (showEditDialog) {
                    EditNameDialog(
                        currentName = user.name,
                        onDismiss = { showEditDialog = false },
                        onConfirm = { newName ->
                            viewModel.updateName(newName)
                            showEditDialog = false
                        }
                    )
                }
            }
            is ProfileState.Unauthenticated -> { /* Navigation handled by LaunchedEffect */ }
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF637381),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color(0xFF919EAB))
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212B36))
        }
    }
}

@Composable
fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Nombre") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name) },
                enabled = name.isNotBlank() && name != currentName
            ) {
                Text("Guardar", color = Color(0xFF81C784), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}
