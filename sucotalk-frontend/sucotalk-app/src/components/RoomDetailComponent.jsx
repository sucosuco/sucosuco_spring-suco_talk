import React, { Component } from 'react';
import SucoTalkService from '../service/SucoTalkService';
import SockJS from 'sockjs-client';
import StompJs from 'stompjs';

class RoomDetailComponent extends Component {

    constructor(props) {
        super(props)
    
        this.state = {
            room: {},
            members: [],
            message: '',
            messages:[],
            sockJs: {},
            stompClient: {},
        }
    }

    componentDidMount() {
        SucoTalkService.getRoomDetail(this.props.match.params.id).then((res) => {
            this.setState({  
                room: res.data.room,
                members: res.data.room.members,
                messages: res.data.messages
            });
            this.connect();
        }); 
    }

    changeMessageHandler = (event) => {
        this.setState({message: event.target.value})
    }

    exit() {
        SucoTalkService.exitRoom(this.props.match.params.id).then(() => {
            alert('나가기 성공');
            this.props.history.push('/');
        })
        .catch(error => {
            alert(error.response.data.message)
            this.componentDidMount();
        })
    }

    connect() {
        const sockJs = new SockJS('http://localhost:8080/ws-stomp');
        const stompClient = StompJs.over(sockJs)
        const roomId = this.state.room.id;
        const messages = this.state.messages;
        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/sub/chat/room/' + roomId, function(message) {
                const receive = JSON.parse(message.body);
                messages.push(receive);
                refresh(messages)
            })
        });

        const refresh = (messages) => {
            this.setState({
                messages : messages
            })
        }

        this.setState({
            sockJs: sockJs,
            stompClient: stompClient
        })
    }

    sendMessage() {
        const auth_token = "Bearer "+ localStorage.getItem("authorization")
        this.state.stompClient.send("/pub/chat/message", {'Authorization': auth_token}, JSON.stringify({
            roomId: this.state.room.id,
            contents: this.state.message
        }))
    }

    render() {
        return (
            <div>
                <div class="row">
                    <div class="col-md-8">
                        <h2>{this.state.room.name}</h2>
                        {
                            this.state.members.map (
                                member =>
                                    <h5>{member.name}</h5>
                            )
                        }
                    </div>
                    <div class="col-md-2">
                        <button id="logInBtn" class="btn btn-primary btn-block" type="button"
                                onClick={() => this.exit()}>나가기
                        </button>
                    </div>
                </div>
                <div class="input-group">
                    <div class="input-group-prepend">
                        <label class="input-group-text">내용</label>
                    </div>
                    <input type="text" class="form-control" value = {this.state.message} onChange={this.changeMessageHandler}/>
                    <div class="input-group-append">
                        <button class="btn btn-primary" type="button" onClick={() => this.sendMessage()}>보내기</button>
                    </div>
                </div>
                <ul class="list-group">
                    {
                        this.state.messages.map (
                            message => 
                            <li class = "list-group-item">
                                {message.sender.name} : {message.contents}     - {message.sendTime}
                            </li>
                        )
                    }
                </ul>
            </div>
        );
    }
}

export default RoomDetailComponent;