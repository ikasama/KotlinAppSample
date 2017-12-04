package com.badlogic.masaki.passwordmanagementsample.util

import android.util.Base64
import android.content.Context
import com.badlogic.masaki.passwordmanagementsample.util.Constants.DEFAULT_KEY
import com.badlogic.masaki.passwordmanagementsample.util.Constants.FIXED_SALT
import com.badlogic.masaki.passwordmanagementsample.util.Constants.STRETCH_COUNT
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.Crypto
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity
import com.facebook.crypto.exception.CryptoInitializationException
import com.facebook.crypto.exception.KeyChainException
import com.facebook.crypto.keychain.KeyChain
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.MessageDigest

/**
 * Created by masaki on 2017/11/16.
 */
private object Constants {
    const val DEFAULT_KEY = "DEFAULT_KEY"
    const val STRETCH_COUNT = 64
    const val FIXED_SALT = "uZRjELuNNgI9cTFud4KW5LUbCffUu4l609mLdYHN9GcMIh6JFrpH4Xkj5zSVlPPu"
}

//fun encryptWithSalt(context: Context?, srcText: String?, salt: String? = "",
//                    stretchCount: Int = STRETCH_COUNT, key: String? = DEFAULT_KEY): String? {
//    @Throws(UnsupportedEncodingException::class
//            ,CryptoInitializationException::class
//            ,KeyChainException::class
//            ,IOException::class)
//    tailrec fun go(crypto: Crypto, srcArray: ByteArray, salt: String, stretchCount: Int, key: String): ByteArray? {
//        return if (stretchCount <= 0) {
//            srcArray
//        } else {
//            //UTFでbyte[]に変換して暗号化する
//            //val cipherText: ByteArray? = crypto.encrypt(srcText?.toByteArray(), Entity.create(key))
//            val cipherText: ByteArray? = crypto.encrypt(srcArray + salt.toByteArray() + FIXED_SALT.toByteArray(), Entity.create(key))
//            //val s:String? = Base64.encodeToString(cipherText, Base64.DEFAULT)
//            //val s: String = Base64.encodeToString(cipherText, Base64.DEFAULT)
//            go(crypto, cipherText!!, salt, stretchCount - 1, key)
//            //return go(crypto, getEncryptedBase64Text(crypto, srcText, key), stretchCount - 1, key)
//        }
//    }
//
//
//    requireNotNull(context, {"context must not be null"})
//    requireNotNull(srcText, {"srcText must not be null"})
//    requireNotNull(key, {"key must not be null"})
//
//    if (stretchCount <= 0) {
//        return srcText
//    } else {
//        val crypto: Crypto? = createCrypto(context)
//        //UTFでbyte[]に変換して暗号化する
//        //val cipherText: ByteArray? = crypto?.encrypt(srcText?.toByteArray(), Entity.create(key))
//        val cipherText: ByteArray? = srcText?.toByteArray()
//        //val s:String? = Base64.encodeToString(cipherText, Base64.DEFAULT)
//        //val s: String = Base64.encodeToString(cipherText, Base64.DEFAULT)
//        when(crypto?.isAvailable) {
//            true -> return Base64.encodeToString(go(crypto, srcText?.toByteArray()!!, salt!!, stretchCount, key!!), Base64.DEFAULT)
//        //true -> return encrypt(context, s!!, stretchCount - 1, key!!)
//            else -> return null
//        }
//        //return encrypt(context, srcText, (stretchCount - 1), key)
//    }
//}
//
//fun encrypt(context: Context?, srcText: String?, stretchCount: Int = STRETCH_COUNT, key: String? = DEFAULT_KEY): String? {
//    fun getEncryptedBase64Text(crypto: Crypto, srcText: String, key: String): String {
//        return Base64.encodeToString(crypto.encrypt(srcText.toByteArray(), Entity.create(key)), Base64.DEFAULT)
//    }
//
//    @Throws(UnsupportedEncodingException::class
//            ,CryptoInitializationException::class
//            ,KeyChainException::class
//            ,IOException::class)
//    tailrec fun go(cryp: Crypto, byteArray: ByteArray, stretch: Int, encryptionKey: String): ByteArray? {
//        if (stretch <= 0) {
//            return byteArray
//        } else {
//            //UTFでbyte[]に変換して暗号化する
//            //val cipherText: ByteArray? = crypto.encrypt(srcText?.toByteArray(), Entity.create(key))
//            val cipherText: ByteArray? = cryp.encrypt(byteArray, Entity.create(encryptionKey))
//            //val s:String? = Base64.encodeToString(cipherText, Base64.DEFAULT)
//            //val s: String = Base64.encodeToString(cipherText, Base64.DEFAULT)
//            return go(cryp, cipherText!!, stretch - 1, encryptionKey)
//            //return go(crypto, getEncryptedBase64Text(crypto, srcText, key), stretchCount - 1, key)
//        }
//    }
//
//
//    requireNotNull(context, {"context must not be null"})
//    requireNotNull(srcText, {"srcText must not be null"})
//    requireNotNull(key, {"key must not be null"})
//
//    if (stretchCount <= 0) {
//        return srcText
//    } else {
//        val crypto: Crypto? = createCrypto(context)
//        //UTFでbyte[]に変換して暗号化する
//        //val cipherText: ByteArray? = crypto?.encrypt(srcText?.toByteArray(), Entity.create(key))
//        val cipherText: ByteArray? = srcText?.toByteArray()
//        //val s:String? = Base64.encodeToString(cipherText, Base64.DEFAULT)
//        //val s: String = Base64.encodeToString(cipherText, Base64.DEFAULT)
//        when(crypto?.isAvailable) {
//            true -> return Base64.encodeToString(go(crypto, srcText?.toByteArray()!!, stretchCount, key!!), Base64.DEFAULT)
//            //true -> return encrypt(context, s!!, stretchCount - 1, key!!)
//            else -> return null
//        }
//        //return encrypt(context, srcText, (stretchCount - 1), key)
//    }
//
////    val crypto: Crypto? = createCrypto(context)
////    when(crypto?.isAvailable) {
////        true -> return go(crypto, srcText!!, key!!)
////        else -> return null
////    }
//}
//
//fun decrypt(context: Context?, encryptedText: String?, stretchCount: Int = STRETCH_COUNT, key: String = DEFAULT_KEY): String? {
//    requireNotNull(context, {"context must not be null"})
//    requireNotNull(encryptedText, {"encryptedText must not be null"})
//    requireNotNull(key, {"key must not be null"})
//
//    tailrec fun go(cryp: Crypto, byteArray: ByteArray, stretch: Int, encryptionKey: String): ByteArray {
//        if (stretchCount <= 0) {
//            return byteArray
//        } else {
//            val decryptedBytes: ByteArray? = cryp.decrypt(byteArray, Entity.create(encryptionKey))
//            //val s: String? = decryptedBytes?.toString(Charset.defaultCharset())
//            //return s
//            return go(cryp, decryptedBytes!!, stretch -1, encryptionKey)
//        }
//    }
//
//    val crypto: Crypto? = createCrypto(context)
//    val rawEncryptedBytes: ByteArray = Base64.decode(encryptedText, Base64.DEFAULT)
////    val decryptedBytes: ByteArray? = crypto?.decrypt(rawEncryptedBytes, Entity.create(key))
////    val s: String? = decryptedBytes?.toString(Charset.defaultCharset())
//
//    if (stretchCount <= 0) {
//        return encryptedText
//    } else {
//        when(crypto?.isAvailable) {
//            //true -> return go(crypto, rawEncryptedBytes, stretchCount!!, key!!).toString(Charset.defaultCharset())
//            true -> return String(go(crypto, rawEncryptedBytes, stretchCount!!, key!!))
//            else -> return null
//        }
//    }
//}
//
//fun getPasswordHash(baseSalt: String?, password: String?): String? {
//    requireNotNull(baseSalt, {"baseSalt must not be null"})
//    val salt = generateSalt(baseSalt)
//
//    for (i in 1..STRETCH_COUNT) {
//
//    }
//    return null
//}
//
//fun generateSalt(baseSalt: String?): String? {
//    return baseSalt + FIXED_SALT
//}
//
//private fun createCrypto(context: Context?, config: CryptoConfig? = CryptoConfig.KEY_256): Crypto? {
//    val keyChain: KeyChain? = SharedPrefsBackedKeyChain(context, config ?: CryptoConfig.KEY_256)
//    return AndroidConceal.get()?.createDefaultCrypto(keyChain)
//}

fun getSalt(id: String): String = id + FIXED_SALT

fun hashPassword(nickname: String, password: String): String {
    tailrec fun go(digest: MessageDigest, hash: String, pass: String,
                   salt: String, stretchCount: Int): String {
        return if (stretchCount >= STRETCH_COUNT) {
            hash
        } else {
            digest.update((hash + pass + salt).toByteArray())
            val hash = bytesToHexString(digest.digest())
            go(digest, hash, pass, salt, stretchCount + 1)
        }
    }

    val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
    return when(messageDigest) {
        is MessageDigest -> go(messageDigest, password, password, getSalt(nickname), 0)
        else -> ""
    }
}
//
//fun hashSHA256(str: String): String? {
//    try {
//        MessageDigest.getInstance("SHA-256")?.let {
//            it.update(str.toByteArray())
//            return bytesToHexString(it.digest())
//        }
//        return null
//    } catch (e: Exception) {
//        e.printStackTrace()
//        return null
//    }
//}

private fun bytesToHexString(bytes: ByteArray): String {
    val sb = StringBuffer()
    for (i in bytes.indices) {
        val hex = Integer.toHexString(0xFF and bytes[i].toInt())
        if (hex.length == 1) {
            sb.append('0')
        }
        sb.append(hex)
    }
    return sb.toString()
}
