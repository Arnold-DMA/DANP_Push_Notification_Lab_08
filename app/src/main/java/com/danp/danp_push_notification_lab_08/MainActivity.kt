package com.danp.danp_push_notification_lab_08

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danp.danp_push_notification_lab_08.ui.theme.DANP_Push_Notification_Lab_08Theme
import com.danp.danp_push_notification_lab_08.ui.theme.Purple500
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DANP_Push_Notification_Lab_08Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.d("Notificación FCM", "Error al obtener el token de registro de FCM", task.exception)
                                return@OnCompleteListener
                            }

                            val token: String? = task.result
                            Log.d("Token FCM", token, task.exception)
                            Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
                        })

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Notificaciones FCM",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Purple500)
                                    .padding(15.dp)
                            )
                        }

                        val extraStadistica: String? = intent.getStringExtra("accion")
                        var estad: Boolean = false
                        extraStadistica?.let {
                            Log.d("etiqueta", extraStadistica.toString())
                            Estadistica()
                            estad = true
                        }
                        val extraBotones: Boolean = intent.getBooleanExtra("botones", false)

                        if(extraBotones) {
                            Log.d("etiqueta", extraBotones.toString())
                            Opciones()
                            estad = true
                        }

                        if (!estad)
                            FirebaseMessaging.getInstance().token
                                .addOnCompleteListener(OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        Log.d("Notificación FCM", "Error al obtener el token de registro de FCM", task.exception)
                                        return@OnCompleteListener
                                    }

                                    val token: String? = task.result
                                })
                            Principal()
                    }
                }
            }
        }
    }

    @Composable
    fun Principal() {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_lecturas),
                contentDescription = "Lectura",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }

    @Composable
    fun Opciones(){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {

            }) {
                Text(text = "Nueva Lectura")
            }
            Button(onClick = {

            }) {
                Text(text = "Ver registros")
            }
        }
    }

    @Composable
    fun Estadistica() {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_estadistica),
                contentDescription = "Estadistica",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}

