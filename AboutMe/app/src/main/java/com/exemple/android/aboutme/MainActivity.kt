package com.exemple.android.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.exemple.android.aboutme.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.user = User("Felipe Wisniewski")

        done_button.setOnClickListener {
            addNickname(it)
        }
    }

    private fun addNickname(buttonView: View) {
        binding.invalidateAll()

        nickname_edit.visibility = View.GONE
        buttonView.visibility = View.GONE
        nickname_text.visibility = View.VISIBLE

        //Hide Keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(buttonView.windowToken, 0)
    }
}
