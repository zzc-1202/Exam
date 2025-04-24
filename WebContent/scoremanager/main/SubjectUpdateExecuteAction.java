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

public class SubjectUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        SubjectDao subjectDao = new SubjectDao();
        Map<String, String> errors = new HashMap<>();

        // パラメータ取得
        String originalCd = req.getParameter("original_cd");
        String newCd = req.getParameter("cd");
        String name = req.getParameter("name");

        // バリデーション
        if (newCd == null || newCd.trim().isEmpty()) {
            errors.put("1", "科目コードを入力してください");
        } else if (!newCd.matches("[A-Za-z0-9]+")) {
            errors.put("1", "科目コードは英数字のみ使用できます");
        }

        if (name == null || name.trim().isEmpty()) {
            errors.put("2", "科目名を入力してください");
        }

        // 重複チェック（コード変更時のみ）
        if (!newCd.equals(originalCd)) {
            Subject existing = subjectDao.get(newCd, teacher.getSchool());
            if (existing != null) {
                errors.put("1", "この科目コードは既に存在します");
            }
        }

        if (errors.isEmpty()) {
            // 科目情報取得
            Subject subject = subjectDao.get(originalCd, teacher.getSchool());
            if (subject == null) {
                errors.put("99", "科目が見つかりません");
            } else {
                // 科目情報更新
                subject.setCd(newCd);
                subject.setName(name);

                // コード変更時は削除→新規登録
                if (!newCd.equals(originalCd)) {
                    subjectDao.delete(subject);
                }

                boolean result = subjectDao.save(subject);
                if (!result) {
                    errors.put("99", "更新に失敗しました");
                }
            }
        }

        // 結果処理
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("original_cd", originalCd);
            req.setAttribute("cd", newCd);
            req.setAttribute("name", name);
            req.getRequestDispatcher("SubjectUpdate.action?code=" + originalCd).forward(req, res);
        } else {
            req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
        }
    }
}