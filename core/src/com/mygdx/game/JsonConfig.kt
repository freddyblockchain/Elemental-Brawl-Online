package com.example.game

import com.mygdx.game.Action.TouchAction
import com.mygdx.game.Models.CustomFields
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Serializable
data class IpifyResponse(val ip: String)

object JsonConfig {
    val json: Json = Json {
        serializersModule = SerializersModule {
            polymorphic(TouchAction::class) {
                subclass(TouchAction.Move::class, TouchAction.Move.serializer())
                subclass(TouchAction.FireAbility::class, TouchAction.FireAbility.serializer())
            }
            polymorphic(CustomFields::class) {
                subclass(CustomFields.EmptyCustomFields::class, CustomFields.EmptyCustomFields.serializer())
                subclass(CustomFields.PlayerCustomFields::class, CustomFields.PlayerCustomFields.serializer())
            }

        }
        classDiscriminator = "type"
    }
}