@file:Suppress("DEPRECATION")

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
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_home_page.frame_song1
import kotlinx.android.synthetic.main.activity_home_page.frame_song2
import kotlinx.android.synthetic.main.activity_home_page.frame_song3
import kotlinx.android.synthetic.main.activity_home_page.frame_song4
import kotlinx.android.synthetic.main.activity_home_page.img_artist1
import kotlinx.android.synthetic.main.activity_home_page.img_artist2
import kotlinx.android.synthetic.main.activity_home_page.img_artist3
import kotlinx.android.synthetic.main.activity_home_page.img_artist4
import kotlinx.android.synthetic.main.activity_home_page.img_icon
import kotlinx.android.synthetic.main.activity_home_page.img_icon_ambience
import kotlinx.android.synthetic.main.activity_home_page.img_icon_home
import kotlinx.android.synthetic.main.activity_home_page.img_icon_settings
import kotlinx.android.synthetic.main.activity_home_page.img_icon_theme
import kotlinx.android.synthetic.main.activity_home_page.img_song1
import kotlinx.android.synthetic.main.activity_home_page.img_song2
import kotlinx.android.synthetic.main.activity_home_page.img_song3
import kotlinx.android.synthetic.main.activity_home_page.img_song4
import kotlinx.android.synthetic.main.activity_home_page.main_layout
import kotlinx.android.synthetic.main.activity_home_page.tabBar
import kotlinx.android.synthetic.main.activity_home_page.txt_songDetails1
import kotlinx.android.synthetic.main.activity_home_page.txt_songDetails2
import kotlinx.android.synthetic.main.activity_home_page.txt_songDetails3
import kotlinx.android.synthetic.main.activity_home_page.txt_songDetails4
import kotlinx.android.synthetic.main.activity_home_page.txt_songTitle1
import kotlinx.android.synthetic.main.activity_home_page.txt_songTitle2
import kotlinx.android.synthetic.main.activity_home_page.txt_songTitle3
import kotlinx.android.synthetic.main.activity_home_page.txt_songTitle4
import kotlinx.android.synthetic.main.popup_window.view.*
import kotlinx.android.synthetic.main.themes_page.*

@Suppress("SameParameterValue")
class HomePage : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

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
        txt_welcome.text = "Welcome, $setName!"
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

        // Store images of mood faces
        val moodFacesImages = arrayOf(R.drawable.img_face_happy, R.drawable.img_face_calm, R.drawable.img_face_sad)

        // Display current mood
        img_mood_changer.setImageResource(moodFacesImages[moodIndex])

        // Display songs
        displaySongs(moodIndex)

        // Click to change moods
        img_mood_changer.setOnClickListener{
            // Disable text
            txt_clickToChangeMood.visibility = View.GONE

            // Increment mood index
            moodIndex += 1

            // Go back to first index for mood changer if reached more than the limit
            if (moodIndex > moodFacesImages.size - 1){
                moodIndex = 0
            }

            // Change a mood
            img_mood_changer.setImageResource(moodFacesImages[moodIndex])

            // Change in displaying songs
            displaySongs(moodIndex)
        }

        // Click information icon
        img_icon.setOnClickListener{
            openPopUpWindow()
        }
    }

    private fun openPopUpWindow(){
        // Create pop-up window
        val popupLayout = layoutInflater.inflate(R.layout.popup_window, null)

        // Make pop-up window as dialog
        val myPopup = Dialog(this)
        myPopup.setContentView(popupLayout)

        // Set information to pop-up window
        popupLayout.txt_info_header.text = "- HOME -"
        popupLayout.txt_info_paragraph.text = "Discover a collection of songs based on your current mood. Pressing on one of the songs will redirect you to YouTube."

        // Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        // Close pop-up window by clicking anywhere
        popupLayout.setOnClickListener {
            myPopup.dismiss()
        }
    }

    // Function for displaying songs
    private fun displaySongs(moodIndex: Int){
        // Get Box images
        val boxImageView = arrayOf(img_song1, img_song2, img_song3, img_song4, img_song5)

        // Initialise Box Colours
        val boxColors = arrayOf(
            R.drawable.img_box_happy,
            R.drawable.img_box_calm,
            R.drawable.img_box_sad)

        // Change Box Colours based on Mood's index
        for (img in boxImageView){
            img.setImageResource(boxColors[moodIndex])
        }

        var initDelay : Long = 0 // initialise animation delay
        val delayMilliSecs : Long = 150 // initialise adding delay to play each animation

        // Store frames of songs
        val songFrames = arrayOf(frame_song1, frame_song2, frame_song3, frame_song4, frame_song5)
        for (i in songFrames.indices) {
            // Hide frames first
            displayViews(arrayOf(songFrames[i]), false)

            // Play animation and display each frame
            Handler().postDelayed({
                displayViews(arrayOf(songFrames[i]), true)
                playAnimation(arrayOf(songFrames[i]), Anim.FadeInSlide)
            }, delayMilliSecs+initDelay)

            // Increment animation delay
            initDelay+=delayMilliSecs

            // Click on one of the songs
            songFrames[i].setOnClickListener{
                // Open song's YouTube by passing song's index and mood's index
                openSongYoutube(i, moodIndex)
            }
        }

        // Update song details
        displaySongsDetails(moodIndex)
    }

    // Function for opening song's YouTube
    private fun openSongYoutube(songIndex: Int, moodIndex: Int){
        // Store YouTube links
        val songLinks = arrayOf(
            // Happy songs links
            arrayOf(
                "xYEXbulCq2Y", // link 1
                "UqyT8IEBkvY", // link 2
                "EXWOJvlDwbU", // link 3
                "WXrdYwG17PE", // link 4
                "BOyO8sZOaOQ" // link 5
            ),

            // Calm songs links
            arrayOf(
                "hpfLKbjTWn0", // link 1
                "jDv72Ul3fQ0", // link 2
                "8ulR00x-B1I", // link 3
                "mrojrDCI02k", // link 4
                "wtVHR_1fS5k" // link 5
            ),

            // Sad songs links
            arrayOf(
                "axV7NhKArV0", // link 1
                "t1dvrcqlQgI", // link 2
                "K3Qzzggn--s", // link 3
                "_3ngiSxVCBs", // link 4
                "LI3E709rQDE" // link 5
            ),
        )

        // Open YouTube based on the song selected
        openYoutubeLink(songLinks[moodIndex][songIndex])
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

    // Function for displaying songs details based on mood's index
    @SuppressLint("SetTextI18n")
    private fun displaySongsDetails(moodIndex: Int){
        // Get views
        val songTitlesView = arrayOf(txt_songTitle1, txt_songTitle2, txt_songTitle3, txt_songTitle4, txt_songTitle5)
        val artistImageView = arrayOf(img_artist1, img_artist2, img_artist3, img_artist4, img_artist5)
        val songDetailsView = arrayOf(txt_songDetails1, txt_songDetails2, txt_songDetails3, txt_songDetails4, txt_songDetails5)

        // Store song details based on mood's index
        val songDetails = arrayOf(
            // Happy songs
            mapOf(
                "Artists" to arrayOf(
                    "Jordy Chandra",
                    "Bruno Mars",
                    "Tom Misch",
                    "Parov Stelar",
                    "JVKE"),
                "Titles" to arrayOf(
                    "A Nice Spring Evening",
                    "24K Magic",
                    "Disco Yes",
                    "Catgroove",
                    "this is what falling in love feels like"),
                "Images" to arrayOf(
                    R.drawable.img_artist_jordy,
                    R.drawable.img_artist_bruno,
                    R.drawable.img_artist_tom,
                    R.drawable.img_artist_parov,
                    R.drawable.img_artist_jvke),
                "Genres" to arrayOf(
                    "Lofi",
                    "Dance Pop",
                    "Funk",
                    "Electro Swing",
                    "Pop"),
                "Languages" to arrayOf(
                    "Instrumental",
                    "English",
                    "English",
                    "Instrumental",
                    "English"),
                "TitleColor" to arrayOf("#000000"),
                "TextColor" to arrayOf("#FFFFFF")
            ),

            // Calm songs
            mapOf(
                "Artists" to arrayOf(
                    "Joe Hisashi",
                    "Lamp",
                    "keshi",
                    "Pink Floyd",
                    "Jami Lynne"),
                "Titles" to arrayOf(
                    "A town with an ocean view",
                    "20 Year Old Love",
                    "right here",
                    "Breathe",
                    "WHITESPACE"),
                "Images" to arrayOf(
                    R.drawable.img_artist_joe,
                    R.drawable.img_artist_lamp,
                    R.drawable.img_artist_keshi,
                    R.drawable.img_artist_pinkfloyd,
                    R.drawable.img_artist_jami),
                "Genres" to arrayOf(
                    "Classical",
                    "Shibuya-kei",
                    "Alternative R&B",
                    "Psychedelic Rock",
                    "Dreamcore"),
                "Languages" to arrayOf(
                    "Instrumental",
                    "Japanese",
                    "English",
                    "English",
                    "Instrumental"),
                "TitleColor" to arrayOf("#000000"),
                "TextColor" to arrayOf("#FFFFFF")
            ),

            // Sad songs
            mapOf(
                "Artists" to arrayOf(
                    "tricot",
                    "New West",
                    "Joji",
                    "C418",
                    "Ichika Nito"),
                "Titles" to arrayOf(
                    "Jet Black",
                    "Those Eyes",
                    "Slow Dancing In The Dark",
                    "Sweden",
                    "i miss you"),
                "Images" to arrayOf(
                    R.drawable.img_artist_tricot,
                    R.drawable.img_artist_newwest,
                    R.drawable.img_artist_joji,
                    R.drawable.img_artist_c418,
                    R.drawable.img_artist_ichika),
                "Genres" to arrayOf(
                    "Math Rock",
                    "Indie-Pop",
                    "R&B Soul",
                    "Ambient",
                    "Lofi Hip Hop"),
                "Languages" to arrayOf(
                    "Japanese",
                    "English",
                    "English",
                    "Instrumental",
                    "Instrumental"),
                "TitleColor" to arrayOf("#000000"),
                "TextColor" to arrayOf("#FFFFFF")
            )
        )

        // Update song details based on Mood's index
        for (i in songTitlesView.indices){
            // Check the length of the Song Title for setting limits
            val maxSongTitleLength = 16
            val getSongTitle = songDetails[moodIndex]["Titles"]?.get(i).toString()
            val setSongTitle: String = if (getSongTitle.length > maxSongTitleLength){
                getSongTitle.substring(0,maxSongTitleLength) + ".."
            } else {
                getSongTitle
            }

            // Update song title and its text color
            songTitlesView[i].text = setSongTitle
            songTitlesView[i].setTextColor(Color.parseColor(songDetails[moodIndex]["TitleColor"]?.get(0).toString()))

            // Update Artist's image
            artistImageView[i].setImageResource(songDetails[moodIndex]["Images"]?.get(i) as Int)

            // Update Song's details and its text color
            val artistName = songDetails[moodIndex]["Artists"]?.get(i) as String
            val genreSong = songDetails[moodIndex]["Genres"]?.get(i) as String
            val languageSong = songDetails[moodIndex]["Languages"]?.get(i) as String
            songDetailsView[i].text = "Artist: $artistName\nGenre: $genreSong\nLanguage: $languageSong"
            songDetailsView[i].setTextColor(Color.parseColor(songDetails[moodIndex]["TextColor"]?.get(0).toString()))
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