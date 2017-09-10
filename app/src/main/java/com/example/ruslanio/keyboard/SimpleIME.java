package com.example.ruslanio.keyboard;

import android.app.Service;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;

/**
 * Created by Ruslanio on 10.09.2017.
 */

public class SimpleIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private boolean mCaps = false;
    private KeyboardView mKeyBoardView;
    private Keyboard mKeyboard;


    @Override
    public View onCreateInputView() {
        mKeyBoardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard,null,false);
        mKeyboard = new Keyboard(this,R.xml.qwerty);
        mKeyBoardView.setKeyboard(mKeyboard);
        mKeyBoardView.setOnKeyboardActionListener(this);
        return mKeyBoardView;

    }

    private void playSound(int keyCode){
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode){
            case 32:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d("tag", primaryCode+"");
        InputConnection ic = getCurrentInputConnection();
        playSound(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                mCaps = !mCaps;
                mKeyboard.setShifted(mCaps);
                mKeyBoardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && mCaps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
