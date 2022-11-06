package edu.skillbox.m2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import edu.skillbox.m2.databinding.CustomViewBinding

class CustomView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    private val binding = CustomViewBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setUpperStringText(text: String) {
        binding.upperString.text = text
    }

    fun setLowerStringText(text: String) {
        binding.lowerString.text = text
    }

}
