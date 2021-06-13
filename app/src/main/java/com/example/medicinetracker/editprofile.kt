package com.example.medicinetracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_editprofile.*

class editprofile : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        profupdate.setOnClickListener{
            val newusername = newprofusername.text.toString()
            if(newusername.isEmpty()){
                Toast.makeText(this, "Cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            edit_prof_pbar.visibility = View.VISIBLE
            updateprofile()
        }
    }
    private fun updateprofile(){
        val newusername = newprofusername.text.toString()

        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .update("pname", newusername).addOnSuccessListener {
                val user = Firebase.auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = newusername

                }
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Main", "Displayname updated with name $newusername")
                            //Toast.makeText(this,"User Profilename: ${user.displayName}",Toast.LENGTH_SHORT).show()

                        }
                    }
                changectandpt(newusername)
                Toast.makeText(this, "Updated details successfully", Toast.LENGTH_SHORT).show()
                edit_prof_pbar.visibility=View.INVISIBLE
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error while updating: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun changectandpt(newusername: String) {
        val user = Firebase.auth.currentUser?.uid.toString()
        db.collection("caretakers").whereEqualTo("cuid", user).get()
            .addOnCompleteListener{ task->
                if (task.isSuccessful){
                    for (document in task.result){
                        val map: MutableMap<Any, String> = HashMap()
                        map["cname"] = newusername
                        db.collection("caretakers").document(document.id).set(map, SetOptions.merge())
                    }
                }
            }
            .addOnFailureListener{
                Log.d("edit","Not found in caretakers")
            }
        db.collection("patient").whereEqualTo("puid", user).get()
            .addOnCompleteListener{ task->
                if (task.isSuccessful){
                    for (document in task.result){
                        val map: MutableMap<Any, String> = HashMap()
                        map["pname"] = newusername
                        db.collection("patient").document(document.id).set(map, SetOptions.merge())
                    }
                }
                Log.d("edit","Edited profile successfully")
            }
            .addOnFailureListener{
                Log.d("edit","Not found in patients")
            }


    }
}