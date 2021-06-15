import React, { Component } from 'react';
import LoginComponent from '../components/LoginComponent';

class HeaderComponent extends Component {
    constructor(props) {
        super(props)
    }

    componentDidMount() {
        console.log(this.props.user)
    }

    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                        <div class="container-fluid"><a href="https://localhost:3000" className="navbar-brand"> SucoSuco</a></div>
                        <LoginComponent user = {this.props.user} login = {this.props.login} logout = {this.props.logout}/>
                    </nav>
                </header>
            </div>
        );
    }
}

export default HeaderComponent;