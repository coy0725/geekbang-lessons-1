
<head>
    <jsp:directive.include
            file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
    <title>My Home Page</title>
</head>

<body>
<div class="container-lg">
    <!-- Content here -->
<h1>    register success!</h1>
   <p><b>用户名:</b>
        <%= request.getParameter("name")%>
    </p>
    <p><b>手机号:</b>
        <%= request.getParameter("phoneNumber")%>
    </p>

</div>
</body>