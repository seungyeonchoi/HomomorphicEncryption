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
//        d1 = new Data(userA,2,kgc.a,kgc.pkSet);
//        d2 = new Data(userB,2,kgc.a,kgc.pkSet);
//
//        System.out.println(test());
    }
//
//    public static Boolean test(){
//        int parent = hash((d1.c1 % kgc.p )%kgc.a) *d2.c2;
//        int child = hash((d2.c1 % kgc.p  )%kgc.a)*d1.c2;
//        System.out.println(parent);
//        System.out.println(child);
//        return child/parent == 1 ? true : false;
//    }

    public static int hash(int exponent){ //data에도 있는데 하나로 하는 방법을 모르겠음!
        return (int)Math.pow(2, exponent); //(의문)mod ? 를 해야할까
    }

}