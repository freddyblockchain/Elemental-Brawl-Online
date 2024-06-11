import com.mygdx.game.Algorand.AlgorandManager
import com.mygdx.game.Models.VerificationData

class VerificationManager {
    companion object {
        var serverUUID = "random_string"
        fun getVerificationData(messageToSign: String = serverUUID): VerificationData {
            val signatureBytes = AlgorandManager.playerAccount.signBytes(messageToSign.toByteArray())
            return VerificationData(messageToSign, signatureBytes.bytes, AlgorandManager.playerAccount.address.toString())
        }
    }
}