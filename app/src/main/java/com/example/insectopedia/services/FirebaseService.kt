package com.example.insectopedia.services

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insectopedia.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FirebaseService {
    companion object {
        private lateinit var auth: FirebaseAuth
        fun signIn(
            activity: AppCompatActivity,
            context: Context
        ) {
            auth = Firebase.auth
            auth.signInWithEmailAndPassword(
                context.resources.getString(R.string.FirebaseEmail),
                context.resources.getString(R.string.FirebasePassword)
            ).addOnCompleteListener(activity) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(
                        context, "Firebase authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity.finish()
                }
            }
        }
    }
}