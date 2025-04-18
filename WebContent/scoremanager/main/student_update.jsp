<%-- 学生登録JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp" >
<c:param name="title">
		得点管理システム
</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
<section>
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報変更</h2>
<form action="StudentUpdateExecute.action" method="post">
<!-- 入学年度 (Hiển thị dạng text, không cho chỉnh sửa) -->
<div class="form-group">
    <label>入学年度</label>
    <p class="form-control-static">${ent_year}</p>
    <input type="hidden" name="ent_year" value="${ent_year}">
</div>

<!-- 学生番号 (Hiển thị dạng text, không cho chỉnh sửa) -->
<div class="form-group">
    <label>学生番号</label>
    <p class="form-control-static">${no}</p>
    <input type="hidden" name="no" value="${no}">
</div>
<div class="mt-2 text-warning">${errors.get("2") }</div>
<div>
<label for="name">氏名</label><br>
<input class="form-control" type="text" id="name" name="name" value="${name }" required maxlength="30" placeholder="氏名を入力してください" />
</div>
<div class="mx-auto py-2">
<label for="class_num">クラス</label>
<select class="form-select" id="class_num" name="class_num">
<c:forEach var="num" items="${class_num_set }">
<%-- 現在のnumと選択されていたclass_numが一致していた場合selectedを追記 --%>
<option value="${num }" <c:if test="${num==class_num }">selected</c:if>>${num }</option>
</c:forEach>
</select>
</div>
<div style="display: flex; align-items: center; gap: 8px;">
  <label for="is_attend">在学中</label>
<input type="checkbox" id="is_attend" name="is_attend" value="true"
       <c:if test="${is_attend}">checked</c:if> />
</div>

<div class="mx-auto py-2">
<button class="btn btn-outline-primary fw-bold px-4 py-2" id="create-button" name="end">変更</button>
</div>
</form>
<a href="StudentList.action">戻る</a>
</section>
</c:param>
</c:import>