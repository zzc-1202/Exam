<%-- 科目変更JSP --%>
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
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>
<form action="SubjectUpdateExecute.action" method="post">
<!-- 元の科目コード (hidden) -->
<input type="hidden" name="original_cd" value="${original_cd}">

<!-- 科目コード (編集可能) -->
<div>
    <label for="cd">科目コード</label><br>
    <input class="form-control" type="text" id="cd" name="cd"
           value="${cd}" required maxlength="10"
           placeholder="科目コードを入力してください" />
</div>
<div class="mt-2 text-warning">${errors.get("1")}</div>

<!-- 科目名 -->
<div>
    <label for="name">科目名</label><br>
    <input class="form-control" type="text" id="name" name="name"
           value="${name}" required maxlength="30"
           placeholder="科目名を入力してください" />
</div>
<div class="mt-2 text-warning">${errors.get("2")}</div>

<div class="mx-auto py-2">
    <button class="btn btn-outline-primary fw-bold px-4 py-2"
            type="submit">変更</button>
</div>
</form>
<a href="SubjectList.action">戻る</a>
</section>
</c:param>
</c:import>