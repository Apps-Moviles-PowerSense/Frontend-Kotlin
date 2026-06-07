package com.soda.powersense.alerts.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soda.powersense.alerts.domain.model.Alert

@Composable
fun AlertItem(alert: Alert){

    Card(
        modifier = Modifier.fillMaxSize().padding(8.dp),
    ) {

        Column {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = alert.message,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = alert.type,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }
    }
}