import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
//정렬: command + alt+ l

public class HomomorphicEncryption {
    public static int lamda = 9;
    public static int gamma = 15; //원래 조건 -> (int)(Math.random()*Math.pow(lamda,5));
    public static int p = 325; //secret key (256 ~ 512)
    public static int a = 13; //
    public static Vector<Integer> pkSet = new Vector<>();
    public static Data d1;
    public static Data d2;
    public static Data d3;

    public static void main(String[] args) {
        //0) parameter 세팅(현재는 디폴트)
        //1) 공개키 쌍 만들기
        makePublicKeySet(5);
        //2) user 만들기
        int []pk = {pkSet.get(4),pkSet.get(3)};
        d1 = new Data(2,2,2,pk);

        pk[0]=pkSet.get(3);
        pk[1]=pkSet.get(2);
        d2 = new Data(3,2,3,pk);

        pk[0]=pkSet.get(4);
        pk[1]=pkSet.get(1);
        d3 = new Data(2,3,4,pk);
        System.out.println(test());
    }
    public static void makePublicKeySet(int count){
        /*
        Xi = p*qi + ri
        qi = 0 ~ (2^lamda)/2 // 0~ any integer
        ri = - (2^lamda) ~ (2^lamda)
         */
        pkSet.clear(); //pkSet 초기화

        int [] qi = new int[count];
        int qMax = ((int)Math.pow(2,gamma))/p;

        for (int i = 0; i < count; i++) {
            qi[i] = (int)(Math.random()*qMax); // 0~ 2^(lamda)/p
            for (int j = i-1; j >= 0; j--) { //중복제거
                if (qi[j] == qi[i]) {
                    i--;
                    continue;
                }
            }
            System.out.println("qi: "+qi[i]);
        }

        int [] ri = new int[count];
        int rMax = 2*((int)Math.pow(2,lamda));
        for (int i = 0; i < count; i++) {
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
        for(int i = 0; i<count; i++){
            pkSet.add(p*qi[i]+ri[i]);
        }
        Collections.sort(pkSet, Comparator.reverseOrder()); //X0 is the largest element
        if (pkSet.get(0) % a != 0){ //X0 mod p = a 조건 체크
            int rest = a - pkSet.get(0)%a;
            pkSet.set(0,pkSet.get(0)+rest);

        }
        System.out.println(pkSet);
    }
    public static Boolean test(){
        int parent = hash((d1.c1 % p)%a) *d2.c2;
        int child = hash((d2.c1 % p)%a)*d1.c2;
        System.out.println(parent);
        System.out.println(child);
        return child/parent == 1 ? true : false;
    }
    public static int hash(int exponent){
        return (int)Math.pow(2, exponent); //(의문)mod ? 를 해야할까
    }
    public static class Data{
        private int qid;
        private int w;
        private int r;
        private int[] pk;
        private int c1;
        private int c2;

        public Data(int qid, int w, int r, int[] pk){
            this.qid = qid;
            this.w = w;
            this.r = r;
            this.pk = pk;
            makeC1();
            makeC2();
        }
        void makeC1(){
            int sumPk = 0;
            for (int pk : pk) {
                sumPk+=pk;
            }
            c1 = w+(r*qid)+(a*sumPk);
            System.out.println("c1 (w+r*qid+a*sumpk): "+c1);
            c1 = c1 % pkSet.get(0); // mod X0
            System.out.println(pkSet.get(0));
            System.out.println("c1 ( ~ mod x0): "+c1);
        }
        void makeC2(){
            c2 = hash(r*qid);
            System.out.println("c2: "+c2);
        }

    }

}