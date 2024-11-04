package com.photo.photography.duplicatephotos.files;

//import com.facebook.appevents.AppEventsConstants;

import com.photo.photography.duplicatephotos.common.CommonUsed;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5ChecksumFile {
    public byte[] createChecksum(String str) throws Exception {
        int read;
        FileInputStream fileInputStream = new FileInputStream(str);
        byte[] bArr = new byte[1024];
        MessageDigest instance = MessageDigest.getInstance("MD5");
        do {
            read = fileInputStream.read(bArr);
            if (read > 0) {
                instance.update(bArr, 0, read);
            }
        } while (read != -1);
        fileInputStream.close();
        return instance.digest();
    }

    public String getMD5Checksum(String str) throws Exception {
        byte[] createChecksum = createChecksum(str);
        String str2 = "";
        for (int i = 0; i < createChecksum.length; i++) {
            str2 = str2 + Integer.toString((createChecksum[i] & 255) + 256, 16).substring(1);
        }
        CommonUsed.logmsg("Checksum: " + str2);
        return str2;
    }

    public String getMD5EncryptedString(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
            messageDigest = null;
        }
        messageDigest.update(str.getBytes(), 0, str.length());
        String bigInteger = new BigInteger(1, messageDigest.digest()).toString(16);
        while (bigInteger.length() < 32) {
            bigInteger = /*AppEventsConstants.EVENT_PARAM_VALUE_NO*/0 + bigInteger;
        }
        CommonUsed.logmsg("Checksum: " + bigInteger);
        return bigInteger;
    }

    public String getMd5OfFile(String str) {
        String str2 = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[1024];
            MessageDigest instance = MessageDigest.getInstance("MD5");
            int i = 0;
            while (i != -1) {
                i = fileInputStream.read(bArr);
                if (i > 0) {
                    instance.update(bArr, 0, i);
                }
            }
            fileInputStream.close();
            byte[] digest = instance.digest();
            for (int i2 = 0; i2 < digest.length; i2++) {
                str2 = str2 + Integer.toString((digest[i2] & 255) + 256, 16).substring(1);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            CommonUsed.logmsg("Md5 exception:  ----" + th.getMessage());
        }
        return str2.toUpperCase();
    }

    public String checksum(File file) {
        int read;
        int i;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] bArr = new byte[1024];
            do {
                read = fileInputStream.read(bArr);
                if (read > 0) {
                    instance.update(bArr, 0, read);
                }
            } while (read != -1);
            fileInputStream.close();
            byte[] digest = instance.digest();
            if (digest == null) {
                return null;
            }
            String str = "0x";
            for (i = 0; i < digest.length; i++) {
                str = str + Integer.toString((digest[i] & 255) + 256, 16).substring(1).toUpperCase();
            }
            return str;
        } catch (Exception unused) {
            return null;
        }
    }

    public String fileToMD5(String str) {
        Throwable th;
        FileInputStream fileInputStream;
        Exception e;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                byte[] bArr = new byte[1024];
                MessageDigest instance = MessageDigest.getInstance("MD5");
                int i = 0;
                while (i != -1) {
                    i = fileInputStream.read(bArr);
                    if (i > 0) {
                        instance.update(bArr, 0, i);
                    }
                }
                String convertHashToString = convertHashToString(instance.digest());
                try {
                    fileInputStream.close();
                } catch (Exception e2) {
                    CommonUsed.logmsg("Exception in reading md5---2: " + e2.getMessage());
                }
                return convertHashToString;
            } catch (Exception e3) {
                e = e3;
                try {
                    CommonUsed.logmsg("Exception in reading md5---1: " + e.getMessage());
                    if (fileInputStream != null) {
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e4) {
                            CommonUsed.logmsg("Exception in reading md5---2: " + e4.getMessage());
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e5) {
            e = e5;
            fileInputStream = null;
            CommonUsed.logmsg("Exception in reading md5---1: " + e.getMessage());
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e6) {
                    CommonUsed.logmsg("Exception in reading md5---2: " + e6.getMessage());
                }
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            if (fileInputStream != null) {
            }
//            throw th;
            return "";
        }
    }

    private String convertHashToString(byte[] bArr) {
        String str = "";
        for (int i = 0; i < bArr.length; i++) {
            str = str + Integer.toString((bArr[i] & 255) + 256, 16).substring(1);
        }
        return str;
    }
}
