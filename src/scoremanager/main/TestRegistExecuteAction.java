package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");
		School school = teacher.getSchool();
		HashMap<Integer, String> errors = new HashMap<>();
		StudentDao studentDao = new StudentDao();
		SubjectDao subjectDao = new SubjectDao();
		TestDao testDao = new TestDao();
		List<Test> tests = new ArrayList<>();
		int i = 0;
		while (true) {
			Test test = new Test();
			if (req.getParameter("students[" + i + "].entYear") == null) {
				break;
			}
			Student student = studentDao.get(req.getParameter("students[" + i + "].no"));
			String classNum = req.getParameter("students[" + i + "].classNum");
			Subject subject = subjectDao.get(req.getParameter("tests[" + i + "].subjectCd"), school);
			int no = Integer.parseInt(req.getParameter("tests[" + i + "].no"));
			String pointStr = req.getParameter("tests[" + i + "].point");
			test.setStudent(student);
			test.setClassNum(classNum);
			test.setSubject(subject);
			test.setSchool(school);
			test.setNo(no);
			if (pointStr != null) {
				try {
					int point = Integer.parseInt(pointStr);
					if (point < 0 || point > 100) {
						errors.put(i, "0~100の範囲で入力してください");
					} else {
						test.setPoint(point);
						tests.add(test);
					}
				} catch (NumberFormatException e) {
					errors.put(i, "0~100の範囲で入力してください");
				}
			}
			i++;
		}
		// エラーがある場合、元のフォームに戻る
		if (!errors.isEmpty()) {
			ClassNumDao classNumDao = new ClassNumDao();
			req.setAttribute("errors", errors); // エラー情報をセット
			LocalDate todaysDate = LocalDate.now();
			int year = todaysDate.getYear();

			// 検索フォームのパラメータを取得
			String entYearStr = req.getParameter("f1");
			String classNum = req.getParameter("f2");
			String subjectCd = req.getParameter("f3");
			String noStr = req.getParameter("f4");
			HashMap<Integer, String> points = new HashMap<>();

			int j = 0;
			while (true) {
				if (req.getParameter("students[" + j + "].entYear") == null) {
					break;
				}
				String pointStr = req.getParameter("tests[" + j + "].point");
				if (pointStr != null) {
					points.put(j, pointStr); // 文字列をそのまま保存
				}
				j++;
			}

			// 検索フォームのパラメータをリクエストに再セット
			req.setAttribute("f1", entYearStr);
			req.setAttribute("f2", classNum);
			req.setAttribute("f3", subjectCd);
			req.setAttribute("f4", noStr);
			req.setAttribute("subjectCd", subjectCd);

			// 必要に応じて検索結果も再取得
			List<Student> students = null;
			if (entYearStr != null && classNum != null && subjectCd != null && noStr != null) {
				int entYear = Integer.parseInt(entYearStr);
				int no = Integer.parseInt(noStr);
				students = studentDao.filter(school, entYear, classNum, false);
				req.setAttribute("students", students);
				req.setAttribute("subject", subjectDao.get(subjectCd, school).getName());
				req.setAttribute("no", no);
			}
			List<Integer> entYearSet = new ArrayList<>();
			for (i = year - 10; i < year + 1; i++) {
				entYearSet.add(i);
			}

			// クラス番号や科目リストなども再セット
			req.setAttribute("subjects", subjectDao.filter(school));
			req.setAttribute("class_num_set", classNumDao.filter(school));
			req.setAttribute("ent_year_set", entYearSet);
			req.setAttribute("points", points);

			// JSP にフォワード
			req.getRequestDispatcher("test_regist.jsp").forward(req, res);
			return;
		}
		testDao.save(tests);

		// 成功した場合は完了画面へ
		req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
	}
}