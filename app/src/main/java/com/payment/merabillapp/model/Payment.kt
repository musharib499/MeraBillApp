package com.payment.merabillapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class Payment(
    var id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val providerName: String? = "",
    val transactionReference: String? = "",
    val type: PaymentType
) : Parcelable