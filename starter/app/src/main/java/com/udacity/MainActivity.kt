package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


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
        loadingButton.setLoadingButtonState(ButtonState.Completed)
        loadingButton.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val action = intent.action

            if (downloadID == id) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    val query = DownloadManager.Query()
                    query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
                    val manager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val cursor: Cursor = manager.query(query)
                    if (cursor.moveToFirst()) {
                        if (cursor.count > 0) {
                            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                loadingButton.setLoadingButtonState(ButtonState.Completed)
                                notificationManager.sendNotification(selectedGitHubRepository.toString(), applicationContext, "Complete")
                            } else {
                                loadingButton.setLoadingButtonState(ButtonState.Completed)
                                notificationManager.sendNotification(selectedGitHubRepository.toString(), applicationContext, "Failed")
                            }
                        }
                    }
                }
            }
        }
    }

    // Get the URI from the selected git hub repository and download it otherwise provide a text that a file is not downloaded and don't call download.
    private fun download() {
        loadingButton.setLoadingButtonState(ButtonState.Clicked)
        animateOnDownload()

        if (selectedGitHubRepository != null) {
            loadingButton.setLoadingButtonState(ButtonState.Loading)
            notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
            createChannel(getString(R.string.githubRepo_notification_channel_id), getString(R.string.githubRepo_notification_channel_name))

            var file = File(getExternalFilesDir(null), "/repos")

            if (!file.exists()) {
                file.mkdirs()
            }

            val request =
                    DownloadManager.Request(Uri.parse(selectedGitHubRepository))
                            .setTitle(getString(R.string.app_name))
                            .setDescription(getString(R.string.app_description))
                            .setRequiresCharging(false)
                            .setAllowedOverMetered(true)
                            .setAllowedOverRoaming(true)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/repos/repository.zip")


            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                    downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } else {
            // Might need to set button state to its default of Completed.
//            loadingButton.setLoadingButtonState(ButtonState.Completed)
            showToast(getString(R.string.noRepotSelectedText))
        }
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }

    // Disable the button while the animation is running
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

    // Used for animating the button background and such definitely not final implementation lol......
    private fun animateOnDownload() {
        val animator = ObjectAnimator.ofFloat(loadingButton, View.TRANSLATION_Y, 200f)
        // tells the animation how many times to repeat after the initial run.
        animator.repeatCount = 1
        // tells the behavior its supposed to have during the repetition.
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(loadingButton)
        animator.start()
    }

    private fun createChannel(channelId: String, channelName: String) {
        // Check to see if the API Level is a API Level 26 as it requires a channel to be created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download is done!"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
} // End of Main Activity
