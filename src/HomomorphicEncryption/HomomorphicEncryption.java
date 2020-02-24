package HomomorphicEncryption;

import java.math.BigInteger;
//정렬: command + alt+ l

public class HomomorphicEncryption {
    public static KGC kgc;
    public static Data d1;
    public static Data d2;
    public static Data d3;

    public static void main(String[] args) {
        //0) parameter 세팅(현재는 디폴트) -> KGC
        //1) 공개키 쌍 만들기
        kgc = new KGC(new BigInteger("5"));
        //2) user 만들기
           User userA = new User(kgc.pkSet);
           User userB = new User(kgc.pkSet);
//
////      data로 넘겨주는 pk는 생성자내에서 생성하는 것으로 변경
        d1 = new Data(userA,BigInteger.valueOf(130),kgc.a, kgc.pkSet);
        d2 = new Data(userB,BigInteger.valueOf(130),kgc.a, kgc.pkSet);

        System.out.println(test());
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