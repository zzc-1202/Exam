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

public class StudentListAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String entYearStr = "";
        String classNum = "";
        String isAttendStr = "";
        int entYear = 0;
        boolean isAttend = false;
        List<Student> students = null;
        LocalDate todayDate = LocalDate.now();
        int year = todayDate.getYear();

        StudentDao sDao = new StudentDao();
        ClassNumDao cNumDao = new ClassNumDao();
        Map<String, String> errors = new HashMap<>();

        // Lấy các tham số từ request
        entYearStr = request.getParameter("f1");
        classNum = request.getParameter("f2");
        isAttendStr = request.getParameter("f3");

        // Chuyển đổi từ String sang số nguyên
        if (entYearStr != null) {
            entYear = Integer.parseInt(entYearStr);
        }

        // Khởi tạo danh sách các năm học
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i < year + 1; i++) {
            entYearSet.add(i);
        }

        // Lấy danh sách lớp từ mã trường của giáo viên
        List<String> list = cNumDao.filter(teacher.getSchool());

        // Lọc sinh viên theo các điều kiện
        if (entYear != 0 && !classNum.equals("0")) {
            students = sDao.filter(teacher.getSchool(), entYear, classNum, isAttend);
        } else if (entYear != 0 && classNum.equals("0")) {
            students = sDao.filter(teacher.getSchool(), entYear, isAttend);
        } else if (entYear == 0 && (classNum == null || classNum.equals("0"))) {
            students = sDao.filter(teacher.getSchool(), isAttend);
        } else {
            errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
            request.setAttribute("errors", errors);
            students = sDao.filter(teacher.getSchool(), isAttend);
        }

        // Thiết lập các giá trị trả về cho request
        request.setAttribute("f1", entYear);      // Năm nhập học
        request.setAttribute("f2", classNum);     // Số lớp

        if (isAttendStr != null) {
            isAttend = true;
            request.setAttribute("f3", isAttend);  // Trạng thái học
        }

        request.setAttribute("students", students);       // Danh sách sinh viên
        request.setAttribute("class_num_set", list);      // Danh sách lớp
        request.setAttribute("ent_year_set", entYearSet); // Danh sách năm học

        // Chuyển tiếp đến trang JSP
        request.getRequestDispatcher("student_list.jsp").forward(request, response);
    }
}
