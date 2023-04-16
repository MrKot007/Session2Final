package com.example.session2try2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.session2try2.Connection.api
import com.example.session2try2.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.goToCreateAccount.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            finish()
        }
        binding.enter.setOnClickListener {
            val email = binding.mailInp.text.toString()
            val password = binding.passInp.text.toString()
            if (email == "" || password == "") {
                showAlertdialog("Заполнены не все поля!", this@SignInActivity)
            }
            if (!checkEmailPattern(email)) {
                showAlertdialog("Почта указана некорректно!", this@SignInActivity)
            }
            api.signIn(ModelAuth(email, password)).push(object: OnGetData<ModelIdentity>{
                override fun onGet(data: ModelIdentity) {
                    SharedPref.saveMail(email, this@SignInActivity)
                    SharedPref.savePassword(password, this@SignInActivity)
                    SharedPref.saveToken(data.token, this@SignInActivity)
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                }

                override fun onError(error: String) {
                    showAlertdialog(error, this@SignInActivity)
                }
            }, this)
        }
    }
    private fun checkEmailPattern(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun showAlertdialog(reason: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(reason)
            .setPositiveButton("Попробовать снова") {
                dialog, id -> dialog.cancel()
            }.create().show()
    }
}