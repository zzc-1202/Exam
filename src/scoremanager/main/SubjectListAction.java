package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectListAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 获取学校信息
        School school = teacher.getSchool();

        SubjectDao subjectDao = new SubjectDao();

        // 获取该学校的所有科目（使用现有的filter方法）
        List<Subject> subjects = subjectDao.filter(school);

        // 设置请求属性
        request.setAttribute("subjects", subjects);

        // 转发到JSP页面
        request.getRequestDispatcher("subject_list.jsp").forward(request, response);
    }
}