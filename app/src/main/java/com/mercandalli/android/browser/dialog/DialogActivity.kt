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
import androidx.annotation.StringDef

class DialogActivity : AppCompatActivity(),
    DialogContract.Screen {

    private val input by bind<EditText>(R.id.activity_dialog_input)
    private val message by bind<TextView>(R.id.activity_dialog_message)
    private val positive by bind<TextView>(R.id.activity_dialog_positive)
    private val negative by bind<TextView>(R.id.activity_dialog_negative)
    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        setFinishOnTouchOutside(false)
        message.movementMethod = ScrollingMovementMethod()
        positive.setOnClickListener {
            userAction.onPositiveClicked(input.text.toString())
        }
        negative.setOnClickListener {
            userAction.onNegativeClicked(input.text.toString())
        }
        val extras = intent.extras
        val jsonString = extras!!.getString(EXTRA_DIALOG_INPUT)!!
        val dialogInput = DialogInput.fromJson(jsonString)
        userAction.onCreate(dialogInput)
    }

    override fun setTitle(text: String) {
        title = text
    }

    override fun setMessage(text: String) {
        message.text = text
    }

    override fun setPositive(text: String) {
        positive.text = text
    }

    override fun setNegative(text: String) {
        negative.text = text
    }

    override fun showInput() {
        input.visibility = View.VISIBLE
    }

    override fun hideInput() {
        input.visibility = View.GONE
    }

    override fun showSoftInput() {
        input.postDelayed({
            input.isFocusableInTouchMode = true
            input.requestFocus()
            KeyboardUtils.showSoftInput(input)
        }, 200)
    }

    override fun quit() {
        finish()
    }

    private fun <T : View> bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
    }

    private fun createUserAction(): DialogContract.UserAction {
        val dialogManager = ApplicationGraph.getDialogManager()
        return DialogPresenter(
            this,
            dialogManager
        )
    }

    data class DialogInput(
        val dialogId: String,
        val title: String,
        val message: String,
        val positive: String,
        val negative: String,
        @DialogType
        val type: String
    ) {
        companion object {

            @StringDef(
                DIALOG_TYPE_ALERT,
                DIALOG_TYPE_PROMPT
            )
            @Retention(AnnotationRetention.SOURCE)
            annotation class DialogType

            const val DIALOG_TYPE_ALERT = "DIALOG_TYPE_ALERT"
            const val DIALOG_TYPE_PROMPT = "DIALOG_TYPE_PROMPT"

            fun toJson(dialogInput: DialogInput): String {
                val json = JSONObject()
                json.put("dialogId", dialogInput.dialogId)
                json.put("title", dialogInput.title)
                json.put("message", dialogInput.message)
                json.put("positive", dialogInput.positive)
                json.put("negative", dialogInput.negative)
                json.put("type", dialogInput.type)
                return json.toString()
            }

            fun fromJson(jsonString: String): DialogInput {
                val json = JSONObject(jsonString)
                val dialogId = json.getString("dialogId")
                val title = json.getString("title")
                val message = json.getString("message")
                val positive = json.getString("positive")
                val negative = json.getString("negative")
                val type = json.getString("type")
                return DialogInput(
                    dialogId,
                    title,
                    message,
                    positive,
                    negative,
                    type
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
