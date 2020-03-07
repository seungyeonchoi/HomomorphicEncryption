package HomomorphicEncryption;

import java.math.BigInteger;
import java.util.Random;

public class UseBiginteger {
    public static void main(String[] args){

        // get random big integer
        Random r = new Random();
        BigInteger rBig = new BigInteger("3");
        System.out.println(rBig.add(BigInteger.valueOf(-1)));
        System.out.println(rBig);
        System.out.println(rBig.bitLength());
        System.out.println(rBig.bitCount());

        int rMax = 2*((int)Math.pow(2,3));
        rMax = -((int)Math.pow(2,3))+ (int)(Math.random()*rMax);
        BigInteger a = BigInteger.valueOf(2).multiply(rBig.pow(3));
        System.out.println(a);
        System.out.println(BigInteger.valueOf(-1).multiply(a));
        //sha256 -> 결과값 128자리 (16진수)

        /* testInvert결론 및 해결해야 할 부분
         * 1. 한글의 경우 str-> hex str로 바꾼 후 다시 한글 str로 변환 과정 추가 필요 (그냥은 한글이 안나옴 -> 인코딩 디코딩문제)
         * 2. hex str-> byteArray로 바꾸는 부분에서 0으로 시작하면 0이 날라감 -> 최종 비교할 때 결과값에 영향을 줄 수도 있음
         */
        testInvert("힣핳");

        /*
         * 한글 쓸려면 StringToHex(str)써 놓은거 쓰면 됨 (아래 코드처럼)
         * BigInteger big = new BigInteger(StringToHex(str),16);
         */
        /* bigInteger상 16진수 사칙연산 테스트
            bigInteger는 16진수를 10진수로 저장을 하고, 연산도 10진수로 계산함
            결과를 16진수로 원하면 바꿔서 출력하면 됨
            1. 덧셈 (.add(BigInteger))-> 16진수 + 10진수 이런 것도 다 가능
            2. 뺄셈 -> 귀찮아서 생략,,,
            3. 곱셈 (.multiply(BigInteger))-> 16진수 * 16진수, 16진수 * 10진수 값 같은 것 확인 끝!
            4. 나눗셈 (.divideAndRemainder(BigInteger))-> divideAndRemainder 쓰면 bigInteger[]로 저장 -> [0]은 몫, [1]은 나머지
            * mod 연산도 내장되어 있음 *
            *숫자가 큰 경우에는 아예 bigInteger로 되어있어야 함*
         */
        String str = "염상희";
        BigInteger big = new BigInteger(StringToHex(str),16);

        int addValue = 100;
        BigInteger addBig= new BigInteger("1217689356359548");

        System.out.println("---- 덧셈 계산 (with int - 작은 수) ----");
        System.out.println("기존 값 : " + big + ", " + big.toString(16));
        System.out.println(big + "+ " + addValue + "=" + big.add(BigInteger.valueOf(16))); //big, 100, big+16
        big = big.add(BigInteger.valueOf(addValue));//결과값을 저장해야 값이 바뀜
        System.out.println("결과 값 :" + big + ", " + big.toString(16));

        System.out.println("---- 덧셈 계산 (with bigInteger - 큰 수) ----");
        System.out.println("기존 값 : " + big + ", " + big.toString(16));
        System.out.println(big.toString(16) + "+ " + addBig.toString(16) + "=" + big.add(addBig));
        big = big.add(addBig);//결과값을 저장해야 값이 바뀜
        System.out.println("결과 값 :" + big + ", " + big.toString(16));

        //곱셈
        BigInteger multiBig= new BigInteger("2513");
        System.out.println("---- 곱셈 계산 (with bigInteger - 큰 수) ----");
        System.out.println("기존 값 : " + big + ", " + big.toString(16));
        System.out.println("10진수 표시 : "+big.toString() + " * " + multiBig.toString() + "=" + big.multiply(multiBig));
        System.out.println("16진수 표시 : "+big.toString(16) + " * " + multiBig.toString(16) + "=" + big.multiply(multiBig));
        big = big.multiply(multiBig);//결과값을 저장해야 값이 바뀜
        System.out.println("결과 값 :" + big + ", " + big.toString(16));

        //나눗셈
        BigInteger divBig= new BigInteger("2508924");
        BigInteger div[] = big.divideAndRemainder(divBig);
        System.out.println("---- 나눗셈 (with bigInteger - 큰 수) ----");
        System.out.println("기존 값 : " + big + ", " + big.toString(16));
        System.out.println("10진수 표시 : "+big.toString() + " / " + divBig.toString() + "=" + div[0] );
        System.out.println("10진수 표시 : "+big.toString() + " % " + divBig.toString() + "=" + div[1] );

        //mod
        System.out.println("-----mod -------");
        BigInteger modBig = new BigInteger("-4");
        BigInteger p = new BigInteger("3");
        System.out.println(p.multiply(modBig.divide(p)));
        System.out.println(modBig.divide(p));

        System.out.println(modBig.mod(p));
        System.out.println(modBig.divideAndRemainder(p)[0]);
        System.out.println(modBig.divideAndRemainder(p)[1]);

        BigInteger modBig2 = new BigInteger("6995329336643818884");
        System.out.println(modBig2.divide(p));
        System.out.println(modBig2.mod(p));
        System.out.println(modBig2.divideAndRemainder(p)[0]);
        System.out.println(modBig2.divideAndRemainder(p)[1]);

        modBig = new BigInteger("0");
        System.out.println(p.multiply(modBig));


        System.out.println("big integer 계산========");
        BigInteger[] b = {new BigInteger("2000524955058347907"), new BigInteger("2084455186876929936"), new BigInteger("2099159857479231595"), new BigInteger("2246614499840824326"), new BigInteger("1942506069975704065"), new BigInteger("2262789276838651085"), new BigInteger("2127185127206494945"), new BigInteger("2042771716040855646"),new BigInteger("2181999317355098944"), new BigInteger("2036178021163894216")};
        for (BigInteger bb:
             b) {
            modBig = modBig.add(bb);
        }
        System.out.println(modBig);
        BigInteger res = modBig.multiply(new BigInteger("73")).divideAndRemainder(new BigInteger("2304138343985970031"))[0];
        System.out.println(res);
        res = modBig.multiply(new BigInteger("73")).divideAndRemainder(new BigInteger("2304138343985970031"))[1];
        System.out.println(res);

        System.out.println(modBig.multiply(new BigInteger("73")));

        BigInteger temp = new BigInteger("-7957");
        System.out.println(temp.mod(new BigInteger("73")));
        System.out.println();
    }

    public static void testInvert(String str){
        System.out.println("----한글 string 출력----");
        System.out.println(str);
        System.out.println("----hexText 출력----");
        String hexText = StringToHex(str);
        System.out.println(hexText);

        System.out.println("----hexText-> byteArray 출력 : 1. byteArray   2. toString----");
        byte byteArray[] = new BigInteger(hexText,16).toByteArray();
        System.out.println(byteArray);
        System.out.println(byteArray.toString()); //string으로 변환 X

        System.out.println("----hexText-> byteArray -> invertHexText 출력----");
        String invertHexText = new BigInteger(byteArray).toString(16);
        System.out.println(invertHexText);

        System.out.println("----hexText to BigInteger : toString----");
        System.out.println(new BigInteger(hexText,16));
        System.out.println(new BigInteger(hexText,16).toString(16));

        System.out.println("----inverthexText to BigInteger : toString----");
        System.out.println(new BigInteger(invertHexText,16).toByteArray().toString());
        System.out.println(new BigInteger(invertHexText,16).toString(16));

        System.out.println("=====계산======");
        System.out.println(BigInteger.TWO.pow(36896));
        System.out.println(BigInteger.TWO.pow(36896).divide(new BigInteger("29470749159174747083425927364711720352589742363820280888489926986591380595030850844325817402857271649395133896163693453518160371552615862628007286942053370074091558659024658027423267836085322987693168670000001656049208929610063953096325838660379475001591470638212610829666990670711028481558586359733498991157757447025072817036294401146758610701376583349179414686061819032212043702726039241811392550001854378087718027483580767252913690546706598533926306480900923432360236328417398161364465181541")));

    }
    public static String StringToHex(String str) {
        String result = "";
        for(int i=0;i<str.length();i++){
            result += String.format("%02x", (int)str.charAt(i));
        }
        return result;
    }

}
