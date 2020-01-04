var TT = TAOTAO = {
    checkLogin: function () {
        var _ticket = $.cookie("TT_TOKEN");
        if (!_ticket) {
            return;
        }
        $.ajax({
            url: "http://localhost:8089/user/token/" + _ticket,
            dataType: "jsonp",
            type: "GET",
            success: function (data) {
                if (data.status == 200) {
                    var username = data.data.username;
                    var html = username + "，欢迎来到欧文购物网！<a href = 'javascript:void(0)' onclick ='logout()'>退出</a>";
                    $("#loginbar").html(html);
                }
            }
        });
    }
}

function logout() {
    var token = $.cookie("TT_TOKEN");
	$.get("http://localhost:8089/user/logout/"+token);
	$.cookie('TT_TOKEN', '', { expires: -1 });
	window.location.href = "http://localhost:8082";
}

$(function () {
    // 查看是否已经登录，如果已经登录查询登录信息
    TT.checkLogin();

});