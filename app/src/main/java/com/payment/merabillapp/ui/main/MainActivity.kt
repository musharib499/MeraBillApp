package com.payment.merabillapp.ui.main


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.payment.merabillapp.R
import com.payment.merabillapp.databinding.ActivityMainBinding
import com.payment.merabillapp.model.Payment
import com.payment.merabillapp.viewModel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.allPayments.collectLatest { payments ->
                updateChips(payments)
            }
        }

        binding.btnAddPayment.setOnClickListener {
            AddPaymentDialog(this@MainActivity, viewModel).show()
        }
    }

    private fun updateChips(payments: List<Payment>) {
        binding.chipGroup.removeAllViews()
        for (payment in payments) {
            val chip = Chip(this).apply {
                text = getString(R.string.payment_info, payment.type, payment.amount)
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    viewModel.delete(payment)
                }
            }
            binding.chipGroup.addView(chip)
        }
    }
}
