package com.example.project2

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class SeasonsFragment : Fragment() {

    private lateinit var wheelImageView: ImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var dateTimeTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private var currentSeason = 0
    private val colors = intArrayOf(0xFF8FBC8F.toInt(), 0xFFFFFF00.toInt(), 0xFFFF4500.toInt(), 0xFFFFFFFF.toInt()) // Green, Yellow, OrangeRed, White
    private val images = intArrayOf(R.drawable.spring, R.drawable.summer, R.drawable.autumn, R.drawable.winter)
    private val musicFiles = intArrayOf(R.raw.spring_song, R.raw.summer_song, R.raw.autumn_song, R.raw.winter_song)

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler()
    private var runnable: Runnable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.season_activity, container, false)

        wheelImageView = rootView.findViewById(R.id.wheelImage)
        backgroundImageView = rootView.findViewById(R.id.backgroundImage)
        dateTimeTextView = rootView.findViewById(R.id.dateTime)
        startButton = rootView.findViewById(R.id.startButton)
        stopButton = rootView.findViewById(R.id.stopButton)

        startButton.setOnClickListener {
            currentSeason = 0
            startSeasonsAnimation()
        }

        stopButton.setOnClickListener {
            stopSeasonsAnimation()
        }

        startSeasonsAnimation()

        return rootView
    }

    private fun animateImageFadeInOut(fromImageResource: Int, toImageResource: Int) {
        val imageView = requireView().findViewById<ImageView>(R.id.backgroundImage)
        val fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = 1000
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                imageView.setImageResource(toImageResource)
                val fadeIn = AlphaAnimation(0.0f, 1.0f)
                fadeIn.duration = 1000
                imageView.startAnimation(fadeIn)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        imageView.startAnimation(fadeOut)

        val sunImageView = requireView().findViewById<ImageView>(R.id.sunImageView)
        val birdsImageView = requireView().findViewById<ImageView>(R.id.birdsImageView)
        val cloudImageView = requireView().findViewById<ImageView>(R.id.cloudImageView)

        val sunAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, -0.1f
        )
        sunAnimation.duration = 2000
        sunAnimation.fillAfter = true
        sunImageView.startAnimation(sunAnimation)

        val birdsAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.2f,
            Animation.RELATIVE_TO_SELF, -0.2f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        birdsAnimation.duration = 3000
        birdsAnimation.repeatCount = Animation.INFINITE
        birdsAnimation.repeatMode = Animation.REVERSE
        birdsImageView.startAnimation(birdsAnimation)

        val cloudAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.3f,
            Animation.RELATIVE_TO_SELF, -0.3f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        cloudAnimation.duration = 4000
        cloudAnimation.repeatCount = Animation.INFINITE
        cloudAnimation.repeatMode = Animation.REVERSE
        cloudImageView.startAnimation(cloudAnimation)

        val wheelAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        wheelAnimation.duration = 3000
        wheelAnimation.repeatCount = Animation.INFINITE
        wheelImageView.startAnimation(wheelAnimation)
    }

    private fun playMusic(musicFileResource: Int) {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
                mediaPlayer = null
            }
        }

        mediaPlayer = MediaPlayer.create(requireContext(), musicFileResource)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private fun startSeasonsAnimation() {

        runnable = object : Runnable {
            override fun run() {
                val nextSeason = (currentSeason + 1) % colors.size
                val colorFrom = colors[currentSeason]
                val colorTo = colors[nextSeason]
                val imageFrom = images[currentSeason]
                val imageTo = images[nextSeason]
                val musicFile = musicFiles[currentSeason]

                val colorAnimator = ObjectAnimator.ofObject(
                    backgroundImageView,
                    "backgroundColor",
                    ArgbEvaluator(),
                    colorFrom,
                    colorTo
                )
                colorAnimator.duration = 2000
                colorAnimator.start()

                animateImageFadeInOut(imageFrom, imageTo)
                playMusic(musicFile)

                currentSeason = nextSeason

                handler.postDelayed(this, 15000) // 15 seconds
            }
        }

        handler.post(runnable!!)
    }

    private fun stopSeasonsAnimation() {
        runnable?.let {
            handler.removeCallbacks(it)
            runnable = null
        }
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
                mediaPlayer = null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSeasonsAnimation()
    }
}
