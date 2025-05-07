package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

	// 基本となるSQL文（subject_cdによる検索）

	private String baseSql = "SELECT * FROM TEST WHERE SUBJECT_CD = ? ";

	public Test get(Student student, Subject subject, School school, int no) throws Exception {

		// SQLに接続

        Connection connection = getConnection();

        PreparedStatement statement = null;

        // 使用する変数を定義

        ResultSet rSet = null;

	    Test test = null;

	    try {

	        // 学校コードで検索するSQL文を準備

	        statement = connection.prepareStatement("SELECT * FROM test WHERE student_no = ? AND subject_cd = ? AND school_cd = ? AND no = ?");

	        statement.setString(1, student.getNo());

	        statement.setString(2, subject.getCd());

	        statement.setString(3, school.getCd());

	        statement.setInt(4, no);

	        rSet = statement.executeQuery();

	        // 結果が存在する場合、Testオブジェクトを作成

	        if (rSet.next()) {

	            test = new Test();

	            test.setSubject(subject);

	            test.setStudent(student);

	            test.setSchool(school);

	            test.setNo(rSet.getInt("no"));

	            test.setPoint(rSet.getInt("point"));

	            // その他必要なTest属性をセット

	            // 他のTest属性があれば設定

	        }

	    } catch (SQLException e) {

	        e.printStackTrace();

	    } finally {

	        // リソースをclose

	        if (rSet != null) {

	            try {

	                rSet.close();

	            } catch (SQLException sqle) {

	                sqle.printStackTrace();

	            }

	        }

	        if (statement != null) {

	            try {

	                statement.close();

	            } catch (SQLException sqle) {

	                sqle.printStackTrace();

	            }

	        }

	        if (connection != null) {

	            try {

	                connection.close();

	            } catch (SQLException sqle) {

	                sqle.printStackTrace();

	            }

	        }

	    }

	    return test;

	}

	private List<Test> postFilter(ResultSet rSet , School school) throws Exception {
	    List<Test> list = new ArrayList<>();

	    try {
	        while (rSet.next()) {
	            Test test = new Test();

	            Student student = new Student();
	            student.setNo(rSet.getString("student_no"));
	            test.setStudent(student);

	            Subject subject = new Subject();
	            subject.setCd(rSet.getString("subject_cd"));
	            test.setSubject(subject);

	            test.setSchool(school);
	            test.setNo(rSet.getInt("no"));
	            test.setPoint(rSet.getInt("point"));
	            test.setClassNum(rSet.getString("class_num"));

	            list.add(test);
	        }
	    } catch (SQLException | NullPointerException e) {
	        e.printStackTrace();
	    }

	    return list;
	}


	public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws SQLException {

	    List<Test> list = new ArrayList<>();

	    Connection connection = null;

	    PreparedStatement statement = null;

	    ResultSet rSet = null;

	    //条件が適用された時にこのStringがbaseSQLに追加される

	    String condition = "and ent_year=? and class_num=?";

	    String order = "order by no asc";

	    try {

	        connection = getConnection();

	        //SQLを連結

	        statement = connection.prepareStatement(baseSql + condition + order);

	        statement.setString(1, subject.getCd());

	        statement.setInt(2, entYear);

	        statement.setString(3, classNum);

	        //SQLを実行

	        rSet = statement.executeQuery();

	        list = postFilter(rSet, school);

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        //リソースをclose

	        if (rSet != null) {

	            try {

	                rSet.close();

	            } catch (SQLException sqle) {

	                sqle.printStackTrace();

	            }

	        }

	        if (statement != null) {

	            try {

	                statement.close();

	            } catch (SQLException sqle) {

	                sqle.printStackTrace();

	            }

	        }

	        if (connection != null) {

	            try {

	                connection.close();

	            } catch (SQLException sqle) {

	                sqle.printStackTrace();

	            }

	        }

	    }

	    return list;

	}

	public boolean save(List<Test> list) throws Exception {
	    // 1. 获取连接并检查非空
	    Connection connection = getConnection();
	    if (connection == null) {
	        throw new SQLException("Failed to get database connection");
	    }

	    boolean allSuccess = true; // 标记所有操作是否全部成功
	    try {
	        connection.setAutoCommit(false); // 事务开始

	        // 2. 遍历执行并记录结果
	        for (Test test : list) {
	            if (!save(test, connection)) {
	                allSuccess = false; // 部分失败
	            }
	        }

	        // 3. 根据结果提交或回滚
	        if (allSuccess) {
	            connection.commit();
	        } else {
	            connection.rollback();
	        }
	    } catch (SQLException e) {
	        // 4. 异常时尝试回滚
	        try {
	            if (connection != null && !connection.isClosed()) {
	                connection.rollback();
	            }
	        } catch (SQLException rollbackEx) {
	            e.addSuppressed(rollbackEx); // 记录回滚异常
	        }
	        throw e;
	    } finally {
	        // 5. 安全关闭连接
	        if (connection != null && !connection.isClosed()) {
	            connection.close();
	        }
	    }
	    return allSuccess;
	}

	private boolean save(Test test , Connection connection )throws Exception{

        boolean result = false;

        PreparedStatement statement = null;

        try {

        	// 試験が既に存在するかどうか確認

            Test tes = get(test.getStudent(), test.getSubject(), test.getSchool() , test.getNo());

            // 試験が存在していた場合はUPDATE、しなかった場合はINSERTを実行

            if (tes == null) {
            	// 新規登録
            	statement = connection.prepareStatement(
						"INSERT INTO test(student_no, subject_cd, school_cd, no, point, class_num) values(?, ?, ?, ?, ?, ?)");
				statement.setString(1, test.getStudent().getNo());
				statement.setString(2, test.getSubject().getCd());
				statement.setString(3, test.getSchool().getCd());
				statement.setInt(4, test.getNo());
				statement.setInt(5, test.getPoint());
				statement.setString(6, test.getClassNum());


            } else {
            	// 更新
                statement = connection.prepareStatement(

                		"UPDATE TEST SET POINT = ? WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?");

                // 送られたtestのデータをセット
                statement.setInt(1, test.getPoint());
                statement.setString(2, test.getStudent().getNo());
                statement.setString(3, test.getSubject().getCd());
                statement.setString(4, test.getSchool().getCd());
                statement.setInt(5, test.getNo());
            }

            // 実行して影響を受けた行数を確認

            int affected = statement.executeUpdate();

            result = (affected > 0);

        } catch (Exception e) {

            throw e;

        } finally {

            // リソースを解放

            if (statement != null) {

                try {

                    statement.close();

                } catch (SQLException sqle) {

                    throw sqle;

                }

            }

        }

        return result ;

    }

}