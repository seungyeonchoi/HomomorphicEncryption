package HomomorphicEncryption;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Random;
import java.util.Vector;

//정렬: command + alt+ l

public class HomomorphicEncryption {

    public static KGC kgc;
    public static Server server;
    public static Data d1;
    public static Data d2;
    public static BigInteger rnum = new BigInteger(SHA1("최승연"), 16);
    public static BigInteger rnum1 = rnum.add(BigInteger.ONE);
    public static String message2 = "";

    public static void main(String[] args) {

        kgc = new KGC();
        server = new Server(kgc.getP(),kgc.getA());
        User userA = new User(kgc.pkSet);
        userA.setAu(kgc.shareAlpha()); //kgc -> user에 alpha 공유 (임의로)
        uploadFile(userA,"염상희");

//        File file = new File("test1.txt");
//        FileWriter writer = null;
//
//        try {
//            // 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
//            writer = new FileWriter(file, true);
//
//            for(int j = 0 ;j<1;j++) {
//                Vector<Boolean> ret = new Vector<>();
//                //0) parameter 세팅(현재는 디폴트) -> KGC
//                //1) 공개키 쌍 만들기
//                kgc = new KGC();
//
//                //2) user 만들기
//                Random r = new Random();
//
//
//                //rnum = BigInteger.ZERO;
//                for (int i = 0; i < 1; i++) {
//                    message2 = "";
//                    User userA = new User(kgc.pkSet);
//                    userA.setAu(kgc.shareAlpha());//user에 alpha 공유 (임의로)
//
//                    User userB = new User(kgc.pkSet);
//                    userB.setAu(kgc.getA());
//
//////      data로 넘겨주는 pk는 생성자내에서 생성하는 것으로 변경
//                    d1 = new Data(userA, rnum,userA.getAu(),kgc.pkSet);
//                    d2 =  new Data(userB, rnum1,kgc.getA(),kgc.pkSet);
////                    d1 = new Data(userA, rnum, kgc.pkSet.firstElement());
////                    d2 =  new Data(userB, rnum, kgc.pkSet.firstElement());
//                    BigInteger res = d1.c1.mod(kgc.getP()).compareTo(kgc.getP().divide(BigInteger.TWO))>0 ? d1.c1.mod(kgc.getP()).subtract(kgc.getP()) : d1.c1.mod(kgc.getP());
//                    d1.c1 = d1.makeCi(res,kgc.getA());
//                    res = d2.c1.mod(kgc.getP()).compareTo(kgc.getP().divide(BigInteger.TWO))>0 ? d2.c1.mod(kgc.getP()).subtract(kgc.getP()) : d2.c1.mod(kgc.getP());
//
//                    d2.c1 = d2.makeCi(res,kgc.getA());
////                    message2 += ("\n\n-------------------------------------------------------"
////                            + "\nkgc.a = " + kgc.a + ", kgc.p = " + kgc.p + "\nx0 = " + kgc.pkSet.get(0) + "\n");
////                    message2 += ("\nw = " + rnum + "\n");
////                    message2 += ("\nuser1.qid = " + userA.qid + ", user1.ri = " + userA.r + "\n user1.riArray = " + userA.rArray
////                            + "\n user1.pkArray = " + userA.pkArray
////                            +"\nuser2.qid = " + userB.qid + ", user2.ri = " + userB.r + "\n user2.riArray = " + userB.rArray
////                            + "\n user2.pkArray = " + userB.pkArray
////                            +"\n\nuser1.c1 (c1.mod x0) = " + d1.c1 + "\nuser1.c2 (H(riqid)) = " + d1.c2);
////                    message2 += ("\n\nuser2.c1 (c1.mod x0) = " + d2.c1 + "\nuser2.c2 (H(riqid)) = " + d2.c2);
//
//                  //  boolean result = test();
//                    boolean result = test(d1.c1, d1.c2, d2.c1, d2.c2);
//                    if (result == false) {
//                        writer.write(message2);
//                        writer.flush();
//                    }
//                    ret.add(result);
//                }
//
//                System.out.println(ret);
//                System.out.println(rnum + "에 대한 검색 결과");
//                System.out.println("p = " + kgc.getP() + ", a = " + kgc.getA() + ", xo = " + kgc.pkSet.get(0).pk);
//                System.out.println(kgc.pkSet.get(0).pk.mod(kgc.getP()) + ", " + kgc.pkSet.get(0).pk.mod(kgc.getA()));
//                if (ret.contains(false))
//                    if (!ret.contains(true))
//                       System.out.println("결과값에 false만 있음");
//                    else
//                        System.out.println("결과값에 true와 false 있음");
//                else
//                    System.out.println("결과값에 true만 있음");
//            }
//        } catch(IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(writer != null) writer.close();
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
    public static void uploadFile(User user, String keyword ){ //소유자 user, , (파일을 나타내는 키워드 -> 이름(상호명)
        Data data = new Data(user, new BigInteger(SHA1(keyword), 16));
        server.uploadFile(data);
    }

    public static BigInteger hash(BigInteger exponent){ //data에도 있는데 하나로 하는 방법을 모르겠음!
        //return new BigInteger("2").pow(exponent.intValue()); //(의문)mod ? 를 해야할까
        return exponent;
    }

    public static Boolean test(){

        System.out.println("c1 mod a = " + d1.c1.mod(kgc.getA()));
        System.out.println("c1 mod a = " + d2.c1.mod(kgc.getA()));

        BigInteger parent;
        if(d1.c1.mod(kgc.getP()).compareTo(kgc.getP().divide(BigInteger.TWO))>0) {
            parent = d1.c1.mod(kgc.getP()).subtract(kgc.getP());
        }
        else {
            parent = d1.c1.mod(kgc.getP());
        }
        System.out.println("c1 mod p = " + parent);
        System.out.println("parent의 c1 mod p mod a = " + parent.mod(kgc.getA()));

        message2 += ("\n\nuser1" +"\nuser1.c1 mod p = " + parent
                +"\nuser1.c1 mod p mod a = " + parent.mod(kgc.getA()));
        parent = hash(parent.mod(kgc.getA()));

        message2 += ("\nH(user1.c1 mod p mod a) = " + parent);
        parent = parent.add(d2.c2);

        message2 += ("\n위의 결과 /2^kgc.getA() = "+parent);
        BigInteger child;
        if(d2.c1.mod(kgc.getP()).compareTo(kgc.getP().divide(BigInteger.TWO))>0) {
            child = d2.c1.mod(kgc.getP()).subtract(kgc.getP());
        }
        else {
            child = d2.c1.mod(kgc.getP());
        }
        System.out.println("c1 mod p = " + child);
        System.out.println("chlid의 c1 mod p mod a = " + child.mod(kgc.getA()));

        message2 += ("\n\nuser1" +"\nuser1.c1 mod p = " + child
                +"\nuser1.c1 mod p mod a = " + child.mod(kgc.getA()));

        child = hash(child.mod(kgc.getA()));

        message2 += ("\nH(user1.c1 mod p mod a) = " + child);

        child = child.add(d1.c2);
        message2 += ("\n위의 결과 /2^kgc.getA() = "+child);

        System.out.println(parent);
        System.out.println(child);

        return parent.subtract(child).equals(BigInteger.ZERO);
    }
    public static Boolean test(BigInteger Ci1, BigInteger Ci2, BigInteger Cj1, BigInteger Cj2){
       //분모
        BigInteger parent = Ci1.mod(kgc.getP()).compareTo(kgc.getP().divide(BigInteger.TWO))>0 ? Ci1.mod(kgc.getP()).subtract(kgc.getP()) : Ci1.mod(kgc.getP());
        parent = hash(parent.mod(kgc.getA()));
        System.out.println("H(Ci1 mod p mod a)(2^hexadecimal): 2^" + parent.toString(16));
        if(parent.equals(rnum.add(d1.getUser().qid.multiply(d1.getUser().r)))){
            System.out.println("d1 일치");
        }
        parent = parent.add(Cj2);

        //분자
        BigInteger child = Cj1.mod(kgc.getP()).compareTo(kgc.getP().divide(BigInteger.TWO))>0 ? Cj1.mod(kgc.getP()).subtract(kgc.getP()) : Cj1.mod(kgc.getP());
        child = hash(child.mod(kgc.getA()));

        System.out.println("H(Cj1 mod p mod a)(2^hexadecimal) : 2^" + child.toString(16));
        if(child.equals(rnum.add(d2.getUser().qid.multiply(d2.getUser().r)))){
            System.out.println("d2 일치");
        }
        //message2 += ("\nH(user1.c1 mod p mod a) = " + child);
        // child = child.multiply(d1.c2);
        child = child.add(Ci2);
//        message2 += ("\n위의 결과 /2^kgc.getA() = "+child);
        System.out.println();
        System.out.println("H(Ci1 mod p mod a)*Cj2(2^hexadecimal) : 2^" +  parent);
        System.out.println("H(Cj1 mod p mod a)*Ci2(2^hexadecimal) : 2^" + child);
        return parent.subtract(child).equals(BigInteger.ZERO);
    }

    public static String SHA1(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1"); // 이 부분을 SHA-256, MD5로만 바꿔주면 된다.
            md.update(str.getBytes()); // "세이프123"을 SHA-1으로 변환할 예정!

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<byteData.length; i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "fail";
    }
}