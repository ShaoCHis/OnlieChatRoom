function login() {
    let userId = document.getElementById("userId").value;
    let password = document.getElementById("password").value;
    let request = {
        "id": userId,
        "password": password
    }
    console.log(request)
    let xhr = new XMLHttpRequest();
    xhr.open('post', 'http://localhost:8081/api/users/session');
    //设置请求参数格式的类型（post请求必须要设置）
    xhr.setRequestHeader('content-type', 'application/json');
    //发送请求
    xhr.send(JSON.stringify(request));
    //获取服务器端响应的数据
    xhr.onload = function () {
        let response=JSON.parse(xhr.responseText)
        if(response.success==true){
            let age=response.data.age;
            let sex=response.data["sex"];
            alert("登陆成功！")
            let url = "home.html?userId="+userId+"&age="+age+"&sex="+sex;
            window.location.href=url
        }else{
            alert(response.message)
        }
    }
}