import React, { Component } from 'react';
import SucoTalkService from '../service/SucoTalkService';
import { withRouter } from 'react-router-dom';
import { Button,Modal } from 'react-bootstrap'

class AllRoomListComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            showHide: false,
            rooms: [],
            selectedRoom: ''
        }
    }

    componentDidMount() {
        SucoTalkService.getRooms().then((res) => {
            this.setState({rooms: res.data});
        });
    }

    handleModalShowHide() {
        this.setState({ showHide: !this.state.showHide })
    }

    clickRoom(roomId) {
        this.handleModalShowHide();
        this.setState({
            selectedRoom: roomId
        })
    }

    enterRoom(roomId) {
        this.props.history.push(`/room/${roomId}`)
    }
    
    enterNewRoom(roomId) {
        SucoTalkService.enterRoom(roomId).then(() => {
            this.props.history.push(`/room/${roomId}`)
        })
        .catch(error => {
            alert(error.response.data.message)
        })
        this.handleModalShowHide();
    }

    render() {
        return (
            <div>
                <ul class="list-group">
                    {
                        this.state.rooms.map (
                            room =>
                            <li class="list-group-item list-group-item-action" onClick = {() => this.clickRoom(room.id)} key = {room.id}>
                                {room.name}
                            </li>
                        )
                    }
                </ul>

                <Modal show = {this.state.showHide}>
                    <Modal.Header closeButton onClick = {() => this.handleModalShowHide()}>
                        <Modal.Title>알림</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>방에 입장 하시겠습니까?</Modal.Body>
                    <Modal.Footer>
                        <Button variant = "secondary" onClick={()=>this.handleModalShowHide()}>
                            닫기
                        </Button>

                        <Button variant = "primary" onClick={()=>this.enterNewRoom(this.state.selectedRoom)}>
                            입장
                        </Button>
                    </Modal.Footer>
                </Modal>
            </div>
        );
    }
}

export default withRouter(AllRoomListComponent);