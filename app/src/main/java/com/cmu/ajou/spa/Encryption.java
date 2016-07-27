package com.cmu.ajou.spa;

import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Minseong on 2016-07-27.
 */
public class Encryption {
    Key pubKey = null;
    Key privKey = null;
    Cipher cipher = null;
    public static final String RSA = "RSA";

    public static String pubKeyStr = null;
/*
    public String getPubKey(){
        return byteArrayToHex(pubKey.getEncoded());
    }

    public String getPrivKey(){
        return byteArrayToHex(privKey.getEncoded());
    }
*/
    public Encryption(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

       try {
            cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



//        System.out.println("pubKeyHex:"+byteArrayToHex(pubKey.getEncoded()));
//        System.out.println("privKeyHex:"+byteArrayToHex(privKey.getEncoded()));

//        pubKeyStr = byteArrayToHex(pubKey.getEncoded());
    }


    /*
    public byte[] encoding(byte[] input, Key key){
        // 공개키를 전달하여 암호화
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] cipherText = null;
        try {
            cipherText = cipher.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("cipher: ("+ cipherText.length +")"+ new String(cipherText));

        return cipherText;
    }

    public void decoding(byte[] cipherText){
        try {
            cipher.init(Cipher.DECRYPT_MODE, privKey);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] plainText = null;
        try {
            plainText = cipher.doFinal(cipherText);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("plain : " + new String(plainText));
    }



    // byte[] to hex sting
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }
    */


    public static String encrypt(String text, PublicKey publicKey) {
        byte[] bytes = text.getBytes();
        String encryptedText = null;
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedText = new String(Base64.encodeBase64(cipher.doFinal(bytes)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encryptedText;
    }

    // hex string to byte[]
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }
        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }
}
