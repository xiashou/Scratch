<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title data-i18n-text="title">淘大师</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link type="text/css" rel="stylesheet" href="/dist/bootstrap/css/bootstrap.css">
	<style>
		.container {width:100%;}
		.container table {width:100%; margin: 0 auto;}
		.container p {width:100%; margin: 10px auto; text-align: left;}
	</style>
</head>
<body>
	<div class="container">  
		<p>
			<button type="button" class="btn btn-success" onclick="window.location.href='add.jsp'">添 加</button>
			<button type="button" class="btn btn-default">Success</button>
		</p>
        <table class="table table-striped table-hover table-bordered">  
            <thead>  
                <tr>  
                    <th>表头1</th>  
                    <th>表头2</th>  
                    <th>表头3</th>  
                </tr>  
            </thead>  
            <tbody>  
                <tr>  
                    <td>第1行第1列</td>  
                    <td>第1行第2列</td>  
                    <td>第1行第3列</td></tr>  
                <tr>  
                    <td>第2行第1列</td>  
                    <td>第2行第2列</td>  
                    <td>第2行第3列</td></tr>  
                <tr>  
                    <td>第3行第1列</td>  
                    <td>第3行第2列</td>  
                    <td>第3行第3列</td></tr>  
                </tbody>  
        </table>  
    </div>  
</body>
</html>
