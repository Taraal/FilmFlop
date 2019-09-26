package com.example.filmflop

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController


//import android.support.v7.app.AppCompatActivity

import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates

class OkHttpRequest(client: OkHttpClient) { // Small class to use okHttp quicker
    private var client = OkHttpClient()

    init {
        this.client = client
    }

    fun GET(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}

fun JSONArray.toJSONObjectList(): List<JSONObject> { // Usage of extension
    var buffer = emptyList<JSONObject>()
    for(i in 0 until this.length()) {
        buffer += this.getJSONObject(i)
    }
    return buffer
}

// =======================================================
// ======================= MAIN CLASS ====================
// =======================================================
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
