package HomomorphicEncryption;

import java.io.*;
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
//    public Vector<Data> readDB(){
//        Vector<Data> arrData = new Vector<Data>();
//        try{
//            FileReader fr = new FileReader("database.txt");
//            BufferedReader br = new BufferedReader(fr);
//            String line = "";
//            while((line = br.readLine()) != null){
//                arrData.add(new Data(new BigInteger(line,16), new BigInteger(br.readLine(),16), new BigInteger(br.readLine(),16)));
//            }
//            fr.close();
//            br.close();
//        } catch (FileNotFoundException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }
//        return arrData;
//    }
//    public void writeDB(Data data){
//
//        try{
//            FileWriter fw = new FileWriter("database.txt", true); //true : 이어쓰기
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(data.c1.toString(16));
//            bw.newLine();
//            bw.write(data.c2.toString(16));
//            bw.newLine();
//            bw.write(data.c3.toString(16));
//            bw.newLine();
//            bw.flush();
//            fw.close();
//            bw.close();
//        } catch (FileNotFoundException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }
//    }
    public void uploadFile(Data data){
        updateData(data);
       // db.updateZindex(1,"바꿈내가");
       // Vector<Data> arrData = readDB();

//        for (Data element: arrData) {
//            if(keywordTest(element.c1,element.c2,data.c1,data.c2)) { //동일한 키워드로 생성된 행이 있을 경우
//                //내 데이터값 넣기 -> 파일입출력으로 하기엔 귀찮으니까 이건 그냥 넣었다 치고
//                System.out.println("동일한 키워드 찾음");
//                return;
//            }
//        }
        //새롭게 추가된 키워드 일 경우
       // writeDB(data);
    }
    public void searchKeyword(Data data){
        updateData(data); //system alpha 입히기
        for (KeywordPEKS keyword: db.selectKeywordPEKS()) { //이미 등록된 peks(wi) 과 비교
            if (keywordTest(data,keyword)){ //일치하는 키워드를 찾으면 zString(110101) 반환
                System.out.println("일치하는 파일 찾기");
                String zString = db.selectZindex(keyword.id);
                Vector<Contract> result = db.selectContract(zString); //result: 반환받은 파일
                for (Contract res:result) {
                    System.out.println(res.id+ "번째 파일 권한 검사");
                    if (keywordTest(data,res)){ //파일에 속한 권한 비교
                        System.out.println(res.id+ "번째 파일이 키워드/ 권한 동일함");
                    }
                }
                return;
            }
        }
        System.out.println("해당하는 키워드가 없습니다.");
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
        parent = hash(parent.mod(a));
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
        parent = hash(parent.mod(a));
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
}
