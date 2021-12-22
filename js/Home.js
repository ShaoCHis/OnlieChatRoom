$(function () {
        alert("欢迎来到我的聊天网站！\n" +
            "在使用时，请注意以下几点：\n" +
            "1、在进行消息发送时，如果要进行群发消息，则使用groupChat:xxxxx(xxxx为您想要发送的消息)\n" +
            "2、若您想要私聊某个好友，则发送格式为privateChat:xxx:hello(xxx为好友的昵称，即页面左侧显示名称)，hello替换为您想要发送的内容即可\n" +
            "注意：你无法对非好友私聊消息\n" +
            "3、转发消息的格式为transmit:xx;xx为您想要转发给的对象，非好友也可;" +
            "注意：文件传输目前仅支持图片形式且大小小于50KB的图片" +
            "祝您使用愉快！\n" +
            "@Author：ShaoCHi;");

        //控制修改信息弹窗的显示与否
        let d = document.getElementsByTagName("dialog")[0];
        let openDialog = document.getElementById("fix");
        let closeDialog = document.getElementById("closeDialog");
        let storeMessages = [];


        openDialog.onclick = function () {
            d.open = "open";
        };
        closeDialog.onclick = function () {
            d.open = "";
        };

        //通过页面跳转传参
        //userId即为该用户的Id
        let userId = $.query.get("userId");
        let age = $.query.get("age");
        let sex = $.query.get("sex");
        $("#name").text(userId);
        $('#age').text(age);
        $('#sex').text(sex);
        getFriendList();

        //WebSocket连接操作
        // const wsuri = "ws://localhost:8888/webSocket";
        const WebSocketUrl = "ws://127.0.0.1:8888/webSocket";
        let ws;//WebSocket连接对象
        //判断当前浏览器是否支持WebSocket
        if (!('WebSocket' in window)) {
            alert('Not support websocket');
        }
        //创建WebSocket连接对象
        // ws = new WebSocket(wsuri);
        ws = new WebSocket(WebSocketUrl);
        console.log("Click");
        //连接成功建立的回调方法
        ws.onopen = function (event) {
            console.log('建立连接');
            //建立连接后立马将该用户注册进入聊天室
            sendMessage("register:" + userId);
        }
        //接收到消息的回调方法
        //对onmessage进行处理，显示私聊消息；
        ws.onmessage = function (event) {
            if (typeof event.data === "string") {
                console.log('接收到内容：' + event.data)
                if (event.data.startsWith("(私聊)")) {
                    let segments = event.data.split(")");
                    storeMessages.push(segments[1]);
                }
                $('#chat_content').append(event.data + '\n')
            } else {
                let flagReader = new FileReader();
                flagReader.readAsArrayBuffer(event.data);
                flagReader.onload = function () {
                    let imageReader = new FileReader();
                    imageReader.readAsDataURL(event.data);
                    console.info("服务器返回的数据大小:", event.data.size);
                    imageReader.onload = function (img) {
                        let imgHtml = "<img src='" + img.target.result + "' style='width: 200px;height: 150px;'>";
                        let d = document.getElementById("server-msg-container")
                        d.innerHTML += imgHtml.replace("data:application/octet-stream;", "data:image/png;") + "<br />";
                        d.scroll(d.scrollWidth, d.scrollHeight);
                    };
                }
            }
        }
        //连接发生错误的回调方法
        ws.onerror = function (event) {
            console.log('发生错误')
        }
        //连接关闭的回调方法
        ws.onclose = function (event) {
            console.log('关闭连接')
        }
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            ws.close();
        }
        //发送消息
        $('#btn_send').click(function () {
            sendMessage($('#send_text').val())
        })
        //监听键盘事件
        $("#send_text").keydown(function (event) {
            if (event.keyCode == 13) {
                sendMessage($('#send_text').val())
                //发送消息后对输入框中内容进行清空
                $('#send_text').val("")
            }
        });

        //发送消息
        function sendMessage(message) {
            //去除输入框因为回车产生的换行符
            message = message.trim()
            //私聊前先判断是否为好友，能否进行私聊
            if (message.startsWith("privateChat:")) {
                let segments = message.split(":");
                if (segments.length == 3 && segments[0] === "privateChat") {
                    let name = segments[1];
                    let xhr = new XMLHttpRequest();
                    xhr.open('get', 'http://localhost:8081/api/users/getFriends/' + userId);
                    //设置请求参数格式的类型（post请求必须要设置）
                    xhr.setRequestHeader('content-type', 'application/json');
                    //发送请求
                    xhr.send();
                    //获取服务器端响应的数据
                    let isFriend = false;
                    xhr.onload = function () {
                        let response = JSON.parse(xhr.responseText)
                        //遍历判断好友是否存在于列表中
                        $.each(response.data, function (i, item) {   //遍历ul中的li
                            if (name === item.userId) {
                                //查找成功，对于好友私聊消息进行存储
                                isFriend = true;
                                ws.send(message)
                                let xhr2 = new XMLHttpRequest();
                                let request = {
                                    "firstUser": userId,
                                    "secondUser": name,
                                    "message": segments[2]
                                }
                                xhr2.open('post', 'http://localhost:8081/api/users/saveFriendMessage');
                                //设置请求参数格式的类型（post请求必须要设置）
                                xhr2.setRequestHeader('content-type', 'application/json');
                                //发送请求
                                xhr2.send(JSON.stringify(request));
                                //获取服务器端响应的数据
                                xhr2.onload = function () {
                                    console.log("private message saving success!")
                                }
                                //return只会退出当前层的函数，并不会全部退出
                                return;
                            }
                        });
                        if (isFriend)
                            return;
                        else {
                            $('#chat_content').append("(系统)您未添加" + name + "为好友，无法对其进行私聊！" + '\n')
                            return;
                        }
                    }
                }
            } else if (message.startsWith("transmit:")) {//进行私聊信息的转发操作
                for(let str in storeMessages){
                    console.log(storeMessages[str])
                    ws.send(message+":"+storeMessages[str])
                }
                $('#chat_content').append("转发成功！\n")
            } else {
                if (message.startsWith("groupChat:")) {
                    //对群聊消息进行存储
                    let segments = message.split(":");
                    let xhr = new XMLHttpRequest();
                    let request = {
                        "userId": userId,
                        "message": segments[1]
                    }
                    xhr.open('post', 'http://localhost:8081/api/users/saveAllMessage');
                    //设置请求参数格式的类型（post请求必须要设置）
                    xhr.setRequestHeader('content-type', 'application/json');
                    //发送请求
                    xhr.send(JSON.stringify(request));
                    //获取服务器端响应的数据
                    xhr.onload = function () {
                        console.log("save all message success!")
                        ws.send(message);
                    }
                    return;
                }
                ws.send(message);
            }
        }

        //关闭连接
        function closeWebSocket() {
            ws.close();
        }

        //点击退出聊天室
        $('#btn_exit').click(function () {
            closeWebSocket();
        })


        //修改个人信息
        $('#submit').click(function () {
            let newPassword = document.getElementById("fixPassword").value;
            let newAge = document.getElementById("fixAge").value;
            let newSex = document.getElementById("fixSex").value;
            console.log(newPassword, newSex, newAge);
            let xhr = new XMLHttpRequest();
            let request = {
                "id": userId,
                "password": newPassword,
                "age": newAge,
                "sex": newSex
            }
            xhr.open('post', 'http://localhost:8081/api/users/update');
            //设置请求参数格式的类型（post请求必须要设置）
            xhr.setRequestHeader('content-type', 'application/json');
            //发送请求
            xhr.send(JSON.stringify(request));
            //获取服务器端响应的数据
            xhr.onload = function () {
                console.log(xhr.getResponseHeader('content-type'))
                let response = JSON.parse(xhr.responseText)
                if (response.success == true) {
                    console.log(response.data)
                    $('#age').text(newAge);
                    $('#sex').text(newSex);
                    let dialog = document.getElementsByTagName("dialog")[0]
                    dialog.open = "";
                    alert("修改成功！");
                } else {
                    alert(response.message)
                }
            }
        })

        //获取好友列表
        function getFriendList() {
            let xhr = new XMLHttpRequest();
            xhr.open('get', 'http://localhost:8081/api/users/getFriends/' + userId);
            //设置请求参数格式的类型（post请求必须要设置）
            xhr.setRequestHeader('content-type', 'application/json');
            //发送请求
            xhr.send();
            //获取服务器端响应的数据
            xhr.onload = function () {
                console.log(xhr.getResponseHeader('content-type'))
                let response = JSON.parse(xhr.responseText)
                let carNewsList = "";
                //遍历得到的List将其作为数据展示在前端页面
                $.each(response.data, function (i, item) {   //遍历ul中的li
                    // carNewsList += "<li>"+item.userId+"</li>";
                    carNewsList += "<li>" + "\n" + "<img src='../Pictures/img/touxiang1.png' alt=''>" + "\n" +
                        "<span>" + item.userId + "</span>" + "\n"
                        + "</li>"
                });
                $('#friendList').html(carNewsList);
            }
        }

        //添加好友
        $('#openAdd').click(function () {
            console.log(1)
            let addDialog = document.getElementById("addFriendId");
            addDialog.open = "open";
        })
        $('#closeAddFriend').click(function () {
            let addDialog = document.getElementById("addFriendId");
            addDialog.open = "";
        })
        $('#addFriend').click(function () {
            let input = document.getElementById("newFriendId").value;
            let xhr = new XMLHttpRequest();
            xhr.open('post', 'http://localhost:8081/api/users/makeFriends');
            //设置请求参数格式的类型（post请求必须要设置）
            xhr.setRequestHeader('content-type', 'application/json');
            let request = {
                "firstUser": userId,
                "secondUser": input
            }
            //发送请求
            xhr.send(JSON.stringify(request));
            //获取服务器端响应的数据
            xhr.onload = function () {
                console.log(xhr.getResponseHeader('content-type'))
                let response = JSON.parse(xhr.responseText)
                if (response.success == true) {
                    alert(response.data)
                    let addDialog = document.getElementById("addFriendId");
                    addDialog.open = "";
                    getFriendList();
                } else {
                    alert(response.message);
                    let addDialog = document.getElementById("addFriendId");
                    addDialog.open = "";
                }
            }
        })

        //删除好友
        $('#openDelete').click(function () {
            console.log(1)
            let deleteDialog = document.getElementById("deleteFriendId");
            deleteDialog.open = "open";
        })
        $('#closeDeleteFriend').click(function () {
            let deleteDialog = document.getElementById("deleteFriendId");
            deleteDialog.open = "";
        })
        $('#deleteFriend').click(function () {
            let input = document.getElementById("delFriendId").value;
            let xhr = new XMLHttpRequest();
            xhr.open('post', 'http://localhost:8081/api/users/deleteFriends');
            //设置请求参数格式的类型（post请求必须要设置）
            xhr.setRequestHeader('content-type', 'application/json');
            let request = {
                "firstUser": userId,
                "secondUser": input
            }
            //发送请求
            xhr.send(JSON.stringify(request));
            //获取服务器端响应的数据
            xhr.onload = function () {
                let response = JSON.parse(xhr.responseText)
                if (response.success == true) {
                    alert(response.data)
                    let deleteDialog = document.getElementById("deleteFriendId");
                    deleteDialog.open = "";
                    getFriendList();
                } else {
                    alert(response.message);
                    let deleteDialog = document.getElementById("deleteFriendId");
                    deleteDialog.open = "";
                }
            }
            setTimeout(window.location.reload(), 2000)
        })

        //获取群聊历史消息
        $('#openHistory').click(function () {
            let xhr = new XMLHttpRequest();
            xhr.open('get', 'http://localhost:8081/api/users/getAllMessage');
            //设置请求参数格式的类型（post请求必须要设置）
            xhr.setRequestHeader('content-type', 'application/json');
            //发送请求
            xhr.send();
            //获取服务器端响应的数据
            xhr.onload = function () {
                let response = JSON.parse(xhr.responseText)
                if (response.success == true) {
                    $.each(response.data, function (i, item) {
                        let time = new Date(new Date(item.date).getTime() + 8 * 60 * 60 * 1000).toISOString()
                            .replace(/T/g, " ")
                            .replace(/\.[\d]{3}Z/, "");
                        $('#totalHistoryMessages').append(item.userId + "(" + time + "):" + item.message + '\n')
                    });
                } else {
                    alert(response.message);
                }
            }
            let d = document.getElementById("totalHistoryMessage");
            d.open = "open"
        })
        $('#closeTotalMessages').click(function () {
            let d = document.getElementById("totalHistoryMessage");
            d.open = ""
        })

        $("#inputFriend").keydown(function (event) {
            if (event.keyCode == 13) {
                //发送消息后对输入框中内容进行清空
                let d = document.getElementById("inputFriend")
                d.open = ""
                openFriendMessage();
            }
        });
        $("#openFriendHistory").click(function () {
            let d = document.getElementById("inputFriend")
            d.open = "open"
        })

        function openFriendMessage() {
            let friendId = document.getElementById("inputFriendId").value;
            let xhr = new XMLHttpRequest();
            xhr.open('post', 'http://localhost:8081/api/users/getFriendMessage');
            //设置请求参数格式的类型（post请求必须要设置）
            xhr.setRequestHeader('content-type', 'application/json');
            let request = {
                "firstUser": userId,
                "secondUser": friendId
            }
            //发送请求
            xhr.send(JSON.stringify(request));
            //获取服务器端响应的数据
            xhr.onload = function () {
                let response = JSON.parse(xhr.responseText)
                if (response.success == true) {
                    $.each(response.data, function (i, item) {
                        let time = new Date(new Date(item.date).getTime() + 8 * 60 * 60 * 1000).toISOString()
                            .replace(/T/g, " ")
                            .replace(/\.[\d]{3}Z/, "");
                        $('#totalFriendMessages').append("From:" + item.firstUser + " to " + item.secondUser + "(" + time + "):" + item.message + '\n')
                    });
                } else {
                    alert(response.message);
                }
            }
            let d = document.getElementById("totalFriendMessage");
            d.open = "open"
        }

        $('#closeFriendMessages').click(function () {
            let d = document.getElementById("totalFriendMessage");
            d.open = ""
        });

        $('#sendFile').click(function () {
            let inputFile = document.getElementById("file");
            let file = inputFile.files;
            console.log(file)
            let reader = new FileReader();
            //以二进制形式读取文件
            reader.readAsArrayBuffer(file[0]);
            //文件读取完毕后，函数响应
            reader.onload = function loaded(evt) {
                let binaryString = evt.target.result;
                console.log(binaryString)
                //发送文件
                ws.send(binaryString);
            }
        })
    }
)