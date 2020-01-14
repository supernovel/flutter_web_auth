package com.linusu.flutter_web_auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class CallbackActivity: Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val url = intent?.data
    val scheme = url?.scheme

    if (scheme != null) {
      val intent = Intent(this.applicationContext, AuthManagementActivity::class.java)
      intent.data = url
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
      startActivity(intent)

      //FlutterWebAuthPlugin.callbacks.remove(scheme)?.success(url.toString())
    }

    finish()
  }
}
