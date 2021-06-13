package com.example.medicinetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.medicinetracker.fragments.Caretaker
import com.example.medicinetracker.fragments.Userprofile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_caretaker_details.*
import kotlinx.android.synthetic.main.activity_patient_detail.*
import org.jetbrains.anko.toast

class CaretakerDetails : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caretaker_details)
        ctname.text=intent.getStringExtra("ctname")
        ctemail.text=intent.getStringExtra("ctemail")
    }





    fun delCaretaker(view: View) {
        auth = Firebase.auth
        ctpbar?.visibility = View.VISIBLE
        val rootRef = FirebaseFirestore.getInstance()
        val itemsRef = rootRef.collection("caretakers")
        val query: Query = itemsRef.whereEqualTo("puid", auth.currentUser?.uid.toString()).whereEqualTo("cemail", ctemail.text.toString())
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    itemsRef.document(document.id).delete()
                    Log.d("Main", "Deleted from caretaker")
                }
            } else {
                Log.d("Main", "Error getting documents: ", task.exception)
            }
        }
            .addOnFailureListener{
                Log.d("Main","Query wrong")
            }

        //deleting from patients
        val ptref = rootRef.collection("patient")
        ptref.whereEqualTo("puid",auth.currentUser?.uid.toString()).whereEqualTo("cemail",ctemail.text.toString())
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    for (document in task.result){
                        ptref.document(document.id).delete()
                        Log.d("Main","Deleted from patients")
                        ctpbar.visibility = View.INVISIBLE
                        toast("Deleted caretaker successfully")
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                }
                else {
                    Log.d("Main", "Error getting documents: ", task.exception)
                }

            }

            .addOnFailureListener{
                Log.d("Main", "Patient could not be deleted : ${it.message}")
            }

    }
}