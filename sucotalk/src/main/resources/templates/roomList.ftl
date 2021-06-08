<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- CSS -->
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
        <div class="col-md-3">
            <h3>친구 리스트</h3>
            <ul class="list-group">
                <li class="list-group-item list-group-item-action" v-bind:class="{ 'active' : isSelected(item.id) }"
                    v-for="item in friends" v-bind:key="item.id"
                    v-on:click="toggleFriends(item.id)">
                    {{item.name}}
                </li>
            </ul>
        </div>
        <div class="col-md-9">
            <h3>채팅방 리스트</h3>
            <div class="input-group">
                <div class="input-group-prepend">
                    <label class="input-group-text">방제목</label>
                </div>
                <input type="text" class="form-control" v-model="room_name" v-on:keyup.enter="createRoom">
                <div class="input-group-append">
                    <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
                    <button class="btn btn-primary" type="button" @click="login">로그인</button>
                </div>
            </div>
            <ul class="list-group">
                <li class="list-group-item list-group-item-action" v-for="item in chatrooms" v-bind:key="item.id"
                    v-on:click="enterRoom(item.id)">
                    {{item.name}}
                </li>
            </ul>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            room_name: '',
            chatrooms: [],
            friends: [],
            selectedFriends: []
        },
        created() {
            this.findAllRoom();
        },
        methods: {
            findAllRoom: function () {
                axios.get('/rooms').then(response => {
                    this.chatrooms = response.data;
                });
            },
            login: function () {
                const name = prompt("아이디를 입력해 주세요")
                const pw = prompt("비밀번호를 입력해주세요")

                axios.post('/member/login', {
                    name: name,
                    password: pw
                }).then(response => {
                    alert('로그인 되었습니다.')
                    this.findFriends();
                });
            },
            createRoom: function () {
                if ("" === this.room_name) {
                    alert("방 제목을 입력해 주십시요.");
                    return;
                } else {
                    axios.post('/rooms', {
                        "name": this.room_name,
                        "members": this.selectedFriends
                    }).then(
                        response => {
                            alert(response + "방 개설에 성공하였습니다.")
                            this.room_name = '';
                            this.findAllRoom();
                            this.selectedFriends = [];
                        }
                    )
                        .catch(response => {
                            alert("채팅방 개설에 실패하였습니다.");
                        });
                }
            },
            enterRoom: function (roomId) {
                location.href='/rooms/'+ roomId;
                /*var sender = prompt('대화명을 입력해 주세요.');
                if(sender != "") {
                    localStorage.setItem('wschat.sender',sender);
                    localStorage.setItem('wschat.roomId',roomId);


                    location.href="/rooms/enter/"+roomId;
                }*/
            },
            findFriends: function () {
                axios.get('/member/friends').then(response => {
                    this.friends = response.data;
                });
            },
            toggleFriends(id) {
                if (this.selectedFriends.includes(id)) {
                    const newArray = [];
                    for (let i = 0; i < this.selectedFriends.length; i++) {
                        if (this.selectedFriends[i] !== id) {
                            newArray.push(this.selectedFriends[i]);
                        }
                    }
                    this.selectedFriends = newArray;
                } else {
                    this.selectedFriends.push(id);
                }

            },
            isSelected(id) {
                return this.selectedFriends.includes(id);
            }
        }
    });
</script>
</body>
</html>