package com.example.medicinetracker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.medicinetracker.fragments.AddMed
import com.example.medicinetracker.fragments.Caretaker
import com.example.medicinetracker.fragments.HomeFragment
import com.example.medicinetracker.fragments.Userprofile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private val homeFragment = HomeFragment()
private val addmed = AddMed()
private val profile = Userprofile()
private val caretaker = Caretaker()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

       // Toast.makeText(this,"User: ${Firebase.auth.currentUser?.displayName.toString()}",Toast.LENGTH_SHORT).show()
        bottomnav.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_add -> replaceFragment(addmed)
                R.id.ic_profile -> replaceFragment(profile)
                R.id.ic_caretaker -> replaceFragment(caretaker)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    //Signout by double back

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to signout", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

    }

}