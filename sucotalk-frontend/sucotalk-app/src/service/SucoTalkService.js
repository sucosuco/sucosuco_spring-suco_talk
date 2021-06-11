import axios from 'axios';

const BASE_URL = "http://localhost:8080/"

class SucoTalkService {

    getRooms() {
        return axios.get(BASE_URL + 'rooms');
    }

    getRoomDeatail(roomId) {
        return axios.get(BASE_URL + 'rooms/detail/' + roomId);
    }

    exitRoom(roomId) {
        return axios.post(BASE_URL + 'rooms/exit/' + roomId);
    }

    login(loginInfo) {
        return axios.post(BASE_URL + '/member/login/', loginInfo);
    }

    logout() {
        return axios.post(BASE_URL + '/member/logout/')
    }
}
export default new SucoTalkService();