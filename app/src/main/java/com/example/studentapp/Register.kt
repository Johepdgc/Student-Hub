package com.example.studentapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.studentapp.R.id.etCollege
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //VARIABLES
        val etName: EditText = findViewById(R.id.etName)
        val etLastName: EditText = findViewById(R.id.etLastName)
        var rgSex: RadioGroup = findViewById(R.id.rgSex)
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

        var auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener{
            var name = etName.text.toString()
            var lastName = etLastName.text.toString()
            var selectedGenderId = rgSex.checkedRadioButtonId
            var gender: String = if (selectedGenderId == R.id.rbMan) {
                "Man"
            } else if (selectedGenderId == R.id.rbWomen) {
                "Woman"
            } else if (selectedGenderId == R.id.rbOther) {
                "Other"
            } else {
                ""
            }
            var day = etDay.text.toString()
            var month = etMonth.text.toString()
            var year = etYear.text.toString()
            var user = etUser.text.toString()
            var career = etCareer.text.toString()
            var college = etCollege.text.toString()
            var entryYear = etEntryYear.text.toString()
            var email = etEmail.text.toString()
            var password = etPassword.text.toString()

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
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Registration successful.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        // If sign in fails, display a message to the user.
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

            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }
}