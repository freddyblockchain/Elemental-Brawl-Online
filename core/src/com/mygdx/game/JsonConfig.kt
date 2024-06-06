package com.example.game

import com.mygdx.game.Action.PlayerAction
import com.mygdx.game.Models.CustomFields
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
data class IpifyResponse(val ip: String)

object JsonConfig {
    val json: Json = Json {
        serializersModule = SerializersModule {
            polymorphic(PlayerAction::class) {
                subclass(PlayerAction.Move::class, PlayerAction.Move.serializer())
                subclass(PlayerAction.FireAbility::class, PlayerAction.FireAbility.serializer())
                subclass(PlayerAction.IcicleAbility::class, PlayerAction.IcicleAbility.serializer())
                subclass(PlayerAction.UpdatePlayerState::class, PlayerAction.UpdatePlayerState.serializer())
            }
            polymorphic(CustomFields::class) {
                subclass(CustomFields.EmptyCustomFields::class, CustomFields.EmptyCustomFields.serializer())
                subclass(CustomFields.PlayerCustomFields::class, CustomFields.PlayerCustomFields.serializer())
            }

        }
        classDiscriminator = "type"
    }
}