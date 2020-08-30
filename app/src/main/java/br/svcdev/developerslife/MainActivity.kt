package br.svcdev.developerslife

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.svcdev.developerslife.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var mRetrofit: NetworkRequest? = null
    private var mBinding: ActivityMainBinding? = null
    private var backStack: Stack<ResponseJson?>? = null
    private var forwardStack: Stack<ResponseJson?>? = null
    private var responseJson: ResponseJson? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        initRetrofit()
        mBinding!!.previousBtn.isEnabled = false
        backStack = Stack()
        forwardStack = Stack()
        mBinding!!.nextBtn.setOnClickListener { v: View? ->
            backStack!!.push(responseJson)
            if (forwardStack!!.isEmpty()) {
                sendRequest()
            } else {
                responseJson = forwardStack!!.pop()
                updateUI(responseJson)
            }
            mBinding!!.previousBtn.isEnabled = true
        }
        mBinding!!.previousBtn.setOnClickListener { v: View? ->
            forwardStack!!.push(responseJson)
            if (!backStack!!.isEmpty()) {
                responseJson = backStack!!.pop()
                mBinding!!.previousBtn.isEnabled = !backStack!!.empty()
                updateUI(responseJson)
            }
        }
    }

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()

    private fun updateUI(responseJson: ResponseJson?) {
        Glide.with(mBinding!!.root)
                .load(responseJson!!.gifUrl)
                .into(mBinding!!.imageView)
        mBinding!!.textView.text = responseJson.description
    }

    private fun sendRequest() {
        mRetrofit!!.sendRequest(true)!!.enqueue(object : Callback<ResponseJson?> {
            override fun onResponse(call: Call<ResponseJson?>, response: Response<ResponseJson?>) {
                if (response.body() != null) {
                    responseJson = response.body()
                    updateUI(responseJson)
                }
            }

            override fun onFailure(call: Call<ResponseJson?>, t: Throwable) {}
        })
    }

    private fun initRetrofit() {
        mRetrofit = Retrofit.Builder().baseUrl("https://developerslife.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(NetworkRequest::class.java)
    }

    override fun onStart() {
        super.onStart()
        sendRequest()
    }
}