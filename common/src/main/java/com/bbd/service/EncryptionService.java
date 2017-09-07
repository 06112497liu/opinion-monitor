package com.bbd.service;

import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.security.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 系统统一使用的加密服务
 *
 * @author tjwang
 */
public class EncryptionService {

    /**
     * 默认hash算法
     */
    public static final String DEFAULT_HASH_ALGORITHM = "SHA-256";                          // MD5或者SHA-256
    private Logger             logger                 = LoggerFactory.getLogger(getClass());
    /**
     * The MessageDigest name of the hash algorithm to use for computing hashes.
     */
    private String             algorithmName          = null;

    /**
     * The number of hash iterations to perform when computing hashes.
     */
    private int                hashIterations         = 1000;

    public EncryptionService() {
        this.algorithmName = DEFAULT_HASH_ALGORITHM;
    }

    public static void main(String[] args) {
        MessageDigest digest = null;
        String content = "123456";
        try {
            digest = MessageDigest.getInstance(DEFAULT_HASH_ALGORITHM);
            digest.update(content.getBytes());
            System.out.println(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the specified plaintext password (usually acquired from your
     * application's 'new user' or 'password reset' workflow) into a formatted
     * string safe for storage. The returned string can be safely saved with the
     * corresponding user account record (e.g. as a 'password' attribute).
     */
    public String encryptPassword(String plaintextPassword, String salt) throws IllegalArgumentException {
        return encrypt(plaintextPassword, salt, hashIterations);
    }

    /**
     * 对纯文本根据组件配置的algorithmName算法进行加密，可指定salt和hash计算次数以增加安全性
     *
     * @param plaintext
     * @param salt
     * @param hashIterations
     * @throws IllegalArgumentException
     */
    public String encrypt(String plaintext, String salt, int hashIterations) throws IllegalArgumentException {
        Assert.hasText(plaintext, "Plain text password name cannot be empty.");
        if (hashIterations < 0) {
            hashIterations = this.hashIterations; // 使用默认的hash迭代次数
        }
        byte[] bytes = plaintext.getBytes();
        byte[] hashedBytes = hash(bytes, salt, hashIterations);
        return Hex.encodeToString(hashedBytes);
    }

    /**
     * Returns {@code true} if the {@code submittedPlaintext} password matches
     * the existing {@code saved} password, {@code false} otherwise.
     */
    public boolean passwordsMatch(String submittedPlaintext, String salt, String encrypted) {
        String encryptdPassword = encryptPassword(submittedPlaintext, salt);
        return encrypted.endsWith(encryptdPassword);
    }

    private String getUsedAlgorithm() {
        if (StringUtils.isNotBlank(this.algorithmName)) {
            return this.algorithmName;
        } else {
            return DEFAULT_HASH_ALGORITHM;
        }
    }

    protected byte[] hash(byte[] bytes, String salt, int hashIterations) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(getUsedAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            logger.error("Not supported algorithm {} error.", getUsedAlgorithm(), e);
            throw new ApplicationException(CommonErrorCode.SYS_ERROR, "不支持的算法" + getUsedAlgorithm(), e);
        }
        if (StringUtils.isNotBlank(salt)) {
            digest.reset();
            digest.update(salt.getBytes());
        }
        byte[] hashed = digest.digest(bytes);
        int iterations = hashIterations - 1; //already hashed once above
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return hashed;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

}
