package HomomorphicEncryption;

import java.math.BigInteger;
import java.util.Vector;
//정렬: command + alt+ l

public class HomomorphicEncryption {
    public static KGC kgc;
    public static Data d1;
    public static Data d2;
    public static Data d3;

    public static void main(String[] args) {
        Vector<Boolean> ret = new Vector<>();
        //0) parameter 세팅(현재는 디폴트) -> KGC
        //1) 공개키 쌍 만들기
        kgc = new KGC(new BigInteger("50"));
        //2) user 만들기
        for(int i = 0;i<100;i++) {
            User userA = new User(kgc.pkSet);
            User userB = new User(kgc.pkSet);
//
////      data로 넘겨주는 pk는 생성자내에서 생성하는 것으로 변경
            d1 = new Data(userA, new BigInteger(UseBiginteger.StringToHex("염"), 16), kgc.a, kgc.pkSet);
            d2 = new Data(userB, new BigInteger(UseBiginteger.StringToHex("염"), 16), kgc.a, kgc.pkSet);

            ret.add(test());
        }

        System.out.println(ret);

        if(ret.contains(false))
            if(!ret.contains(true))
                System.out.println("결과값에 false만 있음");
            else
                System.out.println("결과값에 true와 false 있음");
        else
            System.out.println("결과값에 true만 있음");

    }

    public static Boolean test(){
        System.out.println("c1 mod p = " + d1.c1.mod(kgc.p));
        System.out.println("c1 mod a = " + d1.c1.mod(kgc.a));
        System.out.println("c1 mod p mod a = " + d1.c1.mod(kgc.p).mod(kgc.a));
        System.out.println("c1 mod p = " + d2.c1.mod(kgc.p));
        System.out.println("c1 mod a = " + d2.c1.mod(kgc.a));
        System.out.println("c1 mod p mod a = " + d2.c1.mod(kgc.p).mod(kgc.a));

        BigInteger parent = hash((d1.c1.mod(kgc.p).mod(kgc.a)));
        parent = parent.multiply(d2.c2);
        BigInteger child = hash((d2.c1.mod(kgc.p).mod(kgc.a)));
        child = child.multiply(d1.c2);
        System.out.println(parent);
        System.out.println(child);

        return parent.subtract(child) == BigInteger.valueOf(0) ? true : false;
    }

    public static BigInteger hash(BigInteger exponent){ //data에도 있는데 하나로 하는 방법을 모르겠음!
        return new BigInteger("2").pow(exponent.intValue()); //(의문)mod ? 를 해야할까
    }

}