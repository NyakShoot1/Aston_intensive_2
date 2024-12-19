package ru.nyakshoot.aston_intensive_2

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import ru.nyakshoot.aston_intensive_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val imageView = binding.imageView
        val customTextView = binding.customTextView
        val rainbowDrumView = binding.rainbowDrumView
        val resetButton = binding.resetButton
        val sizeSeekBar = binding.sizeSeekBar

        view.forceLayout()

        sizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rainbowDrumView.setScale(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        rainbowDrumView.setScale(sizeSeekBar.progress.toFloat())

        rainbowDrumView.setOnClickListener {
            rainbowDrumView.startRotation { colorName ->
                if (!rainbowDrumView.isRotating){
                    viewModel.updateState(colorName)
                    viewModel.updateRotation(rainbowDrumView.rotation)
                }
            }
        }

        resetButton.setOnClickListener {
            viewModel.updateState("")
            customTextView.visibility = View.GONE
            imageView.visibility = View.GONE
        }

        viewModel.rotation.observe(this) { rotation ->
            rainbowDrumView.rotation = rotation
        }

        viewModel.currentState.observe(this) { state ->
            when (state) {
                is DrumState.ShowText -> {
                    customTextView.apply {
                        visibility = View.VISIBLE
                        setText("Hello world")
                    }
                    imageView.visibility = View.GONE
                }

                is DrumState.ShowImage -> {
                    imageView.apply {
                        visibility = View.VISIBLE
                        load("https://baconmockup.com/1280/720") {
                            crossfade(true)
                        }
                    }
                    customTextView.visibility = View.GONE
                }
                DrumState.Initial -> {
                    customTextView.visibility = View.GONE
                    imageView.visibility = View.GONE
                }
            }
        }
    }
}