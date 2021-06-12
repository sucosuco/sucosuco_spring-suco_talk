import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'; 
import RoomDetailComponent from './components/RoomDetailComponent';
import HeaderComponent from './components/HeaderComponent';
import SucoTalkService from './service/SucoTalkService';

import MainComponent from './components/MainComponent';

class App extends Component{

  state = {
    user:{}
  };

  constructor(props) {
    super(props);
  }

  componentDidMount() {
    //토큰을 바탕으로 로그인 체크
  }

  render() {
    return (
      <div className="App">
      <HeaderComponent user = {this.state.user} login = {this.login} logout={this.logout}/>
      <Router>
        <div className="container">
          <Switch>
            <Route path = "/" exact render = {(props) => (
              <MainComponent user = {this.state.user}/> )}>
            </Route>
            <Route path = "/room/:id" component = {RoomDetailComponent}></Route>
          </Switch>
        </div>
      </Router>
    </div>
    );
  }

  login = (loginInfo) => {
    SucoTalkService.login(loginInfo).then(res => {
      this.setState({
        user: res.data
      })
    })
  }

  logout = () => {
    SucoTalkService.logout().then(
      this.setState({
        user: {}
      })
    )
  }
}

export default App;
