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
import com.example.filmflop.ui.TitleMovie
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

    private val client = OkHttpClient()
    private val url = "https://api.themoviedb.org/3/search/movie?api_key=54c9cfc3fc5ab9ad5de0e9a0f4bbd3f2&language=en-US&query=Kill%20bill&page=1&include_adult=false"
    var titlemovies: List<TitleMovie> by Delegates.observable(emptyList()) { property, old, new ->
        refreshDisplay()
    }

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

        // Send our request
        refreshData()
    }

    private fun refreshData() {
        OkHttpRequest(client).GET(url, object: Callback {
            override fun onResponse(call: Call?, response: Response) {
                val responseData = response.body()?.string()
                runOnUiThread { // Important, we want to refresh our data on main thread (crash otherwise)
                    try {
                        val json = JSONObject(responseData)
                        println("Request Successful!!")
                        println(json)

                        // mapping from json to list of Stations
                        val titlemovies = json.getJSONArray("results").toJSONObjectList().map {
                            TitleMovie(
                                it.getString("title"),
                                it.getDouble("popularity"),
                                it.getInt("id")
                            )
                        }
                        println(titlemovies)

                        this@MainActivity.titlemovies = titlemovies
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Request Failure.")
            }
        })
    }

    private fun refreshDisplay() {
        // Complexe usage of map and when combined to show that kotlin is fucking awesome
        val cellContentTexts = titlemovies.map {
                it.name
        }

        //FilmsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cellContentTexts) // Adapter usage, we refresh the list with fresh data
    }
}
