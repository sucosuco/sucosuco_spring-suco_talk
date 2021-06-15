import React, { Component } from 'react';
import SucoTalkService from '../service/SucoTalkService';

class RoomDetailComponent extends Component {

    constructor(props) {
        super(props)
    
        this.state = {
            room: {},
            members: [],
            messages:[]
        }
    }

    componentDidMount() {
        SucoTalkService.getRoomDetail(this.props.match.params.id).then((res) => {
            this.setState({  
                room: res.data.room,
                members: res.data.room.members,
                messages: res.data.messages
            });
        }); 
    }

    enter() {
        SucoTalkService.enterRoom(this.props.match.params.id).then(() => {
            alert('들어가기 성공');
            this.componentDidMount();
        })
        .catch(error => {
            alert(error.response.data.message)
            this.componentDidMount();
        })
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
                    <div className="col-md-2">
                        <button id="logInBtn" className="btn btn-primary btn-block" type="button"
                                onClick={() => this.enter()}>들가기
                        </button>
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
                    <input type="text" class="form-control"/>
                    <div class="input-group-append">
                        <button class="btn btn-primary" type="button" onClick="sendMessage">보내기</button>
                    </div>
                </div>
                <ul class="list-group">
                    {
                        this.state.messages.map (
                            message => 
                            <li class = "list-group-item">
                                {message.sender.name} : {message.contents}
                            </li>
                        )
                    }
                </ul>
            </div>
        );
    }
}

export default RoomDetailComponent;