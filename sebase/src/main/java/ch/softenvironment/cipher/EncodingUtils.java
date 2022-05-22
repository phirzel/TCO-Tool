package ch.softenvironment.cipher;

import ch.softenvironment.util.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * UTF-8 based encoding.
 *
 * @since 0.8.2
 */
public final class EncodingUtils {

    //TODO HIP private static final Logger LOGGER = LoggerFactory.getLogger(EncodingUtils.class);
    static final String MASK_PATH_PARAM = "0";

    public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
    /**
     * Encoding used for Hafas::REST-API
     */
    public static final String ENCODING_UTF8 = CHARSET_UTF8.name();

    public static final String ENCODED_COLON = "%3A";
    /**
     * More secure than "SHA-256" and faster on some machines. Needs Java 11.
     */
    private static final String ENCRYPTION_ALGORITHM_SHA3_256 = "SHA3-256";

    private EncodingUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Encode given term into UTF-8. Necessary for URI Url.
     * <p>
     * RFC 3986, chapter 2.2. Reserved Characters:
     * <p>
     * URIs include components and subcomponents that are delimited by characters in the "reserved" set. These characters are called "reserved" because they may (or may not) be defined as delimiters
     * by the generic syntax, by each scheme-specific syntax, or by the implementation-specific syntax of a URI's dereferencing algorithm. If data for a URI component would conflict with a reserved
     * character's purpose as a delimiter, then the conflicting data must be percent-encoded before the URI is formed.
     * <p>
     * reserved = gen-delims / sub-delims
     * <p>
     * gen-delims = ":" / "/" / "?" / "#" / "[" / "]" / "@"
     * <p>
     * sub-delims = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
     * <p>
     * The purpose of reserved characters is to provide a set of delimiting characters that are distinguishable from other data within a URI. URIs that differ in the replacement of a reserved
     * character with its corresponding percent-encoded octet are not equivalent. Percent- encoding a reserved character, or decoding a percent-encoded octet that corresponds to a reserved character,
     * will change how the URI is interpreted by most applications. Thus, characters in the reserved set are protected from normalization and are therefore safe to be used by scheme-specific and
     * producer-specific algorithms for delimiting data subcomponents within a URI.
     *
     * @param plainText any formationChanges as query-parameter
     * @return UTF-8 encoded queryExpression
     * @see <a href="https://tools.ietf.org/html/rfc3986#section-2.2>RFC3986 2.2</a>
     * @see <a href="https://stackoverflow.com/questions/18138011/url-encoding-using-the-new-spring-uricomponentsbuilder">UriComponentBuilder and queryParam</a>
     */
    public static String encode(String plainText) {
        try {
            if (StringUtils.isNullOrEmpty(plainText)) {
                return null;
            }
            // FPLU-3100
            return URLEncoder.encode(plainText, ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            //TODO HIP LOGGER.error("encoding <" + plainText + "> failed", e);
            return plainText;
        }
    }

    /**
     * Decode given term from UTF-8 encoding. Necessary for encoded values in JSON objects.
     * <p>
     * FPLU-1725
     *
     * @param encodedText encoded value
     * @return decoded queryExpression
     */
    public static String decode(String encodedText) {
        if (StringUtils.isNullOrEmpty(encodedText)) {
            return null;
        }
        try {
            return URLDecoder.decode(encodedText, ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            //TODO HIP LOGGER.error("decoding <" + encodedText + "> failed", e);
            return encodedText;
        }
    }

    /**
     * Encrypt text.
     *
     * @param plainText for e.g. password for HTTP-header
     * @return ciphered value
     * @see #decodeBase64(String)
     */
    public static String encodeBase64(String plainText) {
        if (StringUtils.isNullOrEmpty(plainText)) {
            return null;
        }
        return encodeBase64(plainText.getBytes(CHARSET_UTF8));
    }

    /**
     * Base64 encoding schemes are commonly used when there is a need to encode binary data that needs be stored and transferred over media that are designed to deal with textual data. This is to
     * ensure that the data remains intact without modification during transport.
     * <p>
     * Be aware characters like the following are still valid after encoding: SPACE, %, /
     *
     * @param byteArray for e.g. password for HTTP-header in Byte Array
     * @return ciphered value
     * @see #decodeBase64AsBytes(String)
     */
    public static String encodeBase64(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        return new String(Base64.getEncoder().encode(byteArray));
    }

    /**
     * Decrypt cipher.
     *
     * @param encodedText for e.g. encrypted password
     * @return plain text
     */
    public static String decodeBase64(String encodedText) {
        if (StringUtils.isNullOrEmpty(encodedText)) {
            return null;
        }
        return new String(decodeBase64AsBytes(encodedText));
    }

    /**
     * Decrypt cipher.
     *
     * @param encodedText for e.g. encrypted password
     * @return plain text
     * @see #encodeBase64(byte[])
     */
    public static byte[] decodeBase64AsBytes(/*@NonNull*/ String encodedText) {
        return Base64.getDecoder().decode(encodedText.getBytes(CHARSET_UTF8));
    }

    public static String encodeBase64Url(String plainText) {
        if (StringUtils.isNullOrEmpty(plainText)) {
            return null;
        }
        return encodeBase64Url(plainText.getBytes(CHARSET_UTF8));
    }

    /**
     * Base64 encoding schemes are commonly used when there is a need to encode binary data that needs be stored and transferred over media that are designed to deal with textual data. This is to
     * ensure that the data remains intact without modification during transport.
     * <p>
     * This encoding frees result from unwanted characters like: SPACE, %, /
     *
     * @param byteArray for e.g. password for HTTP-header in Byte Array
     * @return ciphered value
     * @see #decodeBase64UrlAsBytes(String)
     */
    public static String encodeBase64Url(/*@NonNull*/ byte[] byteArray) {
        return new String(Base64.getUrlEncoder().encode(byteArray));
    }

    public static String decodeBase64Url(String encodedText) {
        if (StringUtils.isNullOrEmpty(encodedText)) {
            return null;
        }
        return new String(decodeBase64UrlAsBytes(encodedText));
    }

    /**
     * Decrypt cipher.
     *
     * @param encodedText for e.g. encrypted password
     * @return plain text
     * @see #encodeBase64Url(byte[])
     */
    public static byte[] decodeBase64UrlAsBytes(/*@NonNull*/ String encodedText) {
        return Base64.getUrlDecoder().decode(encodedText.getBytes(CHARSET_UTF8));
    }

    /**
     * Creates a hash code of the given string. Remains consistent among multiple executions of the same application, for the very same input.
     *
     * @param input from which value a hash should be calculated. (for e.g. serialized object)
     * @return hashed value of given String
     */
    public static String calculateConsistentHashCode(/*@NonNull*/ String input, /*@NonNull*/MessageDigest messageDigest) {
        final byte[] hashBytes = messageDigest.digest(input.getBytes());
        return bytesToHex(hashBytes);
    }

    private static String bytesToHex(/*@NonNull*/ byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte hash1 : hash) {
            String hex = Integer.toHexString(0xff & hash1);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Returns an instance of SHA-3-256 message digester.
     * <p>
     * Warning: Proved as non re-entrant, therefore create an instance for each usage separately!
     *
     * @see <a href="https://www.baeldung.com/sha-256-hashing-java">Baeldung</a>
     */
    public static MessageDigest createMessageDigestSHA3_256() {
        try {
            return MessageDigest.getInstance(ENCRYPTION_ALGORITHM_SHA3_256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Support for " + ENCRYPTION_ALGORITHM_SHA3_256 + " was expected, but not available.", e);
        }
    }
}
