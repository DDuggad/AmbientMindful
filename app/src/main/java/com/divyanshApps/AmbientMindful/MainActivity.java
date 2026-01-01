package com.divyanshApps.AmbientMindful;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("SetTextI18n") // Suppress warnings for hardcoded text
public class MainActivity extends AppCompatActivity {

    private MediaPlayer rainPlayer, forestPlayer, firePlayer, oceanPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link buttons to logic
        setupButton(findViewById(R.id.btnRain), R.raw.rain, "Rain");
        setupButton(findViewById(R.id.btnForest), R.raw.forest, "Forest");
        setupButton(findViewById(R.id.btnFire), R.raw.fire, "Fire");
        setupButton(findViewById(R.id.btnOcean), R.raw.ocean, "Ocean");
    }

    private void setupButton(Button btn, int soundResId, String name) {
        btn.setOnClickListener(v -> {
            MediaPlayer player = getPlayer(name);

            if (player != null && player.isPlaying()) {
                // STOP LOGIC
                player.pause();
                player.seekTo(0);
                btn.setText("Play " + name);
                // Reset color to Original Blue (0xFF6495ED)
                btn.setBackgroundTintList(ColorStateList.valueOf(0xFF6495ED));
            } else {
                // PLAY LOGIC
                if (player == null) {
                    player = MediaPlayer.create(this, soundResId);
                    player.setLooping(true);
                    setPlayer(name, player);
                }
                player.start();
                btn.setText("Stop " + name);
                // Change color to Green (0xFF3CB371)
                btn.setBackgroundTintList(ColorStateList.valueOf(0xFF3CB371));
            }
        });
    }

    private MediaPlayer getPlayer(String name) {
        switch (name) {
            case "Rain": return rainPlayer;
            case "Forest": return forestPlayer;
            case "Fire": return firePlayer;
            case "Ocean": return oceanPlayer;
            default: return null;
        }
    }

    private void setPlayer(String name, MediaPlayer player) {
        switch (name) {
            case "Rain": rainPlayer = player; break;
            case "Forest": forestPlayer = player; break;
            case "Fire": firePlayer = player; break;
            case "Ocean": oceanPlayer = player; break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rainPlayer != null) rainPlayer.release();
        if (forestPlayer != null) forestPlayer.release();
        if (firePlayer != null) firePlayer.release();
        if (oceanPlayer != null) oceanPlayer.release();
    }
}