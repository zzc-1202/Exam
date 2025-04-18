package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;
public class StudentCreateAction extends Action {

	/**
	 * アクション実行
	 *
	 * 学生登録の画面を表示する
	 *
	 * @param req HTTPリクエスト
	 * @param res HTTPレスポンス
	 * @return なし
	 * @exception
	 *
	 */
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// セッションからログインユーザを取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		// 現在の年を取得
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();

		// ログインユーザーの所属する学校に紐づくクラス番号を取得
		ClassNumDao classNumDao = new ClassNumDao();
		List<String> classList = classNumDao.filter(teacher.getSchool());

		// 10年前から10年後までの年度リストを作成
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 11; i++) {
			entYearSet.add(i);
		}

		// リクエストに画面表示に利用するデータを設定
		req.setAttribute("class_num_set", classList);
		req.setAttribute("ent_year_set", entYearSet);

		// 学生登録画面へフォワード
		req.getRequestDispatcher("student_create.jsp").forward(req, res);
	}
}