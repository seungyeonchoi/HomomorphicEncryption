package HomomorphicEncryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Vector;

//정렬: command + alt+ l

public class HomomorphicEncryption {

    /*
    *의문사항
    * 1) 로그인을 하지 않는데 user정보를 어떻게 관리할까? local로?
    * 2) 근로자/ 점주가 동명이인일 때?
    * */
    public static KGC kgc;
    public static Server server;
    public static Data d1;
    public static Data d2;
    public static BigInteger rnum = new BigInteger(SHA1("최승연"), 16);
    public static BigInteger rnum1 = rnum.add(BigInteger.ONE);
    public static String message2 = "";
    //qid: (1번째 최승연) fe10876d3bcaac90ba0968a4c
    //qid: (2번째 최승연) cb066fe11fed84bc5dcb04c08
    //qid: (3번째 최승연) 8efe655fd42e129d02264320a
    //qid: (4번째 염상희) cb4bfb4affd69a854de71aa0c
    //qid: (5번째 염상희) cb4bfb4affd69a854de71aa0c
    public static void main(String[] args) {

        //시작 전 kgc 및 server 생성
        settingToStart();

        //user생성
        User userA = new User(kgc.pkSet);
        userA.setAu(kgc.shareAlpha()); //kgc -> user에 alpha 공유 (임의로)
       // userA.qid = new BigInteger("cb066fe11fed84bc5dcb04c08", 16);
        searchKeyword(userA,"최승연");
       // requestToUpload(userA, new String[]{"염상희2", "최승연2"});
        //파일 업로드 (ex 1. userA의 염상희 최승연  2. userA의 염상희 박소영)
//        Vector<String> str = new Vector<>();
//
//        str.add("염상희");
//        str.add("최승연");
//        requestToUpload(userA, str);
//
//        str.clear();
//
//        str.add("염상희");
//        str.add("박소영");
//        requestToUpload(userA, str);
//
//        //키워드 검색 (ex userA's qid로 박소영 검색)
//        User userB = new User(kgc.pkSet);
//        userB.setAu(kgc.shareAlpha()); //kgc -> user에 alpha 공유 (임의로)
//        userB.qid = new BigInteger("cb066fe11fed84bc5dcb04c08", 16);
//        searchKeyword(userB,"박소영");

    }

    //시작전 kgc 및 server 생성
    public static void settingToStart(){
        kgc = new KGC();
        server = new Server(kgc.getP(),kgc.getA());
    }

    //파일 업로드
    public static void requestToUpload(User user, String[] keywords){
        //근로자 or 점주 둘 중한명만 파일등록함
        server.uploadContract(new Data(user, new BigInteger(SHA1(keywords[0]),16),user.getAu(),kgc.pkSet));
        //키워드 기반 암호문 생성
        Data[] datas = new Data[2];
        for(int i = 0; i<2;i++){ //한 파일에 키워드가 2개니까 !
            datas[i] = new Data(user, new BigInteger(SHA1(keywords[i]),16),user.getAu(),kgc.pkSet);
        }
        server.uploadKeyword(datas);
    }
    public static void requestToUpload(User user, Vector<String> keyword){
        //keyword -> biginteger로 변경 후 검색문 생성
        //1. 검색문 생성 (여러개의 ci3만들 필요 x) -> 일단은 c1,c2,c3 모두 생성
        Vector<Data> data = new Vector<Data>();
        Vector<Integer> keywordNum = new Vector<>(); //zString을 만들기 위한 용도

        for(String i : keyword) {
            data.add(new Data(user, new BigInteger(SHA1(i),16),user.getAu(),kgc.pkSet));
            //updateData 없앤다면, updateContract, updateKeyword 함수에 적힌 대로 변경해야 함
            server.addSystemAlpha(data.lastElement());
        }

        //2. 새로운 user라면 추가 -> 일단 pass -> 새로운 user인지 확인할 수 없음

        //3. 파일 권한 업로드 -> 하나의 data객체만 이용하면 됨
        server.uploadContract(data.get(0));

        //4. 키워드 추가하기 (return 키워드들의 index)
        for(Data d : data){
            System.out.println("updateKeyword");
            keywordNum.add(server.updateKeyword(d));
        }

        //5. zString 변경
        server.updateZString(keywordNum);
        return;
    }

    //키워드 검색
    public static void searchKeyword(User user, String keyword){
        Vector<Integer> correctFile = new Vector<>();
        Data data = new Data(user, new BigInteger(SHA1(keyword),16));
        correctFile = server.searchKeyword(data);
        System.out.println(correctFile);
    }

    //해쉬함수
    public static String SHA1(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1"); // 이 부분을 SHA-256, MD5로만 바꿔주면 된다.
            md.update(str.getBytes()); // "세이프123"을 SHA-1으로 변환할 예정!

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<byteData.length; i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "fail";
    }
}