package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var selectedGitHubRepository: String? = null

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    // Get the URI from the selected git hub repository and download it otherwise provide a text that a file is not downloaded and don't call download.
    private fun download() {
        if (selectedGitHubRepository != null) {
            val request =
                    DownloadManager.Request(Uri.parse(selectedGitHubRepository))
                            .setTitle(getString(R.string.app_name))
                            .setDescription(getString(R.string.app_description))
                            .setRequiresCharging(false)
                            .setAllowedOverMetered(true)
                            .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                    downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else {
            var noSelectionText = "Please select a file to download"
            showToast(noSelectionText)
        }

    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    // We don't want to do this here because the toast will show when the button is clicked.
    // Good for a concept.
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val isChecked = view.isChecked

            // Check which radio button was selected
            when (view.getId()) {

                R.id.rb_glide ->
                    if (isChecked) {
                        // Handle the click for this option here.
                        selectedGitHubRepository = getString(R.string.glideGithubURL)
                        val glideText = "Glide has been selected"
                        showToast(glideText)
                    }

                R.id.rb_loadApp ->
                    if (isChecked) {
                        // Handle the click for this option here.
                        selectedGitHubRepository = getString(R.string.loadAppGithubURL)
                        val loadAppText = "Load App has been selected"
                        showToast(loadAppText)
                    }

                R.id.rb_retrofit -> {
                    if (isChecked) {
                        // Handle this event
                        selectedGitHubRepository = getString(R.string.retrofitGithubURL)
                        val retrofitText = "Retrofit has been selected"
                        showToast(retrofitText)
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.show()
    }
}
