package com.quick.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import java.net.URL

class BusActivity : AppCompatActivity(), AnkoLogger {
    var buses:List<Bus>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
        //get jsond
        doAsync {
            val json = URL("https://api.myjson.com/bins/1aojef").readText()
            buses = Gson().fromJson<List<Bus>>(json,
                object : TypeToken<List<Bus>>(){}.type)
            buses?.forEach {
                info("${it.BusID} ${it.RouteID} ${it.Speed}")
            }
            uiThread {
                recycler.layoutManager = LinearLayoutManager(this@BusActivity) as RecyclerView.LayoutManager?
                recycler.setHasFixedSize(true)
                recycler.adapter = BusAdapter()
            }
        }

    }

    inner class BusAdapter() : RecyclerView.Adapter<BusHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_bus, parent, false)
            return BusHolder(view)
        }

        override fun getItemCount(): Int {
            val size = buses?.size?: 0
            return size
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            val bus = buses?.get(position)
            holder.bindBus(bus!!)
        }

    }

    inner class BusHolder(view: View) : RecyclerView.ViewHolder(view){
        val idText: TextView = view.bus_id
        val routeText: TextView = view.route_id
        val speedText: TextView = view.speed
        fun bindBus(bus: Bus) {
            idText.text = bus.BusID
            routeText.text = bus.RouteID
            speedText.text = bus.Speed
        }
    }
}

data class Bus(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)