package com.linusu.flutter_web_auth

import android.app.Service
import android.content.Intent
import android.os.Binder
import io.flutter.Log

internal class KeepAliveService : Service() {
  override fun onBind(intent: Intent) = binder

  companion object {
    private val binder = Binder()
  }
}