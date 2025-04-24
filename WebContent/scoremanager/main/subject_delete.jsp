<%-- 科目削除JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
<c:param name="title">
    得点管理システム
</c:param>

<c:param name="scripts"></c:param>

<c:param name="content">
<section>
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2>
<form action="SubjectDeleteExecute.action" method="post">
<!-- 科目コード (表示のみ) -->
<div class="form-group">
    <label>科目コード</label>
    <p class="form-control-static">${cd}</p>
    <input type="hidden" name="cd" value="${cd}">
</div>

<!-- 科目名 (表示のみ) -->
<div class="form-group">
    <label>科目名</label>
    <p class="form-control-static">${name}</p>
    <input type="hidden" name="name" value="${name}">
</div>

<div class="mx-auto py-2">
    <button class="btn btn-danger fw-bold px-4 py-2" type="submit">削除</button>
    <a href="SubjectList.action" class="btn btn-outline-secondary ms-2">戻る</a>
</div>
</form>
</section>
</c:param>
</c:import>