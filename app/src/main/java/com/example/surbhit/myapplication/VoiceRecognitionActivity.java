package com.example.surbhit.myapplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class VoiceRecognitionActivity extends Activity implements RecognitionListener {
    private static final int MAX_RECOGNIZE_DURATION = 10000;
    Vibrator v;
    private static final String KEY_TEXT_VALUE = "textValue";
    AudioManager amanager;
    public boolean isRecording = false;
    private boolean isStop = false;
    private static final int DELAY_AFTER_START_BEEP = 100;
    private static final int MAX_RETRY_RECOGNIZE = 100;
    private static final Handler healthCheckHandler = new Handler();
    private TextView returnedText,returnedText2,returnedText3;
    private SpeechToText.SpeechToTextCallback callback;
    private int recognizeRetry = 0;
    StringBuilder sb;
    private SpeechToText.SpeechToTextListener listener;
    int count;
    private Button toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private ArrayList<String> ab=new ArrayList<String>();
   // private static boolean isSpeechRecognizerAlive = false;
    private static final int HEALTH_CHECK_INTERVAL_MS = 4000;  // Every 4 seconds
    private static boolean isPaused = false;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ArrayAdapter<String> listAdapter ;
    //public static  String finalo = null;
  public static String finalo2= " ";
    private Handler handler = new Handler();
    PowerManager.WakeLock fullWakeLock,partialWakeLock,open;
    PowerManager pm;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    ScrollTextView scrolltext;
    // RecognitionProgressView recognitionProgressView;
ScrollView scroller;
    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            //    Logger.debug("SpeechToText", "timeout, stop listening");
            speech.stopListening();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_voice_recognition1);
        dohere(savedInstanceState);
//        scroller = ((ScrollView) findViewById(R.id.scroller));
//        int[] colors = {
//                ContextCompat.getColor(this, R.color.color1),
//                ContextCompat.getColor(this, R.color.color2),
//                ContextCompat.getColor(this, R.color.color3),
//                ContextCompat.getColor(this, R.color.color4),
//                ContextCompat.getColor(this, R.color.color5)
//        };
//      //  list = (ListView) findViewById(R.id.list);
//      //  ListView list = findViewById(android.R.id.list);
//
//        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ab){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//
//                View view = super.getView(position, convertView, parent);
//
//                TextView textview = (TextView) view.findViewById(android.R.id.text1);
//                Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/roboto.thin-italic.ttf");
//                textview.setTypeface(custom_font);
//                //Set your Font Size Here.
//                textview.setTextSize(20);
//                textview.setTextColor(Color.parseColor("#FFFFFF"));
//
//                return view;
//            }
//        };
//
//        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
//       // list.setAdapter(adapter);
////ab.add("The Times of India is an Indian English-language daily newspaper owned by The Times Group. It is the fourth-largest newspaper in India by circulation and largest selling English-language daily in the world according to Audit Bureau of Circulations.The Economic Times; Navbharat Times; Maharashtra Times; Ei Samay; Mumbai Mirror");
//
//       // texx = (TextView) findViewById(R.id.texaa);
//
//        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),  "fonts/roboto.thin-italic.ttf");
//       // texx.setTypeface(custom_font);
//
//      //  returnedText = (TextView) findViewById(R.id.textView1);
//        returnedText2 = (TextView) findViewById(R.id.textView2);
//      //  returnedText2.setSelected(true);
//        returnedText3 = (TextView) findViewById(R.id.textView3);
//       // progressBar = (ProgressBar) findViewById(R.id.progressBar1);
//        toggleButton = (Button) findViewById(R.id.toggleButton1);
//
//      //  progressBar.setVisibility(View.INVISIBLE);
//        speech = SpeechRecognizer.createSpeechRecognizer(this);
//        speech.setRecognitionListener(this);
//      //   recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);
//     //   recognitionProgressView.setSpeechRecognizer(speech);
//     //   recognitionProgressView.setColors(colors);
//
//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en-IN");
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"hi");
//
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 120000000);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 120000000);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 120000000);
//     //   recognitionProgressView.play();
//
//
//      //  String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
//        //        "Jupiter", "Saturn", "Uranus", "Neptune"};
//        //ArrayList<String> planetList = new ArrayList<String>();
//        //  planetList.addAll( Arrays.asList(planets) );
//      //  ab.addAll(Arrays.asList(planets));
//      //  adapter.notifyDataSetChanged();
//        sb = new StringBuilder();
////sb.append("     ");
//        toggleButton.performClick();
//
//
//        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
//        assert amanager != null;
//        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
//        createWakeLocks();
//       // setMarqueeSpeed(returnedText2,10000,true);
//
//        scrolltext=(ScrollTextView) findViewById(R.id.scrolltext);
//
//
//
////        if (savedInstanceState != null) {
////            CharSequence savedText = savedInstanceState.getCharSequence(KEY_TEXT_VALUE);
////            returnedText2.setText(savedText);
////        }
//
//        returnedText3.setMovementMethod(new ScrollingMovementMethod());

    }


    private void dohere(Bundle savedInstanceState){

        scroller = ((ScrollView) findViewById(R.id.scroller));
        int[] colors = {
                ContextCompat.getColor(this, R.color.color1),
                ContextCompat.getColor(this, R.color.color2),
                ContextCompat.getColor(this, R.color.color3),
                ContextCompat.getColor(this, R.color.color4),
                ContextCompat.getColor(this, R.color.color5)
        };
        //  list = (ListView) findViewById(R.id.list);
        //  ListView list = findViewById(android.R.id.list);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ab){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                TextView textview = (TextView) view.findViewById(android.R.id.text1);
                Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/roboto.thin-italic.ttf");
                textview.setTypeface(custom_font);
                //Set your Font Size Here.
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#FFFFFF"));

                return view;
            }
        };

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // list.setAdapter(adapter);
//ab.add("The Times of India is an Indian English-language daily newspaper owned by The Times Group. It is the fourth-largest newspaper in India by circulation and largest selling English-language daily in the world according to Audit Bureau of Circulations.The Economic Times; Navbharat Times; Maharashtra Times; Ei Samay; Mumbai Mirror");

        // texx = (TextView) findViewById(R.id.texaa);

        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),  "fonts/roboto.thin-italic.ttf");
        // texx.setTypeface(custom_font);

        //  returnedText = (TextView) findViewById(R.id.textView1);
        returnedText2 = (TextView) findViewById(R.id.textView2);
        //  returnedText2.setSelected(true);
        returnedText3 = (TextView) findViewById(R.id.textView3);
        // progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (Button) findViewById(R.id.toggleButton1);

        //  progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        //   recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);
        //   recognitionProgressView.setSpeechRecognizer(speech);
        //   recognitionProgressView.setColors(colors);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en-IN");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"hi");

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 120000000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 120000000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 120000000);
        //   recognitionProgressView.play();


        //  String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
        //        "Jupiter", "Saturn", "Uranus", "Neptune"};
        //ArrayList<String> planetList = new ArrayList<String>();
        //  planetList.addAll( Arrays.asList(planets) );
        //  ab.addAll(Arrays.asList(planets));
        //  adapter.notifyDataSetChanged();
        sb = new StringBuilder();
//sb.append("     ");
        toggleButton.performClick();


        amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert amanager != null;
        // amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        //  amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        int set_volume = 0;
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, set_volume, 0);






        // amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
      //  assert amanager != null;
      //  amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
       // amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        createWakeLocks();
        // setMarqueeSpeed(returnedText2,10000,true);

        scrolltext=(ScrollTextView) findViewById(R.id.scrolltext);



        if (savedInstanceState != null) {
           // CharSequence savedText = savedInstanceState.getCharSequence(KEY_TEXT_VALUE);
           // returnedText2.setText(savedText);
         //   toggleButton.performClick();

          //  CharSequence myString1 = savedInstanceState.getString("text2");
       // CharSequence myString2 = savedInstanceState.getString("text3");
      //  returnedText2.setText(myString1);
     //   returnedText3.setText(myString2);

        }

        returnedText3.setMovementMethod(new ScrollingMovementMethod());
    }
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        outState.putCharSequence("text2", returnedText2.getText());
        outState.putCharSequence("text3", returnedText3.getText());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.


       // CharSequence myString1 = savedInstanceState.getString("text2");
     //   CharSequence myString2 = savedInstanceState.getString("text3");
       // returnedText2.setText(myString1);
      //  returnedText3.setText(myString2);
        toggleButton.performClick();
//.dohere();

    }



    public void sendNotification() {

        //Get an instance of NotificationManager//

         mBuilder =
                new NotificationCompat.Builder(this)

                        .setContentTitle("Pi Glove")
                        .setContentText("Abhinav your name is called")
                          .setSmallIcon(R.drawable.ic_mic_24dp);

//        Intent notificationIntent = new Intent(this, Voice.class);
//
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);



        // Gets an instance of the NotificationManager service//
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, VoiceRecognitionActivity.class), PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(contentIntent);
//        Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//


    }

    protected void createWakeLocks(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        assert powerManager != null;
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK");
      //  open=powerManager.newWakeLock(PowerManager.RELEASE_FLAG_WAIT_FOR_NO_PROXIMITY | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
    }



    @Override
    public void onResume() {
      //  returnedText3.setText(sb);

        Log.i(LOG_TAG, "Resume");

        assert amanager != null;
        // amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        //  amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        int set_volume = 0;
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, set_volume, 0);

        super.onResume();

    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "Pause");
        assert amanager != null;
        // amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        //  amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        int set_volume = 80;
        amanager.setStreamVolume(AudioManager.STREAM_MUSIC, set_volume, 0);

        super.onPause();
      //  partialWakeLock.acquire();

    }




    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
     //   scroller.fullScroll(View.FOCUS_DOWN);

      //  recognitionProgressView.onBeginningOfSpeech();

        //  progressBar.setIndeterminate(false);
      //  progressBar.setMax(10);
        handler.postDelayed(mStopRunnable, MAX_RECOGNIZE_DURATION);

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {

       // speech.startListening(recognizerIntent);
      //  recognitionProgressView.onEndOfSpeech();
       // scroller.fullScroll(View.FOCUS_DOWN);

        //  toggleButton.setChecked(false);
        if (listener != null && isRecording) listener.onSpeechStop(0);
        isRecording = false;
        handler.removeCallbacks(mStopRunnable);
    }

    @Override
    public void onError(int error) {
        speech.cancel();
        handler.removeCallbacks(mStopRunnable);
        // recording but has error
        if (!isStop) {
            if (recognizeRetry < MAX_RETRY_RECOGNIZE) {
                if (error == SpeechRecognizer.ERROR_NO_MATCH) {
                    // showCenterToast(R.string.errorResultNoMatch);
                }
                recognizeRetry++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listenAgainWhenError();
                    }
                }, 100);
                // Logger.debug(TAG, "Error, try again: " + recognizeRetry);
            } else {
                // Logger.debug(TAG, "Error, max retry");
               // playErrorSound();
                isRecording = false;
                isStop = false;
                speech.setRecognitionListener(null);
                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        // showCenterToast(R.string.errorResultAudioError);
                        break;
                    case SpeechRecognizer.ERROR_CLIENT:
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        //showCenterToast(R.string.errorResultNetworkError);
                        break;
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        //  showCenterToast(R.string.errorResultNetworkError);
                        break;
                    case SpeechRecognizer.ERROR_SERVER:
                        //    showCenterToast(R.string.errorResultServerError);
                        break;
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        //  showCenterToast(R.string.errorResultServerError);
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        // showCenterToast(R.string.errorResultNoMatch);
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        // showCenterToast(R.string.errorResultTimeout);
                        break;
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        // This is programmer error.
                        break;
                    default:
                        break;
                }
            }
        } else {
            // stop recording by manual
            speech.setRecognitionListener(null);
            recognizeRetry = 0;
            isStop = false;
        }
       // returnedText.setText(errorMessage);
       // toggleButton.setChecked(false);
    }


    public void listenAgainWhenError() {
        if (speech == null) {
            isRecording = false;
            //showCenterToast(context.getString(R.string.err_NoDefaultRecognizer));
            return;
        }
        // stop speech
        //  mTts.stopSpeak();
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"hi");

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 120000000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 120000000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 120000000);
        isRecording = true;
        isStop = false;
        // set listener
        speech.setRecognitionListener(this);
        // set listener
        speech.startListening(recognizerIntent);
        if (listener != null) listener.onSpeechStart();
        //  Logger.debug(TAG, "listen");
    }


    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        returnedText2.setVisibility(View.VISIBLE);

        scrolltext.setVisibility(View.INVISIBLE);
        ArrayList<String> results =
                arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),  "fonts/med.ttf");
        returnedText2.setTypeface(custom_font);

       String a= results.get(0);
       returnedText2.setTextSize(30);
        returnedText2.setText(StringUtils.capitalize(a));
      //returnedText3.setGravity(Gravity.BOTTOM);

       // scroller.fullScroll(View.FOCUS_DOWN);

        //returnedText2.setVisibility(View.INVISIBLE);
       // scrolltext.setText(StringUtils.capitalize(a));
     //   scrolltext.startScroll();

    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        scrolltext.setVisibility(View.VISIBLE);

//        Log.i(LOG_TAG, "onResults");
        ArrayList<String> match =
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
String fin=match.get(0);

if (fin.contains("abhinav")){
            v.vibrate(500);

   sendNotification();

           // open.acquire();

   // wl.acquire();

}
        Typeface custom_font = Typeface.createFromAsset(this.getAssets(),  "fonts/med.ttf");

        scrolltext.setTypeface(custom_font);
        scrolltext.setTextSize(30);
        scrolltext.setTextColor(Color.WHITE);




      custom_font = Typeface.createFromAsset(this.getAssets(),  "fonts/roboto.thin-italic.ttf");
        returnedText3.setTypeface(custom_font);
        ab.add((StringUtils.capitalize(fin)));
  Boolean matcher= fin.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$");


       sb = new StringBuilder();
        for (String s : ab)
        {
            sb.append(s);
           // sb.append("\t");
            sb.append(". ");

        }
       // StringUtils.capitalize(sb.toString().toLowerCase());
//       String a= sb.toString();
//       String b= StringUtils.capitalize(a.toLowerCase());
//       int len=b.length();
//        Log.i(LOG_TAG, "String Lol " + len);

      //  b = b.substring(len-1,len).toUpperCase() + b.substring(len).toLowerCase();
        Log.i(LOG_TAG, "Bottom: " + scroller.getBottom());

        scroller.scrollTo(0, 70);
       // scroller.arrowScroll()
//scroller.
        scrolltext.setText(StringUtils.capitalize(fin));
        returnedText2.setVisibility(View.INVISIBLE);
       // scrolltext.startScroll();
        scrolltext.startScroll();


        returnedText3.setText(sb);
        Log.i(LOG_TAG, "Sb Leng"+sb.length());

        if(sb.length()>300){
    returnedText3.setScrollY(40);
}

        //scroller.f(View.FOCUS_DOWN);


        toggleButton.performClick();

    //    scroller.post(new Runnable() { public void run() { scroller.fullScroll(View.FOCUS_DOWN); } });
    }





    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
//        if (recognitionProgressView != null)
//            recognitionProgressView.onRmsChanged(rmsdB);
       // progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";

                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
    private void playStopSound() {
        playSound(R.raw.stop);
    }

    private void playErrorSound() {
        playSound(R.raw.error);
    }

    private void playSound(int sound) {
        MediaPlayer mp = MediaPlayer.create(this, sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }
    public void start(View view) {
        speech.startListening(recognizerIntent);

    }




}
