package com.example.ruslanio.keyboard;

import android.content.ContentValues;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.example.ruslanio.keyboard.database.helper.DBHelper;
import com.example.ruslanio.keyboard.util.DateManager;

import java.util.Calendar;


/**
 * Created by Ruslanio on 10.09.2017.
 */

public class SimpleIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private boolean isShown = false;
    private boolean mCaps = false;
    private KeyboardView mKeyBoardView;
    private Keyboard mKeyboard;
    private StringBuilder mSentenceBuilder;
    private StringBuilder mWordBuilder;

    private int[] mNumbers = new int[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57};

    private int mWordCount = 0;
    private DBHelper mDBHelper;

    private static final int KEY_CODE_SPACE = 32;
    private static final int WORD_COUNT = 5;

    private static final String IME_SERVICE_TAG = "ime_service";


    @Override
    public void onCreate() {
        super.onCreate();
        mDBHelper = new DBHelper(getApplicationContext());
    }

    @Override
    public View onCreateInputView() {
        Log.d(IME_SERVICE_TAG, "onCreateInputView");

        mSentenceBuilder = new StringBuilder();
        mWordBuilder = new StringBuilder();

        mKeyBoardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null, false);
        mKeyboard = new Keyboard(this, R.xml.qwerty);
        mKeyBoardView.setKeyboard(mKeyboard);
        mKeyBoardView.setOnKeyboardActionListener(this);

        return mKeyBoardView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveString();
        if (mDBHelper != null)
            mDBHelper.close();
        Log.d(IME_SERVICE_TAG, "onDestroy");
    }

    private void playSound(int keyCode) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
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
            default:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
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

        if (mWordCount == WORD_COUNT) {
            mWordCount = 0;
            saveString();
        }
        Log.d(IME_SERVICE_TAG, primaryCode + "");
        InputConnection ic = getCurrentInputConnection();
        playSound(primaryCode);
        switch (primaryCode) {

            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                if (mWordBuilder.length() != 0)
                    mWordBuilder.setLength(mWordBuilder.length() - 1);
                break;
            case Keyboard.KEYCODE_SHIFT:
                mCaps = !mCaps;
                mKeyboard.setShifted(mCaps);
                mKeyBoardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case KEY_CODE_SPACE:
                String word = mWordBuilder.toString();
                mSentenceBuilder.append(word).append(" ");
                mWordCount++;
                mWordBuilder = new StringBuilder();
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && mCaps) {
                    code = Character.toUpperCase(code);
                }
                if (checkCode(primaryCode))
                    mWordBuilder.append(code);
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onWindowShown() {
        super.onWindowShown();
        isShown = true;
    }

    @Override
    public void onWindowHidden() {
        super.onWindowHidden();
        isShown = false;
        onKey(KEY_CODE_SPACE, null);
        saveString();
    }

    private void saveString() {
        if (!mSentenceBuilder.toString().equals("")) {
            Calendar calendar = Calendar.getInstance();
            String date = DateManager.parseDate(calendar).toString();

            String sentence = mSentenceBuilder.toString();
            mSentenceBuilder = new StringBuilder();

            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT, sentence);
            contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_STATUS, DBHelper.STATUS_CLIENT);
            contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_DATE, date);
            mDBHelper.getWritableDatabase().insert(DBHelper.TEXT_ENTITY_TABLE_NAME, null, contentValues);
        }
    }


    private boolean checkCode(int code) {
        if (code == KEY_CODE_SPACE)
            return false;

        for (int i = 0; i < mNumbers.length - 1; i++)
            if (code == mNumbers[i])
                return false;
        return true;
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
