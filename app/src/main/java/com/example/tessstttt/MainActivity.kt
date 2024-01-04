package com.example.tessstttt

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tessstttt.ui.theme.lightBlue

import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}
@Composable
fun WeatherApp() {

    var weather by remember { mutableStateOf<Weather?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        fetchWeatherData(context, "Izmail") { result ->
            result.onSuccess { data ->
                weather = data
            }
        }
    }

    Image(
        painter = painterResource(id = R.drawable.background_image),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.6f),
        contentScale = ContentScale.FillBounds
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            shape = RoundedCornerShape(3.dp),
            backgroundColor = lightBlue,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            elevation = 1.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weather?.let {
                        Text(
                            text = it.timeNow,
                            style = TextStyle(fontSize = 15.sp),
                            color = Color.White
                        )
                    }
                    AsyncImage(
                        model = weather?.weatherIcon,
                        contentDescription = "im2",
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
                weather?.let {
                    Text(
                        text = it.name,
                        style = TextStyle(fontSize = 20.sp),
                        color = Color.White
                    )
                }
                weather?.let {
                    Text(
                        text = it.currentTemp,
                        style = TextStyle(fontSize = 35.sp),
                        color = Color.White
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = null,
                        tint = Color.White,
                    )
                    weather?.let {
                        Text(
                            text = it.currentCondiotin,
                            style = TextStyle(fontSize = 15.sp),
                            color = Color.White
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.refresh_img),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}
        data class Weather(
            val name: String,
            val timeNow: String,
            val currentTemp: String,
            val currentCondiotin: String,
            val weatherIcon: String
        )
fun fetchWeatherData(
    context: android.content.Context,
    cityName: String,
    onResult: (Result<Weather>) -> Unit
) {
    val url =
        "https://api.weatherapi.com/v1/forecast.json?key=2424de849688485f9b583700231812&q=Izmail&days=1&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)

    val request = StringRequest(Request.Method.GET, url,
        { response ->
            try {
              val weather = Weather(
                    JSONObject(response).getJSONObject("location").getString("name"),
                    JSONObject(response).getJSONObject("location").getString("localtime"),
                    JSONObject(response).getJSONObject("current").getString("temp_c"),
                    JSONObject(response).getJSONObject("current").getJSONObject("condition")
                        .getString("text"),
                    "https:${JSONObject(response).getJSONObject("current").getJSONObject("condition")
                        .getString("icon")}"
                )
                onResult(Result.success(weather))
            } catch (e: Exception) {

                e.printStackTrace()
                onResult(Result.failure(e))
            }
        },
        { error ->
            onResult(Result.failure(error))
        })
    queue.add(request)
}