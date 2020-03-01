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

    public static BigInteger lamda = new BigInteger("24"); //한글 한글자로 test할려면 최소 5이성
    public static BigInteger eta = new BigInteger("260"); //원래 조건 -> (int)(Math.random()*Math.pow(lamda,2)), 개인키의 길이
    public static BigInteger gamma = new BigInteger("343"); //원래 조건 -> (int)(Math.random()*Math.pow(lamda,5)) -> 현재 lamda^3
    public static BigInteger pkSetSize = new BigInteger("350");//감마 + 람다 (but 너무 커서 일단 감마^3+람다로)

    public static BigInteger p; // 비트의 수가 eta (lamda^2)
    public static BigInteger a;
    //public static int p = 327; //secret key (256 ~ 512)
    //public static int a = 13; //
    public static Vector<BigInteger> pkSet = new Vector<>();

    public static Vector<Vector<BigInteger>> temp = new Vector<>();

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

        //p = 2의 38승 + 자리 랜덤 값의 다음 소수 , 2의 48( O(lamda^2) = O(eta) ), 19+19+9 (360 = subset 개수)
        p = new BigInteger("10",16).add(new BigInteger(9,r)).nextProbablePrime();//최대 2^39
        //a = 2의 18승 + 18자리 랜덤 값 , 2의 19승
        while (p.bitLength() != eta.intValue()){  //만약 찾은 소수가 lamda^2 의 len 보다 크면 같을 때 까지 다시 뽑기
            p = new BigInteger(eta.intValue(),r).nextProbablePrime();
        }
        a = new BigInteger("10",16).add(new BigInteger(6,r)).nextProbablePrime();//최대 2^19
        while (a.bitLength() != lamda.intValue()){  //만약 찾은 소수가 lamda^2 의 len 보다 크면 같을 때 까지 다시 뽑기
            a = new BigInteger(lamda.intValue(),r).nextProbablePrime();
        }
        //a = BigInteger.TWO;

        System.out.println("p = " + p + ", a = " + a );
        System.out.println("p(2) = " + p.toString(2) + ", a(2) = " + a.toString(2));
        System.out.println("p(2) = " + p.bitLength() + ", a(2) = " + a.bitLength());


        Vector<BigInteger> qi = new Vector<>();
        System.out.println(new BigInteger("2").pow(gamma.intValue()) + ", bit length " + new BigInteger("2").pow(gamma.intValue()).bitLength());
        BigInteger qMax = new BigInteger("2").pow(gamma.intValue()).divide(p);
        System.out.println("qMax = " + qMax.toString() + "qMax.bitLength() = " + qMax.bitLength());

        for (int i = 0; i < pkSetSize.intValue(); i++) {
            qi.add(new BigInteger(qMax.bitLength()-1,r));// 0~ 2^(lamda)/p
//            for (int j = i-1; j >= 0; j--) { //중복제거
//                if (qi.get(j) == qi.get(i)) {
//                    i--;
//                    continue;
//                }
//            }
        }
        System.out.println("qi = " + qi);

        Vector<BigInteger> ri = new Vector<>();
        BigInteger rMax = new BigInteger("2").pow(lamda.intValue()).multiply(BigInteger.valueOf(2));
        System.out.println("rMax = " + rMax.toString());

        for (int i = 0; i < pkSetSize.intValue(); i++) {
            ri.add(new BigInteger(lamda.intValue()-1,r).multiply(new BigInteger("-1").pow(new BigInteger(1,r).intValue())));
//            ri.add(new BigInteger("2").pow(lamda.intValue()).multiply(BigInteger.valueOf(-1)).add(BigInteger.ONE)
//                    .add(new BigInteger(rMax.bitLength()-1,r)));
//            ri.add(new BigInteger(rMax.bitLength()-2,r));
//            for (int j = i-1; j >= 0; j--)  { //중복제거
//                if (ri.get(j) == ri.get(i)) {
//                    i--;
//                }
//            }
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
            System.out.println(p.multiply(qi.get(i)).add(ri.get(i)).bitLength());
            Vector<BigInteger> temp2 = new Vector<>();
            temp2.add(p.multiply(qi.get(i)).add(ri.get(i)).multiply(new BigInteger("-1")));
            temp2.add(ri.get(i));
            temp.add(temp2);
        }
        System.out.println(pkSet);

        Collections.sort(pkSet, Comparator.reverseOrder()); //X0 is the largest element
        Collections.sort(temp,Comparator.comparing(Vector::firstElement)); //X0 is the largest element
        System.out.println(pkSet);
        System.out.println(temp);

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
    }
}
