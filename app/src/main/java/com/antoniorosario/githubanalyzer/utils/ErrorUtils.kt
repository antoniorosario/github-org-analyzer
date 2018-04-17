package com.antoniorosario.githubanalyzer.utils

import android.util.Log
import com.antoniorosario.githubanalyzer.BASE_URL
import com.antoniorosario.githubanalyzer.di.Injector
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.io.IOException

object ErrorUtils {

    fun parseError(response: Response<*>): APIError {

        // Look up a converter for the Error type on the Retrofit instance.
        val errorConverter =
                Injector.provideRetrofit(BASE_URL)
                        .responseBodyConverter<APIError>(APIError::class.java, arrayOfNulls<Annotation>(0))

        // Convert the error body into our Error type.
        val error: APIError
        try {
            error = errorConverter.convert(response.errorBody())
        } catch (e: IOException) {
            return APIError(documentationURL = " ", message = "Unknown error.")
        }
        Log.d("ErrorUtils", "Documentation url : ${error.documentationURL} \n Error Message:${error.message}")
        return error
    }
}

data class APIError(
        @SerializedName("documentation_url")
        val documentationURL: String,
        val message: String
)