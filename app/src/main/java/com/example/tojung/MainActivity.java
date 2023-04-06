package com.example.tojung;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // OpenAI API endpoint
    private static final String BASE_URL = "https://api.openai.com/v1/";

    //API 키 값 삽입
    private static final String API_KEY = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);

        // 모든 요청 헤더에 API 키를 추가하는 INterceptor가 포함된 OkHttpClient 인스턴스 생성
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        // GPT-3 API 엔드포인트를 위한 Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //예시: GPT-3 "completions" 엔드포인트 호출하여 텍스트 생성
        OpenaiService service = retrofit.create(OpenaiService.class);
        String prompt = "Hello, GPT-3!";
        int maxTokens = 50;
        Call<CompletionResponse> call = service.complete(prompt, maxTokens);
        call.enqueue(new Callback<CompletionResponse>() {
            @Override
            public void onResponse(Call<CompletionResponse> call, Response<CompletionResponse> response) {
                if (response.isSuccessful()) {
                    String generatedText = response.body().getChoices().get(0).getText();
                    // Do something with the generated text
                } else {
                    // 에러처리
                }
            }

            @Override
            public void onFailure(Call<CompletionResponse> call, Throwable t) {
                // 네트워크 에러 처리
            }
        });
    }
}
