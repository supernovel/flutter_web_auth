package com.linusu.flutter_web_auth

import android.app.Activity
import java.util.HashMap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

import androidx.browser.customtabs.CustomTabsIntent

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterWebAuthPlugin(private val activity: Activity) : MethodCallHandler, PluginRegistry.ActivityResultListener {
    private var result : Result? = null

    companion object {
        private const val AUTH_REQUEST_CODE = 23

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_web_auth")
            val plugin = FlutterWebAuthPlugin(registrar.activity())
            channel.setMethodCallHandler(plugin)
            registrar.addActivityResultListener(plugin)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "authenticate") {
            val callbackUrlScheme = call.argument<String>("callbackUrlScheme")!!

            this.result = result

            val intent = Intent(activity, AuthManagementActivity::class.java).apply {
                putExtra(AuthManagementActivity.INPUT_STRING_URL, call.argument<String>("url"))
                putExtra(AuthManagementActivity.INPUT_STRING_URL, call.argument<String>("url"))
                putExtra(AuthManagementActivity.INPUT_STRING_CALLBACK_SCHEME, callbackUrlScheme)
            }

            activity.startActivityForResult(intent, AUTH_REQUEST_CODE)
        } else {
            result.notImplemented()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if(requestCode == AUTH_REQUEST_CODE){
            if(resultCode == AuthManagementActivity.RESULT_OK){
                result?.success(data?.data.toString())
            }else{
                val errorCode = data?.getStringExtra(AuthManagementActivity.ERROR_CODE_KEY)
                val errorMessage = data?.getStringExtra(AuthManagementActivity.ERROR_MESSAGE_KEY)
                result?.error(errorCode, errorMessage, errorMessage)
            }

            return true
        }

        return false
    }
}
