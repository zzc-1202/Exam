package bean;

import java.io.Serializable;

public class ClassNum implements Serializable {

	/**
	 * クラス名:class_num
	 */
	private String class_num;

	/**
	 * 学校:School
	 */
	private School school;


	/**
	 * ゲッター・セッター
	 */

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getClassNum() {
		return class_num;
	}

	public void setClassNum(String class_num) {
		this.class_num = class_num;
	}
}