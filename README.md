LICENCE
MIT License

Copyright (c) 2024 FREDERIK NIELSEN

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

# Elemental Brawl Online

Elemental Brawl Online is a realtime online multiplayer action game. 
The goal of the game is to defeat other players, and attain new
abilities by doing so. The game is loosely based on the Warcraft
3 mod "Warlock". 

## Code from Previous Projects

A lot of code related to the game engine comes from my Game Engine repository, which you can find here: [GameEngine Repository](https://github.com/freddy212/GameEngine).
This code is present on both the server and client applications. 

The code written specifically for this hackathon includes:
- Everything Algorand
- Everything Client/server communication and networking logic
- Design and implementation of abilities and reward system.

## Repositories and Code Used in Elemental Brawl Online

- **Client side Android Application**: [Elemental Brawl Online](https://github.com/freddyblockchain/Elemental-Brawl-Online)
- **Game Server Application**: [EBO Server](https://github.com/freddyblockchain/EBOServer)
- **EBOOnline Shop Contract**: [ebo_approval.tl](https://github.com/freddyblockchain/Tealish-Contracts)

## Algorand Asset and Account Information on Testnet

- **Game Server Address**: `2HV6JENG4SZ2MKHAHXB3HBUVVJWF4RCE2P76HASN47CRBTLXX2KOUMRVA4`
- **Shop Contract Application Id**: `676531650` 
- **Shop Contract Application Address**: `3HCZCKEI33BLEQ62G7H3ZEBGKTYGLRTFCLQXH6DGV4MUA36GNOZ57CQDTE`
- **Gold**: `676111222`
- **Fireball**: `676532256`
- **Icicle**: `677924248`
- **Snowball**: `677926089`
- **FireDash**: `678786539`

## Technologies Used

- Kotlin
- Algorand
- Libgdx
- Android
- Ktor
- SSE
- UDP

## Project Architecture

**Elemental Brawl Online** has these three main components working together:

1. **Game Server**:
    - It acts as the single source of truth for the game state.
    - The server receives player input via UDP and streams the game state via server-side events.
    - The server has access to an Algorand asset called "gold," which it can send to players as a reward for progressing in the game.

2. **Player Application**:
    - Players use an Android application made with libGDX.
    - This application creates and stores an Algorand account associated with the player.
    - The Algorand account is used to buy abilities in the game and to authenticate the player to the server.

3. **Smart Contract (Shop)**:
    - The game server account has deployed a smart contract called the "Shop."
    - The game server account can create assets in the shop and set prices on assets.
    - Players can buy assets from the shop with gold, and they are guaranteed that the items have a use in the game.

## Novel Ideas in EBO

**Elemental Brawl Online (EBO)** is introducing several innovative concepts in the gaming space, particularly in its use handling player progress and authentication:

1. **Player Ownership of Progress**:
    - Players retain ownership of their in-game progress through their Algorand accounts.
    - This approach shifts the responsibility of tracking player progress away from the server, allowing the server to focus exclusively on managing the game state.

2. **Reduced Server Load**:
    - The server no longer needs to handle the verification of player progress or identity.
    - EBO employs a novel authentication mechanism that is lightweight, enabling the server to efficiently verify that the player sending UDP input messages actually owns the account they claim to have.

The first point enable potential future use for the assets accrued by the player in other areas than EBO.
The second point can be used in all game servers, to quickly and securely authenticate players, and view
what they have access to. 

## Authentication

The authentication mechanism in **Elemental Brawl Online (EBO)** works as follows:

1. **UUID Generation**:
    - Every game tick (approximately every 50 ms), the server generates a unique UUID.
    - This UUID is sent to each connected player.

2. **Player Signing**:
    - Players must sign this UUID with their Algorand account each time they send an input to the server.
    - The signed UUID is included in the player's input message.

3. **Server Verification**:
    - The server verifies that the player correctly signed the UUID it sent.
    - This ensures the player owns the account they claim to have, streamlining the server's job to simply verifying the signature.

4. **Replay Attack Mitigation**:
    - The risk of replay attacks is minimal because players must sign the most recent UUIDs.

This approach minimizes cheating, while maintaining the fast I/O speed required for real-time gameplay.



## Roadmap

There are two low-hanging fruits to be explored with the current design of the game:

1. **Game Interoperability**: Since the player owns the assets they buy, these could potentially be used in other games. These games could use the assets in inventive ways. A fireball in EBO could be a fireball card in EBO the card game.
2. **Secondary Markets**:
   - Assets from the shop and gold are not soulbound, making it possible for players to use, trade, and sell the assets as they see fit.
   - The shop smart contract could also be expanded to include different kinds of currency if the game creator wants to have different tradeable currencies in the game.


