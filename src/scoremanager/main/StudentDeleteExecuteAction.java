package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentDeleteExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        StudentDao studentDao = new StudentDao();
        Map<String, String> errors = new HashMap<>();


        String studentNo = req.getParameter("no");
        if (studentNo == null || studentNo.isEmpty()) {
            errors.put("1", "学生番号が不正です");
        }


        Student currentStudent = studentDao.get(studentNo, teacher.getSchool());
        if (currentStudent == null) {
            errors.put("1", "学生が存在しません");
        }


        if (errors.isEmpty()) {

            currentStudent.setName(req.getParameter("name"));
            currentStudent.setClassNum(req.getParameter("class_num"));
            currentStudent.setAttend("true".equals(req.getParameter("is_attend")));


            int result = studentDao.delete(studentNo, teacher.getSchool());

            if (result == 0) {
                errors.put("99", "削除が失敗しました");
            }
        }


        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);

            req.setAttribute("ent_year", req.getParameter("ent_year"));
            req.setAttribute("no", studentNo);
            req.setAttribute("name", req.getParameter("name"));
            req.setAttribute("class_num", req.getParameter("class_num"));
            req.setAttribute("is_attend", "true".equals(req.getParameter("is_attend")));

            req.getRequestDispatcher("StudentDelete.action?no=" + studentNo).forward(req, res);
        } else {
            req.getRequestDispatcher("student_delete_done.jsp").forward(req, res);
        }
    }
}