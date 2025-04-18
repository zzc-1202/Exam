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
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報削除</h2>
<form action="StudentDeleteExecute.action" method="post">
<!-- 入学年度 () -->
<div class="form-group">
    <label>入学年度</label>
    <p class="form-control-static">${ent_year}</p>
    <input type="hidden" name="ent_year" value="${ent_year}">
</div>

<!-- 学生番号 () -->
<div class="form-group">
    <label>学生番号</label>
    <p class="form-control-static">${no}</p>
    <input type="hidden" name="no" value="${no}">
</div>

<div class="form-group">
    <label>氏名</label>
    <p class="form-control-static">${name}</p>
    <input type="hidden" name="name" value="${name}">
</div>

<div class="form-group">
    <label>クラス</label>
    <p class="form-control-static">${class_num}</p>
    <input type="hidden" name="class_num" value="${class_num}">
</div>


<div style="display: flex; align-items: center; gap: 8px;">
  <label for="is_attend">在学中</label>
<input type="checkbox" id="is_attend" name="is_attend" value="true"
       <c:if test="${is_attend}">checked</c:if> />
</div>

<div class="mx-auto py-2">
<button class="btn btn-outline-primary fw-bold px-4 py-2" id="create-button" name="end">削除</button>
</div>
</form>
<a href="StudentList.action">戻る</a>
</section>
</c:param>
</c:import>