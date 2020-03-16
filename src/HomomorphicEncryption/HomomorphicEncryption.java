package HomomorphicEncryption;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;
//정렬: command + alt+ l

public class HomomorphicEncryption {
    public static KGC kgc;


    public static String message2 = "";

    public static void main(String[] args) {

        File file = new File("test1.txt");
        FileWriter writer = null;

        try {
            // 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
            writer = new FileWriter(file, true);

            for(int j = 0 ;j<1;j++) {
                Vector<Boolean> ret = new Vector<>();
                //0) parameter 세팅(현재는 디폴트) -> KGC
                //1) 공개키 쌍 만들기
                kgc = new KGC();

                //2) user 만들기
                BigInteger rnum = new BigInteger("10");
                BigInteger rnum1 = rnum.add(BigInteger.ONE);
                User userA = new User(kgc.pkSet, kgc.temp);
                User userB = new User(kgc.pkSet, kgc.temp);

                Data d1 = new Data(userA, rnum, kgc.a, kgc.pkSet);
                Data d2 = new Data(userB, rnum, kgc.a, kgc.pkSet);
                d2 = new Data(userA, rnum, kgc.a, kgc.pkSet);
                for (int i = 0; i < 1; i++) {
                    message2 = "";

////      data로 넘겨주는 pk는 생성자내에서 생성하는 것으로 변경


                    message2 += ("\n\n-------------------------------------------------------"
                            + "\nkgc.a = " + kgc.a + ", kgc.p = " + kgc.p + "\nx0 = " + kgc.pkSet.get(0) + "\n");

                    message2 += ("\nw = " + rnum + "\n");

                    message2 += ("\nuser1.qid = " + userA.qid + ", user1.ri = " + userA.r + "\n user1.riArray = " + userA.rArray
                            + "\n user1.pkArray = " + userA.pkArray
                            +"\nuser2.qid = " + userB.qid + ", user2.ri = " + userB.r + "\n user2.riArray = " + userB.rArray
                            + "\n user2.pkArray = " + userB.pkArray
                            +"\n\nuser1.c1 (c1.mod x0) = " + d1.c1 + "\nuser1.c2 (H(riqid)) = " + d1.c2);

                    message2 += ("\n\nuser2.c1 (c1.mod x0) = " + d2.c1 + "\nuser2.c2 (H(riqid)) = " + d2.c2);

                    boolean result = test(d1,d2);
                    if (result == true) {
                        writer.write(message2);
                        writer.flush();
                    }
                    ret.add(result);
                }

                System.out.println(ret);

                System.out.println(rnum + "에 대한 검색 결과");
                System.out.println("p = " + kgc.p + ", a = " + kgc.a + ", xo = " + kgc.pkSet.get(0));
                System.out.println(kgc.pkSet.get(0).mod(kgc.p) + ", " + kgc.pkSet.get(0).mod(kgc.a));
                if (ret.contains(false))
                    if (!ret.contains(true))
                        System.out.println("결과값에 false만 있음");
                    else
                        System.out.println("결과값에 true와 false 있음");
                else
                    System.out.println("결과값에 true만 있음");

            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static BigInteger hash(BigInteger exponent){ //data에도 있는데 하나로 하는 방법을 모르겠음!
        //return new BigInteger("2").pow(exponent.intValue()); //(의문)mod ? 를 해야할까
        return exponent;
    }

    public static Boolean test(Data d1, Data d2){
        BigInteger parent;
        if(d1.c1.mod(kgc.p).compareTo(kgc.p.divide(BigInteger.TWO))>0) {
            parent = d1.c1.mod(kgc.p).subtract(kgc.p);
        }
        else {
            parent = d1.c1.mod(kgc.p);
        }
        System.out.println("parent: c1 mod p = " + parent.toString(16));


        message2 += ("\n\nuser1" +"\nuser1.c1 mod p = " + parent
                +"\nuser1.c1 mod p mod 2 = " + parent.mod(BigInteger.TWO));
        parent = hash(parent);
        //       BigInteger parent = hash((d1.c1.mod(kgc.p).mod(kgc.a)));


        message2 += ("\nH(user1.c1 mod p) = " + parent);

       // parent = parent.multiply(d2.c2);
        parent = parent.add(d2.c2);

//        if(!parent.divide(BigInteger.TWO.pow(kgc.a.intValue())).equals(BigInteger.ZERO))
//            parent = parent.divide(BigInteger.TWO.pow(kgc.a.intValue()));
        message2 += ("\n위의 결과 / : "+parent);


        BigInteger child;
        if(d2.c1.mod(kgc.p).compareTo(kgc.p.divide(BigInteger.TWO))>0) {
            child = d2.c1.mod(kgc.p).subtract(kgc.p);
        }
        else {
            child = d2.c1.mod(kgc.p);
        }
        System.out.println("chlid의 c1 mod p = " + child.toString(16));
        System.out.println("chlid의 c1 mod p mod 2 = " + child.mod(BigInteger.TWO));

        message2 += ("\n\nuser1" +"\nuser1.c1 mod p = " + child
                +"\nuser1.c1 mod p mod 2 = " + child.mod(BigInteger.TWO));

        child = hash(child);
        //       BigInteger parent = hash((d1.c1.mod(kgc.p).mod(kgc.a)));


        message2 += ("\nH(user1.c1 mod p) = " + child);

       // child = child.multiply(d1.c2);

        child = child.add(d1.c2);

//        if(!child.divide(BigInteger.TWO.pow(kgc.a.intValue())).equals(BigInteger.ZERO))
//            child = child.divide(BigInteger.TWO.pow(kgc.a.intValue()));
        message2 += ("\n위의 결과 : "+child);

//        if(parent.equals(child))
//            return true;
//        else if(parent.divide(child).equals(hash(kgc.p.mod(kgc.a))))
//            return true;
//        else if(child.divide(parent).equals(hash(kgc.p.mod(kgc.a))))
//            return true;
//        else return false;

        System.out.println(parent.toString(16));
        System.out.println(child.toString(16));

        return parent.subtract(child) == BigInteger.valueOf(0) ? true : false;
    }

}