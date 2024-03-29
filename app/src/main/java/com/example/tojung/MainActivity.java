package com.example.tojung;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tojung.CpvProc;

public class MainActivity extends AppCompatActivity {

    TextView chatting;  //chatgpt 대답
    TextView ques; //stt Text
    Button option_button;
    Intent intent;
    Button button; //음성 버튼
    SpeechRecognizer mRecognizer; //음성인식기 객체
    final int PERMISSION = 1;
    List<Message> messageList; //메시지를 저장하는 리스트
    MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    private static final String MY_SECRET_KEY = BuildConfig.CHATGPT_API; //chatgpt
    /**!!!!!!!!!!!!!!!!!!!!!** API **!!!!!!!!!**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //------------------*다이얼로그 흐리게 만듬*------------------//
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        option_button =(Button) findViewById(R.id.option_button); //옵션버튼
        ques = findViewById(R.id.ques); //질문
        chatting=findViewById(R.id.chatting); //봇대답
        button = findViewById(R.id.button); //음성 버튼

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        //RecognizerIntent 생성:: 음성인식 기능 인텐트 설정
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName()); //여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR"); //언어 설정


        ImageView girl = (ImageView) findViewById(R.id.girl);
        girl.setImageResource(R.drawable.girl);

        //안드로이드 버전 확인 후 음성 권한 체크
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        button.setOnClickListener(new View.OnClickListener(){ //녹음버튼 눌렀을 때!!!
            @Override
            public void onClick(View v){
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
                //새 SpeechRecognizer를 만드는 팩토리 메서드
                mRecognizer.setRecognitionListener(listener); //리스너 설정
                mRecognizer.startListening(intent);

            }
        });

        option_button.setOnClickListener(new View.OnClickListener() { //버튼누르면 gif나옴!
            //옵션 버튼 클릭시 화면 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), option.class);
                startActivity(intent);
            }
        });

        client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

    }


    /**--------------------------채팅 출력--------------------------**/

    void addToChat(String message, String sentBy) {
        SharedPreferences sharedPref = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        final String clientId = sharedPref.getString("application_client_id", BuildConfig.APPLICATION_CLIENT_ID);
        final String clientSecret = sharedPref.getString("application_client_secret", BuildConfig.APPLICATION_CLIENT_SECRET);
        runOnUiThread(new Runnable() {
            String speaker;
            @Override
            public void run() {
                String chatLog = chatting.getText().toString();
                if(!chatLog.isEmpty()){
                    chatLog += "\n\n";
                }
                speaker = "mijin";
                chatLog = message;
                chatting.setText(chatLog);
                String strText = chatting.getText().toString();

                MainActivity.NaverTTSTask tts = new MainActivity.NaverTTSTask();
                tts.execute(strText, speaker, clientId, clientSecret);
                ScrollView scrollView = findViewById(R.id.chatscroll);

                chatting.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }



    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    /**--------------------------chatgpt API 불러오기--------------------------**/
    void callAPI(String sttText){
        messageList.add(new Message("입력 중. . .", Message.SENT_BY_BOT));

        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();

        try {
            //AI 속성설정
            baseAi.put("role", "user");
            baseAi.put("content", "You are a helpful and kind talk AI Assistant.");
            //유저 메세지
            userMsg.put("role", "user");
            userMsg.put("content", sttText);
            //array로 담아서 한번에 보낸다
            arr.put(baseAi);
            arr.put(userMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject object = new JSONObject();
        try {
            //모델명 변경
            object.put("model", "gpt-3.5-turbo");
            object.put("messages", arr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer "+MY_SECRET_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        //아래 result 받아오는 경로가 좀 수정되었다.
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        // 응답 메시지에 '감사' 또는 '고맙' 단어가 있는지 확인
                        if (result.contains("감사")|| result.contains("고맙")) {
                            ImageView girl = findViewById(R.id.girl);
                            // 단어가 들어가면 GIF 재생 로직 실행
                            playGifAndThenChangeImage(girl, R.drawable.girl_smile, 0, R.drawable.girl, 1);
                        }

                        else if (result.contains("우와")|| result.contains("대단")) {
                            ImageView girl = findViewById(R.id.girl);
                            // 단어가 들어가면 GIF 재생 로직 실행
                            playGifAndThenChangeImage(girl, R.drawable.girl_wow, 0, R.drawable.girl, 1);
                        }

                        else if (result.contains("슬퍼")|| result.contains("절망")) {
                            ImageView girl = findViewById(R.id.girl);
                            // 단어가 들어가면 GIF 재생 로직 실행
                            playGifAndThenChangeImage(girl, R.drawable.girl_sad, 0, R.drawable.girl, 1);
                        }

                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }

    /**--------------------------인터페이스 콜백 처리--------------------------**/
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            //말하기 시작할 준비가 되면 호출
            Toast.makeText(getApplicationContext(),"음성인식 시작",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            //말하기 시작했을 때 호출
        }

        @Override
        public void onRmsChanged(float v) {
            //입력받는 소리의 크기를 알려줌
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            //말을 시작하고 인식이 된 단어를 버퍼에 담음
        }

        @Override
        public void onEndOfSpeech() {
            //말하기를 중지하면 호출
        }

        @Override
        public void onError(int error) {
            //네트워크 또는 인식 오류가 발생했을 때 호출
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER 가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러 발생 : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle result) {
            //인식 결과가 준비되면 호출
            //말을 하면 ArrayList에 결과를 저장하고 textView에 단어를 이어줌

            ArrayList<String> matches =
                    result.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            //matches: STT를 통해 인식된 문자열 결과물들이 저장된 ArrayList
            //for문을 이용해 ArrayList 결과를 가져와 textView에 단어 이어주는 과정 수행 후 출력
            for(int i =0;i<matches.size(); i++){
                ques.setText(matches.get(i));

                String sttResult = matches.get(i); // STT 결과 리스트 사용
                addToChat(sttResult, Message.SENT_BY_ME);
                callAPI(sttResult);
            }
        }

        @Override
        public void onPartialResults(Bundle partresult) {
            //부분 인식 결과를 사용할 수 있을 때 호출
        }

        @Override
        public void onEvent(int i, Bundle params) {
            //향후 이벤트를 추가하기 위해 예약
        }
    };

    /**--------------------CPV 관련 코드--------------------**/
    public class NaverTTSTask extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... strings) {

            CpvProc.main(strings[0], strings[1], strings[2], strings[3]);
            return null;
        }
    }
    /**--------------------GIF 관련 코드--------------------**/
    /**-playGifAndThenChangeImage(girl, gif위치, 0, 다시바꿀사진, 반복횟수);-**/
    private void playGifAndThenChangeImage(ImageView imageView, @DrawableRes int gifResId, @DrawableRes int placeholderResId, @DrawableRes int endImageResId, int loopCount) {
        Glide.with(getApplicationContext())
                .asGif()
                .load(gifResId)
                .placeholder(placeholderResId)
                .into(new CustomTarget<GifDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                        // GIF 드로어블을 이미지뷰에 설정합니다.
                        imageView.setImageDrawable(resource);

                        // 루프 횟수를 설정합니다.
                        resource.setLoopCount(loopCount);

                        // 애니메이션 종료 후 이미지를 변경하는 리스너를 추가합니다.
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                // 이미지를 지정한 종료 이미지로 변경합니다.
                                imageView.setImageResource(endImageResId);
                            }
                        });

                        // GIF 애니메이션을 시작합니다.
                        resource.start();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // 아무것도 수행하지 않습니다.
                    }
                });
    }


}


