import retrofit2.Call
import retrofit2.http.*

interface OpenaiService {

    @Headers("Content-Type: application/json")
    @POST("completions")
    fun complete(
        @Body requestBody: CompletionRequestBody
    ): Call<CompletionResponse>
}
