import React, { Component } from 'react';
import SucoTalkService from '../service/SucoTalkService';
import { withRouter } from 'react-router-dom';

class AllRoomListComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            rooms: []
        }
    }

    componentDidMount() {
        SucoTalkService.getRooms().then((res) => {
            this.setState({rooms: res.data});
        });
    }

    enterRoom(roomId) {
        this.props.history.push(`/room/${roomId}`)
    }

    render() {
        return (
            <div>
                <ul class="list-group">
                    {
                        this.state.rooms.map (
                            room =>
                            <li class="list-group-item list-group-item-action" onClick = {() => this.enterRoom(room.id)} key = {room.id}>
                                {room.name}
                            </li>
                        )
                    }
                </ul>
            </div>
        );
    }
}

export default withRouter(AllRoomListComponent);