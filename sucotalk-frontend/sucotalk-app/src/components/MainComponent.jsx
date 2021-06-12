import React, { Component } from 'react';
import RoomListComponent from './RoomListComponent';

class MainComponent extends Component {
    constructor(props) {
        super(props)
        console.log(this.props.user)
    }

    render() {
        return(
            <RoomListComponent user = {this.props.user}/>
        );
    }
}

export default MainComponent;