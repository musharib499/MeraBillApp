package com.payment.merabillapp.repository

import com.payment.merabillapp.model.Payment
import kotlinx.coroutines.flow.StateFlow


interface PaymentRepositoryImp {
    fun getAllPayments(): StateFlow<List<Payment>>
    suspend fun insert(payment: Payment)
    suspend fun delete(payment: Payment)
    suspend fun savePaymentsToFile()
    suspend fun loadPaymentsFromFile()
}