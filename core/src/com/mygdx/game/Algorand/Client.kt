

import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.IndexerClient

val EBOAlgorandClient  = AlgodClient("https://testnet-api.algonode.cloud", 443, "")
val indexer = IndexerClient("https://testnet-idx.algonode.cloud", 443, "")