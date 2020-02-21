package HomomorphicEncryption;

import java.util.Vector;

public class User {
    private int qidRange = 3;
    private int rRange = 3;
    private int pkSize = 2;

    public int qid;
    public int r;
    public int[] pk;

    public User(Vector<Integer> pkSet){
        this(3,3,2,pkSet);
    }

    public User(int qidRange, int rRange, int pkSize, Vector<Integer> pkSet){
        this.qidRange = qidRange;
        this.rRange = rRange;
        this.pkSize = pkSize;
        UserKeyGen(pkSet);
    }

    //사용자의 키 생성 (qid, r, pk)
    void UserKeyGen(Vector<Integer> pkSet){
        qid = (int)(Math.random()*qidRange)+1;
        r = (int)(Math.random()*rRange)+1;
        makeUserKeySet(pkSet);

        System.out.println("qid = " + qid + ", r = " + r);
        for(int i=0;i<pkSize;i++){
            System.out.println("pk"+i+"값 = " + pk[i]);
        }
    }

    //나중에 검색문에서 사용할 r 변경 가능하도록
    void ChangeUserR(){
        r = (int)(Math.random()*rRange)+1;
    }

    //사용자마다 랜덥의 public key set 만드는 함수
    void makeUserKeySet(Vector<Integer> pkSet){
        System.out.println(pkSet.size());
        pk = new int[pkSize--];
        boolean usedpk[] = new boolean[pkSet.size()]; //default = false

        while(pkSize >= 0) {
            int pknum = (int)(Math.random()*pkSet.size());
            if (usedpk[pknum]) continue;
            usedpk[pknum] = true;
            pk[pkSize--] = pkSet.get(pknum);
        }
    }
}
