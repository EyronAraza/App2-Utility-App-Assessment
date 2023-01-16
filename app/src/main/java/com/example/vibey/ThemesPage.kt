@file:Suppress("SameParameterValue", "DEPRECATION")

package com.example.vibey

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.popup_window.view.*
import kotlinx.android.synthetic.main.themes_page.*
import kotlinx.android.synthetic.main.song_window.view.*
import kotlinx.android.synthetic.main.song_window.view.window_box

class ThemesPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.themes_page)

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

        // Display Theme Selection
        displayThemesOnScreen()

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
        popupLayout.txt_info_header.text = "- MUSIC THEMES -"
        popupLayout.txt_info_paragraph.text = "Choosing one of the themes will display songs related to that theme.\n\nTapping on one of the songs will provide you more information such as the artist, genre and lyrics.\n\nYou can also open the song's YouTube by pressing the Play Button."

        // Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        // Close pop-up window by clicking anywhere
        popupLayout.setOnClickListener {
            myPopup.dismiss()
        }
    }

    // Function for displaying theme selection
    @SuppressLint("SetTextI18n")
    private fun displayThemesOnScreen(){
        // Initialise header
        txt_header.text = "MUSIC THEMES"
        playAnimation(arrayOf(txt_header), Anim.FadeIn)

        // Hide certain views
        displayViews(arrayOf(btn_back, scrollView_songs, bg_theme), false)

        // Show certain views
        displayViews(arrayOf(scrollView_themes), true)

        // Storing frames for 'Music Themes'
        val themeFrames = arrayOf(frame_blossom, frame_bedroom, frame_love, frame_study, frame_dance)

        // Play animation of displaying each frame
        var initDelay: Long = 0 // initialise animation delay
        val delayMilliSecs : Long = 150 // add delay to play each animation
        for (frame in themeFrames) {
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

        // Click on one of the Music Themes
        for (i in themeFrames.indices){
            themeFrames[i].setOnClickListener {
                // Display songs based on the selected theme by passing index
                displaySongsOnScreen(i)
            }
        }
    }

    // Function for displaying songs after selecting a theme
    @SuppressLint("SetTextI18n")
    private fun displaySongsOnScreen(themeIndex : Int){
        // Initialise Headers for Theme
        val themeHeaders = arrayOf("CHERRY BLOSSOMS", "BEDROOM", "LOVE", "STUDYING", "DANCE")

        // Initialise Backgrounds for Theme
        val themeBackgrounds = arrayOf(
            R.drawable.bg_theme_blossoms,
            R.drawable.bg_theme_bedroom,
            R.drawable.bg_theme_love,
            R.drawable.bg_theme_study,
            R.drawable.bg_theme_dancing)

        // Initialise Box Colours
        val boxColors = arrayOf(
            R.drawable.box_blossom,
            R.drawable.box_bedroom,
            R.drawable.box_love,
            R.drawable.box_study,
            R.drawable.box_dance)

        // Get Box images
        val boxImageView = arrayOf(img_song1, img_song2, img_song3, img_song4)

        var initDelay : Long = 0 // initialise animation delay
        val delayMilliSecs : Long = 150 // initialise adding delay to play each animation

        // Change background based on Theme's index
        bg_theme.setBackgroundResource(themeBackgrounds[themeIndex])
        bg_theme.visibility = View.VISIBLE

        // Change Header based on Theme's index
        txt_header.text = themeHeaders[themeIndex]

        // Play Fade-in animation for certain views
        playAnimation(arrayOf(bg_theme, txt_header), Anim.FadeIn)

        // Hide themes
        scrollView_themes.visibility = View.GONE

        // Show songs
        scrollView_songs.visibility = View.VISIBLE

        // Show Back Button
        displayViews(arrayOf(btn_back), true)

        // Display songs
        val songFrames = arrayOf(frame_song1, frame_song2, frame_song3, frame_song4)
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
                // Open window displaying song information by passing theme's index and song's index
                openSongWindow(themeIndex, i)
            }
        }

        // Change Box Colours based on Theme's index
        for (img in boxImageView){
            img.setImageResource(boxColors[themeIndex])
        }

        // Change details of the song
        setSongDetails(themeIndex)

        // Back button
        btn_back.setOnClickListener{
            // Go back to 'Music Themes'
            displayThemesOnScreen()
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun openSongWindow(themeIndex: Int, songIndex: Int){
        //-- Initialise view colors for each theme --//
        // Blossom Theme
        val blossomWindowColor = R.drawable.box_window_blossom
        val blossomBoxColor = "#9E9497"
        val blossomTextBoxColor = "#FFFFFF"

        // Bedroom Theme
        val bedroomWindowColor = R.drawable.box_window_bedroom
        val bedroomBoxColor = "#434B52"
        val bedroomTextBoxColor = "#FFFFFF"

        // Love Theme
        val loveWindowColor = R.drawable.box_window_love
        val loveBoxColor = "#866666"
        val loveTextBoxColor = "#FFFFFF"

        // Study Theme
        val studyWindowColor = R.drawable.box_window_study
        val studyBoxColor = "#634D36"
        val studyTextBoxColor = "#FFFFFF"

        // Dance Theme
        val danceWindowColor = R.drawable.box_window_dance
        val danceBoxColor = "#A69278"
        val danceTextBoxColor = "#FFFFFF"


        // Store view colours of each theme
        val storeViewColours = arrayOf(
            arrayOf(blossomWindowColor,blossomBoxColor,blossomTextBoxColor),
            arrayOf(bedroomWindowColor,bedroomBoxColor,bedroomTextBoxColor),
            arrayOf(loveWindowColor,loveBoxColor,loveTextBoxColor),
            arrayOf(studyWindowColor,studyBoxColor,studyTextBoxColor),
            arrayOf(danceWindowColor,danceBoxColor,danceTextBoxColor)
        )

        // Store information about the Artist's song
        val songInformation = arrayOf(
            // Blossom Theme Songs
            arrayOf(
                // Song 1
                mapOf(
                    SongInfo.Artist to "tricot",
                    SongInfo.Image to R.drawable.img_artist_tricot,
                    SongInfo.SongName to "Jet Black",
                    SongInfo.YouTube to "axV7NhKArV0",
                    SongInfo.WindowColor to blossomWindowColor,
                    SongInfo.BoxColor to blossomBoxColor,
                    SongInfo.TextBoxColor to blossomTextBoxColor,
                    SongInfo.About to "Tricot is a Japanese rock band from Kyoto. The band was formed in 2010 by vocalist and guitarist Ikumi \"Ikkyu\" Nakajima, guitarist Motoko \"Motifour\" Kida and bassist Hiromi \"Hirohiro\" Sagane. Known for their intricate rhythms and visual identity, they have released seven full-length studio albums.",
                    SongInfo.Genre to "MATH ROCK.\nMath rock is a subgenre of indie rock and post-hardcore that emphasizes progressive rock-inspired guitar riffs and unconventional harmonies and time signatures. While most math rock bands have lead vocalists, some math rock songs feature extended instrumental passages.",
                    SongInfo.Lyric to "In the long line\n" +
                            "Thieves\n" +
                            "Were mixed in\n" +
                            "Feigning ignorance\n" +
                            "\n" +
                            "Resigning\n" +
                            "To an estranged sensitivity\n" +
                            "Trying to softly\n" +
                            "Get washed out\n" +
                            "\n" +
                            "No matter where I hide\n" +
                            "I will not remember\n" +
                            "Even if hide my head, a shaken tail\n" +
                            "Will know where it needs to be\n" +
                            "Even if rain falls\n" +
                            "Tomorrow won’t change\n" +
                            "Even if you hide your heart, the hair you tied up\n" +
                            "Will obscure your sight\n" +
                            "\n" +
                            "Will obscure your sight\n" +
                            "\n" +
                            "Inside the treasure box\n" +
                            "Thieves\n" +
                            "Were shining\n" +
                            "Feigning ignorance\n" +
                            "\n" +
                            "Resigning\n" +
                            "To a loosened sense of morals\n" +
                            "About to shine and become deceitful\n" +
                            "\n" +
                            "Fuwafuwa\n" +
                            "Kirakira\n" +
                            "\n" +
                            "No matter what gets robbed\n" +
                            "I won’t lose a thing\n" +
                            "My head keeps overflowing\n" +
                            "I won’t ever get tired\n" +
                            "My birthday, I probably\n" +
                            "Won’t get to celebrate it well\n" +
                            "Even if I gave everything, it would not be enough\n" +
                            "\n" +
                            "Inside the treasure box, your hair, fluffy\n" +
                            "Things not worth returning\n" +
                            "Leaving behind a stolen mark\n" +
                            "\n" +
                            "Aloof\n" +
                            "In the long line\n" +
                            "Shiny\n" +
                            "Inside the treasure box"
                ),
                // Song 2
                mapOf(
                    SongInfo.Artist to "ZUN",
                    SongInfo.Image to R.drawable.img_artist_zun,
                    SongInfo.SongName to "Sakura, Sakura",
                    SongInfo.YouTube to "DoBbCCteNLs",
                    SongInfo.WindowColor to blossomWindowColor,
                    SongInfo.BoxColor to blossomBoxColor,
                    SongInfo.TextBoxColor to blossomTextBoxColor,
                    SongInfo.About to "Jun'ya Ota, known by his pseudonym ZUN, is a Japanese composer and video game developer best known for developing and self-publishing the Touhou Project video game series through the dojin group Team Shanghai Alice, of which he is the only member.",
                    SongInfo.Genre to "VIDEO GAME OST.\nVideo game music is the soundtrack that accompanies video games. Early video game music was once limited to sounds of early sound chips, such as programmable sound generators or FM synthesis chips. These limitations have led to the style of music known as chiptune, which became the sound of the first video games.",
                    SongInfo.Lyric to "No lyrics."
                ),
                // Song 3
                mapOf(
                    SongInfo.Artist to "Lamp",
                    SongInfo.Image to R.drawable.img_artist_lamp,
                    SongInfo.SongName to "20 Year Old Love",
                    SongInfo.YouTube to "jDv72Ul3fQ0",
                    SongInfo.WindowColor to blossomWindowColor,
                    SongInfo.BoxColor to blossomBoxColor,
                    SongInfo.TextBoxColor to blossomTextBoxColor,
                    SongInfo.About to "Lamp is a Japanese indie band formed in 2000.\n\nSomeya started playing guitar in middle school. In the folk club during high school, he became friends with Nagai through their common love of 60’s music. During college, when a friend introduced Sakakibara to Someya, he decided to start Lamp.\n\nLamp's style has been described as Shibuya-kei, city pop, and café music, but they have also been described as difficult to place into one genre. The band often incorporates elements of bossa nova, jazz, soul, and funk into their music. They have cited Brazilian music, The Beatles, The Beach Boys, and Simon & Garfunkel as influences.",
                    SongInfo.Genre to "SHIBUYA-KEI.\nShibuya-kei (Japanese: 渋谷系, lit. \"Shibuya style\") is a microgenre of pop music or a general aesthetic that flourished in Japan in the mid-to late 1990s. The music genre is distinguished by a \"cut-and-paste\" approach that was inspired by the kitsch, fusion, and artifice from certain music styles of the past. The most common reference points were 1960s culture and Western pop music, especially the work of Burt Bacharach, Brian Wilson, Phil Spector, and Serge Gainsbourg.",
                    SongInfo.Lyric to "Towards clear flowers\n" +
                            "When I bring my lips near\n" +
                            "You face this way\n" +
                            "With a smile like spring\n" +
                            " \n" +
                            "Watching the petals that fall\n" +
                            "The pinwheel in my hand\n" +
                            "Continues to turn\n" +
                            "The time I yearned\n" +
                            "Idly passes by\n" +
                            " \n" +
                            "I like you\n" +
                            "I like you\n" +
                            " \n" +
                            "With the wind’s breeze\n" +
                            "The petals have fallen into the pond\n" +
                            "I peer in\n" +
                            "To see a shadow reflected on water\n" +
                            " \n" +
                            "You gave me an orange ohajiki\n" +
                            "In the warmth of spring\n" +
                            "I lied down and smiled\n" +
                            "The time I yearned\n" +
                            "Idly passes by\n" +
                            " \n" +
                            "I like you\n" +
                            "I like you\n" +
                            "I like you\n" +
                            "I like you"
                ),
                // Song 4
                mapOf(
                    SongInfo.Artist to "Jordy Chandra",
                    SongInfo.Image to R.drawable.img_artist_jordy,
                    SongInfo.SongName to "A Nice Spring Evening",
                    SongInfo.YouTube to "xYEXbulCq2Y",
                    SongInfo.WindowColor to blossomWindowColor,
                    SongInfo.BoxColor to blossomBoxColor,
                    SongInfo.TextBoxColor to blossomTextBoxColor,
                    SongInfo.About to "Jordy Chandra is a Lofi Producer.\n\n'Place to be Mellow and Chill, I Share feelings through Music, I can't categorize my music so i simply call it beautiful and hope you feel the same! :)' - Jordy Chandra",
                    SongInfo.Genre to "LOFI.\nLo-fi is a music or production quality in which elements usually regarded as imperfections in the context of a recording or performance are present, sometimes as a deliberate choice.",
                    SongInfo.Lyric to "No lyrics."
                )
            ),

            // Bedroom Theme Songs
            arrayOf(
                // Song 1
                mapOf(
                    SongInfo.Artist to "New West",
                    SongInfo.Image to R.drawable.img_artist_newwest,
                    SongInfo.SongName to "Those Eyes",
                    SongInfo.YouTube to "t1dvrcqlQgI",
                    SongInfo.About to "New West is a Toronto-based music collective formed in 2017. The group is comprised of four like-minded songwriters and instrumentalists from varying notable studio production and musical backgrounds: Kala Wita, Vella, Ben Key and Noel West.\n" +
                            "\n" +
                            "The four members that make up New West met primarily through Noel. In 2017, Noel conducted separate studio sessions with Kala Wita, who was introduced to Noel via the Toronto music scene and childhood friend Ben Key with the intention of forming a collaborative project. The three inevitably started sessions together however it was the addition of Vella, friend and colleague to Ben, that completed the equation. The chemistry was instantaneous. In 2018, the group changed their name from The .wav to New West, ready to introduce a new signature sound to the Toronto music scene.\n" +
                            "\n" +
                            "All music released by New West is written and performed by all four members. It is this characteristic that defines them as a collective and separates them from other artists and bands. Each member contributes as a multifaceted instrumentalist/songwriter, putting forth a fresh spin on collaborative music. The New West sound is comprised of a wide pallet of sonic inspirations but drawing primarily from British acts such as Oasis, Coldplay, The Rolling Stones, The Beatles and Toronto artists such as The Weeknd and Daniel Caesar. Their songs emanate themes of nostalgia, infatuation, heartbreak and self-awareness with a focus on songwriting and lyricism. More information and new music will be announced when available.",
                    SongInfo.Genre to "INDIE-POP.\nIndie pop is a music genre and subculture that combines guitar pop with DIY ethic in opposition to the style and tone of mainstream pop music. It originated from British post-punk in the late 1970s and subsequently generated a thriving fanzine, label, and club and gig circuit.",
                    SongInfo.Lyric to "When we're out in a crowd laughing loud and nobody knows why\n" +
                            "When we're lost at a club getting drunk and you give me that smile\n" +
                            "Going home in the back of a car and your hand touches mine\n" +
                            "When we're done making love and you look up and give me those eyes\n" +
                            "\n" +
                            "Cause' all of the small things that you do\n" +
                            "Are what remind me why I fell for you\n" +
                            "And when we're apart and I'm missing you\n" +
                            "I close my eyes and all I see is you\n" +
                            "And the small things you do\n" +
                            "\n" +
                            "When you call me at night while you're out getting high with your friends\n" +
                            "Every hi, every bye, every I love you you've ever said\n" +
                            "\n" +
                            "Cause' all of the small things that you do\n" +
                            "Are what remind me why I fell for you\n" +
                            "And when we're apart and I'm missing you\n" +
                            "I close my eyes and all I see is you\n" +
                            "And the small things you do\n" +
                            "\n" +
                            "When we're done making love and you look up and give me those eyes\n" +
                            "\n" +
                            "Cause' all of the small things that you do\n" +
                            "Are what remind me why I fell for you\n" +
                            "And when we're apart and I'm missing you\n" +
                            "I close my eyes and all I see is you\n" +
                            "And the small things you do\n"
                ),
                // Song 2
                mapOf(
                    SongInfo.Artist to "Joji",
                    SongInfo.Image to R.drawable.img_artist_joji,
                    SongInfo.SongName to "Slow Dancing In The..",
                    SongInfo.YouTube to "K3Qzzggn--s",
                    SongInfo.About to "George Kusunoki Miller (ジョージ・楠木・ミラー, Jōji Kusunoki Mirā, born 18 September 1992), known professionally as Joji and formerly for playing the characters Filthy Frank and Pink Guy, is a Japanese singer-songwriter, rapper, former comedian, and YouTuber. Miller's music has been described as a mix between R&B, lo-fi, and trip hop.\n" +
                            "\n" +
                            "Miller created \"The Filthy Frank Show\" on YouTube in 2011 shortly after moving to the United States, gaining recognition for playing oddball characters on the comedy channels TVFilthyFrank, TooDamnFilthy, and DizastaMusic. The channels, which featured comedy hip hop, rants, extreme challenges, and ukulele and dance performances, are noted for their shock humor and prolific virality. Miller's videos helped popularize the Harlem Shake, which contributed to the commercial success of Baauer's song of the same name which led to the production of memes and collaborations with YouTubers. As Pink Guy, Miller released two comedy studio albums and an extended play between 2014 and 2017.\n" +
                            "\n" +
                            "In late 2017, Miller ended \"The Filthy Frank Show\" to pursue a music career under the name Joji. His debut album, Ballads 1, was released in 2018 and featured the single \"Slow Dancing in the Dark\". His second album, Nectar (2020), contained the singles \"Sanctuary\" and \"Run\". In 2022, he released the US Billboard Hot 100 top-ten single \"Glimpse of Us\", his highest-charting song, then going on to be featured on his third album, Smithereens (2022). ",
                    SongInfo.Genre to "R&B SOUL.\nSoul music is a popular music genre that originated in the African American community throughout the United States in the late 1950s and early 1960s. It has its roots in African-American gospel music and rhythm and blues. ",
                    SongInfo.Lyric to
                            "I don't want a friend (Just me)\n" +
                            "I want my life in two (My life in two)\n" +
                            "(Please one more night)\n" +
                            "Waiting to get there\n" +
                            "Waiting for you (Waiting for you)\n" +
                            "(Just one more night)\n" +
                            "(I done fight it all night)\n" +
                            "\n" +
                            "When I'm around slow dancing in the dark\n" +
                            "Don't follow me, you'll end up in my arms\n" +
                            "You done made up your mind\n" +
                            "I don't need no more signs\n" +
                            "Can you?\n" +
                            "Can you?\n" +
                            "\n" +
                            "Give me reasons we should be complete\n" +
                            "You should be with him, I can't compete\n" +
                            "You looked at me like I was someone else, oh well\n" +
                            "Can't you see?\n" +
                            "I don't wanna slow dance\n" +
                            "In the dark, dark\n" +
                            "\n" +
                            "When you gotta run\n" +
                            "Just hear my voice in you (My voice in you)\n" +
                            "Shutting me out of you (shutting me out of you)\n" +
                            "Doing so great (So great, so great)\n" +
                            "You\n" +
                            "\n" +
                            "Used to be the one to hold you when you fall\n" +
                            "Yeah, yeah, yeah (When you fall, when you fall)\n" +
                            "I don't **** with your tone (I don't **** with your tone)\n" +
                            "I don't wanna go home (I don't wanna go home)\n" +
                            "Can it be one night?\n" +
                            "Can you?\n" +
                            "Can you?\n" +
                            "\n" +
                            "Give me reasons we should be complete\n" +
                            "You should be with him, I can't compete\n" +
                            "You looked at me like I was someone else, oh well\n" +
                            "Can't you see?\n" +
                            "I don't wanna slow dance (I don't wanna slow dance)\n" +
                            "In the dark, dark\n" +
                            "In the dark, dark "
                ),
                // Song 3
                mapOf(
                    SongInfo.Artist to "AKFG",
                    SongInfo.Image to R.drawable.img_artist_akfg,
                    SongInfo.SongName to "Rock'n Roll, Morn...",
                    SongInfo.YouTube to "rDTFSD9K9dA",
                    SongInfo.About to "Asian Kung-Fu Generation is a Japanese alternative rock band formed in Yokohama in 1996. For its entire career, the band has consisted of vocalist Masafumi Gotoh, guitarist Kensuke Kita, bassist Takahiro Yamada, and drummer Kiyoshi Ijichi.",
                    SongInfo.Genre to "ALTERNATIVE ROCK.\nAlternative rock, or alt-rock, is a category of rock music that emerged from the independent music underground of the 1970s and became widely popular in the 1990s. \"Alternative\" refers to the genre's distinction from mainstream or commercial rock or pop music.",
                    SongInfo.Lyric to "I’d like to repaint the world if I could\n" +
                            "Not something as far-fetched as eliminating wars\n" +
                            "But that could be interesting too\n" +
                            "\n" +
                            "I’ll never be an actor or a movie star\n" +
                            "In fact, I can’t even laugh well when I’m with you\n" +
                            "There’s nothing I can do about that\n" +
                            "Ah…\n" +
                            "\n" +
                            "What did I do wrong?\n" +
                            "I don’t even know that\n" +
                            "Rolling, rolling\n" +
                            "I’ve never had it to begin with, but it still breaks my heart\n" +
                            "\n" +
                            "I’m sure we’ll always be\n" +
                            "With our hearts intertwined. Rolling, rolling\n" +
                            "I started running, as if rolling on the frozen ground\n" +
                            "\n" +
                            "Even without a reason, I feel somehow sad\n" +
                            "I can’t even cry, so there’s even less hope\n" +
                            "I’ll just sing, as if trying to keep warm on those nights\n" +
                            "\n" +
                            "The stones are rolling, like they’re taking us to somewhere\n" +
                            "New life started from the hardened ground\n" +
                            "Once we’re beyond that hill\n" +
                            "The morning light\n" +
                            "Will lay bare all of your loneliness\n" +
                            "\n" +
                            "A little red car takes you for a ride\n" +
                            "Taking the turn around a distant corner\n" +
                            "From there, you’ll be out of sight\n" +
                            "\n" +
                            "What did I lose?\n" +
                            "I don’t even know that\n" +
                            "Rolling, rolling\n" +
                            "I’ve never had it to begin with, but it still breaks my heart\n" +
                            "\n" +
                            "I’m sure we’ll always be\n" +
                            "With our hearts intertwined. Rolling, rolling\n" +
                            "I started running, as if rolling on the frozen ground"
                ),
                // Song 4
                mapOf(
                    SongInfo.Artist to "Pink Floyd",
                    SongInfo.Image to R.drawable.img_artist_pinkfloyd,
                    SongInfo.SongName to "Breathe",
                    SongInfo.YouTube to "mrojrDCI02k",
                    SongInfo.About to "Pink Floyd were an English rock band formed in London in 1965. Gaining an early following as one of the first British psychedelic groups, they were distinguished by their extended compositions, sonic experimentation, philosophical lyrics and elaborate live shows.",
                    SongInfo.Genre to "PSYCHEDELIC ROCK.\nPsychedelic rock is a rock music genre that is inspired, influenced, or representative of psychedelic culture, which is centered on perception-altering hallucinogenic drugs. The music incorporated new electronic sound effects and recording techniques, extended instrumental solos, and improvisation.",
                    SongInfo.Lyric to "Breathe, breathe in the air\n" +
                            "Don't be afraid to care\n" +
                            "Leave, don't leave me\n" +
                            "Look around, choose your own ground\n" +
                            "\n" +
                            "Long you live and high you fly\n" +
                            "Smiles you'll give and tears you'll cry\n" +
                            "And all you touch and all you see\n" +
                            "Is all your life will ever be\n" +
                            "\n" +
                            "Run, rabbit, run\n" +
                            "Dig that hole, forget the sun\n" +
                            "When at last the work is done\n" +
                            "Don't sit down, it's time to dig another one\n" +
                            "\n" +
                            "Long you live and high you fly\n" +
                            "But only if you ride the tide\n" +
                            "Balanced on the biggest wave\n" +
                            "Race towards an early grave "
                ),
            ),

            // Love Theme Songs
            arrayOf(
                // Song 1
                mapOf(
                    SongInfo.Artist to "keshi",
                    SongInfo.Image to R.drawable.img_artist_keshi,
                    SongInfo.SongName to "right here",
                    SongInfo.YouTube to "8ulR00x-B1I",
                    SongInfo.About to "Casey Luong, known mononymously by his stage name Keshi, is an American singer, songwriter, record producer, and multi-instrumentalist. Known for his distant falsetto vocals and textural instrumentals, he has accumulated over one billion streams with songs such as \"Like I Need U\", \"2 Soon\" and \"Right Here\".",
                    SongInfo.Genre to "ALTERNATIVE R&B.\nAlternative R&B is a term used by music journalists to describe a stylistic alternative to contemporary R&B that began in the mid 2000s and came to prominence with musical artists such as Frank Ocean, The Weeknd, SZA, Khalid, and others.",
                    SongInfo.Lyric to "I think some words are overdue\n" +
                            "Could we just do it over?\n" +
                            "Can we just talk it out like friends,\n" +
                            "Because I need your shoulder?\n" +
                            "I know we ended on the wrong terms\n" +
                            "But I said we're past it\n" +
                            "So why you texting me with questions\n" +
                            "You don't gotta ask me, like:\n" +
                            "\n" +
                            "\"I know it's random, how you been?\n" +
                            "Do you remember 'bout this band you said you listened to?\n" +
                            "When we were younger\n" +
                            "When we were softer\n" +
                            "When we were all about each other\n" +
                            "Hope life is treating you better\n" +
                            "Better, better\"\n" +
                            "\n" +
                            "Girl, what's with that?\n" +
                            "Yo, cut the act\n" +
                            "'Cause I don't got time for laughs\n" +
                            "No, I do not want the past\n" +
                            "But if you are ever in need\n" +
                            "And God has you down on your knees\n" +
                            "And you do not know who to be\n" +
                            "Then go on and come home to me\n" +
                            "\n" +
                            "Because I'm here\n" +
                            "If you need me I'll be here\n" +
                            "Right here\n" +
                            "Said I'm here\n" +
                            "If you need me I'll be here\n" +
                            "Right here, oh\n" +
                            "\n" +
                            "And how you liking this new guy?\n" +
                            "I know you always want the new life\n" +
                            "I hope you taking care of you, like\n" +
                            "The way you cared for me in my time\n" +
                            "See you got yourself some new friends\n" +
                            "No, you don't need to go to Houston\n" +
                            "You got me thinking back about then\n" +
                            "Girl, I wonder why you texting\n" +
                            "\n" +
                            "Girl, what's with that?\n" +
                            "Yo, cut the act\n" +
                            "'Cause I don't got time for laughs\n" +
                            "No, I do not want the past\n" +
                            "But if you are ever in need\n" +
                            "And God has you down on your knees\n" +
                            "And you do not know who to be\n" +
                            "Then go on and come home to me\n" +
                            "\n" +
                            "Because I'm here\n" +
                            "If you need me I'll be here\n" +
                            "Right here\n" +
                            "Said I'm here\n" +
                            "If you need me I'll be here\n" +
                            "Right here, oh"
                ),
                // Song 2
                mapOf(
                    SongInfo.Artist to "JVKE",
                    SongInfo.Image to R.drawable.img_artist_jvke,
                    SongInfo.SongName to "this is what falling..",
                    SongInfo.YouTube to "BOyO8sZOaOQ",
                    SongInfo.About to "Jacob Lawson, known professionally as Jvke, is an American singer-songwriter, producer, and social media personality. During the COVID-19 lockdowns, he started creating TikTok videos for his songs, one of which, \"Upside Down\", went viral in 2021.",
                    SongInfo.Genre to "POP.\nPop music is a genre of popular music that originated in its modern form during the mid-1950s in the United States and the United Kingdom. The terms popular music and pop music are often used interchangeably, although the former describes all music that is popular and includes many disparate styles.",
                    SongInfo.Lyric to "Feel like sun, on my skin\n" +
                            "So this is love\n" +
                            "I know it is\n" +
                            "I know I sound super cliché\n" +
                            "But you make me feel\n" +
                            "Some type of way\n" +
                            "This is falling, falling in love\n" +
                            "\n" +
                            "Ooh yeah\n" +
                            "I got a lot on my mind\n" +
                            "Got some more on my plate\n" +
                            "My baby got me looking forward\n" +
                            "To the end of the day\n" +
                            "What you say?\n" +
                            "You and me?\n" +
                            "Just forget about the past\n" +
                            "Throw it in the trash\n" +
                            "What you say?\n" +
                            "You and me?\n" +
                            "Live the life we never had\n" +
                            "Like we're never going back\n" +
                            "\n" +
                            "Feel like sun, on my skin\n" +
                            "So this is love\n" +
                            "I know it is\n" +
                            "I know I sound super cliché\n" +
                            "But you make me feel\n" +
                            "Some type of way\n" +
                            "This is falling, falling in love\n" +
                            "\n" +
                            "I know I sound super cliché\n" +
                            "But you make me feel\n" +
                            "Some type of way\n" +
                            "This is falling, falling in love\n" +
                            "Yeah\n" +
                            "This is falling, falling in love\n" +
                            "Ooh\n" +
                            "This is falling, falling in love"
                ),
                // Song 3
                mapOf(
                    SongInfo.Artist to "Kessoku Band",
                    SongInfo.Image to R.drawable.img_artist_kessoku,
                    SongInfo.SongName to "If I could be a con..",
                    SongInfo.YouTube to "-LwBbLa_Vhc",
                    SongInfo.About to "Kessoku Band (結束バンド Kessoku Bando) is the main unit band of Bocchi the Rock! series. The name \"Kessoku Band\" is a pun on \"Kessoku\" (unity) and \"Kessoku Band\" (cable tie). The band has been drawn as the center of this work. The unit band is based on Shimokitazawa's live house STARRY, where Nijika's older sister, Seika, is a store manager. ",
                    SongInfo.Genre to "J-ROCK.\nJapanese rock, sometimes abbreviated to J-rock, is rock music from Japan. Influenced by American and British rock of the 1960s, the first rock bands in Japan performed what is called Group Sounds, with lyrics almost exclusively in English. ",
                    SongInfo.Lyric to "Just a bit before 6 o’clock\n" +
                            "The first star can be seen right there\n" +
                            "Stepping on shadows surrendering myself to the night on the way home\n" +
                            "No matter how you look\n" +
                            "That one and only star\n" +
                            "Shining brightly from hundreds of millions of light years away\n" +
                            "\n" +
                            "Must be nice, to be loved by everyone\n" +
                            "“No such thing, I have always been alone”\n" +
                            "\n" +
                            "If I could be a constellation with you\n" +
                            "In the starry night sky, that’s my only wish\n" +
                            "Expressing ourselves in flickers, sparkles, and pulses\n" +
                            "If I could be a constellation with you\n" +
                            "As if looking up pointing at the sky\n" +
                            "Keeping the line between us connected\n" +
                            "No matter how bright I shine\n" +
                            "\n" +
                            "Just a bit before 8 o’clock\n" +
                            "The night sky is now filled with stars\n" +
                            "Shining from millions of light years away, it might not still be there\n" +
                            "\n" +
                            "The moon seemed so beautiful I felt like crying\n" +
                            "Because someday we will have to part\n" +
                            "\n" +
                            "If I could be a constellation with you\n" +
                            "Flowing like a comet, I talked to myself\n" +
                            "Leaving trails of light as if the night became a prism\n" +
                            "If I could be a constellation with you\n" +
                            "If my dearest wish somehow reached someone\n" +
                            "Will the night change\n" +
                            "After I trace it with my lines?\n" +
                            "\n" +
                            "After a long journey, we ended up meeting\n" +
                            "Maybe we were destined to meet no matter what\n" +
                            "In a world beyond the clouds\n" +
                            "\n" +
                            "If I could be a constellation with you\n" +
                            "Painting the sky and creating our ideal night\n" +
                            "Even if the moon will always outshine us\n" +
                            "That’s why I want to be a constellation with you\n" +
                            "Shining in myriads of colors\n" +
                            "The lines connecting us won’t be broken\n" +
                            "No matter how bright you shine"
                ),
                // Song 4
                mapOf(
                    SongInfo.Artist to "Baekhyun",
                    SongInfo.Image to R.drawable.img_artist_baekhyun,
                    SongInfo.SongName to "UN Village",
                    SongInfo.YouTube to "-EfjXQgE1e8",
                    SongInfo.About to "Byun Baek-hyun, better known mononymously as Baekhyun, is a South Korean singer, songwriter and actor. Baekhyun began training under SM Entertainment in 2011. A few months later, he made his debut in 2012 as a member of the South Korean-Chinese boy group Exo, its sub-group Exo-K and later on its sub-unit Exo-CBX.",
                    SongInfo.Genre to "K-POP.\nK-pop (Korean: 케이팝; RR: keipap), short for Korean popular music, is a form of popular music originating in South Korea as part of South Korean culture. It includes styles and genres from around the world, such as pop, hip hop, R&B, experimental, rock, jazz, gospel, reggae, electronic dance, folk, country, disco, and classical on top of its traditional Korean music roots. The term \"K-pop\" became popular in the 2000s, especially in the international context. The Korean term for domestic pop music is gayo (Korean: 가요; Hanja: 歌謠), which is still widely used within South Korea.  While \"K-pop\" can refer to all popular music or pop music from South Korea, it is colloquially often used in a narrower sense for any Korean music and artists associated with the entertainment and idol industry in the country, regardless of the genre. ",
                    SongInfo.Lyric to "Navigation set to the Dokseo Children’s Park\n" +
                            "Pressing on the accelerator\n" +
                            "I used to be the only one who knew that place now\n" +
                            "Everyone’s trying to go\n" +
                            "Everywhere, anywhere\n" +
                            "I can see it\n" +
                            "Girl we need to be romantic\n" +
                            "That’s what we need right now\n" +
                            " \n" +
                            "Even if you say you already knew\n" +
                            "With the feeling of that first time\n" +
                            "I’ll lead you, lean on me\n" +
                            "I know I have the right\n" +
                            "To spend your precious time\n" +
                            "I know that, I know that, I know\n" +
                            "So right now we’re\n" +
                            " \n" +
                            "In Hannam-dong UN Village hill\n" +
                            "Looking at the moon on the hill\n" +
                            "You & me,mm,yeah\n" +
                            "UN Village hill,ay\n" +
                            "Looking at the moon, side by side\n" +
                            "You & me relax and chillin’,ay,yeah\n" +
                            " \n" +
                            "I can’t show you perfection\n" +
                            "But right in this moment\n" +
                            "The wind lets us go with the flow\n" +
                            "When I whisper love into your ears\n" +
                            "I want your once indifferent eyes\n" +
                            "To tell me you love me too\n" +
                            " \n" +
                            "Even if you say you already knew\n" +
                            "With the feeling of that first time\n" +
                            "I’ll lead you, lean on me\n" +
                            "I know I have the right\n" +
                            "To spend your precious time\n" +
                            "I know that, I know that, I know\n" +
                            "So right now we’re\n" +
                            " \n" +
                            "In Hannam-dong UN Village hill\n" +
                            "Looking at the moon on the hill\n" +
                            "You & me,mm,yeah\n" +
                            "UN Village hill,ay\n" +
                            "Looking at the moon, side by side\n" +
                            "You & me relax and chillin’,ay,yeah\n" +
                            " \n" +
                            "Rolling rolling rolling hills\n" +
                            "Up the rolling hills\n" +
                            "Rolling rolling rolling hills\n" +
                            "There’s a broken lamp post\n" +
                            "If you see it, turn your light\n" +
                            "Cuz this place is bright enough with you alone\n" +
                            " \n" +
                            "In Hannam-dong UN Village hill\n" +
                            "Looking at the moon on the hill\n" +
                            "You & me,mm,yeah\n" +
                            "UN Village hill,ay\n" +
                            "Looking at the moon, side by side\n" +
                            "You & me relax and chillin’,ay,yeah"
                ),
            ),

            // Study Theme Songs
            arrayOf(
                // Song 1
                mapOf(
                    SongInfo.Artist to "Joe Hisashi",
                    SongInfo.Image to R.drawable.img_artist_joe,
                    SongInfo.SongName to "A town with an oce..",
                    SongInfo.YouTube to "hpfLKbjTWn0",
                    SongInfo.About to "Mamoru Fujisawa, known professionally as Joe Hisaishi, is a Japanese composer, musical director, conductor and pianist, known for over 100 film scores and solo albums dating back to 1981. He is also known for his piano scores.",
                    SongInfo.Genre to "CLASSICAL.\nClassical music generally refers to the art music of the Western world, considered to be distinct from Western folk music or popular music traditions. It is sometimes distinguished as Western classical music, as the term \"classical music\" also applies to non-Western art music.",
                    SongInfo.Lyric to "No lyrics."
                ),
                // Song 2
                mapOf(
                    SongInfo.Artist to "C418",
                    SongInfo.Image to R.drawable.img_artist_c418,
                    SongInfo.SongName to "Sweden",
                    SongInfo.YouTube to "_3ngiSxVCBs",
                    SongInfo.About to "Daniel Rosenfeld, better known by his stage/online name C418, is a German musician, producer and sound engineer, best known as the composer and sound designer for the sandbox video game Minecraft.",
                    SongInfo.Genre to "AMBIENCE.\nAmbient music is a genre of music that emphasizes tone and atmosphere over traditional musical structure or rhythm. It may lack net composition, beat, or structured melody. It uses textural layers of sound that can reward both passive and active listening and encourage a sense of calm or contemplation.",
                    SongInfo.Lyric to "No lyrics."
                ),
                // Song 3
                mapOf(
                    SongInfo.Artist to "Jami Lynne",
                    SongInfo.Image to R.drawable.img_artist_jami,
                    SongInfo.SongName to "WHITESPACE",
                    SongInfo.YouTube to "wtVHR_1fS5k",
                    SongInfo.About to "JAMI LYNNE (Heads Up! Hot Dogs, Wonder Momo: Typhoon Booster) is a composer and performing musician from Tucson, AZ. Jami blends electronic pop music with the emotional imperfection of punk rock and silver-age idol music to create an experience that pushes forward into the future with the ideals of youth and hope. Drawing inspiration from games like EarthBound/MOTHER, Animal Crossing, Persona, and Umihara Kawase, Jami, along with Pedro, hopes to express an earnest and sentimental connection with the themes of OMORI to create an unforgettable and lastingly meaningful experience with the help of your heart. ",
                    SongInfo.Genre to "DREAMCORE.\nDreamcore is a surrealist aesthetic that uses motifs commonly associated with dreams, daydreams or nightmares, portrayed through media such as images, videos and, on occasion, music. Dreamcore shares many similarities with weirdcore; however, it is not intended to give off dread or uneasy feelings. Instead, dreamcore focuses on emulating the general feeling of a dream and uses brighter color. ",
                    SongInfo.Lyric to "No lyrics."
                ),
                // Song 4
                mapOf(
                    SongInfo.Artist to "Ichika Nito",
                    SongInfo.Image to R.drawable.img_artist_ichika,
                    SongInfo.SongName to "i miss you",
                    SongInfo.YouTube to "LI3E709rQDE",
                    SongInfo.About to "Internet sensation and renowned Guitarist Ichika Nito, gained his global following and reputation through a unique combination of complex techniques and an easily identifiable signature sound. He captivates music fans and musicians alike with his unconventional, experimental style and a wholly original musical approach, through which he has become a favorite player and a major influence to players around the world. ",
                    SongInfo.Genre to "LOFI HIP HOP.\nLofi hip hop (also known as chillhop and lofi beats to study to) is a form of downtempo music that combines elements of hip hop and chill-out music. It was popularized in the 2010s on YouTube and has been referred to as an Internet meme. \n" +
                            "\n" +
                            "Lo-fi hip-hop originated within the underground beatmaking hip-hop scene of the 2000s, particularly after the advent of Roland SP-303 and Roland SP-404 samplers, each of which featured the \"lo-fi\" effect as a separate button. Roland SP samplers, particularly Boss SP-202, 303 and 404 were sporadically used by beatmakers and DJs since the early 2000s, but it was Madlib who arguably[according to whom?] paid stronger attention to the SP samplers, after showcasing them at his Red Bull Academy lecture in Brazil in 2002. It was also in Brazil in 2002 where Madlib created Rhinestone Cowboy, Raid, and Strange Ways for his 2004 collaborative album with MF DOOM called Madvillainy. The three mentioned beats were all composed using 303 and a tape deck. Vice contributor Luke Winkie suggested that \"if there is one shared touchstone for lo-fi hip-hop, it's probably [the 2004 MF Doom and Madlib album] Madvillainy\". Among the early adopters of the 404 stood Jneiro Jarel, who is credited as the first artist to use SP-404 in an official release, after releasing Three Piece Puzzle in 2005.",
                    SongInfo.Lyric to "No lyrics."
                ),
            ),

            // Dance Theme Songs
            arrayOf(
                // Song 1
                mapOf(
                    SongInfo.Artist to "Parov Stelay",
                    SongInfo.Image to R.drawable.img_artist_parov,
                    SongInfo.SongName to "Catgroove",
                    SongInfo.YouTube to "WXrdYwG17PE",
                    SongInfo.About to "Marcus Füreder, better known by his stage name Parov Stelar, is an Austrian musician, composer, producer, DJ and designer. His musical style is based on a combination of jazz, house, electro, hip hop, and pop.",
                    SongInfo.Genre to "ELECTRO SWING.\nElectro swing, or swing house, is an electronic dance music genre that combines the influence of vintage or modern swing and jazz mixed with house and hip hop.",
                    SongInfo.Lyric to "No lyrics."
                ),
                // Song 2
                mapOf(
                    SongInfo.Artist to "Bruno Mars",
                    SongInfo.Image to R.drawable.img_artist_bruno,
                    SongInfo.SongName to "24K Magic",
                    SongInfo.YouTube to "UqyT8IEBkvY",
                    SongInfo.About to "Peter Gene Hernandez, known professionally as Bruno Mars, is an American singer, songwriter, and record producer. He is known for his stage performances, retro showmanship, and for performing in a wide range of musical styles, including pop, R&B, funk, soul, reggae, disco, and rock.",
                    SongInfo.Genre to "DANCE POP.\nDance-pop is a popular music subgenre that originated in the late 1970s to early 1980s. It is generally uptempo music intended for nightclubs with the intention of being danceable but also suitable for contemporary hit radio. Developing from a combination of dance and pop with influences of disco, post-disco and synth-pop, it is generally characterised by strong beats with easy, uncomplicated song structures which are generally more similar to pop music than the more free-form dance genre, with an emphasis on melody as well as catchy tunes. The genre, on the whole, tends to be producer-driven, despite some notable exceptions.",
                    SongInfo.Lyric to "Tonight\n" +
                            "I just want to take you higher\n" +
                            "Throw your hands up in the sky\n" +
                            "Let's set this party off right\n" +
                            "\n" +
                            "Players, put yo' pinky rings up to the moon\n" +
                            "Girls, what y'all trying to do?\n" +
                            "24 karat magic in the air\n" +
                            "Head to toe so player\n" +
                            "Uh, look out!\n" +
                            "\n" +
                            "Pop pop, it's show time (Show time)\n" +
                            "Show time (Show time)\n" +
                            "Guess who's back again?\n" +
                            "Oh they don't know? (Go on tell 'em)\n" +
                            "Oh they don't know? (Go on tell 'em)\n" +
                            "I bet they know soon as we walk in\n" +
                            "(Showin' up)\n" +
                            "Wearing Cuban links (ya)\n" +
                            "Designer minks (ya)\n" +
                            "Inglewood's finest shoes (Whoop, whoop)\n" +
                            "Don't look too hard\n" +
                            "Might hurt ya'self\n" +
                            "Known to give the color red the blues\n" +
                            "\n" +
                            "Ooh shit, I'm a dangerous man with some money in my pocket\n" +
                            "(Keep up)\n" +
                            "So many pretty girls around me and they waking up the rocket\n" +
                            "(Keep up)\n" +
                            "Why you mad? Fix ya face\n" +
                            "Ain't my fault y'all be jocking\n" +
                            "(Keep up)\n" +
                            "\n" +
                            "Players only, come on\n" +
                            "Put your pinky rings up to the moon\n" +
                            "Girls, what y'all trying to do?\n" +
                            "24 karat magic in the air\n" +
                            "Head to toe so player\n" +
                            "Uh, look out!\n" +
                            "\n" +
                            "Second verse for the hustlas (hustlas)\n" +
                            "Gangstas (gangstas)\n" +
                            "Bad bitches and ya ugly ass friends (Haha)\n" +
                            "Can I preach? (Uh oh) Can I preach? (Uh oh)\n" +
                            "I gotta show 'em how a pimp get it in\n" +
                            "First, take your sip (sip), do your dip (dip)\n" +
                            "Spend your money like money ain't shit (Whoop, whoop)\n" +
                            "We too fresh\n" +
                            "Got to blame it on Jesus\n" +
                            "Hashtag blessed\n" +
                            "They ain't ready for me\n" +
                            "\n" +
                            "I'm a dangerous man with some money in my pocket\n" +
                            "(Keep up)\n" +
                            "So many pretty girls around me and they waking up the rocket\n" +
                            "(Keep up)\n" +
                            "Why you mad? Fix ya face\n" +
                            "Ain't my fault y'all be jocking\n" +
                            "(Keep up)\n" +
                            "\n" +
                            "Players only, come on\n" +
                            "Put your pinky rings up to the moon\n" +
                            "Hey girls\n" +
                            "What y'all trying to do?\n" +
                            "24 karat magic in the air\n" +
                            "Head to toe so player\n" +
                            "Uh, look out!\n" +
                            "\n" +
                            "(Wooh)\n" +
                            "Everywhere I go they be like\n" +
                            "Ooh, so player ooh\n" +
                            "Everywhere I go they be like\n" +
                            "Ooh, so player ooh\n" +
                            "Everywhere I go they be like\n" +
                            "Ooh, so player ooh\n" +
                            "Now, now, now\n" +
                            "Watch me break it down like (Uh)\n" +
                            "24 karat, 24 karat magic\n" +
                            "What's that sound?\n" +
                            "24 karat, 24 karat magic\n" +
                            "Come on now\n" +
                            "24 karat, 24 karat magic\n" +
                            "Don't fight the feeling\n" +
                            "Invite the feeling\n" +
                            "\n" +
                            "Just put your pinky rings up to the moon\n" +
                            "Girls, what y'all trying to do?\n" +
                            "(Tell me what y'all trying to do?)\n" +
                            "24 karat magic in the air\n" +
                            "Head to toe so player\n" +
                            "Put your pinky rings up to the moon\n" +
                            "Girls, what y'all trying to do? (Do)\n" +
                            "24 karat magic in the air\n" +
                            "Head to toe so player\n" +
                            "(24 karat)\n" +
                            "Uh, look out\n" +
                            "\n" +
                            "(24 karat magic, magic, magic)"
                ),
                // Song 3
                mapOf(
                    SongInfo.Artist to "Amine",
                    SongInfo.Image to R.drawable.img_artist_amine,
                    SongInfo.SongName to "Reel It In",
                    SongInfo.YouTube to "kBHWo29IQBI",
                    SongInfo.About to "Adam Aminé Daniel (born April 18, 1994) is an American rapper, singer, and songwriter. He first gained notability for his commercial debut single, \"Caroline\", which peaked at number 11 on the US Billboard Hot 100 chart. Aminé released his debut studio album Good for You, on July 28, 2017, and his second studio album, Limbo, on August 7, 2020.",
                    SongInfo.Genre to "HIP HOP RAP.\nHip hop music or hip-hop music, also known as rap music and formerly known as disco rap, is a genre of popular music that originated in New York City in the 1970s. It consists of stylized rhythmic music (usually built around drum beats) that commonly accompanies rapping, a rhythmic and rhyming speech that is chanted. It developed as part of hip hop culture, a subculture defined by four key stylistic elements: MCing/rapping, DJing/scratching with turntables, break dancing, and graffiti writing. Other elements include sampling beats or bass lines from records (or synthesized beats and sounds), and rhythmic beatboxing. While often used to refer solely to rapping, \"hip hop\" more properly denotes the practice of the entire subculture. The term hip hop music is sometimes used synonymously with the term rap music, though rapping is not a required component of hip hop music; the genre may also incorporate other elements of hip hop culture, including DJing, turntablism, scratching, beatboxing, and instrumental tracks. ",
                    SongInfo.Lyric to "(SOME PROFANITY ARE REMOVED)\n\n" + "Right now!\n" +
                            "\n" +
                            "Reel it in\n" +
                            "I got the bag, tell a friend\n" +
                            "She got some , bring it in\n" +
                            "I'm the news, CNN (yeah)\n" +
                            "Reel it in\n" +
                            "I got the bag, tell a friend\n" +
                            "She got some , bring it in\n" +
                            "I'm the news, CNN (yeah) (hey!)\n" +
                            "\n" +
                            "Trippin'\n" +
                            "Rollie got the tick tock missin'\n" +
                            "pimpin' (ayy)\n" +
                            "Broke  got me livin' (wow)\n" +
                            "You Jordan or Pippin (true)\n" +
                            "I'm Kobe I'm never gon' dish it\n" +
                            "Or miss it\n" +
                            "I'm business as and I ain't got the time to just kick it\n" +
                            "\n" +
                            "I don't trust nobody 'cause I can't be fake\n" +
                            "Frontin' ain't an option 'cause my soul can't change\n" +
                            "And I'm tired of being humble, I feel no way\n" +
                            "It's that young , I might pull up to your  like\n" +
                            "\n" +
                            "Reel it in (ayy)\n" +
                            "I got the bag, tell a friend\n" +
                            "She got some , bring it in (ayy)\n" +
                            "I'm the news, CNN (yeah)\n" +
                            "Reel it in\n" +
                            "I got the bag, tell a friend\n" +
                            "She got some , bring it in\n" +
                            "I'm the news, CNN\n" +
                            "\n" +
                            "Jesus Christ! (Hallelujah!)\n" +
                            "I bought that  before I even knew the price (hey!)\n" +
                            "This 13k up on my finger,  you right (true)\n" +
                            "Them diamonds dancin', yeah that boy be lookin' nice (ayy)\n" +
                            "I guess that's what you call that mothering ice\n" +
                            "I got a thotiana, put her in designer\n" +
                            "She Italiana, she a sorta kinda\n" +
                            "She Björk cute\n" +
                            "So she really fine, just sorta weird, and she got some cake\n" +
                            "Gimme OPB for my ODB\n" +
                            "These APC, do your ABC's\n" +
                            "I'm a young dread-head with anxiety\n" +
                            "And I love it when a   tryin' me\n" +
                            "\n" +
                            "I don't trust nobody 'cause I can't be fake\n" +
                            "Frontin' ain't an option 'cause my soul can't change\n" +
                            "And I'm tired of being humble,  I feel no way\n" +
                            "It's that young  , I might pull up to your  like\n" +
                            "\n" +
                            "Reel it in (ayy)\n" +
                            "I got the bag, tell a friend (whoo)\n" +
                            "She got some , bring it in (ooh)\n" +
                            " I'm the news, CNN (yeah)\n" +
                            "Reel it in\n" +
                            "I got the bag, tell a friend (true)\n" +
                            "She got some , bring it in\n" +
                            " I'm the news, CNN (yeah) "
                ),
                // Song 4
                mapOf(
                    SongInfo.Artist to "Tom Misch",
                    SongInfo.Image to R.drawable.img_artist_tom,
                    SongInfo.SongName to "Disco Yes",
                    SongInfo.YouTube to "EXWOJvlDwbU",
                    SongInfo.About to "Thomas Abraham Misch (born 25 June 1995) is an English musician and producer. He began releasing music on SoundCloud in 2012 and released his debut studio album Geography in 2018. In 2020, he released his second studio album What Kinda Music in collaboration with English jazz drummer Yussef Dayes, distributed through Blue Note Records and Caroline.",
                    SongInfo.Genre to "FUNK.\nFunk is a music genre that originated in African American communities in the mid-1960s when musicians created a rhythmic, danceable new form of music through a mixture of various music genres that were popular among African Americans in the mid-20th century.",
                    SongInfo.Lyric to "I want to stay, but I need to go\n" +
                            "I want to be the best for you\n" +
                            "But I just don’t know what to do\n" +
                            "'Cause baby, yes I've cried for you\n" +
                            "The time that we have spent together\n" +
                            "Riding through this English weather\n" +
                            "\n" +
                            "And as the pressure builds\n" +
                            "So does the tension between you and me\n" +
                            "Time has gone so fast\n" +
                            "Watching the leaves fall from our tree\n" +
                            "\n" +
                            "Baby, I just want you to know\n" +
                            "I still love you, love you, love you, love you, love you, love you... uh uh\n" +
                            "\n" +
                            "[Poppy Ajudha:]\n" +
                            "And the grass is always greener on the other side\n" +
                            "Maybe you should come and take a trip sometime\n" +
                            "Oh you seem keener when they turn to night\n" +
                            "But you tripping out your mind\n" +
                            "Baby, tell me it's a sign\n" +
                            "\n" +
                            "As the pressure builds\n" +
                            "So does the tension between you and me\n" +
                            "Time has gone so fast\n" +
                            "Watching the leaves fall from our tree\n" +
                            "\n" +
                            "Baby, I just want you to know\n" +
                            "I still love you, love you, love you, love you, love you..."
                ),
            )
        )

        // Create pop-up window
        val songWindow = layoutInflater.inflate(R.layout.song_window, null)

        // Make pop-window as Dialog
        val myPopup = Dialog(this)
        myPopup.setContentView(songWindow)

        // Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        // Close pop-up window by clicking anywhere
        songWindow.setOnClickListener{
            myPopup.dismiss()
        }

        // Update Song Information (artist's name, song name, photo)
        songWindow.txt_win_song_artist.text = songInformation[themeIndex][songIndex][SongInfo.Artist].toString() // artist name
        songWindow.img_win_artistPhoto.setImageResource(songInformation[themeIndex][songIndex][SongInfo.Image] as Int) // artist photo
        songWindow.txt_win_song_title.text = songInformation[themeIndex][songIndex][SongInfo.SongName].toString() // song title
        songWindow.txt_win_boxInfo.setText(songInformation[themeIndex][songIndex][SongInfo.About].toString()) // about artist

        // Update colourings (window, box, text)
        for (i in storeViewColours.indices){
            songWindow.window_box.setImageResource(storeViewColours[themeIndex][0] as Int) // window color
            songWindow.txt_win_boxInfo.background.setColorFilter(Color.parseColor(storeViewColours[themeIndex][1].toString()), PorterDuff.Mode.SRC_ATOP) // text box color
            songWindow.txt_win_boxInfo.setTextColor(Color.parseColor(storeViewColours[themeIndex][2].toString())) // text color in box
        }

        // Button to update 'About Artist' information
        songWindow.btn_win_artistInfo.setOnClickListener{
            songWindow.txt_win_boxInfo.setText(songInformation[themeIndex][songIndex][SongInfo.About].toString()) // about artist
        }

        // Button to update 'Genre' information
        songWindow.btn_win_genreInfo.setOnClickListener{
            songWindow.txt_win_boxInfo.setText(songInformation[themeIndex][songIndex][SongInfo.Genre].toString()) // genre info
        }

        // Button to update 'Lyric' information
        songWindow.btn_win_lyricInfo.setOnClickListener{
            songWindow.txt_win_boxInfo.setText(songInformation[themeIndex][songIndex][SongInfo.Lyric].toString()) // lyrics info
        }

        // Button to open YouTube of the song
        songWindow.btn_win_play.setOnClickListener{
            openYoutubeLink(songInformation[themeIndex][songIndex][SongInfo.YouTube].toString()) // pass YouTube ID
        }
    }

    // Class for map keys of Song Information
    enum class SongInfo {
        Artist, Image, SongName, YouTube, WindowColor, BoxColor, About, Genre, Lyric, TextBoxColor
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

    // Function for updating song details based on theme's index
    @SuppressLint("SetTextI18n")
    private fun setSongDetails(themeIndex: Int){
        // Get views
        val songTitlesView = arrayOf(txt_songTitle1, txt_songTitle2, txt_songTitle3, txt_songTitle4)
        val artistImageView = arrayOf(img_artist1, img_artist2, img_artist3, img_artist4)
        val songDetailsView = arrayOf(txt_songDetails1, txt_songDetails2, txt_songDetails3, txt_songDetails4)

        // Store song details based on theme's index
        val songDetails = arrayOf(
            // Cherry Blossoms Theme
            mapOf(
                "Artists" to arrayOf("tricot", "ZUN", "Lamp", "Jordy Chandra"),
                "Titles" to arrayOf("Jet Black", "Sakura, Sakura", "20 Year Old Love", "A Nice Spring Evening"),
                "Images" to arrayOf(R.drawable.img_artist_tricot, R.drawable.img_artist_zun, R.drawable.img_artist_lamp, R.drawable.img_artist_jordy),
                "Genres" to arrayOf("Math Rock", "Video Game", "Shibuya-kei", "Lofi"),
                "Languages" to arrayOf("Japanese", "Instrumental", "Japanese", "Instrumental"),
                "TitleColor" to arrayOf("#000000"),
                "TextColor" to arrayOf("#C6A0B0")
            ),

            // Bedroom Theme
            mapOf(
                "Artists" to arrayOf("New West", "JOJI", "AKFG", "Pink Floyd"),
                "Titles" to arrayOf("Those Eyes", "Slow Dancing In The Dark", "Rock'n Roll, Morning Light Falls on You", "Breathe"),
                "Images" to arrayOf(R.drawable.img_artist_newwest, R.drawable.img_artist_joji, R.drawable.img_artist_akfg, R.drawable.img_artist_pinkfloyd),
                "Genres" to arrayOf("Indie-Pop", "R&B Soul", "Alternative Rock", "Psychedelic Rock"),
                "Languages" to arrayOf("English", "English", "Japanese", "English"),
                "TitleColor" to arrayOf("#FFFFFF"),
                "TextColor" to arrayOf("#DBDFE1")
            ),

            // Love Theme
            mapOf(
                "Artists" to arrayOf("Keshi", "JVKE", "Kessoku Band", "BAEKHYUN"),
                "Titles" to arrayOf("right here", "this is what falling in love feels like", "If I could be a constellation", "UN Village"),
                "Images" to arrayOf(R.drawable.img_artist_keshi, R.drawable.img_artist_jvke, R.drawable.img_artist_kessoku, R.drawable.img_artist_baekhyun),
                "Genres" to arrayOf("Alternative R&B", "Pop", "J-Rock", "K-pop"),
                "Languages" to arrayOf("English", "English", "Japanese", "Korean"),
                "TitleColor" to arrayOf("#FFFFFF"),
                "TextColor" to arrayOf("#916F6F")
            ),

            // Study Theme
            mapOf(
                "Artists" to arrayOf("Joe Hisashi", "C418", "Jami Lynne", "Ichika Nito"),
                "Titles" to arrayOf("A town with an ocean view", "Sweden", "WHITESPACE", "i miss you"),
                "Images" to arrayOf(R.drawable.img_artist_joe, R.drawable.img_artist_c418, R.drawable.img_artist_jami, R.drawable.img_artist_ichika),
                "Genres" to arrayOf("Classical", "Ambience", "Dreamcore", "Lofi Hip Hop"),
                "Languages" to arrayOf("Instrumental", "Instrumental", "Instrumental", "Instrumental"),
                "TitleColor" to arrayOf("#FFFFFF"),
                "TextColor" to arrayOf("#C2B3A4")
            ),

            // Dance Theme
            mapOf(
                "Artists" to arrayOf("Parov Stelar", "Bruno Mars", "Amine", "Tom Misch"),
                "Titles" to arrayOf("Catgroove", "24k Magic", "Reel It In", "Disco Yes"),
                "Images" to arrayOf(R.drawable.img_artist_parov, R.drawable.img_artist_bruno, R.drawable.img_artist_amine, R.drawable.img_artist_tom),
                "Genres" to arrayOf("Electro Swing", "Dance-Pop", "Hip Hop Rap", "Funk"),
                "Languages" to arrayOf("Instrumental", "English", "English", "Instrumental"),
                "TitleColor" to arrayOf("#000000"),
                "TextColor" to arrayOf("#99866F")
            )
        )

        // Update song details based on theme's index
        for (i in songTitlesView.indices){
            // Check the length of the Song Title for setting limits
            val maxSongTitleLength = 16
            val getSongTitle = songDetails[themeIndex]["Titles"]?.get(i).toString()
            val setSongTitle: String = if (getSongTitle.length > maxSongTitleLength){
                getSongTitle.substring(0,maxSongTitleLength) + ".."
            } else {
                getSongTitle
            }

            // Update song title and its text color
            songTitlesView[i].text = setSongTitle
            songTitlesView[i].setTextColor(Color.parseColor(songDetails[themeIndex]["TitleColor"]?.get(0).toString()))

            // Update Artist's image
            artistImageView[i].setImageResource(songDetails[themeIndex]["Images"]?.get(i) as Int)

            // Update Song's details and its text color
            val artistName = songDetails[themeIndex]["Artists"]?.get(i) as String
            val genreSong = songDetails[themeIndex]["Genres"]?.get(i) as String
            val languageSong = songDetails[themeIndex]["Languages"]?.get(i) as String
            songDetailsView[i].text = "Artist: $artistName\nGenre: $genreSong\nLanguage: $languageSong"
            songDetailsView[i].setTextColor(Color.parseColor(songDetails[themeIndex]["TextColor"]?.get(0).toString()))
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