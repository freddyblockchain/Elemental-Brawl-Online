package com.mygdx.game

// Replace with your actual package name
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mygdx.game.Algorand.EBOSecurePreferences
import java.io.IOException
import java.security.GeneralSecurityException


class AndroidSecurePreferences(context: Context?, val fileName: String) : EBOSecurePreferences {
    private var sharedPreferences: SharedPreferences? = null

    init {
        try {
            val masterKey = MasterKey.Builder(context!!)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            sharedPreferences = EncryptedSharedPreferences.create(
                context,
                fileName,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun putString(key: String?, value: String?) {
        sharedPreferences!!.edit().putString(key, value).apply()
    }

    override fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences!!.getString(key, defaultValue)
    }
}