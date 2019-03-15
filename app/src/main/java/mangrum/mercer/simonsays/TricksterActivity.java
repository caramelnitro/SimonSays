package mangrum.mercer.simonsays;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TricksterActivity extends AppCompatActivity implements View.OnClickListener {
    Random r = new Random();
    final int TIMER_ONE = 1000;
    int iSub = 0;
    static int turns = 1;
    static int guess = 0;
    int score = 0;
    int [] reqs = new int[8];
    int [] colors = new int[8];
    final int [] color = {R.drawable.ltgreen_button, R.drawable.ltred_button, R.drawable.ltyellow_button, R.drawable.ltblue_button};
    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trickster);

        soundsLoaded = new HashSet<>();
        //create preferences/get value from previous games
        SharedPreferences prefs = this.getSharedPreferences("SimonSays", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        score = prefs.getInt("HighScoreTrickster", 0);
        TextView tv = findViewById(R.id.highScore);
        tv.setText("High Score: " + score);

        final Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Button playButton = findViewById(R.id.playButton);
        final Button greenSquare = findViewById(R.id.button);
        greenSquare.setOnClickListener(this);
        final Button redSquare = findViewById(R.id.button2);
        redSquare.setOnClickListener(this);
        final Button yellowSquare = findViewById(R.id.button3);
        yellowSquare.setOnClickListener(this);
        final Button blueSquare = findViewById(R.id.button4);
        blueSquare.setOnClickListener(this);
        TextView tv = findViewById(R.id.highScore);
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setUsage(AudioAttributes.USAGE_GAME);



        SoundPool.Builder sp = new SoundPool.Builder();
        sp.setAudioAttributes(builder.build());
        sp.setMaxStreams(6);

        soundPool = sp.build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundsLoaded.add(sampleId);
                } else {
                    Log.i("Error", "Sound not loaded!");
                }
            }
        });

        final int greenId = soundPool.load(this, R.raw.green, 1);
        final int redId = soundPool.load(this, R.raw.red, 1);
        final int yellowId = soundPool.load(this, R.raw.yellow, 1);
        final int blueId = soundPool.load(this, R.raw.blue, 1);
        final int loseId = soundPool.load(this, R.raw.wrong, 1);
        final int winId = soundPool.load(this, R.raw.win, 1);

        if (v.getId() == R.id.button && reqs[guess] == 1) {
            playSound(greenId);
            guess++;
        } else if (v.getId() == R.id.button2 && reqs[guess] == 2) {
            playSound(redId);
            guess++;
        } else if (v.getId() == R.id.button3 && reqs[guess] == 3) {
            playSound(yellowId);
            guess++;
        } else if (v.getId() == R.id.button4 && reqs[guess] == 0) {
            playSound(blueId);
            guess++;
        } else if (v.getId() != R.id.playButton) {
            turns = 1;
            guess = 0;
            playSound(loseId);
            playButton.setClickable(true);
        }
        if (v.getId() == R.id.playButton || guess == turns) {
            final int[] all = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

            playButton.setClickable(false);
            Handler handler = new Handler();
            int compSeq = all[r.nextInt(all.length)];
            if (turns == guess) {
                guess = 0;
                turns++;
            }
            if (turns == 9) {
                playSound(winId);
            } else {
                reqs[turns - 1] = (compSeq % 4);
                compSeq = all[r.nextInt(all.length)];
                colors[turns - 1] = color[compSeq%4];
                iSub = 0;
                for (int i = 0; i < turns; i++) {
                    final int temp = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int turnTime = 1250;
                            if (reqs[iSub] == 1) {
                                greenSquare.setBackgroundResource(colors[temp]);
                                playSound(greenId);
                                greenSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        greenSquare.setBackgroundResource(R.drawable.white_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 2) {
                                redSquare.setBackgroundResource(colors[temp]);
                                playSound(redId);
                                redSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        redSquare.setBackgroundResource(R.drawable.white_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 3) {
                                yellowSquare.setBackgroundResource(colors[temp]);
                                playSound(yellowId);
                                yellowSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        yellowSquare.setBackgroundResource(R.drawable.white_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 0) {
                                blueSquare.setBackgroundResource(colors[temp]);
                                playSound(blueId);
                                blueSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        blueSquare.setBackgroundResource(R.drawable.white_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            }
                            playButton.postDelayed(new Runnable() {

                                @Override
                                public void run() {

                                }
                            }, turns * turnTime);
                        }
                    }, 1500 * i);
                }
                if(turns>score) {
                    SharedPreferences prefs = this.getSharedPreferences("SimonSays", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("HighScoreTrickster", turns);
                    editor.commit();
                    tv.setText("High Score: " + turns);
                }
            }
        }
    }
    private void playSound(int id){
        if(soundsLoaded.contains(id)) {
            soundPool.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }
}

