package com.divyanshApps.AmbientMindful;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Use an array to store the 4 media players.
    // Index 0: Rain, 1: Forest, 2: Fire, 3: Ocean
    private final MediaPlayer[] players = new MediaPlayer[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize each track.
        // We pass the Switch ID, SeekBar ID, Audio Resource ID, and the Array Index.
        setupTrack(R.id.switchRain,   R.id.seekRain,   R.raw.rain,   0);
        setupTrack(R.id.switchForest, R.id.seekForest, R.raw.forest, 1);
        setupTrack(R.id.switchFire,   R.id.seekFire,   R.raw.fire,   2);
        setupTrack(R.id.switchOcean,  R.id.seekOcean,  R.raw.ocean,  3);
    }

    /**
     * This single function sets up the logic for any sound track.
     * Explain this: "Instead of repeating code, I created a reusable function."
     */
    private void setupTrack(int switchId, int seekId, int rawFile, int index) {
        Switch toggle = findViewById(switchId);
        SeekBar volumeSlider = findViewById(seekId);

        // 1. Handle ON/OFF Logic
        toggle.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                // Create player and start loop
                players[index] = MediaPlayer.create(this, rawFile);
                players[index].setLooping(true);
                players[index].start();

                // Set initial volume
                updateVolume(index, volumeSlider.getProgress());
            } else {
                // Stop and release memory
                if (players[index] != null) {
                    players[index].release();
                    players[index] = null;
                }
            }
        });

        // 2. Handle Volume Logic
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (players[index] != null) {
                    updateVolume(index, progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Helper to convert 0-100 slider value to 0.0-1.0 volume
    private void updateVolume(int index, int progress) {
        float volume = progress / 100f;
        players[index].setVolume(volume, volume);
    }

    // Cleanup memory when app is fully closed (swiped away)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (MediaPlayer p : players) {
            if (p != null) p.release();
        }
    }
}