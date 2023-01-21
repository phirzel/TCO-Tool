package ch.softenvironment.cipher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Peter Hirzel
 */
@Deprecated(since = "1.6.0")
public class CipherTest {

	private Key key = null;
	private final String algorithm = "DES";
	/*
                         DES
                         DESede
                        AES (with Java 2 SDK, v 1.4.2)
                        Blowfish
                        PBEWithMD5AndDES
                        PBEWithMD5AndTripleDES
                        Diffie-Hellman key agreement among multiple parties
                        HmacMD5
                        HmacSHA1
    */
	private final String secretMessage = "������ 'Hello'[arg]{���}$<>&";

	@Before
	public void setUp() {
		key = KeyTool.generateKey(algorithm);
	}

	@Test
	public void streamEncryption() throws UnsupportedEncodingException {
		String charSet = "UTF-8";    // "ISO-8859-1"

		ByteArrayInputStream inPlain = new ByteArrayInputStream(secretMessage.getBytes(charSet));
		ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
		CipherTool encipher = new CipherTool(key);
		encipher.encrypt(inPlain, encrypted);
		assertFalse("encrypt()", secretMessage.equals(encrypted.toString(charSet)));

		DecipherTool decipher = new DecipherTool(key);
		ByteArrayOutputStream decrypted = new ByteArrayOutputStream();
		decipher.decrypt(new ByteArrayInputStream(encrypted.toByteArray()), decrypted);
		assertTrue("decrypt()", secretMessage.equals(decrypted.toString(charSet)));
/*	
	String path = System.getProperty("java.io.tmpdir") + File.separator + "TestCase_Cipher";
	try {
		// create plain Test file
		File file = new File(path + ".txt");
		FileOutputStream outStream = new FileOutputStream(file);
		PrintStream printStream = new PrintStream(outStream);
		printStream.print(secretMessage);
		outStream.flush();
		outStream.close();
	} catch (IOException e) {
		System.out.println("Error writing <TestCase_Cipher.txt>");
	}
	CipherTool encipher = new CipherTool(key);
	encipher.encrypt(new FileInputStream(new File(path + ".txt")),
					new FileOutputStream(new File(path + ".encrypted")));
					
	//	decrypt encrypted file
	FileInputStream in = new FileInputStream(new File(path + ".encrypted"));
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	DecipherTool decipher = new DecipherTool(key);
	decipher.decrypt(in, out);
*/
	}

	@Ignore
	public void encrypt() {
		CipherTool encipher = new CipherTool(key);
		String encrypted = encipher.encrypt(secretMessage);
		Assert.assertFalse("encrypt", secretMessage.equals(encrypted));

		DecipherTool decipher = new DecipherTool(key);
		assertTrue("decrypt", secretMessage.equals(decipher.decrypt(encrypted)));
	}

	@Ignore
	public void createKey_saving() {
		Key keySaved = KeyTool.createKey(algorithm, System.getProperty("java.io.tmpdir") + "Cipher.key");
		Key keyLoaded = KeyTool.readKey(algorithm, System.getProperty("java.io.tmpdir") + "Cipher.key");
		byte[] saved = keySaved.getEncoded();
		for (int i = 0; i < saved.length; i++) {
			assertTrue("KeyTool#save/load()", saved[i] == keyLoaded.getEncoded()[i]);
		}
	}

	@Test
	public void wrap_generatedKey() {
		// key is the key to wrap a key
		Key keyToTransmit = KeyTool.generateKey(algorithm);
		byte[] wrappedKey = KeyTool.wrap(key, keyToTransmit, algorithm);
		assertTrue("wrap", wrappedKey != null);

		Key keyTransmitted = KeyTool.unwrap(key, algorithm, Cipher.SECRET_KEY /*symmetric Key*/, wrappedKey);
		assertTrue("unwrap", keyToTransmit.equals(keyTransmitted));
	}

	@Test
	public void getDesKey() {
		byte[] key = {(byte) 0x17, (byte) 0x02, (byte) 0x05,
			(byte) 0x65, (byte) 0x41, (byte) 0x13,
			(byte) 0x62, (byte) 0x08};
		assertTrue("getDesKey", KeyTool.getDesKey(key) instanceof SecretKey);
	}
}
