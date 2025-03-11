package com.payment.merabillapp.repository

import android.content.Context
import com.google.gson.Gson
import com.payment.merabillapp.model.Payment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : PaymentRepositoryImp {
    private val file = File(context.filesDir, "LastPayment.txt")
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    private val paymentsFlow = _payments.asStateFlow()

    override fun getAllPayments(): StateFlow<List<Payment>> = paymentsFlow

    override suspend fun insert(payment: Payment) {
        _payments.value += payment
    }

    override suspend fun delete(payment: Payment) {
        _payments.value = _payments.value.filterNot { it.id == payment.id }
    }

    override suspend fun savePaymentsToFile() {
        val jsonData = gson.toJson(_payments.value)
        file.writeText(jsonData)
    }

    override suspend fun loadPaymentsFromFile() {
        if (file.exists()) {
            val jsonData = file.readText()
            val payments: List<Payment> =
                gson.fromJson(jsonData, Array<Payment>::class.java).toList()
            payments.forEach { insert(it) }
        }
    }

}