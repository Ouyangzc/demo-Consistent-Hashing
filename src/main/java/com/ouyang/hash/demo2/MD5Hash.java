package com.ouyang.hash.demo2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MD5Hash
 * @Description md5 hash
 * 在一致性哈希算法中，使用MD5哈希算法的主要原因是它的分布性和均匀性较好，能够有效地在哈希环上分布数据节点，从而减小数据迁移的幅度。
 * 以下是为什么在一致性哈希算法中推荐使用MD5哈希算法的一些原因：
 * 均匀性： MD5算法将任意长度的输入数据映射到固定长度的输出（128位），在输出空间内分布比较均匀。这有助于在哈希环上均匀地分布数据节点，减小节点之间的数据倾斜程度。
 * 无序性： MD5算法生成的哈希值在输出空间内是近似无序的，即相邻输入的哈希值在输出空间内没有明显的关联。这有助于在哈希环上分布数据节点，不易产生热点问题。
 * 随机性： MD5算法的输出看起来是随机的，即使输入数据只有微小的变化，输出也会产生较大的差异。这有助于在数据节点的分布中增加随机性，减小节点之间的重叠。
 * 不可逆性： 一致性哈希算法中使用哈希值作为节点标识，而不需要反向还原到原始数据。MD5的不可逆性保证了节点标识的唯一性。
 * MD5在一致性哈希算法中有其优势，但也要考虑到其安全性问题。在安全性要求较高的场景，可以考虑使用更安全的哈希算法，如SHA-256。
 * @Author OuYang
 * @Date 2023/8/21 10:31
 * @Version 1.0
 */
public class MD5Hash implements HashFunction {

    MessageDigest instance;

    public MD5Hash() {
        try {
            instance = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    public long hash(String key) {
        //充值MessageDigest状态
        instance.reset();
        //将key的字节数组传入
        instance.update(key.getBytes());
        //获取MD5哈希值的字节数组
        byte[] digest = instance.digest();

        long h = 0;
        //取前4位，转换成32位整数
        for (int i = 0; i < 4; i++) {
            h <<= 8;
            h |= ((int) digest[i]) & 0xFF;
        }
        return h;
    }

    public static void main(String[] args) {
        MD5Hash md5Hash = new MD5Hash();
        long hash = md5Hash.hash("192.168.60.49#1");
        System.out.println(hash);
    }
}
