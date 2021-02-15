package com.example.lestutosdeproc

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        // remove app title
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) { Log.d("ERROR", e.toString()) }

        // start instance
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "Starting instance")

        // make app fullscreen
        try{
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } catch (e: NullPointerException) { Log.d("TAG", e.toString()) }

        // default activity main layout
        setContentView(R.layout.activity_main)

        // webview to show processus'peertube
        val myWebView: WebView = findViewById(R.id.procwebview)
        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
        myWebView.loadUrl("https://peertube.lestutosdeprocessus.fr/videos/recently-added")



        // imagebutton for discord app
        val discordButton: ImageButton = findViewById(R.id.discord) as ImageButton
        discordButton.setOnClickListener {
            try {
                val url = "https://discord.gg/JJNxV2h"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(getApplicationContext(), "There are no web clients installed.", Toast.LENGTH_SHORT).show()
            }
        }


        // imagebutton for mail app
        val mailButton: ImageButton = findViewById(R.id.mail) as ImageButton
        mailButton.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("processus@thiefin.fr"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Email via l'application Android")
            i.putExtra(Intent.EXTRA_TEXT, "")
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show()
            }
        }


        Log.d("ProcService", "Starting ProcService from MainActivity")
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(11, ComponentName(this, ProcService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                .setPersisted(true)
                .build()
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val s = scheduler.schedule(jobInfo)

    }
}