package com.example.studentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("posts")

        val etPost: EditText = findViewById(R.id.etPost)
        val btnCreatePost: Button = findViewById(R.id.btnCreatePost)

        btnCreatePost.setOnClickListener {
            val postText = etPost.text.toString()
            val currentUser = auth.currentUser

            if (currentUser != null) {
                val userId = currentUser.uid
                val post = Post(userId, postText)

                val postId = database.push().key
                if (postId != null) {
                    database.child(postId).setValue(post)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Post creado exitosamente", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Error al crear el post", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}