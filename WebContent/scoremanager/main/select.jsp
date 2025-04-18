<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="/common/base.jsp" %>

<h2>得点管理システム</h2>

<!-- テーブルを作成し、枠線を設定します -->
<table border="1">
<tr align="center">
    <th>入学年度</th>
    <th>学生番号</th>
    <th>氏名</th>
    <th>クラス</th>
    <th>在学中</th>
</tr>

<c:forEach var="student" items="${list}">
<tr>
    <td>${student.ENT_YEAR}</td>
    <td>${student.NO}</td>
    <td>${student.NAME}</td>
    <td>${student.CLASS_NUM}</td>
    <td>
        <!-- Radio button -->
      <td style="text-align: center;">
        <form action="changeStatus.jsp" method="post" style="display: inline;">
            <input type="hidden" name="no" value="${student.NO}" />
            
            <label>
                <input type="radio" name="active" value="true"
                       <c:if test="${student.ACTIVE}">checked</c:if> />
                はい
            </label>
            <label>
                <input type="radio" name="active" value="false"
                       <c:if test="${!student.ACTIVE}">checked</c:if> />
                いいえ
            </label>

            <!-- Nút gửi biểu mẫu -->
            <input type="submit" value="変更" />
        </form>
    </td>
</tr>
</c:forEach>
</table>

