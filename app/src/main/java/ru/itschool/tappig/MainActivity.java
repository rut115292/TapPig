package ru.itschool.tappig;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    int nTaps;
    TextView textTop;
    TextView textBottom;
    ImageView imgPig;
    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    int pigSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textTop = findViewById(R.id.textTop);
        textBottom = findViewById(R.id.textBottom);
        imgPig = findViewById(R.id.imagePig);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
        pigSoundId = soundPool.load(this, R.raw.pig1, 1);

        loadPigs();
        updateTextBottom();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePigs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    public void tap(View view) {
        nTaps++;
        updateTextBottom();

        /*if(nTaps%10 == 0) mediaPlayer = android.media.MediaPlayer.create(MainActivity.this, R.raw.pig1);
        else mediaPlayer = android.media.MediaPlayer.create(MainActivity.this, R.raw.pig2);
        mediaPlayer.start();*/
        soundPool.play(pigSoundId, 1f, 1f, 1, 0, 1.0f);

    }

    void updateTextBottom(){
        textBottom.setText("Свинья потрогана "+nTaps+ end());
    }

    String end(){
        if(nTaps%10>1 && nTaps%10<5 && nTaps/10%10!=1){
            return " раза";
        }
        return " раз";
    }
    public void savePigs() {
        SharedPreferences prefs = getSharedPreferences("PiggyPrefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("num taps", nTaps).apply();
    }

    public void loadPigs(){
        SharedPreferences prefs = getSharedPreferences( "PiggyPrefs", Context.MODE_PRIVATE);
        nTaps =  prefs.getInt("num taps", 0);
    }
}