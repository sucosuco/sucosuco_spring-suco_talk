<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-md-10">
            <h2>{{room.name}}</h2>
            <h5 v-for="member in room.members">{{member.name}}</h5>
        </div>
        <div class="col-md-2">
            <button id="logInBtn" class="btn btn-primary btn-block" type="button" @click="exit">나가기</button>
        </div>
    </div>

    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">내용</label>
        </div>
        <input type="text" class="form-control" v-model="message" v-on:keypress.enter="message">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="message">보내기</button>
        </div>
    </div>
    <ul class="list-group">
        <li class="list-group-item" v-for="message in messages">
            {{message.sender.name}} - {{message.contents}}     [{{message.sendTime}}]
        </li>
    </ul>
    <div></div>
</div>
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    // websocket & stomp initialize
    var sock = new SockJS("/ws-stomp");
    var ws = Stomp.over(sock);
    // vue.js
    var vm = new Vue({
        el: '#app',
        data: {
            roomId: ${room.id},
            room: {},
            sender: '',
            message: '',
            messages: []
        },
        created() {
            this.findRoom();
        },
        methods: {
            findRoom: function () {
                axios.get('/rooms/detail/' + this.roomId).then(response => {
                    this.room = response.data.room;
                    this.messages = response.data.messages;
                });
            },
            message: function () {
                ws.send("/pub/chat/message", {}, JSON.stringify({
                    type: 'TALK',
                    roomId: this.roomId,
                    sender: this.sender,
                    message: this.message
                }));
                this.message = '';
            },
            recvMessage: function (recv) {
                this.messages.unshift({
                    "type": recv.type,
                    "sender": recv.type == 'ENTER' ? '[알림]' : recv.sender,
                    "message": recv.message
                })
            },
            exit: function () {
                axios.post('/rooms/exit/' + this.roomId)
                .then(response => {
                    alert('나가기 성공');
                    location.href = '/roomList';
                })
                .catch(error=>{
                    alert(error.response.data.message);
                })
            }
        }
    });
    // pub/sub event
    ws.connect({}, function (frame) {
        ws.subscribe("/sub/chat/room/" + vm.$data.roomId, function (message) {
            var recv = JSON.parse(message.body);
            vm.recvMessage(recv);
        });
        ws.send("/pub/chat/message", {}, JSON.stringify({
            type: 'ENTER',
            roomId: vm.$data.roomId,
            sender: vm.$data.sender
        }));
    }, function (error) {
        alert("error " + error);
    });
</script>
</body>
</html>


