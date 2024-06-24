package com.mygdx.game.AndroidAdapters

interface SpeechRecognizer {
    fun startListening(): Unit
    fun stopListening(): Unit
}