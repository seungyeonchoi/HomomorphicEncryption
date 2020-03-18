package HomomorphicEncryption;

import java.math.BigInteger;
import java.util.Vector;

public class Data {

    private User user;
    private BigInteger w;
    public BigInteger c1;
    public BigInteger c2;

    public Data(User user, BigInteger w, BigInteger a, Vector<BigInteger> pkSet){
        this.user = user;
        this.w = w;
        makeC1(a, pkSet);
        makeC2();
    }

    void makeC1(BigInteger a, Vector<BigInteger> pkSet){

        //sum 사용자의 x 구하기
        BigInteger sumPk = BigInteger.ZERO;
        for (BigInteger pk : user.pk) {
            sumPk = sumPk.add(pk);
        }

        System.out.println("w(hexadecimal) = " + w.toString(16) + ", r(hexadecimal) = " + user.r.toString(16) + ", qid(hexadecimal) = " + user.qid.toString(16));
        System.out.println();

        //ci1계산하기 (mod범위에 맞추어서)
        c1 = w.add(user.r.multiply(user.qid)).add(a.multiply(sumPk)); //w+(user.r*user.qid)+(a*sumPk);

        if(c1.mod(pkSet.get(0)).compareTo(pkSet.get(0).divide(BigInteger.TWO))>0)
            c1 = c1.divideAndRemainder(pkSet.get(0))[1].subtract(pkSet.get(0));
        else
            c1 = c1.divideAndRemainder(pkSet.get(0))[1]; //c1 % pkSet.get(0); // mod X0

        System.out.println("c1(hexadecimal): "+c1.toString(16));

    }

    void makeC2(){
        //ci2계산하기
        c2 = hash(user.r.multiply(user.qid));
        System.out.println("c2(2^hexadecimal): 2^"+c2.toString(16));
        System.out.println();
    }

    public static BigInteger hash(BigInteger exponent){
        //return BigInteger.valueOf(2).pow(exponent.intValue());
        return exponent;
    }

}