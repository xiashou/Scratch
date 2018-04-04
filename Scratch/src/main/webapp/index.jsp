<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + path + ":" + request.getServerPort();
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>智能工匠 - Desktop Sample App</title>

    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/css/desktop.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/css/icon.css" />

    <script type="text/javascript" src="<%=basePath %>/resources/js/include-ext.js"></script>
    <script type="text/javascript" src="<%=basePath %>/dist/ext/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript">
        Ext.Loader.setPath({
            'Ext.ux.desktop': '/mgr/frame',
            MyDesktop: '/mgr/ux',
            Business: '/mgr/biz'
        });

        Ext.require('MyDesktop.App');

        var myDesktopApp;
        Ext.onReady(function () {
            myDesktopApp = new MyDesktop.App();
        });
    </script>
</head>

<body>

    <a href="http://www.sencha.com" target="_blank" alt="2015 © copyright" id="poweredby"><div></div></a>

</body>
</html>
