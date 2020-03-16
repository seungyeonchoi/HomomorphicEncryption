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

    public static BigInteger lamda = new BigInteger("3"); //한글 한글자로 test할려면 최소 5이성
    public static BigInteger eta = lamda.pow(2);  //원래 조건 -> (int)(Math.random()*Math.pow(lamda,2)), 개인키의 길이
    public static BigInteger gamma = lamda.pow(5); //원래 조건 -> (int)(Math.random()*Math.pow(lamda,5)) -> 현재 lamda^3
    public static BigInteger pkSetSize = lamda.add(gamma);//감마 + 람다 (but 너무 커서 일단 감마^3+람다로)

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

        //p = 2의 38승 + 자리 랜덤 값의 다음 소수 , 2의 48( O(lam®da^2) = O(eta) ), 19+19+9 (360 = subset 개수)
        p = new BigInteger(eta.intValue(),r).nextProbablePrime();
                //.nextProbablePrime();//최대 2^39
        while (p.bitLength() != eta.intValue()){
            p = new BigInteger(eta.intValue(),r).nextProbablePrime();
        }
        //a = 2의 18승 + 18자리 랜덤 값 , 2의 19승

        a = new BigInteger("10",16).add(new BigInteger(6,r)).nextProbablePrime();//최대 2^19
        while (a.bitLength() != lamda.intValue()){  //만약 찾은 소수가 lamda^2 의 len 보다 크면 같을 때 까지 다시 뽑기
            a = new BigInteger(lamda.intValue(),r).nextProbablePrime();
        }
        while (p.mod(a) == BigInteger.ZERO || p.bitLength() != eta.intValue()){  //만약 찾은 소수가 lamda^2 의 len 보다 크면 같을 때 까지 다시 뽑기
            p = new BigInteger(eta.intValue(),r);
        }
        //a = BigInteger.TWO;

        System.out.println("p = " + p + p.toString(16) + ", a = " + a );
        System.out.println("p(2) = " + p.toString(2) + ", a(2) = " + a.toString(2));
        System.out.println("p(2) = " + p.bitLength() + ", a(2) = " + a.bitLength());


        Vector<BigInteger> qi = new Vector<>();
       // System.out.println(new BigInteger("2").pow(gamma.intValue()) + ", bit length " + new BigInteger("2").pow(gamma.intValue()).bitLength());
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

        for (int i = 0; i < 5; i++) {
            ri.add(new BigInteger(lamda.intValue()-1,r).multiply(new BigInteger("-1").pow(new BigInteger(1,r).intValue())));
        }
        System.out.println("ri = " + ri);

        for(int i = 0; i<5; i++){
            pkSet.add(p.multiply(qi.get(i)).add(ri.get(i)));
          //  System.out.println(p.multiply(qi.get(i)).add(ri.get(i)).bitLength());
            Vector<BigInteger> temp2 = new Vector<>();
            temp2.add(p.multiply(qi.get(i)).add(ri.get(i)).multiply(new BigInteger("-1")));
            temp2.add(ri.get(i));
            temp.add(temp2);
        }

        Collections.sort(pkSet, Comparator.reverseOrder()); //X0 is the largest element
        Collections.sort(temp,Comparator.comparing(Vector::firstElement)); //X0 is the largest element

    //    System.out.println(temp);

        BigInteger x0 = pkSet.get(0);
        while (x0.mod(BigInteger.TWO) == BigInteger.ZERO || x0.mod(p).mod(BigInteger.TWO) == BigInteger.ZERO){ //x0은 홀수인가? x0 mod p 는 홀수인가?
            x0 = x0.add(BigInteger.ONE);
            if (x0.mod(BigInteger.TWO) != BigInteger.ZERO && x0.mod(p).mod(BigInteger.TWO) != BigInteger.ZERO){
                break;
            }
        }
        pkSet.set(0,x0);
        System.out.println("pkset");
        for(int i = 0; i<pkSet.size();i++){
            System.out.println(pkSet.get(i).toString(16));
        }

        System.out.println("x0 mod 2 = " + pkSet.get(0).mod(BigInteger.TWO));
        System.out.println("xo mod 2 != 0 -> " + pkSet.get(0).mod(BigInteger.TWO)+", xo mod p = odd : "+ pkSet.get(0).mod(p));
    }
}
