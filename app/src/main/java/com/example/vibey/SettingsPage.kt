package com.example.vibey

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_settings_page.*
import kotlinx.android.synthetic.main.activity_settings_page.img_icon
import kotlinx.android.synthetic.main.activity_settings_page.img_icon_ambience
import kotlinx.android.synthetic.main.activity_settings_page.img_icon_home
import kotlinx.android.synthetic.main.activity_settings_page.img_icon_settings
import kotlinx.android.synthetic.main.activity_settings_page.img_icon_theme
import kotlinx.android.synthetic.main.activity_settings_page.main_layout
import kotlinx.android.synthetic.main.activity_settings_page.tabBar
import kotlinx.android.synthetic.main.popup_window.view.*
import kotlinx.android.synthetic.main.themes_page.*

class SettingsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)

        // Set Background and Menu bar
        val intentBackground = intent
        val backgroundIndexIN = intentBackground.getIntExtra("background", 0)
        val backgroundFilesIN = arrayOf(R.drawable.bg_blue_violet, R.drawable.bg_maroon, R.drawable.bg_smith_apple, R.drawable.bg_sky_blue)
        val boxTabColors = arrayOf(R.drawable.box, R.drawable.box_tab_red,R.drawable.box_tab_green,R.drawable.box_tab_blue)
        main_layout.setBackgroundResource(backgroundFilesIN[backgroundIndexIN])
        tabBar.setBackgroundResource(boxTabColors[backgroundIndexIN])

        // Initialise background index
        var backgroundIndex = backgroundIndexIN

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

        // Store Background images
        val backgrounds = arrayOf(img_bg_violet, img_bg_maroon, img_bg_green, img_bg_blue)

        // Store background files
        val backgroundFiles = arrayOf(R.drawable.bg_blue_violet, R.drawable.bg_maroon, R.drawable.bg_smith_apple, R.drawable.bg_sky_blue)

        // Selecting a background
        for (i in backgrounds.indices){
            backgrounds[i].setOnClickListener{
                // Change check mark's constraint connection to other view (this will change check mark's position)
                val set = ConstraintSet()
                set.clone(main_layout) // get layout's ConstraintLayout
                set.connect(img_icon_tick.id, ConstraintSet.LEFT, backgrounds[i].id, ConstraintSet.LEFT, 0)
                set.connect(img_icon_tick.id, ConstraintSet.RIGHT, backgrounds[i].id, ConstraintSet.RIGHT, 0)
                set.connect(img_icon_tick.id, ConstraintSet.TOP, backgrounds[i].id, ConstraintSet.TOP, 0)
                set.connect(img_icon_tick.id, ConstraintSet.BOTTOM, backgrounds[i].id, ConstraintSet.BOTTOM, 0)
                set.applyTo(main_layout) // apply to layout's ConstraintLayout

                // Change background and menu bar color
                main_layout.setBackgroundResource(backgroundFiles[i])
                tabBar.setBackgroundResource(boxTabColors[i])

                // Assign background index
                backgroundIndex = i
            }
        }

        // Set current position of check mark
        saveCheckMarkPosition(backgrounds, backgroundIndexIN)

        // Click information icon
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
        popupLayout.txt_info_header.text = "- SETTINGS -"
        popupLayout.txt_info_paragraph.text = "Selecting one of the four background options will change the app's entire background."

        // Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        // Close pop-up window by clicking anywhere
        popupLayout.setOnClickListener {
            myPopup.dismiss()
        }
    }

    private fun saveCheckMarkPosition(backgrounds: Array<ImageView>, currentPosition: Int){
        // Selecting a background
        for (i in backgrounds.indices){
            // Change check mark's constraint connection to other view (this will change check mark's position)
            val set = ConstraintSet()
            set.clone(main_layout) // get layout's ConstraintLayout
            set.connect(img_icon_tick.id, ConstraintSet.LEFT, backgrounds[currentPosition].id, ConstraintSet.LEFT, 0)
            set.connect(img_icon_tick.id, ConstraintSet.RIGHT, backgrounds[currentPosition].id, ConstraintSet.RIGHT, 0)
            set.connect(img_icon_tick.id, ConstraintSet.TOP, backgrounds[currentPosition].id, ConstraintSet.TOP, 0)
            set.connect(img_icon_tick.id, ConstraintSet.BOTTOM, backgrounds[currentPosition].id, ConstraintSet.BOTTOM, 0)
            set.applyTo(main_layout) // apply to layout's ConstraintLayout
        }
    }
}