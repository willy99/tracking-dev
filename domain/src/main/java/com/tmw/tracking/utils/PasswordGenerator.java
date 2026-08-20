package com.tmw.tracking.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PasswordGenerator {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    private boolean useLower;
    private boolean useUpper;
    private boolean useDigits;
    private boolean usePunctuation;
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    private PasswordGenerator() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    public static String generateSecurePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        return passwordGenerator.generate(8);
    }


    private PasswordGenerator(PasswordGeneratorBuilder builder) {
        this.useLower = builder.useLower;
        this.useUpper = builder.useUpper;
        this.useDigits = builder.useDigits;
        this.usePunctuation = builder.usePunctuation;
    }

    public static class PasswordGeneratorBuilder {

        private boolean useLower;
        private boolean useUpper;
        private boolean useDigits;
        private boolean usePunctuation;

        public PasswordGeneratorBuilder() {
            this.useLower = false;
            this.useUpper = false;
            this.useDigits = false;
            this.usePunctuation = false;
        }

        /**
         * Set true in case you would like to include lower characters
         * (abc...xyz). Default false.
         *
         * @param useLower true in case you would like to include lower
         *                 characters (abc...xyz). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder useLower(boolean useLower) {
            this.useLower = useLower;
            return this;
        }

        /**
         * Set true in case you would like to include upper characters
         * (ABC...XYZ). Default false.
         *
         * @param useUpper true in case you would like to include upper
         *                 characters (ABC...XYZ). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder useUpper(boolean useUpper) {
            this.useUpper = useUpper;
            return this;
        }

        /**
         * Set true in case you would like to include digit characters (123..).
         * Default false.
         *
         * @param useDigits true in case you would like to include digit
         *                  characters (123..). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder useDigits(boolean useDigits) {
            this.useDigits = useDigits;
            return this;
        }

        /**
         * Set true in case you would like to include punctuation characters
         * (!@#..). Default false.
         *
         * @param usePunctuation true in case you would like to include
         *                       punctuation characters (!@#..). Default false.
         * @return the builder for chaining.
         */
        public PasswordGeneratorBuilder usePunctuation(boolean usePunctuation) {
            this.usePunctuation = usePunctuation;
            return this;
        }

        /**
         * Get an object to use.
         */
        public PasswordGenerator build() {
            return new PasswordGenerator(this);
        }
    }

    /**
     * This method will generate a password depending the use* properties you
     * define. It will use the categories with a probability. It is not sure
     * that all of the defined categories will be used.
     *
     * @param length the length of the password you would like to generate.
     * @return a password that uses the categories you define when constructing
     * the object with a probability.
     */
    public String generate(int length) {
        // Argument Validation.
        if (length <= 0) {
            return "";
        }

        // Variables.
        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        // Collect the categories to use.
        List<String> charCategories = new ArrayList<String>(4);
        if (useLower) {
            charCategories.add(LOWER);
        }
        if (useUpper) {
            charCategories.add(UPPER);
        }
        if (useDigits) {
            charCategories.add(DIGITS);
        }
        if (usePunctuation) {
            charCategories.add(PUNCTUATION);
        }

        // Build the password.
        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(random.nextInt(charCategories.size()));
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return new String(password);
    }

    private static final String PBKDF2_PREFIX = "pbkdf2";
    private static final int SALT_LENGTH_BYTES = 16;

    /**
     * Hash a plaintext password for storage: PBKDF2-HMAC-SHA256 with a random per-call salt,
     * encoded as {@code pbkdf2$<iterations>$<saltHex>$<hashHex>}. Use with {@link #verifyPassword}.
     */
    public static String hashPassword(final String password) {
        if (password == null) {
            return null;
        }
        final byte[] salt = new byte[SALT_LENGTH_BYTES];
        RANDOM.nextBytes(salt);
        final byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        return PBKDF2_PREFIX + "$" + ITERATIONS + "$" + toHex(salt) + "$" + toHex(hash);
    }

    /**
     * Verify a plaintext password against a stored hash produced by {@link #hashPassword}.
     * Also accepts the legacy unsalted {@link #encryptPassword} hex digest so existing users can
     * still log in — callers should detect that case with {@link #isLegacyHash} and re-hash the
     * password with {@link #hashPassword} once verified, to migrate the stored value.
     */
    public static boolean verifyPassword(final String password, final String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        if (isLegacyHash(storedHash)) {
            final String legacy = encryptPassword(password);
            return legacy != null && MessageDigest.isEqual(legacy.getBytes(), storedHash.getBytes());
        }
        final String[] parts = storedHash.split("\\$");
        if (parts.length != 4) {
            return false;
        }
        try {
            final int iterations = Integer.parseInt(parts[1]);
            final byte[] salt = fromHex(parts[2]);
            final byte[] expected = fromHex(parts[3]);
            final byte[] actual = pbkdf2(password.toCharArray(), salt, iterations, expected.length * 8);
            return MessageDigest.isEqual(expected, actual);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return {@code true} if {@code storedHash} is the legacy unsalted SHA-256 format rather
     * than a {@link #hashPassword} PBKDF2 hash.
     */
    public static boolean isLegacyHash(final String storedHash) {
        return storedHash != null && !storedHash.startsWith(PBKDF2_PREFIX + "$");
    }

    private static byte[] pbkdf2(final char[] password, final byte[] salt, final int iterations, final int keyLengthBits) {
        try {
            final PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(final byte[] bytes) {
        return convertByteArrayToHexString(bytes);
    }

    private static byte[] fromHex(final String hex) {
        final int len = hex.length();
        final byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return result;
    }

    /**
     * @deprecated legacy unsalted single-round SHA-256 hash, kept only for verifying
     * passwords stored before the migration to {@link #hashPassword}. Do not use for new
     * passwords.
     */
    @Deprecated
    public static String encryptPassword(String password) {
        if (password == null) {
            return null;
        }
        String algorithm = "SHA-256";

        byte[] plainText = password.getBytes();

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        md.reset();
        md.update(plainText);
        byte[] encodedPassword = md.digest();

        return String.format("%064x", new java.math.BigInteger(1, encodedPassword));

    }


    public static String generateHash(String message, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new RuntimeException(
                "Could not generate hash from String", ex);
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

}