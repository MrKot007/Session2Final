package com.example.session2try2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.session2try2.Connection.api
import com.example.session2try2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signOut.setOnClickListener {
            api.signOut("Bearer ${SharedPref.getToken(this)}").push(object: OnGetData<Boolean> {
                override fun onGet(data: Boolean) {
                    SharedPref.saveToken("", this@MainActivity)
                    SharedPref.savePassword("", this@MainActivity)
                    SharedPref.saveMail("", this@MainActivity)
                    finishAffinity()
                }

                override fun onError(error: String) {
                    showAlertdialog(error, this@MainActivity)
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
}