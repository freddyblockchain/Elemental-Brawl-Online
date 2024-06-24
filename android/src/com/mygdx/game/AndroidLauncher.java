package com.mygdx.game;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.mediapipe.tasks.components.containers.Embedding;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.Delegate;
import com.google.mediapipe.tasks.text.textembedder.TextEmbedder;
import com.mygdx.game.AndroidAdapters.EBOSecurePreferences;
import com.mygdx.game.AndroidAdapters.SpeechRecognizer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import static com.mygdx.game.AndroidSpeechRecognizer.PERMISSIONS_REQUEST_RECORD_AUDIO;

public class AndroidLauncher extends AndroidApplication {

	private AndroidSpeechRecognizer speechRecognizer = new AndroidSpeechRecognizer(this);
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Security.removeProvider("BC");
		Security.insertProviderAt(new BouncyCastleProvider(), 0);
		EBOSecurePreferences securePreferences = new AndroidSecurePreferences(this, "EBOKeyFile");

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		BaseOptions baseOptions = BaseOptions
				.builder()
				.setModelAssetPath("universal_sentence_encoder.tflite")
				.setDelegate(Delegate.CPU)
				.build();
		var optionsBuilder = TextEmbedder.TextEmbedderOptions.builder().setBaseOptions(baseOptions);
		var options = optionsBuilder.build();
		var textEmbedder = TextEmbedder.createFromOptions(this, options);
		var sentences = new Sentences().commands;

		var sentence = "Alright";
		var sentenceEmbedding = getEmbeddings(sentence, textEmbedder);
        for (String current : sentences) {
            var currentEmbedding = getEmbeddings(current, textEmbedder);
            System.out.println("cosine Similarity between " + sentence + " and " + current + "is :" + TextEmbedder.cosineSimilarity(sentenceEmbedding, currentEmbedding));
            System.out.println("euclidian distance " + sentence + " and " + current + "is :" + euclideanDistance(sentenceEmbedding.floatEmbedding(), currentEmbedding.floatEmbedding()));
        }


		int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
		} else {
			speechRecognizer.initSpeechRecognizer();
		}

		initialize(new ElementalBrawlOnline(securePreferences, speechRecognizer), config);
	}

	public static double euclideanDistance(float[] firstArray, float[] secondArray) {
		if (firstArray.length != secondArray.length) {
			throw new IllegalArgumentException("Arrays must be of the same length");
		}

		double sumVector = 0.0;
		for (int i = 0; i < firstArray.length; i++) {
			double diff = firstArray[i] - secondArray[i];
			sumVector += Math.pow(diff, 2.0);
		}

		return Math.sqrt(sumVector);
	}

	private Embedding getEmbeddings(String sentence, TextEmbedder textEmbedder) {
		return textEmbedder
				.embed(sentence)
				.embeddingResult()
				.embeddings().get(0);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Recognizer initialization is a time-consuming and it involves IO,
				// so we execute it in async task
				speechRecognizer.initSpeechRecognizer();
			} else {
				finish();
			}
		}
	}

}
