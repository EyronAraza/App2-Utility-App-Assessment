package com.example.vibey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_splash_intro_page.*
import kotlinx.android.synthetic.main.activity_splash_intro_page.main_layout

@Suppress("DEPRECATION", "SameParameterValue", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class SplashIntroPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_intro_page)
        // Play Splash Screen Intro
        displayViews(arrayOf(img_logo,txt_app_title),true) // show app logo
        playAnimation(arrayOf(img_logo, txt_app_title), Anim.FadeIn) // play fade in animation

        // Initialise Name and Mood
        var username = ""
        var moodSelectedIndex = 0

        // hide certain views
        displayViews(arrayOf(txt_enter_name, editTxt_name, btn_next, btn_start, txt_select_mood, img_box_trans1,
            img_happy, img_sad, img_calm, btn_start),false)

        Handler().postDelayed({
            playAnimation(arrayOf(img_logo, txt_app_title), Anim.FadeOut) // play fade out animation
        },2400)

        Handler().postDelayed({
            displayViews(arrayOf(img_logo,txt_app_title),false) // hide app logo
        },4400)

        Handler().postDelayed({
            playAnimation(arrayOf(txt_enter_name,editTxt_name,btn_next), Anim.FadeInSlideLeft) // play fade in slide animation
            displayViews(arrayOf(txt_enter_name,editTxt_name,btn_next),true) // display enter name
        },4600)

        // Next Button after inputting name
        btn_next.setOnClickListener{
            playAnimation(arrayOf(txt_enter_name,editTxt_name,btn_next), Anim.FadeOutSlide) // play fade out slide animation

            // Check if user inputs their name
            username = if (editTxt_name.text.toString().trim().isNotEmpty()){
                editTxt_name.text.toString()
            } else {
                "User" // set to default name if no input
            }

            Handler().postDelayed({
                displayViews(arrayOf(txt_enter_name,editTxt_name,btn_next),false) // hide views

            },900)

            // Display mood selection
            Handler().postDelayed({
                playAnimation(arrayOf(txt_select_mood, img_box_trans1, img_happy, img_sad, img_calm, btn_start), Anim.FadeInSlideLeft) // play fade in slide animation
                displayViews(arrayOf(txt_select_mood, img_box_trans1, img_happy, img_sad, img_calm, btn_start),true) // display enter name
            },1000)
        }

        // Mood Selection
        val moodViews = arrayOf(img_happy, img_calm, img_sad)
        for (i in moodViews.indices){
            moodViews[i].setOnClickListener{
                // Change selection's constraint connection to other view
                val set = ConstraintSet()
                set.clone(main_layout) // get layout's ConstraintLayout
                set.connect(img_box_trans1.id, ConstraintSet.LEFT, moodViews[i].id, ConstraintSet.LEFT, 0)
                set.connect(img_box_trans1.id, ConstraintSet.RIGHT, moodViews[i].id, ConstraintSet.RIGHT, 0)
                set.connect(img_box_trans1.id, ConstraintSet.TOP, moodViews[i].id, ConstraintSet.TOP, 0)
                set.connect(img_box_trans1.id, ConstraintSet.BOTTOM, moodViews[i].id, ConstraintSet.BOTTOM, 0)
                set.applyTo(main_layout) // apply to layout's ConstraintLayout

                // Pass mood index
                moodSelectedIndex = i
            }
        }

        // Start Button
        btn_start.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            intent.putExtra("name", username) // pass name
            intent.putExtra("mood", moodSelectedIndex) // pass selected mood's index
            startActivity(intent) // redirect to home page
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
        val animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val animFadeInSlideLeft = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_left)

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

            // Fade-Out animation
            Anim.FadeOut -> {
                for (views in storeViews) {
                    views.startAnimation(animFadeOut)
                }
            }

            // Fade-Out animation
            Anim.FadeInSlideLeft -> {
                for (views in storeViews) {
                    views.startAnimation(animFadeInSlideLeft)
                }
            }
        }
    }

    // Enum class for storing animations
    enum class Anim {
        ClearAnim, FadeInSlide, FadeOutSlide, FadeIn, FadeOut, FadeInSlideLeft
    }
}