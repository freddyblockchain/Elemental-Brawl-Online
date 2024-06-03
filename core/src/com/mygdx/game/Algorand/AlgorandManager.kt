package com.mygdx.game.Algorand

import EBOAlgorandClient
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.mygdx.game.GameObjects.Shop.Inventory
import com.mygdx.game.UI.GoldText
import kotlinx.coroutines.*


class AlgorandManager {
    companion object {
        val goldAsa = 676111222L
        val assetsToOptInto = listOf(goldAsa)
        lateinit var playerAccount: Account
        fun optIntoAssets() {
            assetsToOptInto.forEach {
                val suggestedParams: Response<TransactionParametersResponse> =
                    EBOAlgorandClient.TransactionParams().execute()
                val optInTxn: Transaction =
                    Transaction.AssetAcceptTransactionBuilder().suggestedParams(suggestedParams.body())
                        .sender(playerAccount.address)
                        .assetIndex(goldAsa)
                        .build()

                val sptxn: SignedTransaction = playerAccount.signTransaction(optInTxn)

                val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(sptxn)
                EBOAlgorandClient.RawTransaction().rawtxn(encodedTxBytes).execute()
            }
        }

        fun updateGoldCount(delayTime: Long) {
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            coroutineScope.launch {
                delay(delayTime)
                val information = EBOAlgorandClient.AccountAssetInformation(playerAccount.address, goldAsa).execute()
                val amount = information.body().assetHolding.amount
                Inventory.gold = amount.toInt()
                GoldText.loading = false
            }
        }
    }
}