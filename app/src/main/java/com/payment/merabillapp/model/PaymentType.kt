package com.payment.merabillapp.model

enum class PaymentType {
    CASH, BANK_TRANSFER, CREDIT_CARD;

    override fun toString(): String {
        return name.replace("_", " ")
    }
}