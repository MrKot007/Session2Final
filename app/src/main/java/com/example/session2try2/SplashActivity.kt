package com.example.session2try2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.session2try2.Connection.api

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (SharedPref.checkNotFirstEnter(this@SplashActivity)) {
            SharedPref.saveNotFirstEnter(this@SplashActivity)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                finish()
            }, 1000)
        }else {
            api.signIn(ModelAuth(SharedPref.getMail(this@SplashActivity)!!, SharedPref.getPassword(this@SplashActivity)!!))
                .push(object: OnGetData<ModelIdentity>{
                    override fun onGet(data: ModelIdentity) {

                    }

                    override fun onError(error: String) {
                        startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                        finish()
                    }
                }, this)
        }

    }
}