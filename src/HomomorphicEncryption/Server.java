package HomomorphicEncryption;

import java.math.BigInteger;
import java.util.Vector;

public class Server {

    private BigInteger p; //서버의 개인키
    private BigInteger a; //서버alpha
    public Database db;
    public Server(BigInteger p, BigInteger a){
        this.p = p;
        this.a = a;
        this.db = new Database();
    }

    public Vector<Integer> searchKeyword(Data data){
        Vector<Integer> correctFile = new Vector<>();

        //updateData(data); //system alpha 입히기
        for (KeywordPEKS keyword: db.selectKeywordPEKS()) { //이미 등록된 peks(wi) 과 비교
            if (keywordTest(data,keyword)){ //일치하는 키워드를 찾으면 zString(110101) 반환
                System.out.println("일치하는 파일 찾기");
                String zString = db.selectZindex(keyword.id);
                Vector<Contract> result = db.selectContract(zString); //result: 반환받은 파일
                for (Contract res:result) {
                    System.out.println(res.id+ "번째 파일 권한 검사");
                    if (keywordTest(data,res)){ //파일에 속한 권한 비교
                        correctFile.add(res.id);
                        System.out.println(res.id+ "번째 파일이 키워드/ 권한 동일함");
                    }
                }
            }
        }
        if(correctFile.size() == 0)
            System.out.println("해당하는 키워드가 없습니다.");

        return correctFile;
    }

    public void updateData(Data data){ //user alpha 지우고, system alpha 입히기
       data.c1 = data.makeCi(data.c1.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? data.c1.mod(p).subtract(p) : data.c1.mod(p),a);
       data.c3 = data.makeCi(data.c3.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? data.c3.mod(p).subtract(p) : data.c3.mod(p),a);
    }

    public Boolean keywordTest(BigInteger Ci1, BigInteger Ci2, BigInteger Cj1, BigInteger Cj2){
        //분모
        BigInteger parent = Ci1.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? Ci1.mod(p).subtract(p) : Ci1.mod(p);
        parent = hash(parent.mod(a));
        System.out.println("H(Ci1 mod p mod a)(2^hexadecimal): 2^" + parent.toString(16));
        parent = parent.add(Cj2);

        //분자
        BigInteger child = Cj1.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? Cj1.mod(p).subtract(p) : Cj1.mod(p);
        child = hash(child.mod(a));
        System.out.println("H(Cj1 mod p mod a)(2^hexadecimal) : 2^" + child.toString(16));
        child = child.add(Ci2);

        System.out.println();
        System.out.println("H(Ci1 mod p mod a)*Cj2(2^hexadecimal) : 2^" +  parent);
        System.out.println("H(Cj1 mod p mod a)*Ci2(2^hexadecimal) : 2^" + child);

        return parent.subtract(child).equals(BigInteger.ZERO);
    }

    public Boolean keywordTest(Data d1, KeywordPEKS d2){
        //분모
        BigInteger parent = d1.c1.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? d1.c1.mod(p).subtract(p) : d1.c1.mod(p);
        parent = hash(parent.mod(d1.getUser().getAu()));
        System.out.println("H(Ci1 mod p mod a)(2^hexadecimal): 2^" + parent.toString(16));
        parent = parent.add(d2.c2);

        //분자
        BigInteger child = d2.c1.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? d2.c1.mod(p).subtract(p) : d2.c1.mod(p);
        child = hash(child.mod(a));
        System.out.println("H(Cj1 mod p mod a)(2^hexadecimal) : 2^" + child.toString(16));
        child = child.add(d1.c2);

        System.out.println();
        System.out.println("H(Ci1 mod p mod a)*Cj2(2^hexadecimal) : 2^" +  parent);
        System.out.println("H(Cj1 mod p mod a)*Ci2(2^hexadecimal) : 2^" + child);

        return parent.subtract(child).equals(BigInteger.ZERO);
    }
    public Boolean keywordTest(Data d1, Contract d2){ //c3(권한 테스트 함수)
        //분모
        BigInteger parent = d1.c3.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? d1.c3.mod(p).subtract(p) : d1.c3.mod(p);
        parent = hash(parent.mod(d1.getUser().getAu()));
        System.out.println("H(Ci1 mod p mod a)(2^hexadecimal): 2^" + parent.toString(16));
        parent = parent.add(d2.c2);

        //분자
        BigInteger child = d2.c3.mod(p).compareTo(p.divide(BigInteger.TWO))>0 ? d2.c3.mod(p).subtract(p) : d2.c3.mod(p);
        child = hash(child.mod(a));
        System.out.println("H(Cj1 mod p mod a)(2^hexadecimal) : 2^" + child.toString(16));
        child = child.add(d1.c2);

        System.out.println();
        System.out.println("H(Ci1 mod p mod a)*Cj2(2^hexadecimal) : 2^" +  parent);
        System.out.println("H(Cj1 mod p mod a)*Ci2(2^hexadecimal) : 2^" + child);

        return parent.subtract(child).equals(BigInteger.ZERO);
    }

    public BigInteger hash(BigInteger exponent){
        return exponent;
    }

    //계약서 업로드
    public void updateContract(Data data){
        //단, 복사본을 생성해서 바꾼 데이터로 사용
         Data copydata = data;
         updateData(copydata);
         db.insertContract(copydata);

    }

    //키워드 업로드 및 처음 생성하는 키워드에 대한 zindex 생성
    public int updateKeyword(Data data){
        //키워드만 검색 -> 효율적인 검색 필요 (지금은 n^2)

        for (KeywordPEKS keyword: db.selectKeywordPEKS()) { //이미 등록된 peks(wi) 과 비교
            if (keywordTest(data,keyword)){
                return keyword.id;
            }
        }

        db.insertKeywordPEKS(data);

        int keywordCnt = db.getTupleNum("KeywordPEKS");
        int contractCnt = db.getTupleNum("Contract") - 1;

        String zString = "";
        for(int i =0;i<contractCnt ; i++){
            zString +="0";
        }

        updateData(data);

        //없는 키워드라면 z-index에 추가 string 0으로 채워서 마지막 파일은 1
        db.insertZindex(keywordCnt,zString);

        return keywordCnt;

    }

    //파일 추가시 z-index의 zString변경경
    public void updateZString(Vector<Integer> keywordNum){
        db.updateZString(keywordNum);
    }

}
