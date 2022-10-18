package com.example.firststep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import pl.droidsonroids.gif.GifImageView

class MainActivity : AppCompatActivity() {
    private lateinit var shape: GifImageView
    private lateinit var alpha: GifImageView
    private lateinit var digit: GifImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main)

        // setup
        shape = findViewById(R.id.shape)
        alpha = findViewById(R.id.alpha)
        digit = findViewById(R.id.digit)


        shape.setOnClickListener {
            var intent = Intent(this,Sample::class.java)
            intent.putExtra("setView","shape")
            startActivity(intent)
        }

        alpha.setOnClickListener{
            var intent = Intent(this,Sample::class.java)
            intent.putExtra("setView","alpha")
            startActivity(intent)
        }

        digit.setOnClickListener{
            var intent = Intent(this,Sample::class.java)
            intent.putExtra("setView","digit")
            startActivity(intent)
        }


    }
}