package com.quick.shop

import android.app.Activity
import android.content.Intent
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

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_function.view.*

class MainActivity : AppCompatActivity() {
    var signup = false
    val auth = FirebaseAuth.getInstance()
    private val RC_NICKNAME: Int = 210
    private val RC_SIGNUP: Int = 200
    val functions = listOf("Camera",
        "Invite friend",
        "Parking",
        "Download coupons",
        "A",
        "B",
        "News",
        "News",
        "News",
        "News",
        "Maps")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        /*if (!signup) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }*/
        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
        //RecyclerView

        recyler.layoutManager = LinearLayoutManager(this)
        recyler.setHasFixedSize(true)
        recyler.adapter = FunctionAdapter()
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
        }

    }

    class FunctionHolder(view : View) : RecyclerView.ViewHolder(view){
        var nameText : TextView = view.name
    }

    override fun onResume() {
        super.onResume()
//        nickname.text = getNickname()
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    nickname.text = dataSnapshot.value as String
                }

            })
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        } else {
            Log.d("MainActivity", "authChanged: ${auth.currentUser?.uid}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NicknameActivity::class.java)
                startActivityForResult(intent, RC_NICKNAME)
            }
        }
        if (requestCode == RC_NICKNAME) {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
