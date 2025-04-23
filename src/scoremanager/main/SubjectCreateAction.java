package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import tool.Action;

public class SubjectCreateAction extends Action {

    /**
     * 科目登録画面を表示する
     */
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログインユーザを取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher)session.getAttribute("user");

        // 科目登録画面へフォワード
        req.getRequestDispatcher("subject_create.jsp").forward(req, res);
    }
}