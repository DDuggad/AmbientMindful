package com.divyanshApps.AmbientMindful;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("UseSwitchCompatOrMaterialXml")
public class MainActivity extends AppCompatActivity {

    // Array to hold 4 players: 0=Rain, 1=Forest, 2=Fire, 3=Ocean
    private final MediaPlayer[] players = new MediaPlayer[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all tracks
        setupTrack(R.id.switchRain,   R.id.seekRain,   R.raw.rain,   0);
        setupTrack(R.id.switchForest, R.id.seekForest, R.raw.forest, 1);
        setupTrack(R.id.switchFire,   R.id.seekFire,   R.raw.fire,   2);
        setupTrack(R.id.switchOcean,  R.id.seekOcean,  R.raw.ocean,  3);
    }

    /**
     * Core Logic: Connects UI (Switch/SeekBar) to Audio (MediaPlayer)
     */
    private void setupTrack(int switchId, int seekId, int rawFile, int index) {
        Switch toggle = findViewById(switchId);
        SeekBar volumeSlider = findViewById(seekId);

        // 1. Handle ON/OFF
        toggle.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                // Initialize & Start
                if (players[index] == null) {
                    players[index] = MediaPlayer.create(this, rawFile);
                    players[index].setLooping(true);
                }
                players[index].start();

                // Sync volume immediately
                updateVolume(index, volumeSlider.getProgress());
            } else {
                // Stop & Release Resource
                if (players[index] != null) {
                    players[index].stop();
                    players[index].release();
                    players[index] = null;
                }
            }
        });

        // 2. Handle Volume (Real-time mixing)
        volumeSlider.setOnSeekBarChangeListener(new SimpleSeekBarListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (players[index] != null) {
                    updateVolume(index, progress);
                }
            }
        });
    }

    /**
     * Helper: Convert slider 0-100 to volume 0.0-1.0
     */
    private void updateVolume(int index, int progress) {
        float vol = progress / 100f;
        if (players[index] != null) {
            players[index].setVolume(vol, vol);
        }
    }

    /**
     * Cleanup: Only release players on valid destruction
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (MediaPlayer p : players) {
            if (p != null) {
                p.release();
            }
        }
    }

    /**
     * Abstract Helper to remove clutter from the main code.
     * This fixes warnings about "missing override methods".
     */
    private abstract static class SimpleSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}