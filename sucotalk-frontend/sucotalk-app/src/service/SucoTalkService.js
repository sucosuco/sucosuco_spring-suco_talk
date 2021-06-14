import axios from 'axios';

const BASE_URL = "http://localhost:8080/"

class SucoTalkService {

    getRooms() {
        return axios.get(BASE_URL + 'rooms');
    }

    getMyRooms() {
        return axios.get(BASE_URL + 'rooms/my', { withCredentials: true });
    }

    getAccessibleRooms() {
        return axios.get(BASE_URL + 'rooms/accessible', { withCredentials: true });
    }

    getRoomDetail(roomId) {
        return axios.get(BASE_URL + 'rooms/detail/' + roomId);
    }

    getFriends() {
        return axios.get(BASE_URL + 'member/friends', { withCredentials: true });
    }

    createRoom(roomInfo) {
        return axios.post(BASE_URL + 'rooms', roomInfo, { withCredentials: true });
    }

    exitRoom(roomId) {
        return axios.post(BASE_URL + 'rooms/exit/' + roomId, {withCredentials: true});
    }

    login(loginInfo) {
        return axios.post(BASE_URL + 'member/login/', loginInfo, { withCredentials: true });
    }

    logout() {
        return axios.post(BASE_URL + 'member/logout/')
    }
}
export default new SucoTalkService();