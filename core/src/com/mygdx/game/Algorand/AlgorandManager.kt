package com.mygdx.game.Algorand

import EBOAlgorandClient
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.transaction.*
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.mygdx.game.Abilities.AbilityManager
import com.mygdx.game.Action.PlayerAction
import com.mygdx.game.GameObjects.Shop.InventoryManager
import com.mygdx.game.GameObjects.Shop.ShopItem
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.UI.AbilityButton
import com.mygdx.game.UI.BuyingText
import com.mygdx.game.UI.GoldText
import com.mygdx.game.UI.UIManager.Companion.uiElements
import com.mygdx.game.playerActions
import kotlinx.coroutines.*
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder


class AlgorandManager {
    companion object {
        val appId = 676531650L
        val appApplicationAccount = "3HCZCKEI33BLEQ62G7H3ZEBGKTYGLRTFCLQXH6DGV4MUA36GNOZ57CQDTE"
        val goldAsa = 676111222L
        val fireballAsa = 676532256L
        val abilityAsas = listOf(fireballAsa)

        val assetsToOptInto = listOf(goldAsa) + abilityAsas
        lateinit var playerAccount: Account

        fun getAbilityPrices(): Map<Long, Int> {
            val abilityPriceMap = mutableMapOf<Long, Int>()

            abilityAsas.forEach {
                val boxesRequest = EBOAlgorandClient.GetApplicationBoxByName(appId)
                val box = boxesRequest.name("int:$it").execute()
                val value = box.body().value()
                val decodedBytes = org.bouncycastle.util.encoders.Base64.decode(value)

                val byteBuffer = ByteBuffer.wrap(decodedBytes)
                val longValue = byteBuffer.long
                val result = longValue.toInt()
                println(result)

                abilityPriceMap[it] = result
            }

            return abilityPriceMap.toMap()
        }

        fun itob(assetId: Long): ByteArray {
            val buffer = ByteBuffer.allocate(8) // 8 bytes buffer
            buffer.order(ByteOrder.BIG_ENDIAN) // false for big-endian
            buffer.putLong(assetId)
            return buffer.array()
        }

        fun optIntoAssets() {
            assetsToOptInto.forEach {
                val suggestedParams: Response<TransactionParametersResponse> =
                    EBOAlgorandClient.TransactionParams().execute()
                val optInTxn: Transaction =
                    Transaction.AssetAcceptTransactionBuilder().suggestedParams(suggestedParams.body())
                        .sender(playerAccount.address)
                        .assetIndex(it)
                        .build()

                val sptxn: SignedTransaction = playerAccount.signTransaction(optInTxn)

                val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(sptxn)
                EBOAlgorandClient.RawTransaction().rawtxn(encodedTxBytes).execute()
            }
        }

        fun updatePlayerState(delayTime: Long) {
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            coroutineScope.launch {
                delay(delayTime)
                updatePlayerAbilities()
                playerActions.add(PlayerAction.UpdatePlayerState())
                updateGoldCount()
            }
        }

        private fun updatePlayerAbilities() {
            val account = EBOAlgorandClient.AccountInformation(playerAccount.address).execute().body()
            account.assets.forEach {
                if (it.assetId in AbilityManager.abilityMap.keys && it.amount >= BigInteger.ONE) {
                    val ability = AbilityManager.abilityMap[it.assetId]!!
                    InventoryManager.abilityList.add(ability)
                    uiElements.add(AbilityButton(ability) { ability.onPress()})

                    val shopItems = AreaManager.getActiveArea()!!.gameObjects.filterIsInstance<ShopItem>()
                    shopItems.forEach { shopItem ->
                        if(shopItem.ability == ability){
                            AreaManager.getActiveArea()!!.gameObjects.remove(shopItem)
                        }
                    }
                }
            }
        }

        private fun updateGoldCount() {
            val information = EBOAlgorandClient.AccountAssetInformation(playerAccount.address, goldAsa).execute()
            val amount = information.body().assetHolding.amount
            InventoryManager.gold = amount.toInt()
            GoldText.loading = false
        }

        fun buyAbility(abilityAsset: Long) {
            val rsp: Response<TransactionParametersResponse> = EBOAlgorandClient.TransactionParams().execute()
            val sp: TransactionParametersResponse = rsp.body()

            val initialBalance = EBOAlgorandClient.AccountInformation(playerAccount.address).execute().body().amount
            println("Initial Balance: $initialBalance microAlgos")

            val atc = AtomicTransactionComposer()
            val transactionSigner = playerAccount.transactionSigner

            //Get asset total Later

            val sendGold = Transaction.AssetTransferTransactionBuilder().suggestedParams(sp)
                .sender(playerAccount.address)
                .assetIndex(goldAsa)
                .assetReceiver(appApplicationAccount)
                .assetAmount(1)
                .build()

            val assets = listOf<Long>(abilityAsset)
            val methodName = "buy_asset"
            val methodNameBytes: ByteArray = methodName.toByteArray()
            val args: List<ByteArray> = listOf(methodNameBytes)

            val boxes: List<AppBoxReference> = listOf(AppBoxReference(appId, itob(abilityAsset)))


            val buyAbility: Transaction = Transaction.ApplicationCallTransactionBuilder()
                .genesisID(sp.genesisId)
                .genesisHash(sp.genesisHash)
                .firstValid(sp.lastRound)
                .lastValid(sp.lastRound + 1000L)
                .sender(playerAccount.address)
                .args(args)
                .applicationId(appId)
                .foreignAssets(assets)
                .boxReferences(boxes)
                .flatFee(sp.minFee * 2)
                .build()

            atc.addTransaction(TransactionWithSigner(sendGold, transactionSigner))
            atc.addTransaction(TransactionWithSigner(buyAbility, transactionSigner))

            println("Transaction fee: ${sp.fee} microAlgos")

            val coutineScope = CoroutineScope(Dispatchers.Default)
            coutineScope.launch {
                val result = atc.execute(EBOAlgorandClient, 4)
                updatePlayerState(0)
                BuyingText.buying = false
                println("result is: " + result.txIDs)
            }
        }
    }
}