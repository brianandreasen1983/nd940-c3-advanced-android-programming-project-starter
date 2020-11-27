package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var selectedGitHubRepository: String? = null
    lateinit var loadingButton: LoadingButton

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingButton = findViewById(R.id.loadingButton)

        loadingButton.setOnClickListener {
            animateOnDownloadButtonClicked()
            download()
        }

        // Create the channel for the notification here
        // Put the channel Id and the channel name into strings.xml
//        createChannel(getString(R.string.githubRepo_notification_channel_id), getString(R.string.githubRepo_notification_channel_name))
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

//            if (downloadID == id) {
//                val action = intent.action
//                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
//                    val query = DownloadManager.Query()
//                    query.setFilterById(downloadID)
//                    val c = downloadManager!!.query(query)
//                    if (c.moveToFirst()) {
//                        val columnIndex = c
//                                .getColumnIndex(DownloadManager.COLUMN_STATUS)
//                        if (DownloadManager.STATUS_SUCCESSFUL == c
//                                        .getInt(columnIndex)
//                        ) {
//                            check_status = " Success"
//                        }
//                    }
//                }
//
//                // download completed
//                // This is likely the observable that is being called to check for state.
//                //custom_button.hasCompletedDownload()
//
//                //sendNotification(downloadID.toInt())
//            }
        }
    }

    // Get the URI from the selected git hub repository and download it otherwise provide a text that a file is not downloaded and don't call download.
    private fun download() {
        if (selectedGitHubRepository != null) {
            // Testing notification this is not the final implementation
            // We will eventually want to move this code so that the notification is only called when the download is complete.
            // Once the download is complete we will want to open it in the DetailActivity screen
            notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
            createChannel(getString(R.string.githubRepo_notification_channel_id), getString(R.string.githubRepo_notification_channel_name))
            // This shall be moved tomorrow to the right place this assumes the download is complete.
            notificationManager.sendNotification(selectedGitHubRepository.toString(), applicationContext, "In Progress")

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
            showToast(getString(R.string.noRepotSelectedText))
        }
    }

    // Unsure what or why we need the companion object for this project.
    companion object {
        private const val CHANNEL_ID = "channelId"
    }

    // Disable the button while the animation is running
    // Read more about extension functions in Kotlin slightly different than C#.
    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }
            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    // Responsible for assigning the correct github repository
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val isChecked = view.isChecked
            when (view.getId()) {

                R.id.rb_glide ->
                    if (isChecked) {
                        selectedGitHubRepository = getString(R.string.glideGithubURL)
                        showToast(getString(R.string.glideText))
                    }

                R.id.rb_loadApp ->
                    if (isChecked) {
                        selectedGitHubRepository = getString(R.string.loadAppGithubURL)
                        showToast(getString(R.string.loadAppText))
                    }

                R.id.rb_retrofit -> {
                    if (isChecked) {
                        selectedGitHubRepository = getString(R.string.retrofitGithubURL)
                        showToast(getString(R.string.retrofitText))
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    // TODO: Complete the animation function and test it to ensure completion of the Custom Views Rubric.
    // The custom button properties like background, text and additional circle are animated by changing the width, text, and color
    private fun animateOnDownloadButtonClicked() {
    }

    private fun createChannel(channelId: String, channelName: String) {
        // Check to see if the API Level is a API Level 26 as it requires a channel to be created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download is done!"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}
