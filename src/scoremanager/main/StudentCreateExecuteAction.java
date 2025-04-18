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

public class StudentCreateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");
		Student student = new Student();
		StudentDao studentDao = new StudentDao();

		// リクエストパラメーターの取得
		int ent_year = Integer.parseInt(req.getParameter("ent_year"));
		String student_no = req.getParameter("no");
		String student_name = req.getParameter("name");
		String class_num = req.getParameter("class_num");

		Map<String, String> errors = new HashMap<>();

		// 入学年度の選択状態判定
		if (ent_year == 0) {
			errors.put("1", "入学年度を選択してください");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		} else {
			// 学生番号が重複している場合
			if (studentDao.get(student_no) != null) {
				errors.put("2", "学生番号が重複しています");
				// リクエストにエラーメッセージを設定
				req.setAttribute("errors", errors);
			} else {
				// 学生情報を設定
				student.setNo(student_no);
				student.setName(student_name);
				student.setEntYear(ent_year);
				student.setClassNum(class_num);
				student.setAttend(true);
				student.setSchool(teacher.getSchool());
				// 学生情報を登録
				studentDao.save(student);
			}
		}

		// リクエストに入学年度をセット
		req.setAttribute("ent_year", ent_year);
		// リクエストに学生番号をセット
		req.setAttribute("no", student_no);
		// リクエストに氏名をセット
		req.setAttribute("name", student_name);
		// リクエストにクラス番号をセット
		req.setAttribute("class_num", class_num);

		// 画面表示
		if (errors.isEmpty()) {
			// 登録完了画面にフォワード
			req.getRequestDispatcher("student_create_done.jsp").forward(req, res);
		} else {
			// エラーメッセージがある場合
			// 登録画面にフォワード
			req.getRequestDispatcher("StudentCreate.action").forward(req, res);
		}
	}

}