package com.example.firststep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Sample extends AppCompatActivity {

    // creating the object of type DrawView
    // in order to get the reference of the View
    private DrawView paint;

    // variables to control the user-interface
    private CardView click,undocv;
    private TextView screen;
    private FloatingActionButton refresh;
    private FloatingActionButton info;

    private int penColor;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String str = intent.getStringExtra("setView");

        if(str.equals("alpha")) {
            setContentView(R.layout.alpha2);
            penColor = Color.parseColor("#AA336A");
            category = "alphabet";
        }
        else if(str.equals("shape")) {
            setContentView(R.layout.shapes2);
            penColor = Color.parseColor("#00008B");
            category = "shape";
        }
        else if(str.equals("digit")) {
            setContentView(R.layout.digit2);
            penColor = Color.parseColor("#006400");
            category = "digit";
        }
        Toast.makeText(getApplicationContext(),"It is recommended to draw your content a little bigger",Toast.LENGTH_LONG)
                .show();

        // getting the reference of the views from their ids
        paint = (DrawView) findViewById(R.id.draw_view);
        screen = (TextView) findViewById(R.id.screen);
        click = (CardView) findViewById(R.id.click);
        undocv = (CardView) findViewById(R.id.undocv);
        refresh = (FloatingActionButton) findViewById(R.id.refresh);
        info = (FloatingActionButton) findViewById(R.id.info);

        refresh.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        });

        info.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Sample.this);

            switch (category){
                case "alphabet":
                    builder.setMessage("This section support upper case english alphabets A-Z");
                    break;

                case "shape":
                    builder.setMessage("This section support the following shapes: circle (C), triangle (T), rectangle (R), " +
                            "star (S), and heart (H)");
                    break;

                case "digit":
                    builder.setMessage("This section support digits 0-9");
                    break;

                default:
                    builder.setMessage("something's wrong");
            }
            builder.setTitle("INFO: ");
            builder.setCancelable(true);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        undocv.setOnClickListener(view -> paint.undo());

        click.setOnClickListener(view -> {
            Bitmap bmp = paint.save();
            Classify type = new Classify(bmp,getApplicationContext(),category);
            screen.setText(type.getType());
            String actualName = type.getActualName();
            Float confidence = type.getConfidence();

            if (confidence > 75.0f){
                screen.setTextColor(Color.GREEN);

                if(category.equals("alphabet")) speak("It's "+actualName);
                else if(category.equals("shape")) speak("It's a "+actualName);
                else if(category.equals("digit")) speak("That's "+actualName);
            }
            else if(confidence >= 65.0f && confidence <= 75.0f){
                screen.setTextColor(Color.RED);

                if(category.equals("alphabet")) speak("I reckon it's "+actualName);
                else if(category.equals("shape")) speak("It looks like a "+actualName);
                else if(category.equals("digit")) speak("I guess it's "+actualName);
            }
            else{
                screen.setText("😕");
                speak("I don't know what it is");
            }
        });


        // pass the height and width of the custom view
        // to the init method of the DrawView object
        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width, penColor);
            }
        });
    }



    private TextToSpeech tts;
    private void speak(String sentence) {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    tts.setLanguage(Locale.US);
                    tts.speak(sentence,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

    }

}
