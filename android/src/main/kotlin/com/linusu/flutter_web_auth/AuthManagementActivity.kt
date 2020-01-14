package com.linusu.flutter_web_auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent


class AuthManagementActivity : Activity() {
    companion object {
        const val INPUT_STRING_URL = "url"
        const val INPUT_STRING_CALLBACK_SCHEME = "callback_scheme"
        const val RESULT_OK = 0
        const val RESULT_ERROR = 1
        const val ERROR_CODE_KEY = "ERROR_CODE"
        const val ERROR_MESSAGE_KEY = "ERROR_MESSAGE"
        private var mAuthorizationStarted = false

        fun createResponseHandlingIntent(context: Context, responseUri: Uri?): Intent? {
            val intent = createBaseIntent(context)
            intent.data = responseUri
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            return intent
        }

        private fun createBaseIntent(context: Context): Intent {
            return Intent(context, AuthManagementActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        this.intent = intent
    }

    override fun onResume() {
        super.onResume()

        if (!mAuthorizationStarted) {
            val tabsIntent = CustomTabsIntent.Builder().build()
            val keepAliveIntent = Intent().setClassName(this.packageName, KeepAliveService::class.java.canonicalName)

            tabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK)
            tabsIntent.intent.putExtra("android.support.customtabs.extra.KEEP_ALIVE", keepAliveIntent)

            tabsIntent.intent.data = Uri.parse(this.intent.getStringExtra(INPUT_STRING_URL))
            startActivity(tabsIntent.intent)
            mAuthorizationStarted = true
            return
        }

        val data = this.intent?.data

        mAuthorizationStarted = if (data != null) {
            setResult(RESULT_OK, this.intent)
            false
        } else {
            this.intent.putExtra(ERROR_CODE_KEY, "CANCELED")
            this.intent.putExtra(ERROR_MESSAGE_KEY, "User canceled login")
            setResult(RESULT_ERROR, this.intent)
            false
        }

        finish()
    }
}