package com.mygdx.game.Algorand

import EBOAlgorandClient
import com.algorand.algosdk.crypto.Address

class AlgorandManager {
    companion object {
        fun playerIsOptedIntoGold(playerAddress: String): Boolean{
            val goldAsa = 676111222L
            try {
                // Fetch account information
                val accountInfo = EBOAlgorandClient.AccountInformation(Address(playerAddress)).execute().body()
                // Check asset holdings
                for (asset in accountInfo.assets) {
                    if (asset.assetId == goldAsa) {
                        return true
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun optIntoGameAssets(){

        }
    }
}