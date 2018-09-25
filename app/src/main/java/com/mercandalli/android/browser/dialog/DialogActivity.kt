package com.mercandalli.android.browser.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.keyboard.KeyboardUtils
import com.mercandalli.android.browser.main.ApplicationGraph
import org.json.JSONObject
import android.text.method.ScrollingMovementMethod

class DialogActivity : AppCompatActivity() {

    private val input by bind<EditText>(R.id.activity_dialog_input)
    private val message by bind<TextView>(R.id.activity_dialog_message)
    private val positive by bind<TextView>(R.id.activity_dialog_positive)
    private val negative by bind<TextView>(R.id.activity_dialog_negative)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        val dialogManager = ApplicationGraph.getDialogManager()
        val dialogInput = DialogInput.fromJson(intent.extras.getString(EXTRA_DIALOG_INPUT))
        title = dialogInput.title
        message.text = dialogInput.message
        message.movementMethod = ScrollingMovementMethod()
        positive.text = dialogInput.positive
        negative.text = dialogInput.negative
        input.visibility = if (dialogInput.alertFalsePromptTrue) {
            input.postDelayed({
                input.isFocusableInTouchMode = true
                input.requestFocus()
                KeyboardUtils.showSoftInput(input)
            }, 200)
            View.VISIBLE
        } else {
            View.GONE
        }
        positive.setOnClickListener {
            finish()
            dialogManager.onDialogPositiveClicked(
                    DialogManager.DialogAction(
                            dialogInput.dialogId,
                            input.text.toString()
                    )
            )
        }
        negative.setOnClickListener {
            finish()
            dialogManager.onDialogNegativeClicked(
                    DialogManager.DialogAction(
                            dialogInput.dialogId,
                            input.text.toString()
                    )
            )
        }
        setFinishOnTouchOutside(false)
    }

    private fun <T : View> bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
    }

    data class DialogInput(
            val dialogId: String,
            val title: String,
            val message: String,
            val positive: String,
            val negative: String,
            val alertFalsePromptTrue: Boolean
    ) {
        companion object {

            fun toJson(dialogInput: DialogInput): String {
                val json = JSONObject()
                json.put("dialogId", dialogInput.dialogId)
                json.put("title", dialogInput.title)
                json.put("message", dialogInput.message)
                json.put("positive", dialogInput.positive)
                json.put("negative", dialogInput.negative)
                json.put("alertFalsePromptTrue", dialogInput.alertFalsePromptTrue)
                return json.toString()
            }

            fun fromJson(jsonString: String): DialogInput {
                val json = JSONObject(jsonString)
                val dialogId = json.getString("dialogId")
                val title = json.getString("title")
                val message = json.getString("message")
                val positive = json.getString("positive")
                val negative = json.getString("negative")
                val alertTruePromptFalse = json.getBoolean("alertFalsePromptTrue")
                return DialogInput(
                        dialogId,
                        title,
                        message,
                        positive,
                        negative,
                        alertTruePromptFalse
                )
            }
        }
    }

    companion object {
        private const val EXTRA_DIALOG_INPUT = "EXTRA_DIALOG_INPUT"

        @JvmStatic
        fun start(context: Context, dialogInput: DialogInput) {
            val intent = Intent(context, DialogActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            intent.putExtra(EXTRA_DIALOG_INPUT, DialogInput.toJson(dialogInput))
            context.startActivity(intent)
        }
    }
}