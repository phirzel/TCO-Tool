package ch.softenvironment.licence.vendor;

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

/**
 * Generator to generate a Licence Key for an Application-Product.
 *
 * @author Peter Hirzel
 */
@Deprecated(since = "1.6.0")
public class LicenceGenerator {

	private int expirationDuration = -1;
	private String nrUser = null;
	private String pName = null;
	private String pVersion = null;
	private final java.util.Random r = new java.util.Random();

	/**
	 * LicenceGenerator constructor comment.
	 */
	public LicenceGenerator() {
		super();
	}

	/**
	 * Set the Number of Users.
	 */
	private char getCipher(String character, int nonRange, int validLimit) {
		if (character.equals("0")) {
			return getLetter(nonRange);
		} else {
			return (char) (validLimit - (Integer.valueOf(character)).intValue());
		}
	}

	/**
	 * Return the calculated LicenceKey-String.
	 */
	public String getKey() {
		// a
		String k = "" + getCipher(nrUser.substring(2, 3), 97, 81);
		// b
		char c = (char) (expirationDuration + 103);
		k = k + c;
		// c
		k = k + getCipher(nrUser.substring(1, 2), 65, 112);
		// d
		k = k + pName.charAt(3) + "-";
		// e
		k = k + getCipher(pVersion.substring(0, 1), 65, 119);
		// f
		k = k + pName.charAt(1);
		// g
		k = k + getCipher(pVersion.substring(3, 4), 65, 112);
		// h
		k = k + getCipher(pVersion.substring(5, 6), 97, 85) + "-";
		// i
		k = k + pName.charAt(0);
		// j
		k = k + getCipher(pVersion.substring(1, 2), 65, 117);
		// k
		k = k + getLetter(97);
		// l
		k = k + getCipher(pVersion.substring(4, 5), 65, 116) + "-";
		// m
		k = k + pName.charAt(2);
		// n
		k = k + getCipher(pVersion.substring(2, 3), 97, 77);
		// o
		k = k + getLetter(65);
		// p
		k = k + getCipher(nrUser.substring(0, 1), 97, 80);
		return k;
	}

	/**
	 *
	 */
	private char getLetter(int base) {
		return (char) (r.nextInt(25) + base);
	}

	/**
	 * Set the Expiration-Date.
	 */
	public void setExpirationDuration(int index) {
		this.expirationDuration = index;
	}

	/**
	 * Set the Number of Users.
	 */
	public void setNumberOfUsers(Integer number) {
		if (number.intValue() < 10) {
			nrUser = "00" + number.toString();
		} else if (number.intValue() < 100) {
			nrUser = "0" + number.toString();
		} else {
			nrUser = number.toString();
		}
	}

	/**
	 * Set the Product-Name.
	 */
	public void setProductName(String name) {
		this.pName = name;
	}

	/**
	 * Set the Product-Version.
	 */
	public void setProductVersion(String version) {
		this.pVersion = version;
	}
}
