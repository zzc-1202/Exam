package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao {

    // 基础SQL：查询指定学校、入学年度和班级的学生
    private String baseSql = "SELECT ent_year, no, name, class_num FROM student WHERE school_cd=? AND ent_year=? AND class_num=?";

    // 结果集处理方法（保持不变）
    private List<TestListSubject> postFilter(ResultSet rSet) throws Exception {
        List<TestListSubject> list = new ArrayList<>();
        try {
            while (rSet.next()) {
                TestListSubject testListSubject = new TestListSubject();
                testListSubject.setEntYear(rSet.getInt("ent_year"));
                testListSubject.setStudentNo(rSet.getString("no"));
                testListSubject.setStudentName(rSet.getString("name"));
                testListSubject.setClassNum(rSet.getString("class_num"));
                list.add(testListSubject);
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    /**
     * 获取指定条件的科目测试列表
     * @param entYear 入学年度
     * @param classNum 班级编号
     * @param subject 科目对象（包含科目代码）
     * @param school 学校对象（包含学校代码）
     * @return 测试列表
     * @throws Exception
     */
    public List<TestListSubject> filter(int entYear, String classNum, Subject subject, School school) throws Exception {
        List<TestListSubject> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            connection = getConnection();
            // 查询学生基本信息
            statement = connection.prepareStatement(baseSql + " ORDER BY no ASC");
            statement.setString(1, school.getCd());
            statement.setInt(2, entYear);
            statement.setString(3, classNum);

            rSet = statement.executeQuery();
            list = postFilter(rSet);

            // 为每个学生添加考试成绩
            for (TestListSubject tls : list) {
                Map<Integer, Integer> points = getTestPoints(tls.getStudentNo(), subject.getCd());
                tls.setPoints(points);
            }

        } finally {
            // 资源关闭保持不变
            if (rSet != null) try { rSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return list;
    }

    /**
     * 获取学生某科目的所有考试成绩
     * @param studentNo 学号
     * @param subjectCd 科目代码
     * @return 考试编号和分数的映射表
     * @throws Exception
     */
    private Map<Integer, Integer> getTestPoints(String studentNo, String subjectCd) throws Exception {
        Map<Integer, Integer> points = new HashMap<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            connection = getConnection();
            String sql = "SELECT no, point FROM test WHERE student_no=? AND subject_cd=? ORDER BY no";
            statement = connection.prepareStatement(sql);
            statement.setString(1, studentNo);
            statement.setString(2, subjectCd);
            rSet = statement.executeQuery();

            while (rSet.next()) {
                points.put(rSet.getInt("no"), rSet.getInt("point"));
            }
        } finally {
            if (rSet != null) try { rSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return points;
    }

    /**
     * 保存学生考试成绩
     * @param testListSubject 测试列表对象
     * @param subjectCd 科目代码
     * @return 是否保存成功
     * @throws Exception
     */
    public boolean save(TestListSubject testListSubject, String subjectCd) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = true;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            // 先删除该学生的该科目所有考试成绩
            String deleteSql = "DELETE FROM test WHERE student_no=? AND subject_cd=?";
            statement = connection.prepareStatement(deleteSql);
            statement.setString(1, testListSubject.getStudentNo());
            statement.setString(2, subjectCd);
            statement.executeUpdate();
            statement.close();

            // 插入新的考试成绩
            String insertSql = "INSERT INTO test (subject_cd, student_no, no, point, class_num) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(insertSql);

            for (Map.Entry<Integer, Integer> entry : testListSubject.getPoints().entrySet()) {
                statement.setString(1, subjectCd);
                statement.setString(2, testListSubject.getStudentNo());
                statement.setInt(3, entry.getKey());  // 考试编号
                statement.setInt(4, entry.getValue()); // 分数
                statement.setString(5, testListSubject.getClassNum());
                statement.addBatch();
            }

            int[] counts = statement.executeBatch();
            for (int count : counts) {
                if (count != 1) {
                    result = false;
                    break;
                }
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return result;
    }
}