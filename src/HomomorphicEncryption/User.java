package HomomorphicEncryption;

import java.util.Random;
import java.util.Vector;
import java.math.BigInteger;

public class User {
    private int qidRange = 2;
    private int rRange = 2;
    private int pkSize = 5;

    public BigInteger qid;
    public BigInteger r;
    public Vector<BigInteger> pk = new Vector<>();

    public Vector<BigInteger> rArray = new Vector<>();

    public Vector<BigInteger> pkArray = new Vector<>();

    Random rand = new Random();

    public User(Vector<BigInteger> pkSet,Vector<Vector<BigInteger>> temp){
        this(2,2,3,pkSet,temp);
    }

    public User(int qidRange, int rRange, int pkSize, Vector<BigInteger> pkSet, Vector<Vector<BigInteger>> temp){
        this.qidRange = qidRange;
        this.rRange = rRange;
        this.pkSize = pkSize;
        UserKeyGen(pkSet,temp);
    }

    //사용자의 키 생성 (qid, r, pk)
    void UserKeyGen(Vector<BigInteger> pkSet, Vector<Vector<BigInteger>> temp){
        qid = new BigInteger(qidRange,rand);
        while (qid.bitLength() == 0){
            qid = new BigInteger(qidRange,rand);
        }
        r = new BigInteger(rRange,rand);
        while (r.bitLength() == 0){
            r = new BigInteger(rRange,rand);
        }

//        qid = BigInteger.ZERO;
//        r = BigInteger.ZERO;

        makeUserKeySet(pkSet,temp);


        System.out.println("qid = " + qid + ", r = " + r);
//        System.out.println("qid(2) = " + qid.toString(2) + ", r(2) = " + r.toString(2));
//        System.out.println("qid(2) = " + qid.bitLength() + ", r(2) = " + r.bitLength());
        System.out.println("pk = " + pk);
    }

    //나중에 검색문에서 사용할 r 변경 가능하도록
    void ChangeUserR(){
        r = new BigInteger(rRange,rand);    }

    //사용자마다 랜덥의 public key set 만드는 함수
    void makeUserKeySet(Vector<BigInteger> pkSet, Vector<Vector<BigInteger>> temp){
        boolean usedpk[] = new boolean[pkSet.size()]; //default = false

        while(pk.size() < pkSize) {
            int pknum = (int)(Math.random()*pkSet.size());
            if(pknum==0||usedpk[pknum]) continue;
            usedpk[pknum] = true;
            rArray.add(temp.get(pknum).get(1));
            pkArray.add(temp.get(pknum).get(0).multiply(new BigInteger("-1")));
            System.out.print(pknum + ", ");
            pk.add(pkSet.get(pknum));
        }
        System.out.println();
    }
}
