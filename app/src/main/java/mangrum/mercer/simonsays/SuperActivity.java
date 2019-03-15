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

public class SuperActivity extends AppCompatActivity implements View.OnClickListener {

    Random r = new Random();
    final int TIMER_ONE = 1000;
    int iSub = 0;
    static int turns = 1;
    static int guess = 0;
    int score = 0;
    int [] reqs = new int[8];
    private SoundPool soundPool;
    private Set<Integer> soundsLoaded;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_super);

        soundsLoaded = new HashSet<>();
        //create preferences/get value from previous games
        SharedPreferences prefs = this.getSharedPreferences("SimonSays", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        score = prefs.getInt("HighScoreSuper", 0);
        TextView tv = findViewById(R.id.highScore);
        tv.setText("High Score: " + score);
        final Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Button playButton = findViewById(R.id.playButton);
        final Button greenSquare = findViewById(R.id.green_button);
        greenSquare.setOnClickListener(this);
        final Button redSquare = findViewById(R.id.red_button);
        redSquare.setOnClickListener(this);
        final Button yellowSquare = findViewById(R.id.yellow_button);
        yellowSquare.setOnClickListener(this);
        final Button blueSquare = findViewById(R.id.blue_button);
        blueSquare.setOnClickListener(this);

        final Button greenSquare2 = findViewById(R.id.green_button2);
        greenSquare2.setOnClickListener(this);
        final Button redSquare2 = findViewById(R.id.red_button2);
        redSquare2.setOnClickListener(this);
        final Button yellowSquare2 = findViewById(R.id.yellow_button2);
        yellowSquare2.setOnClickListener(this);
        final Button blueSquare2 = findViewById(R.id.blue_button2);
        blueSquare2.setOnClickListener(this);

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

        if (v.getId() == R.id.green_button && reqs[guess] == 1) {
            playSound(greenId);
            guess++;
        } else if (v.getId() == R.id.red_button && reqs[guess] == 2) {
            playSound(redId);
            guess++;
        } else if (v.getId() == R.id.yellow_button && reqs[guess] == 3) {
            playSound(yellowId);
            guess++;
        } else if (v.getId() == R.id.blue_button && reqs[guess] == 0) {
            playSound(blueId);
            guess++;
        }else if (v.getId() == R.id.green_button2 && reqs[guess] == 4) {
            playSound(greenId);
            guess++;
        }else if (v.getId() == R.id.red_button2 && reqs[guess] == 5) {
            playSound(redId);
            guess++;
        }else if (v.getId() == R.id.yellow_button2 && reqs[guess] == 6) {
            playSound(yellowId);
            guess++;
        }else if (v.getId() == R.id.blue_button2 && reqs[guess] == 7) {
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
            final int compSeq = all[r.nextInt(all.length)];
            if (turns == guess) {
                guess = 0;
                turns++;
            }
            if (turns == 9) {
                playSound(winId);
            } else {
                reqs[turns - 1] = (compSeq % 8);
                iSub = 0;
                for (int i = 0; i < turns; i++) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int turnTime = 1250;
                            if (reqs[iSub] == 1) {
                                greenSquare.setBackgroundResource(R.drawable.ltgreen_button);
                                playSound(greenId);
                                greenSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        greenSquare.setBackgroundResource(R.drawable.green_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 2) {
                                redSquare.setBackgroundResource(R.drawable.ltred_button);
                                playSound(redId);
                                redSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        redSquare.setBackgroundResource(R.drawable.red_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 3) {
                                yellowSquare.setBackgroundResource(R.drawable.ltyellow_button);
                                playSound(yellowId);
                                yellowSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        yellowSquare.setBackgroundResource(R.drawable.yellow_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 0) {
                                blueSquare.setBackgroundResource(R.drawable.ltblue_button);
                                playSound(blueId);
                                blueSquare.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        blueSquare.setBackgroundResource(R.drawable.blue_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);


                            }else if(reqs[iSub] == 4) {
                                greenSquare2.setBackgroundResource(R.drawable.ltgreen_button);
                                playSound(greenId);
                                greenSquare2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        greenSquare2.setBackgroundResource(R.drawable.green_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 5) {
                                redSquare2.setBackgroundResource(R.drawable.ltred_button);
                                playSound(redId);
                                redSquare2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        redSquare2.setBackgroundResource(R.drawable.red_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 6) {
                                yellowSquare2.setBackgroundResource(R.drawable.ltyellow_button);
                                playSound(yellowId);
                                yellowSquare2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        yellowSquare2.setBackgroundResource(R.drawable.yellow_button);
                                        iSub++;
                                    }
                                }, TIMER_ONE);
                            } else if (reqs[iSub] == 7) {
                                blueSquare2.setBackgroundResource(R.drawable.ltblue_button);
                                playSound(blueId);
                                blueSquare2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        blueSquare2.setBackgroundResource(R.drawable.blue_button);
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
                    editor.putInt("HighScoreSuper", turns);
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
