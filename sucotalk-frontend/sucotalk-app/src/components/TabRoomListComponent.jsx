import React, { Component } from 'react';
import SucoTalkService from '../service/SucoTalkService';
import { withRouter } from 'react-router-dom';

class TabRoomListComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            myRooms: [],
            accessibleRooms: []
        }
    }

    componentDidMount() {
        SucoTalkService.getMyRooms().then((res) => {
            this.setState({myRooms: res.data});
        });

        SucoTalkService.getAccessibleRooms().then((res) => {
            this.setState({accessibleRooms: res.data});
        });
    }

    enterRoom(roomId) {
        this.props.history.push(`/room/${roomId}`)
    }


    render() {
        return (
            <div>
                <ul class ="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link active" data-toggle="tab" href="#my">내 채팅방</a>
                    </li>
                    <li class="nav-item">
                    <a class="nav-link" data-toggle="tab" href="#accessible">참여 가능한 채팅방</a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane fade show active" id="my">
                        <ul class="list-group">
                            {
                                this.state.myRooms.map (
                                    room =>
                                        <li class="list-group-item list-group-item-action" onClick = {() => this.enterRoom(room.id)} key = {room.id}>
                                            {room.name}
                                        </li>
                                )
                            }
                        </ul>
                    </div>
                    <div class="tab-pane fade" id="accessible">
                        <ul class="list-group">
                            {
                                this.state.accessibleRooms.map (
                                    room =>
                                        <li class="list-group-item list-group-item-action" onClick = {() => this.enterRoom(room.id)} key = {room.id}>
                                            {room.name}
                                        </li>
                                )
                            }
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(TabRoomListComponent);