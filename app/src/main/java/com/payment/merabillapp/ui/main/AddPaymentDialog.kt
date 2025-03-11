package com.payment.merabillapp.ui.main

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.payment.merabillapp.databinding.DialogAddPaymentBinding
import com.payment.merabillapp.model.Payment
import com.payment.merabillapp.model.PaymentType
import com.payment.merabillapp.viewModel.PaymentViewModel
import javax.inject.Inject

class AddPaymentDialog @Inject constructor(
    private val activity: AppCompatActivity,
    private val viewModel: PaymentViewModel
) {
    private val dialogBinding: DialogAddPaymentBinding =
        DialogAddPaymentBinding.inflate(LayoutInflater.from(activity))
    private val existingPaymentTypes: Set<PaymentType> =
        viewModel.allPayments.value.map { it.type }.toSet()

    fun show() {
        val availableTypes = PaymentType.entries.filter { it !in existingPaymentTypes }

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            availableTypes.map { it.toString() })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spPaymentType.adapter = adapter

        dialogBinding.spPaymentType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedType = PaymentType.valueOf(
                        parent.getItemAtPosition(position).toString().replace(" ", "_")
                    )
                    dialogBinding.extraDetailsLayout.visibility =
                        if (selectedType == PaymentType.BANK_TRANSFER || selectedType == PaymentType.CREDIT_CARD) View.VISIBLE else View.GONE
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        val dialog = AlertDialog.Builder(activity)
            .setTitle("Add Payment")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val amount = dialogBinding.etAmount.text.toString().toDoubleOrNull()
                val type = PaymentType.valueOf(
                    dialogBinding.spPaymentType.selectedItem.toString().replace(" ", "_")
                )

                if (amount != null) {
                    if (PaymentType.CASH == type)
                        viewModel.insert(Payment(amount = amount, type = type)) else {
                        viewModel.insert(
                            Payment(
                                amount = amount,
                                providerName = dialogBinding.etProvider.text.toString(),
                                transactionReference = dialogBinding.etTransactionRef.text.toString(),
                                type = type
                            )
                        )
                    }
                } else {
                    Toast.makeText(activity, "Invalid Amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        if (availableTypes.isNotEmpty()) {
            dialog.show()
        }
    }

}
