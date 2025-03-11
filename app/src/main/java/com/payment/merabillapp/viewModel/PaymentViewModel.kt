package com.payment.merabillapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payment.merabillapp.model.Payment
import com.payment.merabillapp.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository,
) : ViewModel() {
    private var debounceJob: Job? = null

    val allPayments: StateFlow<List<Payment>> = repository.getAllPayments()
    val totalAmount: StateFlow<Double> =
        allPayments.map { payments -> payments.sumOf { it.amount } }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)


    init {
        loadPaymentsFromFile()
    }

    fun insert(payment: Payment) {
        viewModelScope.launch {
            repository.insert(payment)
        }
    }

    fun delete(payment: Payment) {
        viewModelScope.launch {
            repository.delete(payment)
        }
    }

    fun savePaymentsToFile() {
        debounceClick {
            repository.savePaymentsToFile()
        }
    }

    private fun loadPaymentsFromFile() {
        viewModelScope.launch {
            repository.loadPaymentsFromFile()
        }

    }

    private fun debounceClick(debounceTime: Long = 1000L, action: suspend () -> Unit) {
        if (debounceJob?.isCancelled == false) return
        debounceJob = viewModelScope.launch {
            action()
            delay(debounceTime)
            debounceJob?.cancel()

        }
    }


}
