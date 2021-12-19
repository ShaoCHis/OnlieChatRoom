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

function checkHobby() {
    let hobbyNum = document.getElementsByName("hobby");
    let hobby = "";
    for (let i = 0; i < hobbyNum.length; ++i) {
        if (hobbyNum[i].checked) {
            hobby = hobbyNum[i];
        }
    }
    if (hobby == "") {
        trip("hobby_trip", "ERROR!!");
        return false;
    } else {
        trip("hobby_trip", "OK!!");
    }
}

function checkSelect() {
    let mySelect = document.getElementById("userType");
    let index = mySelect.selectedIndex;
    let checkValue = mySelect.options[index].value;
    if (checkValue == 0) {
        trip("type_trip", "请选择!!");
        return false;
    } else {
        trip("type_trip", "OK!!");
    }
}

function checkForm() {
    checkSelect();
    checkHobby();
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
        return true;
    } else {
        return false;
    }
}