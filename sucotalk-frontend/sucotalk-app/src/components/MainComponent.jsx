import React, { Component } from 'react';
import RoomListComponent from './RoomListComponent';
import { withRouter } from 'react-router-dom';

class MainComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selectedUser: []
        }
    }

    getSelectedUser(selectedUser) {
        this.setState(
            {
                selectedUser: selectedUser
            }
        ).bind(this)
        console.log(this.state.se)
    }

    createRoom() {
        this.props.history.push('/createRoom')
    }

    render() {
        return(
            <div>
                <button id="createRoomBtn" class="btn btn-primary" type="button"
                        onClick = {(e) => this.createRoom(e)}>채팅방 개설</button>
                <RoomListComponent user = {this.props.user}/>
            </div>
        );
    }
}

export default withRouter(MainComponent);