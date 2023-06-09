package com.example.studentapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //VARIABLES
        val etName: EditText = findViewById(R.id.etName)
        val etLastName: EditText = findViewById(R.id.etLastName)
        val rgSex: RadioGroup = findViewById(R.id.rgSex)
        val etDay: EditText = findViewById(R.id.etDay)
        val etMonth: EditText = findViewById(R.id.etMonth)
        val etYear: EditText = findViewById(R.id.etYear)
        val etUser: EditText = findViewById(R.id.etUser)
        val etCareer: EditText = findViewById(R.id.etCareer)
        val etCollege: EditText = findViewById(R.id.etCollege)
        val etEntryYear: EditText = findViewById(R.id.etEntryYear)
        val etEmail: EditText = findViewById(R.id.etEmailCreation)
        val etPassword: EditText = findViewById(R.id.etPasswordCreation)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        val auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener{
            val name = etName.text.toString()
            val lastName = etLastName.text.toString()
            val gender: String = when (rgSex.checkedRadioButtonId) {
                R.id.rbMan -> {
                    "Man"
                }
                R.id.rbWomen -> {
                    "Woman"
                }
                R.id.rbOther -> {
                    "Other"
                }
                else -> {
                    ""
                }
            }
            val day = etDay.text.toString()
            val month = etMonth.text.toString()
            val year = etYear.text.toString()
            val user = etUser.text.toString()
            val career = etCareer.text.toString()
            val college = etCollege.text.toString()
            val entryYear = etEntryYear.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || lastName.isEmpty() || gender.isEmpty() || day.isEmpty() ||
                month.isEmpty() || year.isEmpty() || user.isEmpty() || career.isEmpty() ||
                college.isEmpty() || entryYear.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Please fill in all the fields.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Welcome to StudentApp!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Registration failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            val userData = UserData(
                name = name,
                lastName = lastName,
                gender = gender,
                day = day,
                month = month,
                year = year,
                user = user,
                career = career,
                college = college,
                entryYear = entryYear,
                email = email,
                password = password
            )

            //Data to Database
            val database = FirebaseDatabase.getInstance()
            val ref = database.reference

            ref.child("users").push().setValue(userData)
        }
    }
}
