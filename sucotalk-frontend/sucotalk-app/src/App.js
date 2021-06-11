import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'; 
import RoomListComponent from './components/RoomListComponent';

function App() {
  return (
    <div className="App">
      <Router>
        <div className="container">
          <Switch>
            <Route path = "/" exact component = {RoomListComponent}></Route>
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
