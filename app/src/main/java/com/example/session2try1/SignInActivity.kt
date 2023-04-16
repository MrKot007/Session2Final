package com.example.session2try1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.session2try1.Connection.api
import com.example.session2try1.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.goToCreateAccount.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
        binding.enter.setOnClickListener {
            val email = binding.mailInp.text.toString()
            val password = binding.passInp.text.toString()
            if (!checkEmailPattern(email)) {
                showAlertDialog("Почта введена некорректно!", this)
                return@setOnClickListener
            }
            if (email == "" || password == "") {
                showAlertDialog("Заполнены не все поля!", this)
                return@setOnClickListener
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
                    showAlertDialog(error, this@SignInActivity)
                }
            }, this)
        }
    }
    private fun checkEmailPattern(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun showAlertDialog(reason: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(reason)
            .setPositiveButton("Попробовать снова") {
                dialog, id -> dialog.cancel()
            }.create().show()
    }
}