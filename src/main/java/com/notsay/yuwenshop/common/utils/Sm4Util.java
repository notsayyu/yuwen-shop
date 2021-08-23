package com.notsay.yuwenshop.common.utils;

import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;


/**
 * @description:
 * @author: dsy
 * @date: 2020/7/30 11:08
 */
public class Sm4Util {
    private static final int DEFAULT_KEY_SIZE = 128;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    private static final String ENCODING = "UTF-8";

    public static final String ALGORITHM_NAME = "SM4";
    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding

    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
// 128-32位16进制；256-64位16进制

    /**
     * 生成CBC暗号
     *
     * @explain CBC模式
     */

    //生成iv
    public static AlgorithmParameters generateIV() throws Exception {
        //iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance(ALGORITHM_NAME);
        params.init(new IvParameterSpec(iv));
        return params;
    }


    private static Cipher generateCbcCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key, generateIV());
        sm4Key = null;
        mode = 0;
        algorithmName = "";
        return cipher;
    }

    /**
     * sm4加密
     *
     * @return 返回16进制的加密字符串
     * @throws Exception
     * @explain 加密模式：CBC
     */
    public static String protectMsg(String key, String paramStr) {
        try {
            String result = "";
            // 16进制字符串-->byte[]
            byte[] keyData = ByteUtils.fromHexString(key);
            // String-->byte[]
            byte[] srcData = paramStr.getBytes(ENCODING);
            paramStr = null;
            // 加密后的数组
            byte[] cipherArray = encrypt_Cbc_Padding(keyData, srcData);
            Arrays.fill(keyData, (byte) 0);
            Arrays.fill(srcData, (byte) 0);

            // byte[]-->hexString
            result = ByteUtils.toHexString(cipherArray);
            Arrays.fill(cipherArray, (byte) 0);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(Code.SYSTEM_ERROR, "sm4加密出错");
        }

    }


    /**
     * 加密模式之CBC
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] encrypt_Cbc_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * sm4解密
     *
     * @return 解密后的字符串
     * @throws Exception
     * @explain 解密模式：采用CBC
     * 16进制密钥
     * 16进制的加密字符串（忽略大小写）
     */
    public static String uncoverMsg(String key, String text) {
        try {
            // 用于接收解密后的字符串
            String result = "";
            // hexString-->byte[]
            byte[] keyData = ByteUtils.fromHexString(key);
            // hexString-->byte[]
            byte[] resultData = ByteUtils.fromHexString(text.toString());
            text = null;
            // 解密
            byte[] srcData = decrypt_Cbc_Padding(keyData, resultData);
            Arrays.fill(keyData, (byte) 0);
            Arrays.fill(resultData, (byte) 0);

            // byte[]-->String
            result = new String(srcData, ENCODING);
            Arrays.fill(srcData, (byte) 0);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(Code.SYSTEM_ERROR, "sm4解密出现错误");
        }

    }

    /**
     * 解密
     *
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] decrypt_Cbc_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * 自动生成密钥
     *
     * @return
     * @explain
     */

    public static String generateKey() {
        return generateKey(DEFAULT_KEY_SIZE);

    }

    public static String generateKey(int keySize) {
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(Code.SYSTEM_ERROR, "生成sm4秘钥出现错误");
        }

        kg.init(keySize, new SecureRandom());

        return ByteUtils.toHexString(kg.generateKey().getEncoded());
    }

    public static void main(String[] args) {
        String msg = "杭州趣链科技";
        String text = protectMsg("91c378dabb2bef2e95271ef6df96aa1d", msg);
        System.out.println(text);
        System.out.println(uncoverMsg("91c378dabb2bef2e95271ef6df96aa1d", text));
    }
}
