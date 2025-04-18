package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");


        Map<String, String> errors = new HashMap<>();
        req.setAttribute("errors", errors);


        LocalDate todaysDate = LocalDate.now();
        int year = todaysDate.getYear();


        ClassNumDao classNumDao = new ClassNumDao();
        List<String> classList = classNumDao.filter(teacher.getSchool());


        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i < year + 11; i++) {
            entYearSet.add(i);
        }


        String studentNo = req.getParameter("no");
        if (studentNo == null || studentNo.isEmpty()) {
            errors.put("1", "学生番号が指定されていません");
            req.getRequestDispatcher("student_update.jsp").forward(req, res);
            return;
        }


        StudentDao studentDao = new StudentDao();
        Student student = studentDao.get(studentNo, teacher.getSchool());
        if (student == null) {
            errors.put("1", "指定された学生が見つかりません");
            req.getRequestDispatcher("student_update.jsp").forward(req, res);
            return;
        }


        req.setAttribute("class_num_set", classList);
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("ent_year", student.getEntYear());
        req.setAttribute("no", student.getNo());
        req.setAttribute("name", student.getName());
        req.setAttribute("class_num", student.getClassNum());
        req.setAttribute("is_attend", student.isAttend());


        req.getRequestDispatcher("student_update.jsp").forward(req, res);
    }
}