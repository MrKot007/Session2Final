package com.example.session2try1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Display.Mode
import com.example.session2try1.Connection.api

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (SharedPref.checkNotFirstEnter(this)) {
            SharedPref.saveNotFirstEnter(this)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            }, 1000)
            finish()
        }else {
            api.signIn(ModelAuth(SharedPref.getMail(this@SplashActivity)!!, SharedPref.getPassword(this@SplashActivity)!!))
                .push(object: OnGetData<ModelIdentity> {
                    override fun onGet(data: ModelIdentity) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }

                    override fun onError(error: String) {
                        startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                        finish()
                    }

                }, this)
        }

    }
}