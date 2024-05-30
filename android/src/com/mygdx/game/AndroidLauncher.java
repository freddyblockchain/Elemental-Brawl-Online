package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Algorand.EBOSecurePreferences;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Security.removeProvider("BC");
		Security.insertProviderAt(new BouncyCastleProvider(), 0);
		EBOSecurePreferences securePreferences = new AndroidSecurePreferences(this, "EBOKeyFile");

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ElementalBrawlOnline(securePreferences), config);
	}
}
