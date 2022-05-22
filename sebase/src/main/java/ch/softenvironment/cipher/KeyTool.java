package ch.softenvironment.cipher;

/*
 * Copyright (C) 2006 Peter Hirzel softEnvironment (http://www.softenvironment.ch).
 * All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Tracer;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

/**
 * Tool to administrate secret Keys (symmetric and asymmetric type). This Class uses Java JCE. Possible <b>Crypto-Algorithms</b>: - DES - DESede - AES (with Java 2 SDK, v 1.4.2) - Blowfish -
 * PBEWithMD5AndDES - PBEWithMD5AndTripleDES - Diffie-Hellman key agreement among multiple parties - HmacMD5 - HmacSHA1
 *
 * @author Peter Hirzel <i>soft</i>Environment
 */
public class KeyTool {

    private static final String algorithm = "DES";

    /**
     * Create a new Crypto-Key and store it into a File.
     *
     * @param algorithm Crypto-Algorithm
     * @param fileName File where key has to be written to
     * @see #getKey(String, String)
     */
    public static Key createKey(String algorithm, String fileName) {
        try {
            java.io.File keyFile = new java.io.File(fileName);
            java.io.FileOutputStream out = new java.io.FileOutputStream(keyFile);

            Key key = generateKey(algorithm);

            // evtl. wrap key here
            Tracer.getInstance().developerWarning("Key-saving/loading only works on the same FileSystem yet (Windows->Linux => wrong key at reading)!");

            // write Key into a KeyFile
            out.write(key.getEncoded());
            out.flush();
            out.close();

            return key;
        } catch (Throwable e) {
            ch.softenvironment.util.Tracer.getInstance().runtimeError("Key-failure", e);
            return null;
        }
    }

    /**
     * Return a SecretKey using <i>DES</i>-Algorithm.
     */
    public static Key getDesKey(byte[] desKeyData) {
        Key key = null;
        try {
            // convert secret key data into a SecretKey object,
            // which can be used for a subsequent Cipher operation
            KeySpec keySpec = new javax.crypto.spec.DESKeySpec(desKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            key = keyFactory.generateSecret(keySpec);
        } catch (Throwable e) {
            ch.softenvironment.util.Tracer.getInstance().runtimeError(null, e);
        }
        return key;
    }

    /**
     * Read a given Crypto-Key from a File which was created by #createKey(..).
     *
     * @param algorithm Crypto-Algorithm (such as "DES", "Blowfish", "DESede",...)
     * @param fileName File where key is contained
     * @see #createKey(String, String)
     */
    public static Key readKey(String algorithm, String fileName) {
        try {
            java.io.File keyFile = new java.io.File(fileName);
            java.io.FileInputStream in = new java.io.FileInputStream(keyFile);

            // read the file containing the key
            byte[] b = new byte[8];
            String keyEncoded = "";
            int i = in.read(b);
            while (i != -1) {
                keyEncoded = keyEncoded + new String(b);
                i = in.read(b);
            }
            in.close();

            // evtl. unwrap here
            Tracer.getInstance().developerWarning("Key-saving/loading only works on the same FileSystem yet (Windows->Linux => wrong key at reading)!");

            // convert secret key data into a SecretKey object,
            // which can be used for a subsequent Cipher operation
            if (algorithm.equals(algorithm)) {
                return getDesKey(keyEncoded.getBytes());
            } else {
                // generically
                KeySpec keySpec = new SecretKeySpec(keyEncoded.getBytes(), algorithm);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
                Key key = keyFactory.generateSecret(keySpec);
                return key;
            }
        } catch (Throwable e) {
            throw new DeveloperException("Error with getting crypto-key in <" + fileName + ">", null, e);
        }
    }

    /**
     * Return a newly generated SecretKey.
     *
     * @param algorithm
     * @return Key (or concrete javax.crypto.SecretKey)
     */
    public static Key generateKey(String algorithm) {
        try {
            javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance(algorithm);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            Tracer.getInstance().debug("Algorithm: " + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Unwrapping a key after it was wrapped.
     *
     * @param keyType any Cipher.*_KEY
     * @see #wrap(..)
     */
    public static Key unwrap(Key cipherKey, String algorithm, final int keyType, byte[] keyWrapped) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.UNWRAP_MODE, cipherKey);
            return cipher.unwrap(keyWrapped, algorithm, keyType);
        } catch (Throwable e) {
            Tracer.getInstance().developerError(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Wrapping a key enables secure transfer of the key from one place to another.
     *
     * @param cipherKey Key to wrap the keyToBeTransmitted (should be known on either side of Transmission)
     * @see #unwrap(..)
     */
    public static byte[] wrap(Key cipherKey, Key keyToBeTransmitted, String algorithm) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.WRAP_MODE, cipherKey);
            return cipher.wrap(keyToBeTransmitted);
        } catch (Throwable e) {
            Tracer.getInstance().developerError(e.getLocalizedMessage());
            return null;
        }
    }
}

