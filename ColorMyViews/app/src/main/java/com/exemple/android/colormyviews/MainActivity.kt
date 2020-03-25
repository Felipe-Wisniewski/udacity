package com.exemple.android.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListeners()
    }

    private fun setListeners() {
        val clickableViews = listOf(box_one_text, box_two_text, box_three_text,
            box_four_text, box_five_text, constraint_layout, red_button, yellow_button, green_button)

        for (view in clickableViews) {
            view.setOnClickListener {
                makeColored(it)
            }
        }
    }

    private fun makeColored(viewClicked: View) {
        when (viewClicked.id) {
            // Boxes using Color class colors for background
            R.id.box_one_text -> viewClicked.setBackgroundColor(Color.DKGRAY)
            R.id.box_two_text -> viewClicked.setBackgroundColor(Color.GRAY)
            // Boxes using Android color resources for background
            R.id.box_three_text -> viewClicked.setBackgroundResource(android.R.color.holo_green_light)
            R.id.box_four_text -> viewClicked.setBackgroundResource(android.R.color.holo_green_dark)
            R.id.box_five_text -> viewClicked.setBackgroundResource(android.R.color.holo_blue_light)
            // Boxes using custom colors for background
            R.id.red_button -> box_three_text.setBackgroundResource(R.color.my_red)
            R.id.yellow_button -> box_four_text.setBackgroundResource(R.color.my_yellow)
            R.id.green_button -> box_five_text.setBackgroundResource(R.color.my_green)

            else -> viewClicked.setBackgroundColor(Color.LTGRAY)
        }
    }
}
