package HomomorphicEncryption;

import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;

public class KGC {

    public static int pkSetSize = 5;
    public static int lamda = 3;
    public static int gamma = 15; //원래 조건 -> (int)(Math.random()*Math.pow(lamda,5));
    public static int p = 327; //secret key (256 ~ 512)
    public static int a = 13; //
    public static Vector<Integer> pkSet = new Vector<>();

    public KGC(){
        this(pkSetSize);
    }

    public KGC(int pkSetSize){
        this.pkSetSize = pkSetSize;
        makePublicKeySet(this.pkSetSize);
    }

    public KGC(int pkSetSize, int lamda, int gamma, int p, int a){
        this.pkSetSize = pkSetSize;
        this.lamda = lamda;
        this.gamma = gamma;
        this.p = p;
        this.a = a;
        makePublicKeySet(this.pkSetSize);
    }

    private void makePublicKeySet(int pkSetSize) {
    /*
        Xi = p*qi + ri
        qi = 0 ~ (2^lamda)/2 // 0~ any integer
        ri = - (2^lamda) ~ (2^lamda)
         */
        pkSet.clear(); //pkSet 초기화

        int [] qi = new int[pkSetSize];
        int qMax = ((int)Math.pow(2,gamma))/p;

        for (int i = 0; i < pkSetSize; i++) {
            qi[i] = (int)(Math.random()*qMax); // 0~ 2^(lamda)/p
            for (int j = i-1; j >= 0; j--) { //중복제거
                if (qi[j] == qi[i]) {
                    i--;
                    continue;
                }
            }
            System.out.println("qi: "+qi[i]);
        }

        int [] ri = new int[pkSetSize];
        int rMax = 2*((int)Math.pow(2,lamda));
        for (int i = 0; i < pkSetSize; i++) {
            ri[i] = -((int)Math.pow(2,lamda))+ (int)(Math.random()*rMax); // 0~ 2^(lamda)/p
            for (int j = i-1; j >= 0; j--)  { //중복제거
                if (ri[j] == ri[i]) {
                    i--;
                }
            }
            System.out.println("ri: "+ri[i]);
        }

//        int [] qi = {1,2,2,32,46};
//        int [] ri = {2,-2,1,1,13};
        for(int i = 0; i<pkSetSize; i++){
            pkSet.add(p*qi[i]+ri[i]);
        }
        Collections.sort(pkSet, Comparator.reverseOrder()); //X0 is the largest element
        if (pkSet.get(0) % p != a){ //X0 mod p = a 조건 체크
            int rest = p - (pkSet.get(0)%p - a);
            int x0 = pkSet.get(0)+rest;
            while (x0 % a == 0){ //x0 mod a =0 이면, x0 값 증가시킴 //근데 따져보니까 (p/a)*x =(k -1 ) 을 만족하는 x와 k가 있으면, 이 조건이 성립하는데 아마 그런경우가 많이 없을듯해 !
                x0 += p;
            }
            System.out.println(x0);
            pkSet.set(0,x0);

        }
        System.out.println(pkSet);
    }
}
