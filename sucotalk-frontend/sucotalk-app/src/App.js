import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import RoomDetailComponent from './components/RoomDetailComponent';
import HeaderComponent from './components/HeaderComponent';
import SucoTalkService from './service/SucoTalkService';

import MainComponent from './components/MainComponent';
import RoomCreateComponent from './components/RoomCreateComponent';

class App extends Component {

    state = {
        user: {}
    };

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        SucoTalkService.isLogged().then(res => {
            this.setState({user: res.data})
        })
    }

    render() {
        return (
            <div className="App">
                <HeaderComponent user={this.state.user} login={this.login} logout={this.logout}/>
                <Router>
                    <div className="container">
                        <Switch>
                            <Route path="/" exact render={() => (
                                <MainComponent user={this.state.user}/>)}>
                            </Route>

                            <Route path="/main" render={() => (
                                <MainComponent user={this.state.user}/>)}>
                            </Route>
                            <Route path="/room/:id" component={RoomDetailComponent}></Route>
                            <Route path="/createRoom" component={RoomCreateComponent}></Route>
                        </Switch>
                    </div>
                </Router>
            </div>
        );
    }

    login = (loginInfo) => {
        SucoTalkService.login(loginInfo).then(res => {
            localStorage.setItem("authorization", res.headers["authorization"])
            this.setState({
                user: res.data
            })
        }).catch(error => {
            alert(error.response.data.message)
        })
    }

    logout = () => {
        SucoTalkService.logout();
        
        alert("정상적으로 로그아웃 되었습니다.")
        this.setState({
            user: {}
        })
        this.render();
    }
}

export default App;
