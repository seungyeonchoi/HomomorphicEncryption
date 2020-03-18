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

    public static BigInteger lamda = new BigInteger("164"); //한글 한글자로 test할려면 최소 5이성
    public static BigInteger eta = new BigInteger("2000"); //원래 조건 -> (int)(Math.random()*Math.pow(lamda,2)), 개인키의 길이
    public static BigInteger gamma = new BigInteger("36896"); //원래 조건 -> (int)(Math.random()*Math.pow(lamda,5)) -> 현재 lamda^3
    public static BigInteger pkSetSize = new BigInteger("10");//감마 + 람다 (but 너무 커서 일단 감마^3+람다로)

    public static BigInteger p; // 비트의 수가 eta (lamda^2)
    public static BigInteger a;

    public static Vector<BigInteger> pkSet = new Vector<>();

    public static Vector<Vector<BigInteger>> temp = new Vector<>();

    public KGC(){
        this(pkSetSize);
    }

    public KGC(BigInteger pkSetSize){
        this.pkSetSize = pkSetSize;
        System.out.println("lamda : " + lamda +", eta : "+eta+", gamma : "+ gamma + "pkSetSize : " + pkSetSize);
        System.out.println("bit length of p : "+eta + ", bit length of a(alpha) : " + lamda + "\n");
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

        //알파 값 설정
        a = BigInteger.ONE;
        for(int i=0;i<lamda.intValue();i++){
            a = a.multiply(BigInteger.TWO);
        }
        a = a.add(new BigInteger(lamda.intValue()-1,r)).nextProbablePrime();//최대 2^19

        //p값 설정 (kgc의 개인키) -> 소수 찾기
        p = BigInteger.ONE;
        for(int i=0;i<eta.intValue();i++){
            p = p.multiply(BigInteger.TWO);
        }
        p = p.add(new BigInteger(eta.intValue()-1,r)).nextProbablePrime();

//        소수가 아닌 알파와 서로소인 수 찾는 식
//        if(p.add(new BigInteger(eta.intValue()-1,r)).mod(a).equals(BigInteger.ZERO))
//            p = p.add(new BigInteger(lamda.intValue()-1,r));//최대 2^39

        System.out.println("p(16) = " + p.toString(16));
        System.out.println("a(16) = " + a.toString(16));

        //pkSet 뽑기 (kgc의 공개키)
        Vector<BigInteger> qi = new Vector<>();
        BigInteger qMax = new BigInteger("2").pow(gamma.intValue()).divide(p);
        for (int i = 0; i < pkSetSize.intValue(); i++) {
            qi.add(new BigInteger(qMax.bitLength()-1,r));// 0~ 2^(lamda)/p
        }

        Vector<BigInteger> ri = new Vector<>();
        BigInteger rMax = new BigInteger("2").pow(lamda.intValue()).multiply(BigInteger.valueOf(2));
        for (int i = 0; i < pkSetSize.intValue(); i++) {
            ri.add(new BigInteger(lamda.intValue()-1,r).multiply(new BigInteger("-1").pow(new BigInteger(1,r).intValue())));
        }

        for(int i = 0; i<pkSetSize.intValue(); i++){
            pkSet.add(p.multiply(qi.get(i)).add(ri.get(i)));
            Vector<BigInteger> temp2 = new Vector<>();
            temp2.add(p.multiply(qi.get(i)).add(ri.get(i)).multiply(new BigInteger("-1")));
            temp2.add(ri.get(i));
            temp.add(temp2);
        }

        Collections.sort(pkSet, Comparator.reverseOrder()); //X0 is the largest element
        Collections.sort(temp,Comparator.comparing(Vector::firstElement)); //X0 is the largest element

        //조건에 맞는 xo만들기
        if (pkSet.get(0).mod(p) != a){ //X0 mod p = a 조건 체크
            BigInteger rest = p.subtract(pkSet.get(0).mod(p).subtract(a));
            BigInteger x0 = pkSet.get(0).add(rest);
            while (x0.mod(a) == BigInteger.valueOf(0)){ //x0 mod a =0 이면, x0 값 증가시킴 //근데 따져보니까 (p/a)*x =(k -1 ) 을 만족하는 x와 k가 있으면, 이 조건이 성립하는데 아마 그런경우가 많이 없을듯해 !
                x0 = x0.add(p);
            }
            pkSet.set(0,x0);
        }

        System.out.println("\nKGC-selected pkSet");
        for(int i=0;i<pkSet.size();i++) {
            if (i == 0) System.out.println("x0(hexadecimal) : " + pkSet.get(i).toString(16));
            else System.out.println(i + "(hexadecimal) : " + pkSet.get(i).toString(16));
        }
    }
}
