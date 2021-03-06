package com.example.noel.videolist.activity.interview.practice;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.noel.videolist.R;
import com.example.noel.videolist.activity.audio.BaseInterviewActivity;
import com.example.noel.videolist.activity.audio.TTSPlayer;

import java.util.Locale;

/**
 * Created by Noel on 4/19/2017.
 */

public class InterviewPracticeActivity extends BaseInterviewActivity {

    private static final String TAG = InterviewPracticeActivity.class.getName();
    private static final int MAX_AMPLITUDE = 30000;

    // TODO: Create a controller class that handles the interview practice gameplay

    TTSPlayer ttsPlayer;

    TextView textViewQuestion;
    ProgressBar progressBarRecordVolume;
    Button buttonReplay;
    Button buttonNext;

    Runnable runEnableButtons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_practice);

        initRunnables();

        // TODO: Check if device has TTS
        ttsPlayer = new TTSPlayer(getApplicationContext(), this);

        textViewQuestion = (TextView) findViewById(R.id.interview_practice_tv_question);
        progressBarRecordVolume = (ProgressBar) findViewById(R.id.interview_practice_pb_recording_level);
        // TODO: Fetch this from database
        textViewQuestion.setText("What is your greatest achievement?");
        progressBarRecordVolume.setMax(MAX_AMPLITUDE);

        initButtons();
        playQuestion();
    }

    private void initButtons() {
        buttonReplay = (Button) findViewById(R.id.interview_practice_b_replay);
        buttonReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playQuestion();
            }
        });
        buttonNext = (Button) findViewById(R.id.interview_practice_b_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save current audio recording and proceed to next question
                audioRecorder.stopRecording();
            }
        });

        setButtonsEnabled(false);
    }

    private void initRunnables() {
        runEnableButtons = new Runnable() {
            @Override
            public void run() {
                setButtonsEnabled(true);
                // TODO: Replace with generated name
                audioRecorder.startRecording(getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp");
            }
        };
    }

    private void setButtonsEnabled(boolean isEnabled) {
        buttonReplay.setEnabled(isEnabled);
        buttonNext.setEnabled(isEnabled);
    }

    private void playQuestion() {
        if (isQuestionPlaying()) {
            return;
        }
        setButtonsEnabled(false);
        String text = textViewQuestion.getText().toString();
        // TODO: Evaluate if sound file is null
        if (true) {
            ttsPlayer.speak(text);
        }
    }

    private boolean isQuestionPlaying() {
        return audioPlayer.isPlaying() && ttsPlayer.isPlaying();
    }

    @Override
    public void onFinishLoading() {
        playQuestion();
    }

    @Override
    public void onFinishPlaying() {
        runOnUiThread(runEnableButtons);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsPlayer.dispose();
    }

    @Override
    public void updateAmplitude(int amplitude) {
        progressBarRecordVolume.setProgress(amplitude);
    }
}
