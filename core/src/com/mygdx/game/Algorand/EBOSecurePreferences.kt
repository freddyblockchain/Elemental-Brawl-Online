package com.mygdx.game.Algorand

// In your core module, create a new interface
interface EBOSecurePreferences {
    fun putString(key: String?, value: String?)
    fun getString(key: String?, defaultValue: String?): String?
}