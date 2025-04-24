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

public class SubjectUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        Map<String, String> errors = new HashMap<>();
        req.setAttribute("errors", errors);

        // リクエストパラメータ取得
        String originalCd = req.getParameter("code");
        if (originalCd == null || originalCd.isEmpty()) {
            errors.put("1", "科目コードが指定されていません");
            req.getRequestDispatcher("subject_update.jsp").forward(req, res);
            return;
        }

        // 科目情報取得
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.get(originalCd, teacher.getSchool());
        if (subject == null) {
            errors.put("1", "指定された科目が見つかりません");
            req.getRequestDispatcher("subject_update.jsp").forward(req, res);
            return;
        }

        // リクエストに属性設定
        req.setAttribute("original_cd", originalCd); // 変更前のコード
        req.setAttribute("cd", subject.getCd());    // 現在のコード
        req.setAttribute("name", subject.getName());

        req.getRequestDispatcher("subject_update.jsp").forward(req, res);
    }
}