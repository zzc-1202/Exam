package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        Map<String, String> errors = new HashMap<>();

        // 参数获取
        String cd = req.getParameter("cd").trim();
        String name = req.getParameter("name").trim();
        School school = teacher.getSchool();

        // 验证科目代码
        if (cd == null || cd.isEmpty()) {
            errors.put("1", "科目コードを入力してください");
        } else if (!cd.matches("[A-Za-z0-9]+")) { // 添加代码格式验证
            errors.put("1", "科目コードは英数字のみ使用できます");
        }

        // 验证科目名
        if (name == null || name.isEmpty()) {
            errors.put("2", "科目名を入力してください");
        }

        // 检查重复
        SubjectDao dao = new SubjectDao();
        if (dao.get(cd, school) != null) {
            errors.put("1", "この科目コードは既に存在します");
        }

        if (errors.isEmpty()) {
            Subject subject = new Subject();
            subject.setCd(cd);
            subject.setName(name);
            subject.setSchool(school);

            // 使用你的DAO保存数据
            boolean success = dao.save(subject);

            if (success) {
                req.getRequestDispatcher("subject_create_done.jsp").forward(req, res);
                return;
            } else {
                errors.put("system", "データベースへの保存に失敗しました");
            }
        }

        // 错误处理
        req.setAttribute("cd", cd);
        req.setAttribute("name", name);
        req.setAttribute("errors", errors);
        req.getRequestDispatcher("SubjectCreate.action").forward(req, res);
    }
}