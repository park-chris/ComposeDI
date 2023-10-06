package com.crystal.listeningcat.message

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystal.listeningcat.R
import com.crystal.listeningcat.databinding.ActivityMessageBinding
import com.crystal.listeningcat.databinding.BottomSheetMessageBinding
import com.crystal.listeningcat.home.FullMessageAdapter
import com.crystal.listeningcat.home.MessageState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MessageActivity : AppCompatActivity() {

    companion object {
        private const val UTTERANCE_ID = "speak_utterance_id_message_activity"
    }

    private lateinit var binding: ActivityMessageBinding
    private lateinit var dialogBinding: BottomSheetMessageBinding

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var messageAdapter: FullMessageAdapter

    private val messageViewModel by lazy {
        ViewModelProvider(this)[MessageViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textToSpeech = TextToSpeech(this, onInitListener())
        textToSpeech.setOnUtteranceProgressListener(textToSpeechListener())

        initRecyclerView()

        CoroutineScope(Dispatchers.Main).launch {
            val messages = messageViewModel.getMessages()
            updateMessage(messages.reversed())
        }

        binding.addButton.setOnClickListener {
            showBottomSheet(null)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

    private fun initRecyclerView() {
        messageAdapter = FullMessageAdapter { message, state ->
            when (state) {
                MessageState.PLAY -> {
                    speakMessage(message)
                }

                MessageState.EDIT -> {
                    showBottomSheet(message)
                }
                MessageState.DELETE -> {
                    AlertDialog.Builder(this)
                        .setMessage(getString(R.string.dialog_message))
                        .setPositiveButton(getString(R.string.delete)) { _, _ ->
                            deleteMessage(message)
                        }
                        .setNegativeButton(getString(R.string.cancel)) {_, _ ->
                        }
                        .show()

                }
            }
        }

        binding.messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }
    }

    private fun textToSpeechListener() = object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            runOnUiThread {
                binding.speakAnimationView.playAnimation()
                binding.speakAnimationView.isVisible = true
            }
        }

        override fun onDone(utteranceId: String?) {
            runOnUiThread {
                binding.speakAnimationView.pauseAnimation()
                binding.speakAnimationView.isVisible = false
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
            runOnUiThread {
                binding.speakAnimationView.pauseAnimation()
                binding.speakAnimationView.isVisible = false
            }
        }

    }

    private fun speakMessage(message: Message) {
        textToSpeech.speak(message.message, TextToSpeech.QUEUE_FLUSH, null, UTTERANCE_ID)
    }

    private fun updateMessage(messages: List<Message>) {
        messageAdapter.submitList(messages)
        binding.infoTextView.isVisible = messages.isEmpty()
    }

    private fun showBottomSheet(message: Message?) {

        val dialog: Dialog = Dialog(this)

        dialogBinding = BottomSheetMessageBinding.inflate(dialog.layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.BottomSheetAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        dialogBinding.messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dialogBinding.clearButton.isVisible = !s.isNullOrEmpty()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        dialogBinding.messageEditText.setText(message?.message)

        dialogBinding.messageButton.setOnClickListener {

            val inputMessage = dialogBinding.messageEditText.text.toString()

            if (inputMessage.isEmpty()) {
                Toast.makeText(this, getString(R.string.input_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (message != null) {
                message.message = inputMessage
                messageViewModel.updateMessage(message)
                editMessage(message)
            } else {
                val newMessage = Message()
                newMessage.message = inputMessage
                messageViewModel.addMessage(newMessage)
                addMessage(newMessage)
            }
            Toast.makeText(this, getString(R.string.save_message), Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialogBinding.clearButton.setOnClickListener {
            dialogBinding.messageEditText.text = null
        }

        dialog.show()

    }

    private fun addMessage(message: Message) {
        val list = mutableListOf<Message>()
        list.addAll(messageAdapter.currentList)
        list.add(0, message)
        updateMessage(list)
    }

    private fun deleteMessage(message: Message) {
        messageViewModel.deleteMessage(message)
        val list = mutableListOf<Message>()
        list.addAll(messageAdapter.currentList)
        list.remove(message)
        updateMessage(list)
    }

    private fun editMessage(message: Message) {
        messageViewModel.updateMessage(message)
        val list = mutableListOf<Message>()
        list.addAll(messageAdapter.currentList)
        val existedMessage = list.filter { it.index == message.index }
        val index = list.indexOf(existedMessage.first())
        list.add(index, message)
        list.remove(existedMessage.first())
        updateMessage(list)
        messageAdapter.notifyItemChanged(index)

    }

    private fun onInitListener() = TextToSpeech.OnInitListener { status ->
        if (status == TextToSpeech.SUCCESS) {
            // 언어 설정
            val result = textToSpeech.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Toast.makeText(
                    this@MessageActivity,
                    getString(R.string.error_not_supported_lang),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}