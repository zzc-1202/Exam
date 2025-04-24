package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectDeleteExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        SubjectDao subjectDao = new SubjectDao();
        Map<String, String> errors = new HashMap<>();

        // 科目コード取得
        String subjectCd = req.getParameter("cd");
        if (subjectCd == null || subjectCd.isEmpty()) {
            errors.put("1", "科目コードが不正です");
        }

        // 科目存在チェック
        Subject subject = subjectDao.get(subjectCd, teacher.getSchool());
        if (subject == null) {
            errors.put("1", "科目が存在しません");
        }

        if (errors.isEmpty()) {
            // 科目削除実行
            boolean result = subjectDao.delete(subject);
            if (!result) {
                errors.put("99", "削除に失敗しました");
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("cd", subjectCd);
            req.setAttribute("name", req.getParameter("name"));
            req.getRequestDispatcher("SubjectDelete.action?code=" + subjectCd).forward(req, res);
        } else {
            req.getRequestDispatcher("subject_delete_done.jsp").forward(req, res);
        }
    }
}