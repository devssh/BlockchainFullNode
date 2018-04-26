package app.utils;

import app.model.Keyz;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import static app.model.Keyz.decodePrivateKeyFromString;

public class SignatureUtils {
    public static transient final String signatureAlgo = "SHA256withECDSA";
    public static transient final String ENCODING = "UTF-8";
    public static transient final int RADIX = 64;

    public static String Sign(PrivateKey privateKey, String message) {
        try {
            Signature dsa = Signature.getInstance(signatureAlgo);
            dsa.initSign(privateKey);
//            dsa.setParameter();
            dsa.update(message.getBytes(ENCODING));
            return new BigInteger(1, dsa.sign()).toString(RADIX);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

//    public static String SignWith(String signedBy, String message) {
//        return Sign(GetKey(signedBy).privateKeyz, message);
//    }

    public static String Sign(String privKey, String message) {
        return Sign(decodePrivateKeyFromString(privKey), message);
    }

    public static boolean Verify(String blockMessage, PublicKey publicKey, String sign) {
        try {
            Signature ecdsa = Signature.getInstance(signatureAlgo);
            ecdsa.initVerify(publicKey);
            ecdsa.update(blockMessage.getBytes(ENCODING));
            return ecdsa.verify(DatatypeConverter.parseBase64Binary(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static boolean Verify(String blockMessage, String pubKey, String sign) {
        return Verify(blockMessage, Keyz.decodePublicKeyFromString(pubKey), sign);
    }

//    public static boolean VerifyWith(String blockMessage, String signedBy, String sign) {
//        return Verify(blockMessage, GetKey(signedBy).publicKeyz, sign);
//    }
}
