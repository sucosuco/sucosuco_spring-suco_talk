import React, { Component } from 'react';

class LoginComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name: '',
            password: ''
        }

        this.changeNameHandler = this.changeNameHandler.bind(this);
        this.changePasswordHandler = this.changePasswordHandler.bind(this);
    }

    changeNameHandler = (event) => {
        this.setState({name: event.target.value})
    }

    changePasswordHandler = (event) => {
        this.setState({password: event.target.value})
    }

    login = (event) => {
        event.preventDefault();
        const user = {
            name : this.state.name,
            password : this.state.password
        }
        this.props.login(user)
    }
    render() {
        if (JSON.stringify({}) === JSON.stringify(this.props.user)) {
            return (
                <div>
                    <div class="row g-3">
                        <div class="col">
                            <input type="text" class="form-control" placeholder="Name" aria-label="Name"
                            value = {this.state.name} onChange={this.changeNameHandler}/>
                        </div>
                        <div class="col">
                            <input type="text" class="form-control" placeholder="Password" aria-label="Password"
                            value = {this.state.passowrd} onChange={this.changePasswordHandler}/>
                        </div>
                        <div class="col">
                            <button className="btn btn-success mt-1" onClick={this.login}>Login</button>
                        </div>
                    </div>
                </div>
            );
        }     
        return (
            <div class="row g-3">
                <div class = "col">
                    <p class = "fs-2 text-white">{this.props.user.name}</p>
                </div>
                <div class = "col">
                    <button className="btn btn-success mt-1" onClick={this.props.logout}>Logout</button>
                </div>
            </div>
            
        );   
    }
}

export default LoginComponent;