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

import ch.softenvironment.util.Tracer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Tool to encrypt any "Message".
 *
 * @author Peter Hirzel <i>soft</i>Environment
 */
public class CipherTool {

	private javax.crypto.Cipher cipher = null;

	/**
	 * Create Instance of CipherTool.
	 */
	public CipherTool(Key key) {
		super();
		try {
			cipher = javax.crypto.Cipher.getInstance(key.getAlgorithm());
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
		} catch (NoSuchPaddingException e) {
			Tracer.getInstance().debug("Padding: " + e.getLocalizedMessage());
		} catch (NoSuchAlgorithmException e) {
			Tracer.getInstance().debug("Algorithm: " + e.getLocalizedMessage());
		} catch (InvalidKeyException e) {
			Tracer.getInstance().debug("Key: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Encrypt from in- to out-Stream. Close the streams after encryption.
	 */
	public void encrypt(java.io.InputStream in, java.io.OutputStream out) {
		try {
			javax.crypto.CipherOutputStream encryptedStream = new javax.crypto.CipherOutputStream(out, cipher);
			int i = in.read();
			while (i != -1) {
				encryptedStream.write(i);
				i = in.read();
			}
			encryptedStream.flush();
			encryptedStream.close();
			out.close();
			in.close();

			// Get intended recipient's public key (e.g., from the recipient's public key certificate)
			// Create Cipher for asymmetric encryption (e.g., RSA), and 
			// initialize it for encryption with recipient's public key 
			// Create SealedObject to seal session key using asymmetric Cipher
			// Serialize SealedObject 
			// Send encrypted message and serialized 
			// SealedObject to intended recipient
		} catch (IOException e) {
			ch.softenvironment.util.Tracer.getInstance().runtimeError("IO-Encryption Problem", e);
		}
	}

	/**
	 * Takes a single String as an argument and returns an Encrypted version of that String in <b>UTF8</b> CharacterSet.
	 *
	 * @param str String to be encrypted
	 * @return <code>String</code> Encrypted version of the provided String
	 */
	public String encrypt(final String str) {
		try {
			// Encode the string into bytes using java.nio.charset.Charset)
			byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);

			// Encrypt
			byte[] enc = cipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			//return new sun.misc.BASE64Encoder().encode(enc);
			return EncodingUtils.encodeBase64(enc);
			//throw new DeveloperException("replace encoder, perhaps use Jakarta Commons Codec API (http://jakarta.apache.org/commons/codec/)");

		} catch (javax.crypto.BadPaddingException e) {
			Tracer.getInstance().debug("Padding: " + e.getLocalizedMessage());
		} catch (IllegalBlockSizeException e) {
			Tracer.getInstance().debug("BlockSize: " + e.getLocalizedMessage());
		}
		return null;
	}
}
