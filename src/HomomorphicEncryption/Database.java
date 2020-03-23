package HomomorphicEncryption;
import java.sql.*;

public class Database {
    private String user_name = "sangseung";
    private String password = "konkuk17sw";
    private String host = "seouldb.cwnpn1rxhuaq.ap-northeast-2.rds.amazonaws.com:3306"; // MySQL 서버 주소
    private String database = ""; // MySQL DATABASE 이름 -> blank 일때가 오류가 안남 이상행 !
    private String mainDB = "mydb";
    PreparedStatement pstmt = null;
    Statement stmt = null;
    ResultSet rs = null;
    private Connection conn = null;

    public Database(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("jdbc 오류");
            e.printStackTrace();
        }
        connect();
    }
    void insertZindex(int id, String zString){
        try{
            String sql = "INSERT INTO "+mainDB+".Zindex(contractId,zString) VALUES (?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, zString);
            isSuccess(pstmt.executeUpdate());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void insertContract(Data data){
        try{
            String sql = "INSERT INTO "+mainDB+".Contract(ci2,ci3) VALUES (?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.c2.toString(16 ));
            pstmt.setString(2, data.c3.toString(16 ));
            isSuccess(pstmt.executeUpdate());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void insertKeywordPEKS(Data data){
        try{

            String sql = "INSERT INTO "+mainDB+".KeywordPEKS(ci1,ci2) VALUES (?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.c1.toString(16 ));
            pstmt.setString(2, data.c2.toString(16 ));
            isSuccess(pstmt.executeUpdate());
        }catch (SQLException e) {
            e.printStackTrace();

        }
    }

    void insertUser(User user, int id){
        try{
            PreparedStatement pstmt = null;
            String sql = "INSERT INTO "+mainDB+".userTable(userId) VALUES (?)";
            System.out.println("sql: "+ sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            isSuccess(pstmt.executeUpdate());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void insert(String sql){
        try{
            PreparedStatement pstmt = null;
           // String sql = "INSERT INTO mydb.contract VALUES (?)";
            pstmt = conn.prepareStatement(sql);
            // 데이터 ing
            pstmt.setInt(1, 5);

            // 5. 쿼리 실행 및 결과 처리
            // SELECT와 달리 INSERT는 반환되는 데이터들이 없으므로
            // ResultSet 객체가 필요 없고, 바로 pstmt.executeUpdate()메서드를 호출하면 됩니다.
            // INSERT, UPDATE, DELETE 쿼리는 이와 같이 메서드를 호출하며
            // SELECT에서는 stmt.executeQuery(sql); 메서드를 사용했었습니다.
            // @return int - 몇 개의 row가 영향을 미쳤는지를 반환
            int count = pstmt.executeUpdate();
            if (count == 0) {
                System.out.println("fail to insert: "+ sql);
            } else {
                System.out.println("success to insert: "+ sql);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void insertKeywordPEKS(){

    }
    void select(String sql) {
        try{

            stmt = conn.createStatement();
            //String sql = "SELECT ip from ipList";
            // 5. 쿼리 수행
            // 레코드들은 ResultSet 객체에 추가된다.
            rs = stmt.executeQuery(sql);
            // 6. 실행결과 출력하기
            while (rs.next()) {
                // 레코드의 칼럼은 배열과 달리 0부터 시작하지 않고 1부터 시작한다.
                // 데이터베이스에서 가져오는 데이터의 타입에 맞게 getString 또는 getInt 등을 호출한다.
                String res = rs.getString(1);
                System.out.println(res);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    void connect(){
        System.out.println("db connect");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useSSL=false", user_name, password);
            System.out.println("정상적으로 연결되었습니다.");
        } catch(SQLException e) {
            System.err.println("conn 오류:" + e.getMessage());
            e.printStackTrace();
        }
    }
    void disconnect(){
        try {
            if(conn != null)
                conn.close();
            if (pstmt != null)
                pstmt.close();
            if (stmt != null)
                stmt.close();
            if(rs != null)
                rs.close();
        } catch (SQLException e) {

        }
    }
    void isSuccess(int result){
        if (result == 0) {
            System.out.println("fail to insert: ");
        } else {
            System.out.println("success to insert: ");
        }
    }
}
