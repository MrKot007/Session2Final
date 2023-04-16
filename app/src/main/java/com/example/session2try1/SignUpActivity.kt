package com.example.session2try1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.session2try1.Connection.api
import com.example.session2try1.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            finish()
        }
        binding.register.setOnClickListener {
            val bio = listOf(
                binding.sname.text.toString(),
                binding.fname.text.toString(),
                binding.patr.text.toString(),
                binding.sexInp.text.toString(),
                binding.date.text.toString(),
                binding.emailInp.text.toString(),
                binding.pass.text.toString(),
                binding.passRep.text.toString()
            )
            bio.forEach {
                if (it == "") {
                    showAlertDialog("Заполнены не все поля!", this@SignUpActivity)
                    return@setOnClickListener
                }
            }
            if (!checkEmailPattern(bio[5])) {
                showAlertDialog("Почта указана некорректно!", this@SignUpActivity)
                return@setOnClickListener
            }
            if (bio[6] != bio[7]) {
                showAlertDialog("Пароли не совпадают!", this@SignUpActivity)
                return@setOnClickListener
            }
            if (bio[3] != "Мужской" || bio[3] != "Женский") {
                showAlertDialog("Пол указан некорректно", this@SignUpActivity)
                return@setOnClickListener
            }
            api.signUp(ModelReg(bio[1], bio[0], bio[2], bio[5], bio[6], bio[4], bio[3])).push(object: OnGetData<ModelIdentity>{
                override fun onGet(data: ModelIdentity) {
                    SharedPref.saveMail(bio[5], this@SignUpActivity)
                    SharedPref.savePassword(bio[6], this@SignUpActivity)
                    api.signIn(ModelAuth(bio[5], bio[6])).push(object: OnGetData<ModelIdentity>{
                        override fun onGet(data: ModelIdentity) {
                            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                            finish()
                        }

                        override fun onError(error: String) {
                            showAlertDialog(error, this@SignUpActivity)
                        }
                    }, this@SignUpActivity)
                }

                override fun onError(error: String) {
                    showAlertDialog(error, this@SignUpActivity)
                }
            }, this)
        }

    }
    private fun showAlertDialog(reason: String, context: Context) {
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