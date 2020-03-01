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
        BigInteger sumPk = BigInteger.ZERO;
        for (BigInteger pk : user.pk) {
            sumPk = sumPk.add(pk);
        }
        System.out.println("w = " + w + ", r = " + user.r + ", qid = " + user.qid);

        c1 = w.add(user.r.multiply(user.qid)).add(a.multiply(sumPk)); //w+(user.r*user.qid)+(a*sumPk);
        System.out.println("c1 (w+r*qid+a*sumpk): "+c1);
        System.out.println(c1.divideAndRemainder(pkSet.get(0))[0] + ", " +c1.divideAndRemainder(pkSet.get(0))[1] +", "+
        c1.mod(pkSet.get(0)));
        //xo으로 나누었을 때  - xo/2 ~ xo/2
        System.out.println(c1.mod(pkSet.get(0)).bitLength()+ ", " + pkSet.get(0).divide(BigInteger.TWO).bitLength());
        if(c1.mod(pkSet.get(0)).compareTo(pkSet.get(0).divide(BigInteger.TWO))>0){
            c1 = c1.divideAndRemainder(pkSet.get(0))[1].subtract(pkSet.get(0));
        }
        else
            c1 = c1.divideAndRemainder(pkSet.get(0))[1]; //c1 % pkSet.get(0); // mod X0
        System.out.println(pkSet.get(0));
        System.out.println("c1 ( ~ mod x0): "+c1);

    }
    void makeC2(){
        System.out.println("riqid : " +user.r.multiply(user.qid));
        c2 = hash(user.r.multiply(user.qid));
 //       System.out.println("c2: "+c2);
    }

    public static BigInteger hash(BigInteger exponent){
        //return BigInteger.valueOf(2).pow(exponent.intValue());
        return exponent;
        //return (int)Math.pow(2, exponent); //(의문)mod ? 를 해야할까
    }

}