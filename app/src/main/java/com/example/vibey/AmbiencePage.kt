package com.example.vibey

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_ambience_page.*
import kotlinx.android.synthetic.main.activity_ambience_page.img_icon
import kotlinx.android.synthetic.main.activity_ambience_page.img_icon_ambience
import kotlinx.android.synthetic.main.activity_ambience_page.img_icon_home
import kotlinx.android.synthetic.main.activity_ambience_page.img_icon_settings
import kotlinx.android.synthetic.main.activity_ambience_page.img_icon_theme
import kotlinx.android.synthetic.main.activity_ambience_page.main_layout
import kotlinx.android.synthetic.main.activity_ambience_page.tabBar
import kotlinx.android.synthetic.main.popup_window.view.*

@Suppress("DEPRECATION", "SameParameterValue")
class AmbiencePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambience_page)

        // Set Background and Menu bar
        val intentBackground = intent
        val backgroundIndex = intentBackground.getIntExtra("background", 0)
        val backgroundFiles = arrayOf(R.drawable.bg_blue_violet, R.drawable.bg_maroon, R.drawable.bg_smith_apple, R.drawable.bg_sky_blue)
        val boxTabColors = arrayOf(R.drawable.box, R.drawable.box_tab_red,R.drawable.box_tab_green,R.drawable.box_tab_blue)
        main_layout.setBackgroundResource(backgroundFiles[backgroundIndex])
        tabBar.setBackgroundResource(boxTabColors[backgroundIndex])

        // Get Name and Current Mood
        val intent2 = intent
        val getName = intent2.getStringExtra("name")
        val getMoodIndex = intent2.getIntExtra("mood", 0)

        // Set Name and Current Mood
        var setName = getName
        var moodIndex = getMoodIndex

        // Menu bar buttons and pages
        val menuButtons = arrayOf(img_icon_home, img_icon_theme, img_icon_ambience, img_icon_settings)
        val storePages = arrayOf(HomePage::class.java, ThemesPage::class.java, AmbiencePage::class.java, SettingsPage::class.java)

        // Assign each menu buttons to open the respective pages
        for (i in menuButtons.indices){
            menuButtons[i].setOnClickListener{
                val intent = Intent(this, storePages[i])
                intent.putExtra("background", backgroundIndex) // pass background index
                intent.putExtra("name", setName) // pass name
                intent.putExtra("mood", moodIndex) // pass selected mood's index
                startActivity(intent) // redirect to page
            }
        }

        // Display ambiences list
        displayAmbiences()

        img_icon.setOnClickListener{
            openPopUpWindow()
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun openPopUpWindow(){
        // Create pop-up window
        val popupLayout = layoutInflater.inflate(R.layout.popup_window, null)

        // Make pop-up window as dialog
        val myPopup = Dialog(this)
        myPopup.setContentView(popupLayout)

        // Set information to pop-up window
        popupLayout.txt_info_header.text = "- AMBIENCES -"
        popupLayout.txt_info_paragraph.text = "A collection of ambience sounds to help you immerse yourself in an environment.\n\nThis helps you to soothe your mind and make you feel less anxious.\n\nIt can used for studying, meditating, relaxing etc. "

        // Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        // Close pop-up window by clicking anywhere
        popupLayout.setOnClickListener {
            myPopup.dismiss()
        }
    }

    private fun displayAmbiences(){
        // Store frames
        val ambienceFrames = arrayOf(frame_cafe, frame_woodland, frame_fireplace, frame_rainforest, frame_island)

        // Play animation of displaying each frame
        var initDelay: Long = 0 // initialise animation delay
        val delayMilliSecs : Long = 150 // add delay to play each animation
        for (frame in ambienceFrames) {
            // Hide frames first
            displayViews(arrayOf(frame), false)

            // Play animation and display each frame
            Handler().postDelayed({
                displayViews(arrayOf(frame), true)
                playAnimation(arrayOf(frame), Anim.FadeInSlide)
            }, delayMilliSecs+initDelay)

            // Increment animation delay
            initDelay+=delayMilliSecs
        }

        // Click on one of the Ambiences
        for (i in ambienceFrames.indices){
            ambienceFrames[i].setOnClickListener {
                // Open ambience's YouTube
                openAmbienceYouTube(i)
            }
        }
    }

    // Function for opening ambience's YouTube
    private fun openAmbienceYouTube(ambienceIndex: Int){
        // Store YouTube links for ambiences
        val ambienceLinks = arrayOf(
            "gaGrHUekGrc", // cafe
            "R_NVBWODk2A", // woodland
            "5_NgwbEs4JE", // fireplace
            "d0Xi5yYIUo4", // rainforest
            "Nep1qytq9JM", // tropical island
        )

        // Open YouTube based on the ambience selectec
        openYoutubeLink(ambienceLinks[ambienceIndex])
    }

    // Function for opening YouTube by passing ID
    private fun openYoutubeLink(youtubeID: String) {
        // YouTube link for app
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeID"))

        // Youtube link for browser
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$youtubeID"))

        // Error handler if user doesn't YouTube app, open browser instead
        try {
            // Open Youtube App
            this.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            // Open YouTube from Browser
            this.startActivity(intentBrowser)
        }
    }

    // Function responsible for showing or hiding an array of views
    private fun displayViews(storeViews: Array<View>, visibility: Boolean) {
        // Show views
        if (visibility) {
            for (views in storeViews) {
                views.visibility = View.VISIBLE
            }
        }
        // Hide views
        else {
            for (views in storeViews){
                views.visibility = View.GONE
            }
        }
    }

    // Function responsible for playing animations
    private fun playAnimation(storeViews: Array<View>, animSelection: Anim) {
        // Load animations
        val animFadeInSlide = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide)
        val animFadeOutSlide = AnimationUtils.loadAnimation(this, R.anim.fade_out_slide)
        val animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Select animation by passing an enum value
        when (animSelection) {
            // Clear animation
            Anim.ClearAnim -> {
                for (views in storeViews) {
                    views.clearAnimation()
                }
            }

            // Fade-In Sliding animation
            Anim.FadeInSlide -> {
                for (views in storeViews) {
                    views.startAnimation(animFadeInSlide)
                }
            }

            // Fade-Out Sliding animation
            Anim.FadeOutSlide -> {
                for (views in storeViews) {
                    views.startAnimation(animFadeOutSlide)
                }
            }

            // Fade-In animation
            Anim.FadeIn -> {
                for (views in storeViews) {
                    views.startAnimation(animFadeIn)
                }
            }
        }
    }

    // Enum class for storing animations
    enum class Anim {
        ClearAnim, FadeInSlide, FadeOutSlide, FadeIn
    }
}