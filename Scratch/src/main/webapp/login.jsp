<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta charset="utf-8"/>
<title>刮奖平台管理系统</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css"/>
<link href="/resources/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="/dist/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="/resources/css/style-metronic.css" rel="stylesheet" type="text/css"/>
<link href="/resources/css/style.css" rel="stylesheet" type="text/css"/>
<link href="/resources/css/login.css" rel="stylesheet" type="text/css"/>
<!-- END THEME STYLES -->
<link rel="shortcut icon" href="favicon.ico"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
<!-- BEGIN LOGO -->
<div class="logo">
	<a href="index.html">
		<img src="/resources/img/logo-big.png" alt=""/>
	</a>
</div>
<!-- END LOGO -->
<!-- BEGIN LOGIN -->
<div class="content">
	<!-- BEGIN LOGIN FORM -->
	<form class="login-form" method="post">
		<h3 class="form-title">身份认证</h3>
		<div class="alert alert-danger display-hide">
			<button class="close" data-close="alert"></button>
			<span>
				 请输入账号和密码.
			</span>
		</div>
		<div class="form-group">
			<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
			<label class="control-label visible-ie8 visible-ie9">账号</label>
			<div class="input-icon">
				<i class="fa fa-user"></i>
				<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="账号" id="username" name="username"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9">密码</label>
			<div class="input-icon">
				<i class="fa fa-lock"></i>
				<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="密码" id="password" name="password"/>
			</div>
		</div>
		<div class="form-actions">
			<button type="submit" class="btn blue pull-right">
			登录 <i class="m-icon-swapright m-icon-white"></i>
			</button>
		</div>
	</form>
	<!-- END LOGIN FORM -->
	
</div>
<!-- END LOGIN -->
<div class="copyright">
	 2018 &copy; Tsingma - Admin.
</div>
<script src="/dist/jquery/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/dist/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/dist/jquery/jquery.validate.min.js" type="text/javascript"></script>
<script src="/dist/jquery/jquery.backstretch.min.js" type="text/javascript"></script>
<script src="/resources/js/app.js" type="text/javascript"></script>
<script src="/resources/js/login.js" type="text/javascript"></script>
<script>
	jQuery(document).ready(function() {     
		App.init();
		Login.init();
	});
</script>
</body>
<!-- END BODY -->
</html>