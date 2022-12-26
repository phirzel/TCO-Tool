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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Peter Hirzel <i>soft</i>Environment
 */
@Slf4j
@Deprecated(since = "1.6.0")
public class DecipherTool {

	private javax.crypto.Cipher cipher = null;

	/**
	 * Ctreate Instance of DecipherTool.
	 */
	public DecipherTool(Key key) {
		super();
		try {
			cipher = javax.crypto.Cipher.getInstance(key.getAlgorithm());
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
		} catch (NoSuchPaddingException e) {
            log.debug("Padding: {}", e.getLocalizedMessage());
        } catch (NoSuchAlgorithmException e) {
            log.debug("Algorithm: {}", e.getLocalizedMessage());
        } catch (InvalidKeyException e) {
            log.debug("Key: {}", e.getLocalizedMessage());
        }
	}

	/**
	 * Decrypt from in to out.
	 */
	public void decrypt(java.io.InputStream in, java.io.OutputStream out) {
		try {
			// 1) receive encrypted message and serialized SealedObject

			// 2) deserialize SealedObject

			// 3) create symmetric Cipher object (using the same algorithm that was used by the encryption application)

			/*
			 * optionally
			 * 3)  Create asymmetric Cipher object, and initialize it for decryption with private key (corresponding to public Key used by the encryption  application)
			 * 4) Unseal the wrapped session key using the asymmetric Cipher
			 */

			// 5) decrypt message by CipherInputStream#read() will decrypt the data
			//    passed through it while reading them from the encrypted file.
			javax.crypto.CipherInputStream plainStream = new javax.crypto.CipherInputStream(in, cipher);
			int i = plainStream.read();
			while (i != -1) {
				out.write(i);
				i = plainStream.read();
			}
			plainStream.close();
			out.close();
		} catch (IOException e) {
            log.warn("Decryption Problem", e);
        }
	}

	/**
	 * Takes a encrypted String as an argument, decrypts and returns the decrypted String.
	 *
	 * @param str Encrypted String to be decrypted
	 * @return <code>String</code> Decrypted version of the provided String
	 */
	public String decrypt(String str) {
		//TODO throw new DeveloperException("replace decoder => see CipherTool.encode()");

		try {
			// Decode base64 to get bytes
		/*
		byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

		// Decrypt
		byte[] utf8 = cipher.doFinal(dec);

		// Decode using java.nio.charset.Charset
		return new String(utf8, "UTF-8");
		 */
			return EncodingUtils.decodeBase64(str);

		} catch (Exception e) {
            log.debug("IO: {}", e.getLocalizedMessage());
        }

		return null;
	}
}
