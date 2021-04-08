package com.quick.shop

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_function.view.*
import org.jetbrains.anko.*
import java.net.URL

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener, AnkoLogger{
    private val TAG = MainActivity::class.java.simpleName
    var signup = false
    var cacheService:Intent? = null
    val auth = FirebaseAuth.getInstance()
    private val RC_NICKNAME: Int = 210
    private val RC_SIGNUP: Int = 200
    private val RC_SIGNIN = 100
    val functions = listOf("Camera",
        "Invite friend",
        "Parking",
        "Download coupons",
        "News",
        "Movie",
        "Bus",
        "News",
        "News",
        "News",
        "Maps")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(it, "Verify email sent", Snackbar.LENGTH_LONG).show()
                    }
                }
        }

        //Spinner
        val colors = arrayOf("Red", "Green", "Blue")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d("MainActivity", "onItemSelected: ${colors[position]}")
            }

        }
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
            val user = auth.currentUser
            Log.d(TAG, "onAuthStateChanged: ${user?.uid}")
            if (user != null) {
                user_info.setText("Email: ${user.email} / ${user.isEmailVerified}")
                verify_email.visibility = if (user.isEmailVerified) View.GONE else View.VISIBLE
            } else {
                user_info.setText("Not login")
                verify_email.visibility = View.GONE
            }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_function, parent, false)
            val holder = FunctionHolder(view)
            return holder
        }

        override fun getItemCount(): Int {
            return functions.size
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nameText.text = functions.get(position)
            holder.itemView.setOnClickListener { view ->
                functionClicked(holder, position)
            }
        }

    }

    private fun functionClicked(holder: FunctionHolder, position: Int) {
        Log.d(TAG, "functionClicked: $position")
        when(position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
            2 -> startActivity(Intent(this, ParkingActivity::class.java))
            5 -> startActivity(Intent(this, MovieActivity::class.java))
            6 -> startActivity(Intent(this, BusActivity::class.java))

        }
    }

    class FunctionHolder(view : View) : RecyclerView.ViewHolder(view){
        var nameText : TextView = view.name
    }

    override fun onResume() {
        super.onResume()
//        nickname.text = getNickname()
        if (auth.currentUser != null) {
            FirebaseDatabase.getInstance()
                .getReference("users")
                .child(auth.currentUser!!.uid)
                .child("nickname")
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.value != null)
                            nickname.text = dataSnapshot.value as String
                    }
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(CacheService.ACTION_CACHE_DONE))
//                toast("MainActivity cache informed")
                info("MainActivity cache informed")
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signin -> {
                startActivityForResult(Intent(this, SignInActivity::class.java),
                    RC_SIGNIN)
                true
            }
            R.id.action_signout -> {
                FirebaseAuth.getInstance().signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
