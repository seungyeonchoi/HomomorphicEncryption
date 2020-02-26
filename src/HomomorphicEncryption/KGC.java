package HomomorphicEncryption;

import java.util.Random;
import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;
import java.math.BigInteger;


/* 20200222 수정사항
 * w + riqid < a <p 해당 항목들 모두 다 biginteger
 * 기존에서 변경된 부분
 * 1. a,p,
 * kgc부분 biginteger로 수정 및 계산 가능하게 변경
 */
public class KGC {
    Random r = new Random();

    public static BigInteger lamda = new BigInteger("5"); //한글 한글자로 test할려면 최소 5이성
    public static BigInteger eta = new BigInteger("20"); //원래 조건 -> (int)(Math.random()*Math.pow(lamda,2)), 개인키의 길이
    public static BigInteger gamma = new BigInteger("25"); //원래 조건 -> (int)(Math.random()*Math.pow(lamda,5));
    public static BigInteger pkSetSize = new BigInteger("50");//감마 + 람다 (but 너무 커서 일단 감마^2+람다로)

    public static BigInteger p;
    public static BigInteger a;
    //public static int p = 327; //secret key (256 ~ 512)
    //public static int a = 13; //
    public static Vector<BigInteger> pkSet = new Vector<>();

    public KGC(){
        this(pkSetSize);
    }

    public KGC(BigInteger pkSetSize){
        this.pkSetSize = pkSetSize;
        makePublicKeySet(this.pkSetSize);
    }

    public KGC(BigInteger pkSetSize, BigInteger lamda, BigInteger gamma, BigInteger p, BigInteger a){
        this.pkSetSize = pkSetSize;
        this.lamda = lamda;
        this.gamma = gamma;
        this.p = p;
        this.a = a;
        makePublicKeySet(this.pkSetSize);
    }

    private void makePublicKeySet(BigInteger pkSetSize) {
    /*
        Xi = p*qi + ri
        qi = 0 ~ (2^lamda)/2 // 0~ any integer
        ri = - (2^lamda) ~ (2^lamda)
         */
        pkSet.clear(); //pkSet 초기화

        //p = 2의 19승 + 18자리 랜덤 값의 다음 소수 , 2의 19승 = 524288
        p = new BigInteger("100000000000000000000000000000000000").add(new BigInteger(38,r)).nextProbablePrime();//최대 2^39
        //a = 2의 18승 + 17자리 랜덤 값 , 2의 18승 = 262144
        a = new BigInteger("100000000000000000",2).add(new BigInteger(18,r)).nextProbablePrime();//최대 2^19

        System.out.println("p = " + p + ", a = " + a );
        System.out.println("p(2) = " + p.toString(2) + ", a(2) = " + a.toString(2));
        System.out.println("p(2) = " + p.bitLength() + ", a(2) = " + a.bitLength());

        Vector<BigInteger> qi = new Vector<>();
        BigInteger qMax = new BigInteger("2").pow(lamda.pow(4).intValue()).divide(p);
        System.out.println("qMax = " + qMax.toString() + "qMax.bitLength() = " + qMax.bitLength());

        for (int i = 0; i < pkSetSize.intValue(); i++) {
            qi.add(new BigInteger(qMax.bitLength()-1,r));// 0~ 2^(lamda)/p
            for (int j = i-1; j >= 0; j--) { //중복제거
                if (qi.get(j) == qi.get(i)) {
                    i--;
                    continue;
                }
            }
        }
        System.out.println("qi = " + qi);

        Vector<BigInteger> ri = new Vector<>();
        BigInteger rMax = new BigInteger("2").pow(lamda.intValue()).multiply(BigInteger.valueOf(2));
        System.out.println("rMax = " + rMax.toString());

        for (int i = 0; i < pkSetSize.intValue(); i++) {
            ri.add(new BigInteger("2").pow(lamda.intValue()).multiply(BigInteger.valueOf(-1))
                    .add(new BigInteger(rMax.bitLength()-1,r)));
//            ri.add(new BigInteger(rMax.bitLength()-2,r));
            for (int j = i-1; j >= 0; j--)  { //중복제거
                if (ri.get(j) == ri.get(i)) {
                    i--;
                }
            }
        }
        System.out.println("ri = " + ri);
//        int rMax = 2*((int)Math.pow(2,lamda));
//        for (int i = 0; i < pkSetSize; i++) {
//            ri[i] = -((int)Math.pow(2,lamda))+ (int)(Math.random()*rMax); // 0~ 2^(lamda)/p
//            for (int j = i-1; j >= 0; j--)  { //중복제거
//                if (ri[j] == ri[i]) {
//                    i--;
//                }
//            }
//                   }

//        int [] qi = {1,2,2,32,46};
//        int [] ri = {2,-2,1,1,13};
        for(int i = 0; i<pkSetSize.intValue(); i++){
            pkSet.add(p.multiply(qi.get(i)).add(ri.get(i)));
        }
        System.out.println(pkSet);

        Collections.sort(pkSet, Comparator.reverseOrder()); //X0 is the largest element
        if (pkSet.get(0).mod(p) != a){ //X0 mod p = a 조건 체크
            BigInteger rest = p.subtract(pkSet.get(0).mod(p).subtract(a));
            BigInteger x0 = pkSet.get(0).add(rest);
            while (x0.mod(a) == BigInteger.valueOf(0)){ //x0 mod a =0 이면, x0 값 증가시킴 //근데 따져보니까 (p/a)*x =(k -1 ) 을 만족하는 x와 k가 있으면, 이 조건이 성립하는데 아마 그런경우가 많이 없을듯해 !
                x0 = x0.add(p);
            }
            System.out.println(x0);
            pkSet.set(0,x0);

        }
        System.out.println("x0 mod p" + pkSet.get(0).mod(p));
        System.out.println("xo mod a != 0 " + pkSet.get(0).mod(a)+"xo mod p = a : "+ pkSet.get(0).mod(p));
        System.out.println(pkSet);
    }
}
