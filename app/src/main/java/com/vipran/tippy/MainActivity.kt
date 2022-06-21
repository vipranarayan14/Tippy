package com.vipran.tippy

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

private const val INITIAL_TIP_PERC = 15
private const val INITIAL_TIP_AMOUNT = 0.0
private const val INITIAL_TOTAL_AMOUNT = 0.0

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTipPerc: SeekBar
    private lateinit var tvTipPerc: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTipPerc = findViewById(R.id.seekBarTipPerc)
        tvTipPerc = findViewById(R.id.tvTipPerc)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTipPerc.progress = INITIAL_TIP_PERC
        tvTipPerc.text = "$INITIAL_TIP_PERC%"
        updateTipDescription(INITIAL_TIP_PERC)

        seekBarTipPerc.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvTipPerc.text = "$progress%"

                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(baseAmount: Editable?) {
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDescription(tipPerc: Int) {
        val tipDescription = when (tipPerc) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }

        tvTipDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipPerc.toFloat() / seekBarTipPerc.max,
            ContextCompat.getColor(this, R.color.tip_worst),
            ContextCompat.getColor(this, R.color.tip_best)
        ) as Int

        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty() || etBaseAmount.text.startsWith('.')) {
            tvTipAmount.text = formatAmount(INITIAL_TIP_AMOUNT)
            tvTotalAmount.text = formatAmount(INITIAL_TOTAL_AMOUNT)
            return
        }

        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTipPerc.progress

        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        tvTipAmount.text = formatAmount(tipAmount)
        tvTotalAmount.text = formatAmount(totalAmount)
    }

    private fun formatAmount(amount: Double): CharSequence {
        return "%.2f".format(amount)
    }
}