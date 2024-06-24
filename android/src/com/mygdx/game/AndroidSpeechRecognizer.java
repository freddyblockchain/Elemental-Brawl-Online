package com.mygdx.game;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.mygdx.game.AndroidAdapters.SpeechRecognizer;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.IOException;

import org.vosk.android.StorageService;

public class AndroidSpeechRecognizer implements SpeechRecognizer {
    public static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private Recognizer recognizer;
    private AudioRecord record;
    private boolean isListening = false;
    private Context context;
    private Model model;

    public AndroidSpeechRecognizer(Context context){
        this.context = context;
    }

    public void initSpeechRecognizer(){
        StorageService.unpack(context, "largevosk", "modeli",
                (model) -> {
                    this.model = model;
                    try {
                        recognizer = new Recognizer(model, 16000.f);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    startListening();
                },
                (exception) -> System.out.println("error " + exception.getMessage()));
    }

    @Override
    public void startListening() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            initSpeechRecognizer();
        }
        int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        record = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        record.startRecording();

        isListening = true;

        new Thread(() -> {
            byte[] buffer = new byte[bufferSize];
            while (isListening) {
                int nread = record.read(buffer, 0, buffer.length);
                if (nread > 0) {
                    if (recognizer.acceptWaveForm(buffer, nread)) {
                        String result = recognizer.getResult();
                        Log.i("Vosk", result);
                    } else {
                        String partialResult = recognizer.getPartialResult();
                        Log.i("Vosk", partialResult);
                    }
                }
            }
        }).start();
    }

    @Override
    public void stopListening() {
        isListening = false;
        if (record != null) {
            record.stop();
            record.release();
            record = null;
        }
    }
}
