package com.example.biletzone.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.biletzone.R
import com.example.biletzone.adapters.EventItemsAdapter
import com.example.biletzone.databinding.ActivityMainBinding
import com.example.biletzone.models.Event
import com.example.biletzone.models.Profile
import com.example.biletzone.services.EventService
import com.example.biletzone.utils.Constants
import com.example.biletzone.utils.Constants.PROFILE_KEY
import com.example.biletzone.utils.Constants.userProfile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var binding: ActivityMainBinding? = null
    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupActionBar()

        binding?.navView?.setNavigationItemSelectedListener(this)

        EventService().loadEventsData(this@MainActivity, true)

        binding?.drawerLayout?.findViewById<FloatingActionButton>(R.id.fab_create_event)?.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateEventActivity::class.java)
            startActivity(intent)
        }
    }

    public fun getJwtTokenFromSharedPreferences(): String? {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.JWT_SHARED_PREF_NAME, "")
    }

    private fun setupActionBar() {
        val mainToolbar: Toolbar? = binding?.drawerLayout?.findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(mainToolbar)
        mainToolbar?.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        mainToolbar?.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        val drawer: DrawerLayout? = binding?.drawerLayout
        if(drawer?.isDrawerOpen(GravityCompat.START) == true) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer?.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val drawer: DrawerLayout? = binding?.drawerLayout
        if(drawer?.isDrawerOpen(GravityCompat.START) == true) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_my_profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                intent.putExtra(PROFILE_KEY, userProfile)
                startActivity(intent)
//                Toast.makeText(this@MainActivity, "Clicked my Profile", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_auth -> {
                val intent = Intent(this@MainActivity, IntroActivity::class.java)
                startActivity(intent)
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)

        return true
    }

    fun updateNavigationUserDetails(profile: Profile, readBoardList: Boolean) {
//        hideProgressDialog()
        this.profile = profile
        val navUserName: TextView? = binding?.navView?.findViewById(R.id.tv_username)

        if (navUserName != null) {
            navUserName.text = "${profile.firstName} ${profile.lastName}"
        }

        if (readBoardList) {
//            showProgressDialog(resources.getString(R.string.please_wait))
            EventService().getEvents(this)
        }
    }

    fun populateEventListInUI(eventList: ArrayList<Event>) {
//        hideProgressDialog()
        val rvBoards: RecyclerView? = binding?.drawerLayout?.findViewById(R.id.rv_events_list)
        val tvNoBoards: TextView? = binding?.drawerLayout?.findViewById(R.id.tv_no_events_available)

        if (rvBoards != null && tvNoBoards != null) {
            rvBoards.layoutManager = LinearLayoutManager(this)

            if (eventList.size > 0) {

                rvBoards.visibility = View.VISIBLE
                tvNoBoards.visibility = View.GONE

                rvBoards.setHasFixedSize(true)

                val adapter = EventItemsAdapter(this, eventList)
                rvBoards.adapter = adapter
                adapter.setOnClickListener(object: EventItemsAdapter.OnClickListener{
                    override fun onClick(position: Int, model: Event) {
//                        val intent = Intent(this@MainActivity, TaskListActivity::class.java)
//                        intent.putExtra(Constants.DOCUMENT_ID, model.documentID)
//                        startActivity(intent)
                    }
                })
            } else {
                rvBoards.visibility = View.GONE
                tvNoBoards.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}