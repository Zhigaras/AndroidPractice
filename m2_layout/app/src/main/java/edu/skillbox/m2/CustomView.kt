package edu.skillbox.m2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import edu.skillbox.m2.databinding.CustomViewBinding

class CustomView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    val binding = CustomViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setText(string: TextView, text: String) {
        string.text = text
    }

}
