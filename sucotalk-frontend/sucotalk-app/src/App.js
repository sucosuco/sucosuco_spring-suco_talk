import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'; 
import RoomListComponent from './components/RoomListComponent';
import RoomDetailComponent from './components/RoomDetailComponent';

function App() {
  return (
    <div className="App">
      <Router>
        <div className="container">
          <Switch>
            <Route path = "/" exact component = {RoomListComponent}></Route>
            <Route path = "/main" exact component = {RoomListComponent}></Route>
            <Route path = "/room/:id" component = {RoomDetailComponent}></Route>
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
