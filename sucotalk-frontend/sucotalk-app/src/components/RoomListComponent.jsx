import React, { Component } from 'react';
import AllRoomListComponent from '../components/AllRoomListComponent';
import TabRoomListComponent from '../components/TabRoomListComponent';
import { withRouter } from 'react-router-dom';

class RoomListComponent extends Component {

    constructor(props) {
        super(props)
    }

    render() {
        if (JSON.stringify({}) === JSON.stringify(this.props.user)) {
            return (
                <div>
                     <AllRoomListComponent/>
                </div>
            )
           
        }
        return (
            <div>
                <TabRoomListComponent/>
            </div>
        );
    }
}

export default withRouter(RoomListComponent);