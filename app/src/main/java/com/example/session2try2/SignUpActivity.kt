package com.example.session2try2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.session2try2.Connection.api
import com.example.session2try2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.enter.setOnClickListener {
            val bio = listOf(
                binding.sname.text.toString(),
                binding.fname.text.toString(),
                binding.patr.text.toString(),
                binding.mail.text.toString(),
                binding.pass.text.toString(),
                binding.passRep.text.toString()
            )
            bio.forEach {
                if (it == "") {
                    showAlertdialog("Заполнены не все поля!", this@SignUpActivity)
                    return@setOnClickListener
                }

            }
            if (bio[4] != bio[5]) {
                showAlertdialog("Пароли не совпадают!", this@SignUpActivity)
                return@setOnClickListener
            }
            if (!checkEmailPattern(bio[3])) {
                showAlertdialog("Почта указана некорректно!", this@SignUpActivity)
                return@setOnClickListener
            }
            api.signUp(ModelReg(bio[1], bio[0], bio[2], bio[3], bio[4], "01.01.2001", "Мужчина")).push(object: OnGetData<ModelIdentity>{
                override fun onGet(data: ModelIdentity) {
                    SharedPref.saveMail(bio[3], this@SignUpActivity)
                    SharedPref.savePassword(bio[4], this@SignUpActivity)
                    SharedPref.saveToken(data.token, this@SignUpActivity)
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    finish()
                }

                override fun onError(error: String) {
                    showAlertdialog(error, this@SignUpActivity)
                }
            }, this)
        }
    }
    private fun showAlertdialog(reason: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(reason)
            .setPositiveButton("Попробовать снова") {
                    dialog, id -> dialog.cancel()
            }.create().show()
    }
    private fun checkEmailPattern(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}