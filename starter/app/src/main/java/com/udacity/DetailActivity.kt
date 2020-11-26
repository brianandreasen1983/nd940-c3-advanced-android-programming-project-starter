package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    // TODO: Detail activity is used to display the name of the repository and status of the download.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        ok_button.setOnClickListener {
            returnToMainActivity()
        }
    }

    // Needs testing should return the user back to the main activity when clicked.
    private fun returnToMainActivity() {
        val  mainActivity = Intent(this, MainActivity.javaClass)
        startActivity(mainActivity)
    }

}
