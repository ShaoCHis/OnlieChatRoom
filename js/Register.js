function trip(obj, trip) {
    document.getElementById(obj).innerHTML = "<b>" + trip + "</b>";
}

function checkSex() {
    let sexNum = document.getElementsByName("sex");
    let sex = "";
    for (let i = 0; i < sexNum.length; ++i) {
        if (sexNum[i].checked) {
            sex = sexNum[i];
        }
    }
    if (sex == "") {
        trip("sex_trip", "ERROR!!");
        return false;
    } else {
        trip("sex_trip", "OK!!");
    }
}

function checkForm() {
    checkSex();
    //获取用户名输入项
    let userName = document.getElementById("userName");
    let uName = userName.value;
    if (uName.length < 1 || uName.length > 10) {
        trip("name_trip", "账号长度为1-10位!!");
        return false;
    } else {
        trip("name_trip", "OK!!");
    }
    //密码长度大于6 和确认必须一致
    let password = document.getElementById("password");
    let userPass = password.value;
    if (userPass.length < 6) {
        trip("password_trip", "密码要>6位!!");
        return false;
    } else {
        trip("password_trip", "OK!!");
    }
    //获取确认密码框的值 let
    let surePassword = document.getElementById("surePassword");
    let surePass = surePassword.value;
    if (userPass != surePass) {
        trip("surePassword_trip", "两次密码不一致!!");
        return false;
    }
    return true;
}

function submitT() {
    if (checkForm()) {
        let password = document.getElementById("password");
        let userPass = password.value;
        let userName = document.getElementById("userName");
        let uName = userName.value;
        let userAge=document.getElementById("userAge");
        let uAge=userAge.value;
        let sexNum = document.getElementsByName("sex");
        let sex = "";
        for (let i = 0; i < sexNum.length; ++i) {
            if (sexNum[i].checked) {
                sex = sexNum[i];
            }
        }
        sex=sex.value;
        let request = {
            "id": uName,
            "password": userPass,
            "age":uAge,
            "sex":sex
        }
        let xhr = new XMLHttpRequest();
        xhr.open('post', 'http://localhost:8081/api/users/register');
        //设置请求参数格式的类型（post请求必须要设置）
        xhr.setRequestHeader('content-type', 'application/json');
        //发送请求
        xhr.send(JSON.stringify(request));
        //获取服务器端响应的数据
        xhr.onload = function () {
            let response=JSON.parse(xhr.responseText)
            console.log(response.success)
            if(response.success==true){
                alert(response.data)
                window.location = 'Login.html'
            }else{
                alert(response.message)
            }
        }
        return true;
    } else {
        return false;
    }
}