import React, {Component} from 'react';
import SucoTalkService from '../service/SucoTalkService';
import {withRouter} from 'react-router-dom';
import { Button,Modal } from 'react-bootstrap'

class TabRoomListComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            myRooms: [],
            accessibleRooms: [],
            selectedRoom: ''
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
                <ul class="nav nav-tabs">
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
                                this.state.myRooms.map(
                                    room =>
                                        <li class="list-group-item list-group-item-action"
                                            onClick={() => this.enterRoom(room.id)} key={room.id}>
                                            {room.name}
                                        </li>
                                )
                            }
                        </ul>
                    </div>
                    <div class="tab-pane fade" id="accessible">
                        <ul class="list-group">
                            {
                                this.state.accessibleRooms.map(
                                    room =>
                                        <li class="list-group-item list-group-item-action"
                                            onClick={() => this.clickRoom(room.id)} key={room.id}>
                                            {room.name}
                                        </li>
                                )
                            }
                        </ul>
                    </div>
                </div>

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

export default withRouter(TabRoomListComponent);