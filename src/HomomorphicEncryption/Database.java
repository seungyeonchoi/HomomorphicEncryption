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
            String sql = "INSERT INTO "+mainDB+".userTable(userId) VALUES (?)";
            System.out.println("sql: "+ sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            isSuccess(pstmt.executeUpdate());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void selectKeywordPEKS(){
        try{
            String sql = "SELECT ci1,ci2 from "+mainDB+".KeywordPEKS";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String ci1 = rs.getString(1);
                String ci2 = rs.getString(2);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    void selectZindex(int id){
        try{
            String sql = "SELECT zString from "+mainDB+".Zindex where keywordId = "+id;
            rs = stmt.executeQuery(sql);
            String res = rs.getString(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    void selectContract(boolean[] zString){
        try{
            String sql = "SELECT ci2,ci3 "+mainDB+".Contract where contractId=?";
            pstmt = conn.prepareStatement(sql);
            for(int i = 0; i<zString.length; i++){
                if (zString[i]){
                    pstmt.setInt(1, i+1);
                    rs = pstmt.executeQuery();
                    String ci2 = rs.getString(1);
                    String ci3 = rs.getString(2);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    void updateZindex(int id, String zString){
        try{
            String sql = "UPDATE "+mainDB+".Zindex set zString=? where keywordId="+id;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, zString);
            isSuccess(pstmt.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void connect(){
        System.out.println("db connect");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useSSL=false", user_name, password);
            stmt = conn.createStatement();
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
