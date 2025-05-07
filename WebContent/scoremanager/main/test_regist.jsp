<%-- 学生一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
<c:param name="title">
		得点管理システム
</c:param>
<c:param name="scripts"></c:param>
<c:param name="content">
<section class="me-4">
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生管理</h2>
<form method="get" action="TestRegist.action">
<div class="row border mx-3 mb-3 py-2 align-items-center rounded"
					id="filter">
<div class="col-2">
<label class="form-label" for="student-f1-select">入学年度</label> <select
							class="form-select" id="student-f1-select" name="f1">
<option value="0">--------</option>
<c:forEach var="year" items="${ent_year_set }">
<option value="${year }"
<c:if test="${year==f1 }">selected</c:if>>${year }</option>
</c:forEach>
</select>
</div>
<div class="col-2">
<label class="form-label" for="student-f2-select">クラス</label> <select
							class="form-select" id="student-f2-select" name="f2">
<option value="0">--------</option>
<c:forEach var="num" items="${class_num_set }">
<option value="${num }" <c:if test="${num==f2 }">selected</c:if>>${num }</option>
</c:forEach>
</select>
</div>
<div class="col-4">
<label class="form-label" for="student-f3-select">科目</label> <select
							class="form-select" id="student-f3-select" name="f3">
<option value="0">--------</option>
<c:forEach var="subject" items="${subjects }">
<option value="${subject.getCd() }"
<c:if test="${subject.getCd()==f3 }">selected</c:if>>${subject.getName() }</option>
</c:forEach>
</select>
</div>
<div class="col-2">
<label class="form-label" for="student-f4-select">回数</label> <select
							class="form-select" id="student-f4-select" name="f4">
<option value="0" <c:if test="${f4==0 }">selected</c:if>>--------</option>
<option value="1" <c:if test="${f4==1 }">selected</c:if>>1</option>
<option value="2" <c:if test="${f4==2 }">selected</c:if>>2</option>
</select>
</div>
<div class="col-2 text-center">
<button class="btn btn-secondary" id="filter-button">検索</button>
</div>
<div class="mt-2 text-warning">${error}</div>
</div>
</form>
<c:if test="${students.size()>0}">
<form method="get" action="TestRegistExecute.action">
<!-- 検索パラメータを hidden フィールドとして保持 -->
<input type="hidden" name="f1" value="${f1}" /> <input
						type="hidden" name="f2" value="${f2}" /> <input type="hidden"
						name="f3" value="${f3}" /> <input type="hidden" name="f4"
						value="${f4}" />

					<div>科目：${subject } (${no }回)</div>
<table class="table table-hover">
<tr>
<th>入学年度</th>
<th>クラス</th>
<th>学生番号</th>
<th>氏名</th>
<th>点数</th>
</tr>
<c:forEach var="student" varStatus="status" items="${students}">
<input type="hidden" name="tests[${status.index}].classNum"
								value="${subject }" />
<input type="hidden" name="tests[${status.index}].no"
								value="${no }" />
<input type="hidden" name="tests[${status.index}].subjectCd"
								value="${subjectCd }" />
<tr>
<td><input type="hidden"
									name="students[${status.index}].entYear"
									value="${student.entYear}" /> ${student.entYear}</td>
<td><input type="hidden"
									name="students[${status.index}].classNum"
									value="${student.classNum}" /> ${student.classNum}</td>
<td><input type="hidden"
									name="students[${status.index}].no" value="${student.no}" />
									${student.no}</td>
<td><input type="hidden"
									name="students[${status.index}].name" value="${student.name}" />
									${student.name}</td>
<td><input type="text" name="tests[${status.index}].point"
									value="${points.get(status.index) }" /> <c:if
										test="${errors.get(status.index) != null}">
<div class="mt-2 text-warning">${errors.get(status.index)}</div>
</c:if></td>
</tr>
</c:forEach>
</table>
<button type="submit" class="btn btn-secondary">登録して終了</button>
</form>
</c:if>
</section>
</c:param>
</c:import>