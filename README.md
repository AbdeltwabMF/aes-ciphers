# AES Ciphers

An implementation of The common AES block cipher modes of operations (ECB, CBC, CTR), with user interface.
It allows you to encrypt and decrypt files visually.


## Features
- Supports all AES key sizes
- Supports all AES common modes


## Notes
- All keys may be 128 bits (16 bytes), 192 bits (24 bytes) or 256 bits (32 bytes) long.
- Key might be a (8-char, 12-char, or 16-char) string.
- Key might be a (32-char, 48-=char, or 64-char) HEX.
- There are many modes of operations, each with various pros and cons. In general though, the CBC and CTR modes are recommended. The ECB is NOT recommended., and is included primarily for completeness.


## Snapshots
![aes-cipher-png](/res/aes-ciphers.png)


## Usage
![aes-cipher-gif](/res/aes-ciphers.gif)


## License
Licensed under the [MIT License](/LICENSE)
