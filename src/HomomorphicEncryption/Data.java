package HomomorphicEncryption;

import java.util.Vector;

public class Data {

    private User user;
    private int w;
    public int c1;
    public int c2;

    public Data(User user, int w, int a, Vector<Integer> pkSet){
        this.user = user;
        this.w = w;
        makeC1(a, pkSet);
        makeC2();
    }

    void makeC1(int a, Vector<Integer> pkSet){
        int sumPk = 0;
        for (int pk : user.pk) {
            sumPk+=pk;
        }
        c1 = w+(user.r*user.qid)+(a*sumPk);
        System.out.println("c1 (w+r*qid+a*sumpk): "+c1);
        c1 = c1 % pkSet.get(0); // mod X0
        System.out.println(pkSet.get(0));
        System.out.println("c1 ( ~ mod x0): "+c1);
    }
    void makeC2(){
        c2 = hash(user.r * user.qid);
        System.out.println("c2: "+c2);
    }

    public static int hash(int exponent){
        return (int)Math.pow(2, exponent); //(의문)mod ? 를 해야할까
    }

}


