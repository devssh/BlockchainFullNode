package app.model;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Keyz {
    public static transient final String ECDSA = "ECDSA";
    public static transient final String SHA_1_PRNG = "SHA1PRNG";
    public static transient final String SECP_256_K_1 = "secp256k1";
    public static transient final int NUM_BYTES = 512;
    public transient PublicKey publicKeyz;
    public transient PrivateKey privateKeyz;

    public String owner;
    public String publicKey;
    public String privateKey;

    public Keyz(String owner, String publicKey, String privateKey) {
        this.owner = owner;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.publicKeyz = decodePublicKeyFromString(publicKey);
        this.privateKeyz = decodePrivateKeyFromString(privateKey);
    }

    public Keyz(String owner, PublicKey publicKeyz, PrivateKey privateKeyz) {
        this.owner = owner;
        this.publicKeyz = publicKeyz;
        this.privateKeyz = privateKeyz;
        this.publicKey = encodeKeyToString(publicKeyz);
        this.privateKey = encodeKeyToString(privateKeyz);
    }


    public static String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static PublicKey decodePublicKeyFromString(String key) {
        try {
            return KeyFactory.getInstance(ECDSA).generatePublic(new X509EncodedKeySpec(decodeKeyFromString(key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static PrivateKey decodePrivateKeyFromString(String key) {
        KeyFactory fact = null;
        try {
            fact = KeyFactory.getInstance(ECDSA);
            return fact.generatePrivate(new PKCS8EncodedKeySpec(decodeKeyFromString(key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    private static byte[] decodeKeyFromString(String key) {
        try {
            return Base64.getDecoder().decode(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static Keyz generateKey() {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyGen1 = KeyPairGenerator.getInstance(ECDSA);
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(SECP_256_K_1);
            SecureRandom random1 = SecureRandom.getInstance(SHA_1_PRNG);
            byte[] seed = random1.generateSeed(NUM_BYTES);
            String seedString = Base64.getEncoder().encodeToString(seed);
            System.out.println(seedString);
            System.out.println("");
            System.out.println("");
            System.out.println(Base64.getEncoder().encodeToString(Base64.getDecoder().decode(seedString)));
            System.out.println("");
            random1.setSeed(seed);
            keyGen1.initialize(ecSpec, random1);
            KeyPair keyPair1 = keyGen1.generateKeyPair();
            PublicKey pub1 = keyPair1.getPublic();
            PrivateKey priv1 = keyPair1.getPrivate();
            System.out.println(new Keyz("random", pub1, priv1));


            KeyPairGenerator keyGen2 = KeyPairGenerator.getInstance(ECDSA);
            ECGenParameterSpec ecSpec2 = new ECGenParameterSpec(SECP_256_K_1);
            SecureRandom random2 = SecureRandom.getInstance(SHA_1_PRNG);
            random2.setSeed(seed);
            keyGen2.initialize(ecSpec2, random2);
            KeyPair keyPair2 = keyGen2.generateKeyPair();
            PublicKey pub2 = keyPair2.getPublic();
            PrivateKey priv2 = keyPair2.getPrivate();
            System.out.println(new Keyz("random", pub2, priv2));


            return new Keyz("random", pub1, priv1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    @Override
    public String toString() {
        return "Keyz{" +
                "owner='" + owner + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}
